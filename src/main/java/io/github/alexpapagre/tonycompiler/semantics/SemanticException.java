package io.github.alexpapagre.tonycompiler.semantics;

public class SemanticException extends RuntimeException {

    public SemanticException(String message) {
        super("Semantic error: " + message);
    }
}
