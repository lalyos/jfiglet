package com.github.lalyos.jfiglet;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.StringTokenizer;

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

    private static enum CAN_VERTICAL_SMUSH {VALID, INVALID, END}

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

    public FigletFont overrideHorizontalLayout(FittingRules.OVERRIDE_LAYOUT layout) {
        if (layout != null) {
            fittingRules.overrideHorizontalLayout(layout);
        }
        return this;
    }

    public FigletFont overrideVerticalLayout(FittingRules.OVERRIDE_LAYOUT layout) {
        if (layout != null) {
            fittingRules.overrideVerticalLayout(layout);
        }
        return this;
    }


    /**
     * Creates a FigletFont as specified at: https://github.com/lalyos/jfiglet/blob/master/figfont.txt
     *
     * @param stream font input stream.
     */

    public FigletFont(InputStream stream) throws IOException {
        font = new char[MAX_CHARS][][];
        BufferedReader data = null;
        String dummyS;
        int dummyI;
        int charCode;

        String codeTag;
        try {
            data = new BufferedReader(new InputStreamReader(stream));

            dummyS = data.readLine();
            StringTokenizer st = new StringTokenizer(dummyS, " ");
            int count = st.countTokens();
            String s = st.nextToken();
            hardblank = s.charAt(s.length() - 1);
            height = Integer.parseInt(st.nextToken());
            heightWithoutDescenders = Integer.parseInt(st.nextToken());
            maxLine = Integer.parseInt(st.nextToken());
            smushMode = Integer.parseInt(st.nextToken());
            dummyI = Integer.parseInt(st.nextToken());

            printDirection = (count >= 6) ? Integer.parseInt(st.nextToken()) : 0;
            fullLayout = (count >= 7) ? Integer.parseInt(st.nextToken()) : null;
            codeTagCount = (count >= 8) ? Integer.parseInt(st.nextToken()) : null;
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
                            font[charCode][h][l] = a;
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
        message = message.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
        int maxLines = message.split("\n").length;
        char[][][] figLines = new char[maxLines][height][];
        int lineNumber = 0;
        for (String line : message.split("\n")) {
            char[][] r = new char[height][];
            for (int c = 0; c < line.length(); c++) {
                char[][] figChar = getChar(line.charAt(c));
                int overlap = 0;
                if (! fittingRules.isHorizontalFullWidthEnabled()) {
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
        for (char[] line : output) {
            result = result + String.valueOf(line).replace(hardblank, ' ') + System.getProperty("line.separator");
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
                    if (fittingRules.isHorizontalFittingEnabled()) {
                        piece2 += SmushingRules.uni_Smush(ch1, ch2, hardblank);
                    } else if (fittingRules.isHorizontalSmushingEnabled()) {
                        piece2 += SmushingRules.uni_Smush(ch1, ch2, hardblank);
                    } else {
                        // Controlled Smushing
                        Character nextCh = (fittingRules.isHorizontalRule1Enabled()) ? SmushingRules.hRule1_Smush(ch1, ch2, hardblank) : null;
                        nextCh = (nextCh == null && fittingRules.isHorizontalRule2Enabled()) ? SmushingRules.hRule2_Smush(ch1, ch2) : nextCh;
                        nextCh = (nextCh == null && fittingRules.isHorizontalRule3Enabled()) ? SmushingRules.hRule3_Smush(ch1, ch2) : nextCh;
                        nextCh = (nextCh == null && fittingRules.isHorizontalRule4Enabled()) ? SmushingRules.hRule4_Smush(ch1, ch2) : nextCh;
                        nextCh = (nextCh == null && fittingRules.isHorizontalRule5Enabled()) ? SmushingRules.hRule5_Smush(ch1, ch2) : nextCh;
                        nextCh = (nextCh == null && fittingRules.isHorizontalRule6Enabled()) ? SmushingRules.hRule6_Smush(ch1, ch2, hardblank) : nextCh;
                        nextCh = (nextCh == null) ? SmushingRules.uni_Smush(ch1, ch2, hardblank) : nextCh;
                        piece2 += nextCh;
                    }
                } else {
                    piece2 += SmushingRules.uni_Smush(ch1, ch2, hardblank);
                }
            }

            if (overlap >= len2) {
                piece3 = "";
            } else {
                piece3 = txt2 != null ? String.valueOf(txt2).substring(overlap, overlap + Math.max(0, len2 - overlap)) : "";
            }
            outputFig[i] = (piece1 + piece2 + piece3).toCharArray();

        }
        return outputFig;
    }

    private int getHorizontalSmushLength(char[] txt1, char[] txt2) {
        if (fittingRules.isHorizontalFullWidthEnabled()) {
            return 0;
        }
        int len1 = txt1 != null ? txt1.length : 0;
        int len2 = txt2 != null ? txt2.length : 0;
        int curDist = 1;
        boolean breakAfter = false;
        if (len1 == 0) {
            return 0;
        }

        distCal:
        while (curDist <= len1) {
            String seg1 = String.valueOf(txt1).substring(len1 - curDist, (len1 - curDist) + curDist);
            String seg2 = String.valueOf(txt2).substring(0, Math.min(curDist, len2));
            for (int ii = 0; ii < Math.min(curDist, len2); ii++) {
                char ch1 = seg1.substring(ii, ii + 1).charAt(0);
                char ch2 = seg2.substring(ii, ii + 1).charAt(0);
                if (ch1 != ' ' && ch2 != ' ') {
                    if (fittingRules.isHorizontalFittingEnabled()) {
                        curDist = curDist - 1;
                        break distCal;
                    } else if (fittingRules.isHorizontalFittingEnabled()) {
                        if (ch1 == hardblank || ch2 == hardblank) {
                            curDist = curDist - 1; // universal smushing does not smush hardblanks
                        }
                        break distCal;
                    } else {
                        breakAfter = true; // we know we need to break, but we need to check if our smushing rules will allow us to smush the overlapped characters

                        boolean validSmush = (fittingRules.isHorizontalRule1Enabled()) && SmushingRules.hRule1_Smush(ch1, ch2, hardblank) != null;
                        validSmush = (!validSmush && fittingRules.isHorizontalRule2Enabled()) ? SmushingRules.hRule2_Smush(ch1, ch2) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.isHorizontalRule3Enabled()) ? SmushingRules.hRule3_Smush(ch1, ch2) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.isHorizontalRule4Enabled()) ? SmushingRules.hRule4_Smush(ch1, ch2) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.isHorizontalRule5Enabled()) ? SmushingRules.hRule5_Smush(ch1, ch2) != null : validSmush;
                        validSmush = (!validSmush && fittingRules.isHorizontalRule6Enabled()) ? SmushingRules.hRule6_Smush(ch1, ch2, hardblank) != null : validSmush;

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
        return Math.min(len1, curDist);
    }

    private CAN_VERTICAL_SMUSH canVerticalSmush(char[] txt1, char[] txt2) {
        if (fittingRules.isVerticalFullWidthEnabled()) {
            return CAN_VERTICAL_SMUSH.INVALID;
        }
        int len1 = txt1 != null ? txt1.length : 0;
        int len2 = txt2 != null ? txt2.length : 0;
        int len = Math.min(len1,len2);
        char ch1, ch2;
        boolean endSmush = false;
        if (len1 == 0 || len2 == 0) {
            return CAN_VERTICAL_SMUSH.INVALID;
        }

        for (int ii = 0; ii < len; ii++) {
            ch1 = txt1[ii];
            ch2 = txt2[ii];
            if (ch1 != ' ' && ch2 != ' ') {
                if (fittingRules.isVerticalFittingEnabled()) {
                    return CAN_VERTICAL_SMUSH.INVALID;
                } else if (fittingRules.isVerticalSmushingEnabled()) {
                    return CAN_VERTICAL_SMUSH.END;
                } else {
                    if (SmushingRules.vRule5_Smush(ch1, ch2) != null) {
                        endSmush = false;
                        continue;
                    } // rule 5 allow for "super" smushing, but only if we're not already ending this smush
                    boolean validSmush = (fittingRules.isVerticalRule1Enabled()) && SmushingRules.vRule1_Smush(ch1, ch2) != null;
                    validSmush = (!validSmush && fittingRules.isVerticalRule2Enabled()) ? SmushingRules.vRule2_Smush(ch1, ch2) != null : validSmush;
                    validSmush = (!validSmush && fittingRules.isVerticalRule3Enabled()) ? SmushingRules.vRule3_Smush(ch1, ch2) != null : validSmush;
                    validSmush = (!validSmush && fittingRules.isVerticalRule4Enabled()) ? SmushingRules.vRule4_Smush(ch1, ch2) != null : validSmush;
                    endSmush = true;
                    if (!validSmush) {
                        return CAN_VERTICAL_SMUSH.INVALID;
                    }
                }
            }
        }
        if (endSmush) {
            return CAN_VERTICAL_SMUSH.END;
        } else {
            return CAN_VERTICAL_SMUSH.VALID;
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
        int curDist = 1;
        while (curDist <= maxDist) {

            char[][] subLines1 = Arrays.copyOfRange(lines1, Math.max(0, len1 - curDist),len1);
            char[][] subLines2 = Arrays.copyOfRange(lines2, 0, Math.min(maxDist, curDist));

            int slen = subLines2.length;
            CAN_VERTICAL_SMUSH result = null;
            for (int ii = 0; ii < slen; ii++) {
                CAN_VERTICAL_SMUSH ret = canVerticalSmush(subLines1[ii], subLines2[ii]);
                if (ret == CAN_VERTICAL_SMUSH.END) {
                    result = ret;
                } else if (ret == CAN_VERTICAL_SMUSH.INVALID) {
                    result = ret;
                    break;
                } else {
                    if (result == null) {
                        result = CAN_VERTICAL_SMUSH.VALID;
                    }
                }
            }

            if (result == CAN_VERTICAL_SMUSH.INVALID) {
                curDist--;
                break;
            }
            if (result == CAN_VERTICAL_SMUSH.END) {
                break;
            }
            if (result == CAN_VERTICAL_SMUSH.VALID) {
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
                if (fittingRules.isVerticalFittingEnabled()) {
                    result[ii] = SmushingRules.uni_Smush(ch1, ch2, hardblank);
                } else if (fittingRules.isVerticalSmushingEnabled()) {
                    result[ii] = SmushingRules.uni_Smush(ch1, ch2, hardblank);
                } else {
                    Character validSmush = (fittingRules.isVerticalRule5Enabled()) ? SmushingRules.vRule5_Smush(ch1, ch2) : null;
                    validSmush = (validSmush != null && fittingRules.isVerticalRule1Enabled()) ? SmushingRules.vRule1_Smush(ch1, ch2) : validSmush;
                    validSmush = (validSmush != null && fittingRules.isVerticalRule2Enabled()) ? SmushingRules.vRule2_Smush(ch1, ch2) : validSmush;
                    validSmush = (validSmush != null && fittingRules.isVerticalRule3Enabled()) ? SmushingRules.vRule3_Smush(ch1, ch2) : validSmush;
                    validSmush = (validSmush != null && fittingRules.isVerticalRule4Enabled()) ? SmushingRules.vRule4_Smush(ch1, ch2) : validSmush;
                    result[ii] = validSmush;
                }
            } else {
                result[ii] = SmushingRules.uni_Smush(ch1, ch2, hardblank);
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
        char[] line;
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
            String line = String.valueOf(lines[ii]) + padding;
            lines[ii] = line.toCharArray();
        }
        return lines;
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

    public static String convertMessage(InputStream fontFileStream, String message, FittingRules.OVERRIDE_LAYOUT horizontalLayout, FittingRules.OVERRIDE_LAYOUT verticalLayout) throws IOException {
        return new FigletFont(fontFileStream).overrideHorizontalLayout(horizontalLayout).overrideVerticalLayout(verticalLayout).convert(message);
    }

    public static String convertMessage(InputStream fontFileStream, String message) throws IOException {
        return convertMessage(fontFileStream,message, FittingRules.OVERRIDE_LAYOUT.DEFAULT, FittingRules.OVERRIDE_LAYOUT.DEFAULT);
    }

    public static String convertMessage(String message, FittingRules.OVERRIDE_LAYOUT horizontalLayout, FittingRules.OVERRIDE_LAYOUT verticalLayout) throws IOException {
        return convertMessage(FigletFont.class.getClassLoader().getResourceAsStream("standard.flf"), message, horizontalLayout, verticalLayout);
    }

    public static String convertMessage(String message) throws IOException {
        return convertMessage(message, FittingRules.OVERRIDE_LAYOUT.DEFAULT, FittingRules.OVERRIDE_LAYOUT.DEFAULT);
    }

    public static String convertMessage(File fontFile, String message, FittingRules.OVERRIDE_LAYOUT horizontalLayout, FittingRules.OVERRIDE_LAYOUT verticalLayout) throws IOException {
        return convertMessage(new FileInputStream(fontFile), message,horizontalLayout, verticalLayout );
    }

    public static String convertMessage(File fontFile, String message) throws IOException {
        return convertMessage(fontFile, message, FittingRules.OVERRIDE_LAYOUT.DEFAULT, FittingRules.OVERRIDE_LAYOUT.DEFAULT);
    }



    public static String convertMessage(String fontPath, String message, FittingRules.OVERRIDE_LAYOUT horizontalLayout, FittingRules.OVERRIDE_LAYOUT verticalLayout) throws IOException {
        InputStream fontStream;
        if (fontPath.startsWith("classpath:")) {
            fontStream = FigletFont.class.getResourceAsStream(fontPath.substring(10));
        } else if (fontPath.startsWith("http://") || fontPath.startsWith("https://")) {
            fontStream = new URL(fontPath).openStream();
        } else {
            fontStream = new FileInputStream(fontPath);
        }
        return convertMessage(fontStream, message, horizontalLayout, verticalLayout);
    }

    public static String convertMessage(String fontPath, String message) throws IOException {
        return convertMessage(fontPath,message, FittingRules.OVERRIDE_LAYOUT.DEFAULT, FittingRules.OVERRIDE_LAYOUT.DEFAULT);
    }
}
