package com.github.lalyos.jfiglet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import static com.github.lalyos.jfiglet.FigletFont.convertMessage;

public class JFiglet {

    public static void main(String[] args) throws IOException {
        Iterator<String> arguments = Arrays.asList(args).iterator();
        String font = null;
        String text = null;
        FittingRules.OVERRIDE_LAYOUT vertical = FittingRules.OVERRIDE_LAYOUT.DEFAULT;
        FittingRules.OVERRIDE_LAYOUT horizontal = FittingRules.OVERRIDE_LAYOUT.DEFAULT;
        while(arguments.hasNext()){
            String arg = arguments.next();
            if (arg.equals("-f")){
                font = arguments.next();
            } else if (arg.equals("-v")){
                try {
                    vertical = FittingRules.OVERRIDE_LAYOUT.valueOf(arguments.next().toUpperCase());
                } catch (IllegalArgumentException ex){
                    usage();
                }
            } else if (arg.equals("-h")){
                try {
                    horizontal = FittingRules.OVERRIDE_LAYOUT.valueOf(arguments.next().toUpperCase());
                } catch (IllegalArgumentException ex){
                    usage();
                }
            } else {
                text = arg;
            }
        }
        if (text != null && font == null) {
            System.out.println(convertMessage(text, horizontal, vertical));
        } else if (text != null) {
            System.out.println(convertMessage(font, text, horizontal, vertical));
        } else {
            usage();
        }
    }

    private static void usage() {
        System.out.println("Usage: java -jar jfiglet.jar [-f FLF] [-h HORIZONTAL SMUSHING] [-v VERTICAL SMUSHING] MESSAGE");
        System.out.println("Prints MESSAGE to stdout as ASCII art using Figlet font");
        System.out.println("Example: java -jar jfiglet.jar -f \"/opt/myfont.flf\" -h \"full_width\" -v \"fitting\" \"Hello World\"");
        System.out.println("\n");
        System.out.println("Figlet font arguments:");
        System.out.println("  -f  (Optional) FLF is font file location within file system, java classpath or www.");
        System.out.println("                 When FLF starts with `http://'|`https://' file will be fetched from WWW,");
        System.out.println("                 if FLF starts with `classpath:' then it will be looked for in JRE classpath,");
        System.out.println("                 otherwise FLF is path to FLF file in file system.");
        System.out.println("  -h  (Optional) Used to override the horizontal smushing of the font, available options:");
        System.out.println("                 " + Arrays.toString(FittingRules.OVERRIDE_LAYOUT.values()).toLowerCase());
        System.out.println("  -v  (Optional) Used to override the vertical smushing of the font, available options:");
        System.out.println("                 " + Arrays.toString(FittingRules.OVERRIDE_LAYOUT.values()).toLowerCase());
        System.exit(1);
    }
}
