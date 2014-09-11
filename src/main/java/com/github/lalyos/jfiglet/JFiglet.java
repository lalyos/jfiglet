package com.github.lalyos.jfiglet;

import java.io.IOException;

import static com.github.lalyos.jfiglet.FigletFont.convertOneLine;

public class JFiglet {

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            System.out.println(convertOneLine(args[0]));
        } else if (args.length == 3) {
            if ("-f".equals(args[0])) {
                System.out.println(convertOneLine(args[1], args[2]));
                return;
            } else if ("-F".equals(args[0])) {
                System.out.println(
                    convertOneLine(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(args[1]),
                        args[2]
                    )
                );
                return;
            }
        }

        usage();
    }

    private static void usage() {
        System.out.println("Usage: java -jar jfiglet.jar [-f FLF_PATH|-F FLF_PATH] MESSAGE");
        System.out.println("Prints MESSAGE to stdout as ASCII art using Figlet font");
        System.out.println("Example: java -jar jfiglet.jar -f \"/opt/myfont.flf\" \"Hello World\"");
        System.out.println("\n");
        System.out.println("Figlet font:");
        System.out.println("  -f  FLF_PATH is file path within file system");
        System.out.println("  -F  FLF_PATH is classpath resource");
    }
}
