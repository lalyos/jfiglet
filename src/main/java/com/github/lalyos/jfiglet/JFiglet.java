package com.github.lalyos.jfiglet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import static com.github.lalyos.jfiglet.FigletFont.convertOneLine;

public class JFiglet {

    public static void main(String[] args) throws IOException {
        final Iterator<String> arguments = Arrays.asList(args).iterator();
        String font = null;
        String text = null;
        PrintStream out = System.out;

        while (arguments.hasNext()) {
            final String arg = arguments.next();
            if ("-f".equals(arg)) {
                font = requireNextArgument(arguments, arg);
            } else if ("-o".equals(arg)) {
                out = new PrintStream(new FileOutputStream(requireNextArgument(arguments, arg)));
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
            out.println(convertOneLine(text));
        } else {
            out.println(convertOneLine(font, text));
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
        pw.println("Usage: java -jar jfiglet.jar [-f FLF] [-o OUTFILE] MESSAGE");
        pw.println("Prints MESSAGE to OUTFILE (default stdout) as ASCII art using Figlet font");
        pw.println("Example: java -jar jfiglet.jar -f \"/opt/myfont.flf\" \"Hello World\"");
        pw.println("\n");
        pw.println("Figlet font:");
        pw.println("  -f  FLF is font file location within file system, java classpath or www.");
        pw.println("      When FLF starts with `http://'|`https://' file will be fetched from WWW,");
        pw.println("      if FLF starts with `classpath:' then it will be looked for in JRE classpath,");
        pw.println("      otherwise FLF is path to FLF file in file system.");
        return result.toString();
    }
}
