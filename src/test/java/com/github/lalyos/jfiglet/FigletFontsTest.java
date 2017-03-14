package com.github.lalyos.jfiglet;

import org.junit.Ignore;
import org.junit.Test;

public class FigletFontsTest {

    @Ignore
    @Test
    public void itLoadAllTheEnum() {
        FigletFonts[] values = FigletFonts.values();
        for (FigletFonts figletFonts : values) {
            System.out.println(figletFonts.name());
//            String result = figletFonts.convert("The quick brown fox jumps over the lazy dog");
            
            String result = figletFonts.convert("A B C");
            System.out.println(result);
        }
    }
    
    @Test
    public void generateMarkdown(){
        FigletFonts[] values = FigletFonts.values();
        for (FigletFonts figletFonts : values) {
            String name = figletFonts.name();
            System.out.println("* [" + name + "](#" + name +")");
        }
        for (FigletFonts figletFonts : values) {
            System.out.println("## " + figletFonts.name());
//            String result = figletFonts.convert("The quick brown fox jumps over the lazy dog");
            
            String result = figletFonts.convert("A B C");
            System.out.println("```" + result + "```");
        }
    }
}
