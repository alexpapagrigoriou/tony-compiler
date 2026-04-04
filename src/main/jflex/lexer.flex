package io.github.alexpapagre.tonycompiler;

import java_cup.runtime.Symbol;

%%

%class Lexer
%throws LexerException
%unicode
%line
%column
%cup

%eofval{
    return createSymbol(Symbols.EOF);
%eofval}

%{
    private int blockComments = 0;

    private char resolveChar(String s) {
        if (s.length() == 1) {
            return s.charAt(0);
        }

        return switch (s.charAt(1)) {
            case 't' -> '\t';
            case 'n' -> '\n';
            case 'r' -> '\r';
            case '0' -> '\0';
            case '\\' -> '\\';
            case '\'' -> '\'';
            case '"' -> '"';
            case 'x' -> (char) Integer.parseInt(s.substring(2), 16);
            default -> throw new RuntimeException("Unknown escape sequence: " + s);
        };
    }

    private String resolveString(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '\\') {
                int skip = s.charAt(i + 1) == 'x' ? 4 : 2;
                sb.append(resolveChar(s.substring(i, i + skip)));
                i += skip;
            } else {
                sb.append(s.charAt(i++));
            }
        }
        return sb.toString();
    }

    private Symbol createSymbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol createSymbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

%state BLOCK_COMMENT

letter = [A-Za-z]
digit = [0-9]

common = [^'\"\\\n\r]
hex = [0-9A-Fa-f]
escape_sequence = (\\t|\\n|\\r|\\0|\\\\|\\'|\\\"|\\x{hex}{hex})

id = {letter}({letter}|{digit}|"_"|"?")*
int_const = {digit}+
char_const = \'({common}|{escape_sequence})\'
string_literal = \"({common}|{escape_sequence})*\"

ws = [ \t\n\r]+
line_comment = "%"[^\n]*(\n)?

%%

<YYINITIAL> {
    "and" { return createSymbol(Symbols.AND); }
    "bool" { return createSymbol(Symbols.BOOL); }
    "char" { return createSymbol(Symbols.CHAR); }
    "decl" { return createSymbol(Symbols.DECL); }
    "def" { return createSymbol(Symbols.DEF); }
    "else" { return createSymbol(Symbols.ELSE); }
    "elsif" { return createSymbol(Symbols.ELSIF); }
    "end" { return createSymbol(Symbols.END); }
    "exit" { return createSymbol(Symbols.EXIT); }
    "false" { return createSymbol(Symbols.FALSE); }
    "for" { return createSymbol(Symbols.FOR); }
    "head" { return createSymbol(Symbols.HEAD); }
    "if" { return createSymbol(Symbols.IF); }
    "int" { return createSymbol(Symbols.INT); }
    "list" { return createSymbol(Symbols.LIST); }
    "mod" { return createSymbol(Symbols.MOD); }
    "new" { return createSymbol(Symbols.NEW); }
    "nil" { return createSymbol(Symbols.NIL); }
    "nilQ" { return createSymbol(Symbols.NILQ); }
    "not" { return createSymbol(Symbols.NOT); }
    "or" { return createSymbol(Symbols.OR); }
    "ref" { return createSymbol(Symbols.REF); }
    "return" { return createSymbol(Symbols.RETURN); }
    "skip" { return createSymbol(Symbols.SKIP); }
    "tail" { return createSymbol(Symbols.TAIL); }
    "true" { return createSymbol(Symbols.TRUE); }

    {id} { return createSymbol(Symbols.ID, yytext()); }

    {int_const} {
        try {
            return createSymbol(Symbols.INT_CONST, Integer.parseInt(yytext()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Integer overflow '" + yytext() + "' at line " + (yyline + 1));
        }
    }

    {char_const} {
        String s = yytext();
        s = s.substring(1, s.length() - 1);
        return createSymbol(Symbols.CHAR_CONST, resolveChar(s));
    }

    {string_literal} {
        String s = yytext();
        s = s.substring(1, s.length()-1);
        return createSymbol(Symbols.STRING_LITERAL, resolveString(s));
    }

    "+" { return createSymbol(Symbols.PLUS); }
    "-" { return createSymbol(Symbols.MINUS); }
    "*" { return createSymbol(Symbols.TIMES); }
    "/" { return createSymbol(Symbols.DIV); }
    "#" { return createSymbol(Symbols.HASH); }
    "=" { return createSymbol(Symbols.EQ); }
    "<>" { return createSymbol(Symbols.NEQ); }
    "<" { return createSymbol(Symbols.LT); }
    ">" { return createSymbol(Symbols.GT); }
    "<=" { return createSymbol(Symbols.LEQ); }
    ">=" { return createSymbol(Symbols.GEQ); }

    "(" { return createSymbol(Symbols.LPAREN); }
    ")" { return createSymbol(Symbols.RPAREN); }
    "[" { return createSymbol(Symbols.LBRACKET); }
    "]" { return createSymbol(Symbols.RBRACKET); }
    "," { return createSymbol(Symbols.COMMA); }
    ";" { return createSymbol(Symbols.SEMICOLON); }
    ":" { return createSymbol(Symbols.COLON); }
    ":=" { return createSymbol(Symbols.ASSIGN); }

    {ws} {}
    {line_comment} {}
    "<*" { blockComments = 1; yybegin(BLOCK_COMMENT); }

    [^] { throw new LexerException("Illegal character '" + yytext() + "'", yyline + 1,  yycolumn + 1); }
}

<BLOCK_COMMENT> {
    "<*" { blockComments++; }
    "*>" { blockComments--; if (blockComments == 0) yybegin(YYINITIAL); }

    <<EOF>> { throw new LexerException("Unterminated block comment", yyline + 1); }
    [^] {}
}
