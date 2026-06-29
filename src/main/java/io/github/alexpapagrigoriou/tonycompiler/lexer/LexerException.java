package io.github.alexpapagrigoriou.tonycompiler.lexer;

public class LexerException extends RuntimeException {

    public LexerException(String message, int line, int column) {
        super("Lexer error at line " + line + ", column " + column + ": " + message);
    }

    public LexerException(String message, int line) {
        super("Lexer error at line " + line + ": " + message);
    }
}
