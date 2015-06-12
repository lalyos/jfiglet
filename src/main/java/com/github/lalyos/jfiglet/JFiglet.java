package com.github.lalyos.jfiglet;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

import static com.github.lalyos.jfiglet.FigletFont.convertMessage;

public class JFiglet {

    public static void main(String[] args) throws IOException {
        final Iterator<String> arguments = Arrays.asList(args).iterator();
        String font = null;
        String text = null;
        PrintStream out = System.out;
        FittingRules.OVERRIDE_LAYOUT vertical = FittingRules.OVERRIDE_LAYOUT.DEFAULT;
        FittingRules.OVERRIDE_LAYOUT horizontal = FittingRules.OVERRIDE_LAYOUT.DEFAULT;
        while(arguments.hasNext()){
            String arg = arguments.next();
            if (arg.equals("-f")){
                font = requireNextArgument(arguments, arg);
            } else if (arg.equals("-v")){
                try {
                    vertical = FittingRules.OVERRIDE_LAYOUT.valueOf(requireNextArgument(arguments, arg));
                } catch (IllegalArgumentException ex){
                    usage();
                }
            } else if (arg.equals("-h")){
                try {
                    horizontal = FittingRules.OVERRIDE_LAYOUT.valueOf(requireNextArgument(arguments, arg));
                } catch (IllegalArgumentException ex){
                    usage();
                }
            } else {
                if (!arguments.hasNext()) {
                    text = arg;
                }
                break;
            }
        }
        if (text == null) {
            System.err.println(usage());
        } else if (font == null) {
            out.println(convertMessage(text, horizontal, vertical));
        } else {
            out.println(convertMessage(font, text, horizontal, vertical));
        }
        out.close();
    }

    private static String requireNextArgument(Iterator<String> arguments, String sw) {
        if (arguments.hasNext()) {
            return arguments.next();
        }
        throw new IllegalStateException("Argument required after " + sw);
    }

    private static String usage() {
        final StringWriter result = new StringWriter();
        final PrintWriter pw = new PrintWriter(result);
        pw.println("Usage: java -jar jfiglet.jar [-f FLF] [-h HORIZONTAL SMUSHING] [-v VERTICAL SMUSHING] MESSAGE");
        pw.println("Prints MESSAGE to stdout as ASCII art using Figlet font");
        pw.println("Example: java -jar jfiglet.jar -f \"/opt/myfont.flf\" -h \"full_width\" -v \"fitting\" \"Hello World\"");
        pw.println("\n");
        pw.println("Figlet font arguments:");
        pw.println("  -f  (Optional) FLF is font file location within file system, java classpath or www.");
        pw.println("                 When FLF starts with `http://'|`https://' file will be fetched from WWW,");
        pw.println("                 if FLF starts with `classpath:' then it will be looked for in JRE classpath,");
        pw.println("                 otherwise FLF is path to FLF file in file system.");
        pw.println("  -h  (Optional) Used to override the horizontal smushing of the font, available options:");
        pw.println("                 " + Arrays.toString(FittingRules.OVERRIDE_LAYOUT.values()).toLowerCase());
        pw.println("  -v  (Optional) Used to override the vertical smushing of the font, available options:");
        pw.println("                 " + Arrays.toString(FittingRules.OVERRIDE_LAYOUT.values()).toLowerCase());
        pw.println("Usage: java -jar jfiglet.jar [-f FLF] [-o OUTFILE] MESSAGE");
        return result.toString();
    }
}
