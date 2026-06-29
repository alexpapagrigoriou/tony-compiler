package io.github.alexpapagrigoriou.tonycompiler.codegen;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import io.github.alexpapagrigoriou.tonycompiler.ast.Program;
import io.github.alexpapagrigoriou.tonycompiler.symbol.BuiltinClass;

public final class CodeGenerator {
    private static final String OUT_DIR = "out";
    private static final String CLASS_NAME = "Main";

    private static final String RUNTIME_PACKAGE = "io/github/alexpapagrigoriou/tonycompiler/runtime/";
    private static final String LIST_NAME = "TonyList";

    private CodeGenerator() {
    }

    public static void generate(Program program) {
        try {
            Path outDir = Path.of(OUT_DIR);
            Files.createDirectories(outDir);

            byte[] bytecode = new JavaAsmGenerator(CLASS_NAME, RUNTIME_PACKAGE, LIST_NAME).generate(program);

            Files.write(outDir.resolve(CLASS_NAME + ".class"), bytecode);

            copyClass(outDir, RUNTIME_PACKAGE + LIST_NAME + ".class");

            for (BuiltinClass builtin : BuiltinClass.values()) {
                copyClass(outDir, RUNTIME_PACKAGE + builtin.getName() + ".class");
            }
        } catch (IOException e) {
            System.err.println("Failed to write bytecode to file!");
            System.exit(1);
        }
    }

    private static void copyClass(Path outDir, String classPath) {
        try {
            try (InputStream in = JavaAsmGenerator.class.getClassLoader().getResourceAsStream(classPath)) {
                if (in == null) {
                    throw new RuntimeException(classPath + " not found in classpath");
                }
                Path target = outDir.resolve(classPath);
                Files.createDirectories(target.getParent());
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy " + classPath, e);
        }
    }
}
