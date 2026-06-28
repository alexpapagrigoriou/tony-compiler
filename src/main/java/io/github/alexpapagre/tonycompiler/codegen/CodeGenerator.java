package io.github.alexpapagre.tonycompiler.codegen;

import io.github.alexpapagre.tonycompiler.ast.Program;

public final class CodeGenerator {

    private CodeGenerator() {
    }

    public static void generate(Program program) {
        program.accept(new JavaAsmGenerator());
    }
}
