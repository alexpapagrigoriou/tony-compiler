package io.github.alexpapagre.tonycompiler;

import java.io.FileNotFoundException;
import java.io.FileReader;

import io.github.alexpapagre.tonycompiler.ast.Program;

public final class ProgramLoader {

    private ProgramLoader() {
    }

    public static Program loadProgram(String[] args) {
        try {
            return parseSource(readFile(extractFilename(args)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static String extractFilename(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar tony-compiler.jar <file>");
            System.exit(1);
        }

        if (!args[0].endsWith(".tony")) {
            System.err.println("File must be a '.tony' file!");
            System.exit(1);
        }

        return args[0];
    }

    private static FileReader readFile(String filename) throws FileNotFoundException {
        return new FileReader(filename);
    }

    private static Program parseSource(FileReader fileReader) throws Exception {
        return (Program) new Parser(new Lexer(fileReader)).parse().value;
    }

}
