package io.github.alexpapagre.tonycompiler;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Usage: java -jar tony.jar <file>");
            System.err.println("Usage: java -jar tony.jar");
            System.exit(1);
        }

        Lexer lexer = null;
        if (args.length == 1) {
            if (!args[0].endsWith(".tony")) {
                System.err.println("File must be a '.tony' file!");
                System.exit(1);
            }

            try {
                lexer = new Lexer(new FileReader(args[0]));
            } catch (IOException e) {
                System.err.println("Could not open file: " + args[0]);
                System.exit(1);
            }
        } else {
            lexer = new Lexer(new InputStreamReader(System.in));
        }

        Parser parser = new Parser(lexer);
        try {
            parser.parse();
        } catch (LexerException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }
}
