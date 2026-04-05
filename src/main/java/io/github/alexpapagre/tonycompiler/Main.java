package io.github.alexpapagre.tonycompiler;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.github.alexpapagre.tonycompiler.ast.Program;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar tony-compiler.jar <file>");
            System.exit(1);
        }

        if (!args[0].endsWith(".tony")) {
            System.err.println("File must be a '.tony' file!");
            System.exit(1);
        }

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(args[0]);
        } catch (IOException e) {
            System.err.println("Could not open file: " + args[0]);
            System.exit(1);
        }

        Lexer lexer = new Lexer(fileReader);
        Parser parser = new Parser(lexer);
        Program program = null;
        try {
            program = (Program) parser.parse().value;
        } catch (LexerException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }
}
