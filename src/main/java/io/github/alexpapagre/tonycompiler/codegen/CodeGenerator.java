package io.github.alexpapagre.tonycompiler.codegen;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import io.github.alexpapagre.tonycompiler.ast.Program;

public final class CodeGenerator {
    private static final String OUT_DIR = "out";
    private static final String CLASS_NAME = "Main";
    private static final String RUNTIME_PATH = "io/github/alexpapagre/tonycompiler/runtime/";
    private static final String RUNTIME_NAME = "Runtime";

    private CodeGenerator() {
    }

    public static void generate(Program program) {
        try {
            Path outDir = Path.of(OUT_DIR);
            Files.createDirectories(outDir);

            byte[] bytecode = new JavaAsmGenerator(CLASS_NAME, RUNTIME_PATH + RUNTIME_NAME).generate(program);

            Files.write(outDir.resolve(CLASS_NAME + ".class"), bytecode);

            copyRuntime(outDir);
        } catch (IOException e) {
            System.err.println("Failed to write bytecode to file!");
            System.exit(1);
        }
    }

    private static void copyRuntime(Path outDir) {
        try {
            String runtimePath = RUNTIME_PATH + RUNTIME_NAME + ".class";

            try (InputStream in = JavaAsmGenerator.class.getClassLoader().getResourceAsStream(runtimePath)) {
                if (in == null) {
                    throw new RuntimeException(RUNTIME_NAME + ".class not found in classpath");
                }

                Path target = outDir.resolve(runtimePath);

                Files.createDirectories(target.getParent());
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to copy runtime", e);
        }
    }
}
