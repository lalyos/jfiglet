package com.github.lalyos.jfiglet;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class FigletFontTest {

    private static final String LINE_ENDING = "\n";

    @Test
    public void testConstruct() throws Exception {
        FigletFont ff = new FigletFont(FigletFontTest.class.getClassLoader().getResourceAsStream("standard.flf"));
        assertEquals("Standard", ff.fontName);
        assertEquals('$', ff.hardblank);
        assertEquals(6, ff.height);
        assertEquals(5, ff.heightWithoutDescenders);
        assertEquals(16, ff.maxLine);
        assertEquals(15, ff.smushMode);
        //space
        assertEquals(' ', ff.font[32][0][0]);
        assertEquals(' ', ff.font[32][0][1]);
        assertEquals(' ', ff.font[32][1][0]);
        assertEquals(' ', ff.font[32][1][1]);
        assertEquals(' ', ff.font[32][2][0]);
        assertEquals(' ', ff.font[32][2][1]);
        assertEquals(' ', ff.font[32][3][0]);
        assertEquals(' ', ff.font[32][3][1]);
        assertEquals(' ', ff.font[32][4][0]);
        assertEquals(' ', ff.font[32][4][1]);
        assertEquals(' ', ff.font[32][5][0]);
        assertEquals(' ', ff.font[32][5][1]);
    }

    @Test
    public void testConstructNoName() throws Exception {
        FigletFont ff = new FigletFont(FigletFontTest.class.getClassLoader().getResourceAsStream("standard-without-name.flf"));
        assertEquals("", ff.fontName);
        assertEquals('$', ff.hardblank);
        assertEquals(6, ff.height);
        assertEquals(5, ff.heightWithoutDescenders);
        assertEquals(16, ff.maxLine);
        assertEquals(15, ff.smushMode);
        //space
        assertEquals(' ', ff.font[32][0][0]);
        assertEquals(' ', ff.font[32][0][1]);
        assertEquals(' ', ff.font[32][1][0]);
        assertEquals(' ', ff.font[32][1][1]);
        assertEquals(' ', ff.font[32][2][0]);
        assertEquals(' ', ff.font[32][2][1]);
        assertEquals(' ', ff.font[32][3][0]);
        assertEquals(' ', ff.font[32][3][1]);
        assertEquals(' ', ff.font[32][4][0]);
        assertEquals(' ', ff.font[32][4][1]);
        assertEquals(' ', ff.font[32][5][0]);
        assertEquals(' ', ff.font[32][5][1]);
    }

    @Test
    public void testGetFont() throws Exception {
        FigletFont ff = new FigletFont(FigletFontTest.class.getClassLoader().getResourceAsStream("standard.flf"));
        assertEquals(' ', ff.getFont()[32][0][0]);
        assertEquals(' ', ff.getFont()[32][0][1]);
        assertEquals(' ', ff.getFont()[32][1][0]);
        assertEquals(' ', ff.getFont()[32][1][1]);
        assertEquals(' ', ff.getFont()[32][2][0]);
        assertEquals(' ', ff.getFont()[32][2][1]);
        assertEquals(' ', ff.getFont()[32][3][0]);
        assertEquals(' ', ff.getFont()[32][3][1]);
        assertEquals(' ', ff.getFont()[32][4][0]);
        assertEquals(' ', ff.getFont()[32][4][1]);
        assertEquals(' ', ff.getFont()[32][5][0]);
        assertEquals(' ', ff.getFont()[32][5][1]);
    }

    @Test
    public void testGetChar() throws Exception {
        FigletFont ff = new FigletFont(FigletFontTest.class.getClassLoader().getResourceAsStream("standard.flf"));
        assertEquals(' ', ff.getChar(32)[0][0]);
        assertEquals(' ', ff.getChar(32)[0][1]);
        assertEquals(' ', ff.getChar(32)[1][0]);
        assertEquals(' ', ff.getChar(32)[1][1]);
        assertEquals(' ', ff.getChar(32)[2][0]);
        assertEquals(' ', ff.getChar(32)[2][1]);
        assertEquals(' ', ff.getChar(32)[3][0]);
        assertEquals(' ', ff.getChar(32)[3][1]);
        assertEquals(' ', ff.getChar(32)[4][0]);
        assertEquals(' ', ff.getChar(32)[4][1]);
        assertEquals(' ', ff.getChar(32)[5][0]);
        assertEquals(' ', ff.getChar(32)[5][1]);
    }

    @Test
    public void testGetCharLineString() throws Exception {
        FigletFont ff = new FigletFont(FigletFontTest.class.getClassLoader().getResourceAsStream("standard.flf"));
        assertEquals("  ", ff.getCharLineString(32,0));
        assertEquals("  ", ff.getCharLineString(32,1));
        assertEquals("  ", ff.getCharLineString(32,2));
        assertEquals("  ", ff.getCharLineString(32,3));
        assertEquals("  ", ff.getCharLineString(32,4));
        assertEquals("  ", ff.getCharLineString(32,5));
    }
    @Test
    public void testGetCharLineStringWithNullLine() throws Exception {
        FigletFont ff = new FigletFont(FigletFontTest.class.getClassLoader().getResourceAsStream("standard-without-name-and-null-line.flf"));
        assertEquals("  ", ff.getCharLineString(32,0));
        assertEquals("  ", ff.getCharLineString(32,1));
        assertEquals("  ", ff.getCharLineString(32,2));
        assertEquals("  ", ff.getCharLineString(32,3));
        assertEquals("  ", ff.getCharLineString(32,4));
        assertNull(ff.getCharLineString(32,5));
    }

    @Test
    public void testConvert() throws Exception {
        String asciiArt = new FigletFont(FigletFontTest.class.getClassLoader().getResourceAsStream("standard.flf")).convert("jfiglet");
        assertEqualsJFiglet(asciiArt);
    }

    @Test
    public void testConvertOneLine() throws Exception {
        String asciiArt = FigletFont.convertOneLine("jfiglet");
        assertEqualsJFiglet(asciiArt);
    }

    @Test
    public void testConvertOneLineInputStream() throws Exception {
        String asciiArt = FigletFont.convertOneLine(FigletFontTest.class.getClassLoader().getResourceAsStream("standard.flf"),"jfiglet");
        assertEqualsJFiglet(asciiArt);
    }

    @Test
    public void testConvertOneLineFontPathClasspath() throws Exception {
        String asciiArt = FigletFont.convertOneLine("classpath:/standard.flf","jfiglet");
        assertEqualsJFiglet(asciiArt);
    }

    @Test
    public void testConvertOneLineFontPathFile() throws Exception {
        File file = new File(FigletFontTest.class.getClassLoader().getResource("standard.flf").getFile());
        String asciiArt = FigletFont.convertOneLine(file.getAbsolutePath(),"jfiglet");
        assertEqualsJFiglet(asciiArt);
    }

    @Test
    public void testConvertOneLineFile() throws Exception {
        File file = new File(FigletFontTest.class.getClassLoader().getResource("standard.flf").getFile());
        String asciiArt = FigletFont.convertOneLine(file,"jfiglet");
        assertEqualsJFiglet(asciiArt);
    }

    private void assertEqualsJFiglet(String asciiArt){
        assertEquals("    _    __   _           _          _   " + LINE_ENDING +
                        "   (_)  / _| (_)   __ _  | |   ___  | |_ " + LINE_ENDING +
                        "   | | | |_  | |  / _` | | |  / _ \\ | __|" + LINE_ENDING +
                        "   | | |  _| | | | (_| | | | |  __/ | |_ " + LINE_ENDING +
                        "  _/ | |_|   |_|  \\__, | |_|  \\___|  \\__|" + LINE_ENDING +
                        " |__/             |___/                  " + LINE_ENDING,
                asciiArt);
    }

}