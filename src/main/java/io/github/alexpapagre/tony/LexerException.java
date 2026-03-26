package io.github.alexpapagre.tony;

public class LexerException extends Exception {
    private final int line, column;

    public LexerException(String message, int line, int column) {
        super("Lexer error at line " + line + ", column " + column + ": " + message);

        this.line = line;
        this.column = column;
    }

    public LexerException(String message, int line) {
        super("Lexer error at line " + line + ": " + message);

        this.line = line;
        this.column = 0;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
