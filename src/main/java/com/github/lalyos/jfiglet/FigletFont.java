package com.github.lalyos.jfiglet;

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.SynchronousQueue;

/**
 * FigletFont implementation. A single static method call will create the ascii
 * art in a mulitilne String. FigletFont format is specified at:
 * https://github.com/lalyos/jfiglet/blob/master/figfont.txt
 * <p/>
 * <pre>
 * <code>String asciiArt = FigletFont.convertOneLine("hello");</code>
 * </pre>
 * <p/>
 * Originally found at: http://www.rigaut.com/benoit/CERN/FigletJava/. Moved to
 * <a href="http://lalyos.github.io/jfiglet/">github.com</a>.
 *
 * @author Benoit Rigaut CERN July 96
 *         www.rigaut.com benoit@rigaut.com
 *         released with GPL the 13th of november 2000 (my birthday!)
 */
public class FigletFont {

    public char hardblank;
    public int height = -1;
    public int heightWithoutDescenders = -1;
    public int maxLine = -1;
    public int smushMode = -1;
    public int printDirection;
    public Integer fullLayout;
    public Integer codeTagCount;
    public char font[][][] = null;
    public String fontName = null;
    final public static int MAX_CHARS = 1024;
    public final FittingRules fittingRules;

    private static enum CAN_VSMUSH {VALID, INVAID, END}

    /**
     * Returns all character from this Font. Each character is defined as
     * char[][]. So the whole font is a char[][][].
     *
     * @return The representation of all characters.
     */
    public char[][][] getFont() {
        return font;
    }

    /**
     * Return a single character represented as char[][].
     *
     * @param c The numerical id of the character.
     * @return The definition of a single character.
     */
    public char[][] getChar(int c) {
        return font[c];
    }

    /**
     * Selects a single line from a character.
     *
     * @param c Character id
     * @param l Line number
     * @return The selected line from the character
     */
    public String getCharLineString(int c, int l) {
        if (font[c][l] == null)
            return null;
        else {
            String ret = new String(font[c][l]);
            return ret.substring(1);
        }
    }

    public FigletFont overridehLayout(FittingRules.LAYOUT layout) {
        fittingRules.overridehLayout(layout);
        return this;
    }

    public FigletFont overridevLayout(FittingRules.LAYOUT layout) {
        fittingRules.overridevLayout(layout);
        return this;
    }


    /**
     * Creates a FigletFont as specified at: https://github.com/lalyos/jfiglet/blob/master/figfont.txt
     *
     * @param stream
     */

    public FigletFont(InputStream stream) throws IOException {
        font = new char[MAX_CHARS][][];
        BufferedReader data = null;
        String dummyS;
        char dummyC;
        int dummyI;
        int charCode;

        String codeTag;
        try {
            data = new BufferedReader(new InputStreamReader(stream));

            dummyS = data.readLine();
            StringTokenizer st = new StringTokenizer(dummyS, " ");
            String s = st.nextToken();
            hardblank = s.charAt(s.length() - 1);
            height = Integer.parseInt(st.nextToken());
            heightWithoutDescenders = Integer.parseInt(st.nextToken());
            maxLine = Integer.parseInt(st.nextToken());
            smushMode = Integer.parseInt(st.nextToken());
            dummyI = Integer.parseInt(st.nextToken());
            printDirection = (st.countTokens() >= 6) ? Integer.parseInt(st.nextToken()) : 0;
            fullLayout = (st.countTokens() >= 7) ? Integer.parseInt(st.nextToken()) : null;
            codeTagCount = (st.countTokens() >= 8) ? Integer.parseInt(st.nextToken()) : null;
            fittingRules = new FittingRules(smushMode, fullLayout);

            /*
             * try to read the font name as the first word of the first comment
             * line, but this is not standardized !
             */
            st = new StringTokenizer(data.readLine(), " ");
            if (st.hasMoreElements())
                fontName = st.nextToken();
            else
                fontName = "";

            for (int i = 0; i < dummyI - 1; i++) // skip the comments
                dummyS = data.readLine();
            charCode = 31;
            while (dummyS != null) {  // for all the characters
                //System.out.print(i+":");
                charCode++;
                for (int h = 0; h < height; h++) {
                    dummyS = data.readLine();
                    if (dummyS != null) {
                        //System.out.println(dummyS);
                        int iNormal = charCode;
                        boolean abnormal = true;
                        if (h == 0) {
                            try {
                                codeTag = dummyS.concat(" ").split(" ")[0];
                                if (codeTag.length() > 2 && "x".equals(codeTag.substring(1, 2))) {
                                    charCode = Integer.parseInt(codeTag.substring(2), 16);
                                } else {
                                    charCode = Integer.parseInt(codeTag);
                                }
                            } catch (NumberFormatException e) {
                                abnormal = false;
                            }
                            if (abnormal)
                                dummyS = data.readLine();
                            else
                                charCode = iNormal;
                        }
                        if (h == 0)
                            font[charCode] = new char[height][];
                        int t = dummyS.length() - 1 - ((h == height - 1) ? 1 : 0);
                        if (height == 1)
                            t++;
                        font[charCode][h] = new char[t];
                        for (int l = 0; l < t; l++) {
                            char a = dummyS.charAt(l);
                            font[charCode][h][l] = (a == hardblank) ? ' ' : a;
                        }
                    }
                }
            }
        } finally {
            if (data != null) {
                data.close();
            }
        }
    }


