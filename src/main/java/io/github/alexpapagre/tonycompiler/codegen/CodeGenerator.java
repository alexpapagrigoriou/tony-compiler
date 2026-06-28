package io.github.alexpapagre.tonycompiler.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.github.alexpapagre.tonycompiler.ast.Program;

public final class CodeGenerator {
    private static final String CLASS_NAME = "Main";

    private CodeGenerator() {
    }

    public static void generate(Program program) {
        byte[] bytecode = new JavaAsmGenerator(CLASS_NAME).generate(program);

        try {
            Files.write(Path.of("target/" + CLASS_NAME + ".class"), bytecode);
        } catch (IOException e) {
            System.err.println("Failed to write bytecode to file!");
            System.exit(1);
        }
    }
}
