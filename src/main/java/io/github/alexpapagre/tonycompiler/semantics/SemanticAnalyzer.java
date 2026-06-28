package io.github.alexpapagre.tonycompiler.semantics;

import io.github.alexpapagre.tonycompiler.ast.Program;

public class SemanticAnalyzer {

    public SemanticAnalyzer() {
    }

    public void analyze(Program program) {
        program.accept(new ScopeChecker());
        program.accept(new TypeChecker());
    }
}
