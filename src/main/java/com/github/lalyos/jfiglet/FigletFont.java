package com.github.lalyos.jfiglet;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * FigletFont Java
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
  
  public char[][][] getFont() {
    return font;
  }
  
  public char[][] getChar(int c) {
    return font[c];
  }

  public String getCharLineString(int c, int l) {
    if (font[c][l] == null)
      return null;
    else {
        String ret = new String(font[c][l]);
        return ret.substring(1);
    }
  }

  public FigletFont(InputStream stream) {
    font = new char[MAX_CHARS][][];
    DataInputStream data;
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

      /* try to read the font name as the first word of
         the first comment line, but this is not standardized ! */
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
    } catch (IOException e)  {
      System.out.println("IO Error: " + e.getMessage());
    }
  }
  
  public static String convertOneLine(String message)  {
        String result = "";

        FigletFont figletFont;
        try {
            InputStream stream = FigletFont.class.getClassLoader().getResourceAsStream("standard.flf");
            figletFont = new FigletFont(stream);
            for (int l = 0; l < figletFont.height; l++) { // for each line
                for (int c = 0; c < message.length(); c++)
                    // for each char
                    result += figletFont.getCharLineString((int) message.charAt(c), l);
                result += '\n';
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
  
  public static void main(String[] args) throws Exception {
      String text = "JFIGLET";
      if (args.length < 1) {
          System.out.println("Usage: java -jar jfiglet.jar <text-to-convert>");
      } else {
          text = args[0];
      }
      System.out.println(convertOneLine(text));
    }
  

}
