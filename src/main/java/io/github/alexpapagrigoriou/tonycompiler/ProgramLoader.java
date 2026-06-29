package io.github.alexpapagrigoriou.tonycompiler;

import java.io.FileNotFoundException;
import java.io.FileReader;

import io.github.alexpapagrigoriou.tonycompiler.ast.Program;

public final class ProgramLoader {

    private ProgramLoader() {
    }

    public static Program loadProgram(String inputFile) {
        try {
            return parseSource(readFile(inputFile));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static FileReader readFile(String filename) throws FileNotFoundException {
        return new FileReader(filename);
    }

    private static Program parseSource(FileReader fileReader) throws Exception {
        return (Program) new Parser(new Lexer(fileReader)).parse().value;
    }

}
