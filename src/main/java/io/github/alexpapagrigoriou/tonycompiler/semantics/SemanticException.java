package io.github.alexpapagrigoriou.tonycompiler.semantics;

public class SemanticException extends RuntimeException {

    public SemanticException(String message) {
        super("Semantic error: " + message);
    }
}