    public String convert(String message) throws IOException {
        String result = "";
        message.replaceAll("\r\n", "\n");
        message.replaceAll("\r", "\n");
        int maxLines = message.split("\n").length;
        char[][][] figLines = new char[maxLines][height][];
        int lineNumber = 0;
        for (String line : message.split("\n")) {
            char[][] r = new char[height][];
            for (int c = 0; c < line.length(); c++) {
                char[][] figChar = getChar(line.charAt(c));
                int overlap = 0;
                if (fittingRules.gethLayout() != FittingRules.LAYOUT.FULL_WIDTH) {
                    overlap = 10000;// a value too high to be the overlap
                    for (int row = 0; row < height; row++) {
                        overlap = Math.min(overlap, getHorizontalSmushLength(r[row], figChar[row]));
                    }
                    overlap = (overlap == 10000) ? 0 : overlap;
                }
                r = horizontalSmush(r, figChar, overlap);
            }
            figLines[lineNumber] = r;
            lineNumber++;
        }
        char[][] output = figLines[0];
        for (int ii = 1; ii < maxLines; ii++) {
            output = smushVerticalFigLines(output, figLines[ii]);
        }
        for (int line = 0; line < output.length; line++) {
            result = result + String.valueOf(output[line]) + System.getProperty("line.separator");
        }
        return result;
    }

    public char[][] horizontalSmush(char[][] textBlock1, char[][] textBlock2, int overlap) {
        char[][] outputFig = new char[height][];
        for (int i = 0; i < height; i++) {
            char[] txt1 = textBlock1[i];
            char[] txt2 = textBlock2[i];
            int len1 = txt1 != null ? txt1.length : 0;
            int len2 = txt2 != null ? txt2.length : 0;
            int overlapStart = len1 - overlap;
            String piece1 = txt1 != null ? String.valueOf(txt1).substring(0, Math.max(0, overlapStart)) : "";
            String piece2 = "";
            String piece3;

            String seg1 = txt1 != null ? String.valueOf(txt1).substring(Math.max(0, len1 - overlap), Math.max(0, len1 - overlap) + overlap) : "";
            String seg2 = txt2 != null ? String.valueOf(txt2).substring(0, Math.min(overlap, len2)) : "";
            for (int j = 0; j < overlap; j++) {
                char ch1 = ((j < len1) ? seg1.substring(j, j + 1) : " ").charAt(0);
                char ch2 = ((j < len2) ? seg2.substring(j, j + 1) : " ").charAt(0);
                if (ch1 != ' ' && ch2 != ' ') {
                    if (fittingRules.gethLayout() != FittingRules.LAYOUT.FITTING) {
                        piece2 += uni_Smush(ch1, ch2, hardblank);
                    } else if (fittingRules.gethLayout() != FittingRules.LAYOUT.SMUSHING) {
                        piece2 += uni_Smush(ch1, ch2, hardblank);
                    } else {
                        // Controlled Smushing
                        Character nextCh = null;
                        nextCh = (nextCh == null && fittingRules.ishRule1()) ? hRule1_Smush(ch1, ch2, hardblank) : nextCh;
                        nextCh = (nextCh == null && fittingRules.ishRule2()) ? hRule2_Smush(ch1, ch2) : nextCh;
                        nextCh = (nextCh == null && fittingRules.ishRule3()) ? hRule3_Smush(ch1, ch2) : nextCh;
                        nextCh = (nextCh == null && fittingRules.ishRule4()) ? hRule4_Smush(ch1, ch2) : nextCh;
                        nextCh = (nextCh == null && fittingRules.ishRule5()) ? hRule5_Smush(ch1, ch2) : nextCh;
                        nextCh = (nextCh == null && fittingRules.ishRule6()) ? hRule6_Smush(ch1, ch2, hardblank) : nextCh;
                        nextCh = (nextCh == null) ? uni_Smush(ch1, ch2, hardblank) : nextCh;
                        piece2 += nextCh;
                    }
                } else {
                    piece2 += uni_Smush(ch1, ch2, hardblank);
                }
            }

            if (overlap >= len2) {
                piece3 = "";
            } else {
                piece3 = String.valueOf(txt2).substring(overlap, overlap + Math.max(0, len2 - overlap));
            }
            outputFig[i] = (piece1 + piece2 + piece3).toCharArray();

        }
        return outputFig;
    }

