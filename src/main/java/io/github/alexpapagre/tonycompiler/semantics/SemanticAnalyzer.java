package io.github.alexpapagre.tonycompiler.semantics;

import io.github.alexpapagre.tonycompiler.ast.Program;

public class SemanticAnalyzer {
    private final ScopeChecker scopeChecker;
    private final TypeChecker typeChecker;

    public SemanticAnalyzer() {
        this.scopeChecker = new ScopeChecker();
        this.typeChecker = new TypeChecker();
    }

    public void analyze(Program program) {
        program.accept(scopeChecker);
        program.accept(typeChecker);
    }
}
