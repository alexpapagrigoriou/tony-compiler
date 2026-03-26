package io.github.alexpapagre.tony;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Usage: tony");
            System.out.println("Usage: tony [file]");
            System.exit(1);
        }

        Lexer lexer;
        if (args.length == 1) {
            if (!args[0].endsWith(".tony")) {
                System.out.println("File must be a '.tony' file!");
                System.exit(1);
            }

            try {
                lexer = new Lexer(new FileReader(args[0]));
            } catch (IOException e) {
                System.out.println("Could not open file: " + args[0]);
                System.exit(1);
                return;
            }
        } else {
            lexer = new Lexer(new InputStreamReader(System.in));
        }

        try {
            int token = lexer.yylex();
            while (token != Lexer.YYEOF) {
                System.out.println("Token type: " + token + " lexeme: " + lexer.yytext());
                token = lexer.yylex();
            }
        } catch (IOException e) {
            System.out.println("Error :" + e.getMessage());
        }
    }
}
