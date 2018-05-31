package com.github.lalyos.jfiglet;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * FigletFont implementation. A single static method call will create the ascii
 * art in a mulitilne String. FigletFont format is specified at:
 * https://github.com/lalyos/jfiglet/blob/master/figfont.txt
 *
 * <pre>
 * <code>String asciiArt = FigletFont.convertOneLine("hello");</code>
 * </pre>
 *
 * Originally found at: http://www.rigaut.com/benoit/CERN/FigletJava/. Moved to
 * <a href="http://lalyos.github.io/jfiglet/">github.com</a>.
 *
 * @author Benoit Rigaut CERN July 96
 * www.rigaut.com benoit@rigaut.com
 * released with GPL the 13th of november 2000 (my birthday!)
 *
 */
public class FigletFont {
  public char hardblank;
  public int height = -1;
  public int heightWithoutDescenders = -1;
  public int maxLine = -1;
  public int smushMode = -1;
  public Integer printDirection = null;
  public Integer fullLayout = null;
  public Integer codetagCount = null;
  public char font[][][] = null;
  public String fontName = "";

  SmushingRulesToApply smushingRulesToApply;

  final public static int MAX_CHARS = 1024;
  final public static int REGULAR_CHARS = 102;

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
     * @param c
     *            The numerical id of the character.
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
        return new String(font[c][l]).replace(hardblank, ' ');
    }
  }

    /**
     * Creates a FigletFont as specified at: https://github.com/lalyos/jfiglet/blob/master/figfont.txt
     *
     * @param stream
     * @throws java.io.IOException
     */
  public FigletFont(InputStream stream) throws IOException {
    font = new char[MAX_CHARS][][];
    BufferedReader data = null;
    String dummyS;
    int commentLines;
    int charCode;

    try {

    data = new BufferedReader(
        new InputStreamReader(new BufferedInputStream(stream),"UTF-8"));

      dummyS = data.readLine();
      StringTokenizer st = new StringTokenizer(dummyS, " ");
      String s = st.nextToken();
      hardblank = s.charAt(s.length() - 1);
      height = Integer.parseInt(st.nextToken());
      heightWithoutDescenders = Integer.parseInt(st.nextToken());
      maxLine = Integer.parseInt(st.nextToken());
      smushMode = Integer.parseInt(st.nextToken());
      commentLines = Integer.parseInt(st.nextToken());
      if(st.hasMoreTokens()) {
        printDirection = Integer.parseInt(st.nextToken());
      }
      if(st.hasMoreTokens()) {
        fullLayout = Integer.parseInt(st.nextToken());
      }
      if(st.hasMoreTokens()) {
        codetagCount = Integer.parseInt(st.nextToken());
      }

      smushingRulesToApply = Smushing.getRulesToApply(smushMode, fullLayout);

      /*
      * try to read the font name as the first word of the first comment
      * line, but this is not standardized !
      */
      if(commentLines > 0) {
        st = new StringTokenizer(data.readLine(), " ");
        if (st.hasMoreElements())
          fontName = st.nextToken();
      }

      int[] charsTo = new int[REGULAR_CHARS];

      int j = 0;
      for(int c = 32; c <= 126; ++c){
        charsTo[j++] = c;
      }
      for(int additional : new int[]{196, 214, 220, 228, 246, 252, 223}){
        charsTo[j++] = additional;
      }

      for (int i = 0; i < commentLines-1; i++) // skip the comments
        dummyS = data.readLine();
      int charPos = 0;
      while (dummyS!=null) {  // for all the characters
        //System.out.print(i+":");
        if(charPos < REGULAR_CHARS) {
          charCode = charsTo[charPos++];
        } else {
          dummyS = data.readLine();
          if (dummyS == null){
            continue;
          }
          charCode = convertCharCode(dummyS);
        }
        for (int h = 0; h < height; h++) {
          dummyS = data.readLine();
          if (dummyS != null){
            if (h == 0)
              font[charCode] = new char[height][];
            int t = dummyS.length() - 1 - ((h == height-1) ? 1 : 0);
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

  int convertCharCode(String input){
    String codeTag = input.concat(" ").split(" ")[0];
    if (codeTag.matches("^0[xX][0-9a-fA-F]+$")) {
      return Integer.parseInt(codeTag.substring(2), 16);
    } else if (codeTag.matches("^0[0-7]+$")) {
      return Integer.parseInt(codeTag.substring(1), 8);
    } else {
      return Integer.parseInt(codeTag);
    }
  }


  public String convert(String message) {
    char[][] convertedMessage = Smushing.convert(this, message);
    StringBuilder result = new StringBuilder();
    for(int l = 0; l < this.height; l++){
      result.append(convertedMessage[l]);
      result.append('\n');
    }
    return result.toString().replace(hardblank, ' ');
  }

  public static String convertOneLine(InputStream fontFileStream, String message) throws IOException {
    return new FigletFont(fontFileStream).convert(message);
  }

  public static String convertOneLine(String message) throws IOException {
    return convertOneLine(FigletFont.class.getClassLoader().getResourceAsStream("standard.flf"), message);
  }

  public static String convertOneLine(File fontFile, String message) throws IOException {
    return convertOneLine(new FileInputStream(fontFile), message);
  }

  public static String convertOneLine(String fontPath, String message) throws IOException {
    InputStream fontStream = null;
    if (fontPath.startsWith("classpath:")) {
      fontStream = FigletFont.class.getResourceAsStream(fontPath.substring(10));
    } else if (fontPath.startsWith("http://") || fontPath.startsWith("https://")) {
      fontStream = new URL(fontPath).openStream();
    } else {
      fontStream = new FileInputStream(fontPath);
    }
    return convertOneLine(fontStream, message);
  }

  FigletFont withSmushingRulesToApply(SmushingRulesToApply smushingRulesToApply){
    this.smushingRulesToApply = smushingRulesToApply;
    return this;
  }
}
