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
  public char font[][][] = null;
  public String fontName = null;
  final public static int MAX_CHARS = 1024;

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
        String ret = new String(font[c][l]);
        return ret.substring(1);
    }
  }

    /**
     * Creates a FigletFont as specified at: https://github.com/lalyos/jfiglet/blob/master/figfont.txt
     *
     * @param stream
     */
  public FigletFont(InputStream stream) throws IOException {
    font = new char[MAX_CHARS][][];
    DataInputStream data = null;
    String dummyS;
    char dummyC;
    int dummyI;
    int charCode;

    String codeTag;
    try {
      data = new DataInputStream(new BufferedInputStream(stream));

      dummyS = data.readLine();
      StringTokenizer st = new StringTokenizer(dummyS, " ");
      String s = st.nextToken();
      hardblank = s.charAt(s.length() - 1);
      height = Integer.parseInt(st.nextToken());
      heightWithoutDescenders = Integer.parseInt(st.nextToken());
      maxLine = Integer.parseInt(st.nextToken());
      smushMode = Integer.parseInt(st.nextToken());
      dummyI = Integer.parseInt(st.nextToken());

            /*
             * try to read the font name as the first word of the first comment
             * line, but this is not standardized !
             */
      st = new StringTokenizer(data.readLine(), " ");
      if (st.hasMoreElements())
        fontName = st.nextToken();
      else
        fontName = "";

      for (int i = 0; i < dummyI-1; i++) // skip the comments
        dummyS = data.readLine();
      charCode = 31;
      while (dummyS!=null) {  // for all the characters
        //System.out.print(i+":");
        charCode++;
        for (int h = 0; h < height; h++) {
          dummyS = data.readLine();
          if (dummyS != null){
            //System.out.println(dummyS);
            int iNormal = charCode;
            boolean abnormal = true;
            if (h == 0) {
              try {
                  codeTag = dummyS.concat(" ").split(" ")[0];
                  if (codeTag.length()>2&&"x".equals(codeTag.substring(1,2))){
                      charCode = Integer.parseInt(codeTag.substring(2),16);
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
            int t = dummyS.length() - 1 - ((h == height-1) ? 1 : 0);
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
        for (int l = 0; l < this.height; l++) { // for each line
            for (int c = 0; c < message.length(); c++)
                // for each char
                result += this.getCharLineString((int) message.charAt(c), l);
            result += '\n';
        }
        return result;
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
}
