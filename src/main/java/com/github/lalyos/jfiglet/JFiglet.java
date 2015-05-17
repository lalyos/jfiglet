package com.github.lalyos.jfiglet;

import java.io.IOException;

import static com.github.lalyos.jfiglet.FigletFont.convertMessage;

public class JFiglet {

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            System.out.println(convertMessage(args[0]));
        } else if (args.length == 3 && "-f".equals(args[0])) {
            System.out.println(convertMessage(args[1], args[2]));
        } else {
            usage();
        }
    }

    private static void usage() {
        System.out.println("Usage: java -jar jfiglet.jar [-f FLF] MESSAGE");
        System.out.println("Prints MESSAGE to stdout as ASCII art using Figlet font");
        System.out.println("Example: java -jar jfiglet.jar -f \"/opt/myfont.flf\" \"Hello World\"");
        System.out.println("\n");
        System.out.println("Figlet font:");
        System.out.println("  -f  FLF is font file location within file system, java classpath or www.");
        System.out.println("      When FLF starts with `http://'|`https://' file will be fetched from WWW,");
        System.out.println("      if FLF starts with `classpath:' then it will be looked for in JRE classpath,");
        System.out.println("      otherwise FLF is path to FLF file in file system.");
    }
}
