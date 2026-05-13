package io.github.alexpapagre.tonycompiler.semantics;

import io.github.alexpapagre.tonycompiler.ast.Program;

public class SemanticAnalyzer {
    private final ScopeChecker scopeChecker;

    public SemanticAnalyzer() {
        this.scopeChecker = new ScopeChecker();
    }

    public void analyze(Program program) {
        program.accept(scopeChecker);
    }
}
