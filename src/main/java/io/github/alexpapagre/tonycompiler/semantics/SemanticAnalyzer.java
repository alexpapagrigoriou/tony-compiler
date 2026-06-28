package io.github.alexpapagre.tonycompiler.semantics;

import io.github.alexpapagre.tonycompiler.ast.Program;

public final class SemanticAnalyzer {

    private SemanticAnalyzer() {
    }

    public static void analyze(Program program) {
        program.accept(new ScopeChecker());
        program.accept(new TypeChecker());
    }
}