    private int getHorizontalSmushLength(char[] txt1, char[] txt2) {
        if (fittingRules.gethLayout() == FittingRules.LAYOUT.FULL_WIDTH) {
            return 0;
        }
        int len1 = txt1 != null ? txt1.length : 0;
        int len2 = txt2 != null ? txt2.length : 0;
        int maxDist = len1;
        int curDist = 1;
        boolean breakAfter = false;
        boolean validSmush = false;
        if (len1 == 0) {
            return 0;
        }

        distCal:
        while (curDist <= maxDist) {
            String seg1 = String.valueOf(txt1).substring(len1 - curDist, (len1 - curDist) + curDist);
            String seg2 = String.valueOf(txt2).substring(0, Math.min(curDist, len2));
            for (int ii = 0; ii < Math.min(curDist, len2); ii++) {
                char ch1 = seg1.substring(ii, ii + 1).charAt(0);
                char ch2 = seg2.substring(ii, ii + 1).charAt(0);
                if (ch1 != ' ' && ch2 != ' ') {
                    if (fittingRules.gethLayout() != FittingRules.LAYOUT.FITTING) {
                        curDist = curDist - 1;
                        break distCal;
                    } else if (fittingRules.gethLayout() != FittingRules.LAYOUT.SMUSHING) {
                        if (ch1 == hardblank || ch2 == hardblank) {
                            curDist = curDist - 1; // universal smushing does not smush hardblanks
                        }
                        break distCal;
                    } else {
                        breakAfter = true; // we know we need to break, but we need to check if our smushing rules will allow us to smush the overlapped characters
                        validSmush = false; // the below checks will let us know if we can smush these characters

                        validSmush = (fittingRules.ishRule1()) ? hRule1_Smush(ch1, ch2, hardblank) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.ishRule2()) ? hRule2_Smush(ch1, ch2) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.ishRule3()) ? hRule3_Smush(ch1, ch2) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.ishRule4()) ? hRule4_Smush(ch1, ch2) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.ishRule5()) ? hRule5_Smush(ch1, ch2) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.ishRule6()) ? hRule6_Smush(ch1, ch2, hardblank) != null : validSmush;

