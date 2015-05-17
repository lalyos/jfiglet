package com.github.lalyos.jfiglet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FigletFontTest {

    @Test
    public void testConvertMessageJFiglet() throws Exception{
        String asciiArt = new FigletFont(
                FigletFontTest.class.getClassLoader().getResourceAsStream("slant.flf"))
                .convert("jfiglet");
        assertEquals("       _  ____ _         __       __ \n" +
                        "      (_)/ __/(_)____ _ / /___   / /_\n" +
                        "     / // /_ / // __ `// // _ \\ / __/\n" +
                        "    / // __// // /_/ // //  __// /_  \n" +
                        " __/ //_/  /_/ \\__, //_/ \\___/ \\__/  \n" +
                        "/___/         /____/                 ",
                asciiArt.replaceAll("\r\n", "\n").replaceAll("\r", "\n"));

    }


    @Test
    public void testConvertMessage() throws Exception{
        String asciiArt = FigletFont.convertMessage(FigletFontTest.class.getClassLoader().getResourceAsStream("slant.flf"), "a");
        assertEquals("        \n" +
                        "  ____ _\n" +
                        " / __ `/\n" +
                        "/ /_/ / \n" +
                        "\\__,_/  \n" +
                        "        \n",
                asciiArt.replaceAll("\r\n", "\n").replaceAll("\r", "\n"));

    }

    @Test
    public void testConvertMultiline() throws Exception{
        String asciiArt = FigletFont.convertMessage(FigletFontTest.class.getClassLoader().getResourceAsStream("slant.flf"), "ab\nc");
        assertEquals("          __  \n" +
                        "  ____ _ / /_ \n" +
                        " / __ `// __ \\\n" +
                        "/ /_/ // /_/ /\n" +
                        "\\__,_//_.___/ \n" +
                        "              \n" +
                        "              \n" +
                        "  _____       \n" +
                        " / ___/       \n" +
                        "/ /__         \n" +
                        "\\___/         \n" +
                        "              \n",
                asciiArt.replaceAll("\r\n", "\n").replaceAll("\r", "\n"));

    }

    @Test
    public void testConvertMultilineSmush() throws Exception{

        String asciiArt = new FigletFont(
                    FigletFontTest.class.getClassLoader().getResourceAsStream("slant.flf"))
                .overridehLayout(FittingRules.LAYOUT.FULL_WIDTH)
                .overridevLayout(FittingRules.LAYOUT.CONTROLLED_SMUSHING)
                .convert("ab\nc");
        assertEquals("            __  \n" +
                        "  ____ _   / /_ \n" +
                        " / __ `/  / __ \\\n" +
                        "/ /_/ /  / /_/ /\n" +
                        "\\__,_/  /_.___/ \n" +
                        "                \n" +
                        "  _____         \n" +
                        " / ___/         \n" +
                        "/ /__           \n" +
                        "\\___/           \n" +
                        "                \n",
                asciiArt.replaceAll("\r\n", "\n").replaceAll("\r", "\n"));

    }
}