                        if (!validSmush) {
                            curDist = curDist - 1;
                            break distCal;
                        }
                    }
                }
            }
            if (breakAfter) {
                break;
            }
            curDist++;
        }
        return Math.min(maxDist, curDist);
    }

    private CAN_VSMUSH canVerticalSmush(char[] txt1, char[] txt2) {
        if (fittingRules.getvLayout() == FittingRules.LAYOUT.FULL_WIDTH) {
            return CAN_VSMUSH.INVAID;
        }
        int len = Math.min(txt1 != null ? txt1.length : 0, txt2 != null ? txt1.length : 0);
        char ch1, ch2;
        boolean endSmush = false;
        boolean validSmush;
        if (len == 0) {
            return CAN_VSMUSH.INVAID;
        }

        for (int ii = 0; ii < len; ii++) {
            ch1 = txt1[ii];
            ch2 = txt2[ii];
            if (ch1 != ' ' && ch2 != ' ') {
                if (fittingRules.getvLayout() == FittingRules.LAYOUT.FITTING) {
                    return CAN_VSMUSH.INVAID;
                } else if (fittingRules.getvLayout() == FittingRules.LAYOUT.SMUSHING) {
                    return CAN_VSMUSH.END;
                } else {
                    if (vRule5_Smush(ch1, ch2) != null) {
                        endSmush = endSmush || false;
                        continue;
                    } // rule 5 allow for "super" smushing, but only if we're not already ending this smush
                    validSmush = false;
                    validSmush = (fittingRules.isvRule1()) ? vRule1_Smush(ch1, ch2) != null : validSmush;
                    validSmush = (!validSmush && fittingRules.isvRule2()) ? vRule2_Smush(ch1, ch2) != null : validSmush;
                    validSmush = (!validSmush && fittingRules.isvRule3()) ? vRule3_Smush(ch1, ch2) != null : validSmush;
                    validSmush = (!validSmush && fittingRules.isvRule4()) ? vRule4_Smush(ch1, ch2) != null : validSmush;
                    endSmush = true;
                    if (!validSmush) {
                        return CAN_VSMUSH.INVAID;
                    }
                }
            }
        }
        if (endSmush) {
            return CAN_VSMUSH.END;
        } else {
            return CAN_VSMUSH.VALID;
        }
    }

    private char[][] smushVerticalFigLines(char[][] output, char[][] lines) {
        int len1 = output[0].length;
        int len2 = lines[0].length;
        int overlap;
        if (len1 > len2) {
            lines = padLines(lines, len1 - len2);
        } else if (len2 > len1) {
            output = padLines(output, len2 - len1);
        }
        overlap = getVerticalSmushDist(output, lines);
        return verticalSmush(output, lines, overlap);
    }

    private int getVerticalSmushDist(char[][] lines1, char[][] lines2) {
        int maxDist = lines1.length;
        int len1 = lines1.length;
        int len2 = lines2.length;
        int curDist = 1;
        while (curDist <= maxDist) {

            char[][] subLines1 = Arrays.copyOfRange(lines1, Math.max(0, len1 - curDist),len1);
            char[][] subLines2 = Arrays.copyOfRange(lines2, 0, Math.min(maxDist, curDist));

            int slen = subLines2.length;//TODO:check this
            CAN_VSMUSH result = null;
            for (int ii = 0; ii < slen; ii++) {
                CAN_VSMUSH ret = canVerticalSmush(subLines1[ii], subLines2[ii]);
                if (ret == CAN_VSMUSH.END) {
                    result = ret;
                } else if (ret == CAN_VSMUSH.INVAID) {
                    result = ret;
                    break;
                } else {
                    if (result == null) {
                        result = CAN_VSMUSH.VALID;
                    }
                }
            }

            if (result == CAN_VSMUSH.INVAID) {
                curDist--;
                break;
            }
            if (result == CAN_VSMUSH.END) {
                break;
            }
            if (result == CAN_VSMUSH.VALID) {
                curDist++;
            }
        }

        return Math.min(maxDist, curDist);
    }

    private char[] verticallySmushLines(char[] line1, char[] line2) {
        int len = Math.min(line1.length, line2.length);
        char[] result = new char[len];
        for (int ii = 0; ii < len; ii++) {
            char ch1 = line1[ii];
            char ch2 = line2[ii];
            if (ch1 != ' ' && ch2 != ' ') {
                if (fittingRules.getvLayout() == FittingRules.LAYOUT.FITTING) {
                    result[ii] = uni_Smush(ch1, ch2, hardblank);
                } else if (fittingRules.getvLayout() == FittingRules.LAYOUT.SMUSHING) {
                    result[ii] = uni_Smush(ch1, ch2, hardblank);
                } else {
                    Character validSmush = null;
                    validSmush = (fittingRules.isvRule5()) ? vRule5_Smush(ch1, ch2) : validSmush;
                    validSmush = (validSmush != null && fittingRules.isvRule1()) ? vRule1_Smush(ch1, ch2) : validSmush;
                    validSmush = (validSmush != null && fittingRules.isvRule2()) ? vRule2_Smush(ch1, ch2) : validSmush;
                    validSmush = (validSmush != null && fittingRules.isvRule3()) ? vRule3_Smush(ch1, ch2) : validSmush;
                    validSmush = (validSmush != null && fittingRules.isvRule4()) ? vRule4_Smush(ch1, ch2) : validSmush;
                    result[ii] = validSmush;
                }
            } else {
                result[ii] = uni_Smush(ch1, ch2, hardblank);
            }
        }
        return result;
    }

    private char[][] verticalSmush(char[][] lines1, char[][] lines2, int overlap) {
        int len1 = lines1.length;
        int len2 = lines2.length;
        char[][] piece1 = Arrays.copyOfRange(lines1, 0, Math.max(0, len1 - overlap));
        char[][] piece2_1 = Arrays.copyOfRange(lines1, Math.max(0, len1 - overlap), len1);
        char[][] piece2_2 = Arrays.copyOfRange(lines2, 0, Math.min(overlap, len2));
        char[][] piece2 = new char[0][];
        char[][] piece3;

        int len = piece2_1.length;
        char[] line = null;
        for (int ii = 0; ii < len; ii++) {
            if (ii >= len2) {
                line = piece2_1[ii];
            } else {
                line = verticallySmushLines(piece2_1[ii], piece2_2[ii]);
            }
            piece2 = Arrays.copyOf(piece2, piece2.length + 1);
            piece2[piece2.length - 1] = line;
        }

        piece3 = Arrays.copyOfRange(lines2, Math.min(overlap, len2), len2);
        char[][] result = new char[piece1.length + piece2.length + piece3.length][];
        System.arraycopy(piece1, 0, result, 0, piece1.length);
        System.arraycopy(piece2, 0, result, piece1.length, piece2.length);
        System.arraycopy(piece3, 0, result, piece1.length + piece2.length, piece3.length);
        return result;
    }

    private char[][] padLines(char[][] lines, int numSpaces) {
        int len = lines.length;
        String padding = "";
        for (int ii = 0; ii < numSpaces; ii++) {
            padding += " ";
        }
        for (int ii = 0; ii < len; ii++) {
            StringBuilder sb = new StringBuilder();
            sb.append(lines[ii]);
            sb.append(padding);
            lines[ii] = sb.toString().toCharArray();
        }
        return lines;
    }

    /*
        Rule 1: EQUAL CHARACTER SMUSHING (code value 1)

        Two sub-characters are smushed into a single sub-character
        if they are the same.  This rule does not smush
        hardblanks.  (See rule 6 on hardblanks below)
    */
    private Character hRule1_Smush(char ch1, char ch2, char hardBlank) {
        if (ch1 == ch2 && ch1 != hardBlank) {
            return ch1;
        }
        return null;
    }

    /*
        Rule 2: UNDERSCORE SMUSHING (code value 2)

        An underscore ("_") will be replaced by any of: "|", "/",
        "\", "[", "]", "{", "}", "(", ")", "<" or ">".
    */
    private Character hRule2_Smush(char ch1, char ch2) {
        char[] rule2Str = "|/\\[]{}()<>".toCharArray();
        if (ch1 == '_') {
            if (Arrays.binarySearch(rule2Str, ch2) != -1) {
                return ch2;
            }
        } else if (ch2 == '_') {
            if (Arrays.binarySearch(rule2Str, ch1) != -1) {
                return ch1;
            }
        }
        return null;
    }

    /*
        Rule 3: HIERARCHY SMUSHING (code value 4)

        A hierarchy of six classes is used: "|", "/\", "[]", "{}",
        "()", and "<>".  When two smushing sub-characters are
        from different classes, the one from the latter class
        will be used.
    */
    private Character hRule3_Smush(char ch1, char ch2) {
        char[] rule3Classes = "|/\\[]{}()<>".toCharArray();
        int r3_pos1 = Arrays.binarySearch(rule3Classes, ch1);
        int r3_pos2 = Arrays.binarySearch(rule3Classes, ch2);
        if (r3_pos1 != -1 && r3_pos2 != -1) {
            if (r3_pos1 != r3_pos2 && Math.abs(r3_pos1 - r3_pos2) != 1) {
                return rule3Classes[Math.max(r3_pos1, r3_pos2)];
            }
        }
        return null;
    }

    /*
        Rule 4: OPPOSITE PAIR SMUSHING (code value 8)

        Smushes opposing brackets ("[]" or "]["), braces ("{}" or
        "}{") and parentheses ("()" or ")(") together, replacing
        any such pair with a vertical bar ("|").
    */
    private Character hRule4_Smush(char ch1, char ch2) {
        char[] rule4Str = "[]{}()".toCharArray();
        int r4_pos1 = Arrays.binarySearch(rule4Str, ch1);
        int r4_pos2 = Arrays.binarySearch(rule4Str, ch2);
        if (r4_pos1 != -1 && r4_pos2 != -1) {
            if (Math.abs(r4_pos1 - r4_pos2) <= 1) {
                return '|';
            }
        }
        return null;
    }

    /*
        Rule 5: BIG X SMUSHING (code value 16)

        Smushes "/\" into "|", "\/" into "Y", and "><" into "X".
        Note that "<>" is not smushed in any way by this rule.
        The name "BIG X" is historical; originally all three pairs
        were smushed into "X".
    */
    private Character hRule5_Smush(char ch1, char ch2) {
        char[] rule5Str = "/\\ \\/ ><".toCharArray();
        Map<Character, Character> rule5Hash = new HashMap<Character, Character>();
        rule5Hash.put('0', '|');
        rule5Hash.put('3', 'Y');
        rule5Hash.put('6', 'X');
        int r5_pos1 = Arrays.binarySearch(rule5Str, ch1);
        int r5_pos2 = Arrays.binarySearch(rule5Str, ch2);
        if (r5_pos1 != -1 && r5_pos2 != -1) {
            if ((r5_pos2 - r5_pos1) == 1) {
                return rule5Hash.get(r5_pos1);
            }
        }
        return null;
    }

    /*
        Rule 6: HARDBLANK SMUSHING (code value 32)

        Smushes two hardblanks together, replacing them with a
        single hardblank.  (See "Hardblanks" below.)
    */
    private Character hRule6_Smush(char ch1, char ch2, char hardBlank) {
        if (ch1 == hardBlank && ch2 == hardBlank) {
            return hardBlank;
        }
        return null;
    }

    /*
        Rule 1: EQUAL CHARACTER SMUSHING (code value 256)

            Same as horizontal smushing rule 1.
    */
    private Character vRule1_Smush(char ch1, char ch2) {
        if (ch1 == ch2) {
            return ch1;
        }
        return null;
    }

    /*
        Rule 2: UNDERSCORE SMUSHING (code value 512)

        Same as horizontal smushing rule 2.
    */
    private Character vRule2_Smush(char ch1, char ch2) {
        char[] rule2Str = "|/\\[]{}()<>".toCharArray();
        if (ch1 == '_') {
            if (Arrays.binarySearch(rule2Str,ch2) != -1) {
                return ch2;
            }
        } else if (ch2 == '_') {
            if (Arrays.binarySearch(rule2Str,ch1) != -1) {
                return ch1;
            }
        }
        return null;
    }

    /*
        Rule 3: HIERARCHY SMUSHING (code value 1024)

            Same as horizontal smushing rule 3.
    */
    private Character vRule3_Smush(char ch1, char ch2) {
        char[] rule3Classes = "|/\\[]{}()<>".toCharArray();
        int r3_pos1 = Arrays.binarySearch(rule3Classes, ch1);
        int r3_pos2 = Arrays.binarySearch(rule3Classes, ch2);
        if (r3_pos1 != -1 && r3_pos2 != -1) {
            if (r3_pos1 != r3_pos2 && Math.abs(r3_pos1 - r3_pos2) != 1) {
                return rule3Classes[Math.max(r3_pos1, r3_pos2)];
            }
        }
        return null;
    }

    /*
        Rule 4: HORIZONTAL LINE SMUSHING (code value 2048)

        Smushes stacked pairs of "-" and "_", replacing them with
        a single "=" sub-character.  It does not matter which is
        found above the other.  Note that vertical smushing rule 1
        will smush IDENTICAL pairs of horizontal lines, while this
        rule smushes horizontal lines consisting of DIFFERENT
        sub-characters.
    */
    private Character vRule4_Smush(char ch1, char ch2) {
        if ((ch1 == '-' && ch2 == '_') || (ch1 == '_' && ch2 == '-')) {
            return '=';
        }
        return null;
    }

    /*
        Rule 5: VERTICAL LINE SUPERSMUSHING (code value 4096)

        This one rule is different from all others, in that it
        "supersmushes" vertical lines consisting of several
        vertical bars ("|").  This creates the illusion that
        FIGcharacters have slid vertically against each other.
        Supersmushing continues until any sub-characters other
        than "|" would have to be smushed.  Supersmushing can
        produce impressive results, but it is seldom possible,
        since other sub-characters would usually have to be
        considered for smushing as soon as any such stacked
        vertical lines are encountered.
    */
    private Character vRule5_Smush(char ch1, char ch2) {
        if (ch1 == '|' && ch2 == '|') {
            return '|';
        }
        return null;
    }

    /*
        Universal smushing simply overrides the sub-character from the
        earlier FIGcharacter with the sub-character from the later
        FIGcharacter.  This produces an "overlapping" effect with some
        FIGfonts, wherin the latter FIGcharacter may appear to be "in
        front".
    */

    private Character uni_Smush(char ch1, char ch2, char hardBlank) {
        if (ch2 == ' ') {
            return ch1;
        } else if (ch2 == hardBlank && ch1 != ' ') {
            return ch1;
        } else {
            return ch2;
        }
    }

    /**
     * Covert One Line no longer applicable, mutli-line support added.
     *
     * @deprecated use {@link #convertMessage(InputStream fontFileStream, String message)} instead.
     */
    @Deprecated
    public static String convertOneLine(InputStream fontFileStream, String message) throws IOException {
        return new FigletFont(fontFileStream).convert(message);
    }

    /**
     * Covert One Line no longer applicable, mutli-line support added.
     *
     * @deprecated use {@link #convertMessage(String message)} instead.
     */
    @Deprecated
    public static String convertOneLine(String message) throws IOException {
        return convertOneLine(FigletFont.class.getClassLoader().getResourceAsStream("standard.flf"), message);
    }

    /**
     * Covert One Line no longer applicable, mutli-line support added.
     *
     * @deprecated use {@link #convertMessage(File fontFile, String message)} instead.
     */
    @Deprecated
    public static String convertOneLine(File fontFile, String message) throws IOException {
        return convertOneLine(new FileInputStream(fontFile), message);
    }

    /**
     * Covert One Line no longer applicable, mutli-line support added.
     *
     * @deprecated use {@link #convertMessage(String fontPath, String message)} instead.
     */
    @Deprecated
    public static String convertOneLine(String fontPath, String message) throws IOException {
        InputStream fontStream;
        if (fontPath.startsWith("classpath:")) {
            fontStream = FigletFont.class.getResourceAsStream(fontPath.substring(10));
        } else if (fontPath.startsWith("http://") || fontPath.startsWith("https://")) {
            fontStream = new URL(fontPath).openStream();
        } else {
            fontStream = new FileInputStream(fontPath);
        }
        return convertOneLine(fontStream, message);
    }

    public static String convertMessage(InputStream fontFileStream, String message) throws IOException {
        return new FigletFont(fontFileStream).convert(message);
    }

    public static String convertMessage(String message) throws IOException {
        return convertMessage(FigletFont.class.getClassLoader().getResourceAsStream("standard.flf"), message);
    }


    public static String convertMessage(File fontFile, String message) throws IOException {
        return convertMessage(new FileInputStream(fontFile), message);
    }

    public static String convertMessage(String fontPath, String message) throws IOException {
        InputStream fontStream;
        if (fontPath.startsWith("classpath:")) {
            fontStream = FigletFont.class.getResourceAsStream(fontPath.substring(10));
        } else if (fontPath.startsWith("http://") || fontPath.startsWith("https://")) {
            fontStream = new URL(fontPath).openStream();
        } else {
            fontStream = new FileInputStream(fontPath);
        }
        return convertMessage(fontStream, message);
    }
}
