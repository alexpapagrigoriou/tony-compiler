package io.github.alexpapagre.tony;

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
    "and" { return createSymbol(Symbols.T_and); }
    "bool" { return createSymbol(Symbols.T_bool); }
    "char" { return createSymbol(Symbols.T_char); }
    "decl" { return createSymbol(Symbols.T_decl); }
    "def" { return createSymbol(Symbols.T_def); }
    "else" { return createSymbol(Symbols.T_else); }
    "elseif" { return createSymbol(Symbols.T_elseif); }
    "end" { return createSymbol(Symbols.T_end); }
    "exit" { return createSymbol(Symbols.T_exit); }
    "false" { return createSymbol(Symbols.T_false); }
    "for" { return createSymbol(Symbols.T_for); }
    "head" { return createSymbol(Symbols.T_head); }
    "if" { return createSymbol(Symbols.T_if); }
    "int" { return createSymbol(Symbols.T_int); }
    "list" { return createSymbol(Symbols.T_list); }
    "mod" { return createSymbol(Symbols.T_mod); }
    "new" { return createSymbol(Symbols.T_new); }
    "nil" { return createSymbol(Symbols.T_nil); }
    "nilQ" { return createSymbol(Symbols.T_nilQ); }
    "not" { return createSymbol(Symbols.T_not); }
    "or" { return createSymbol(Symbols.T_or); }
    "ref" { return createSymbol(Symbols.T_ref); }
    "return" { return createSymbol(Symbols.T_return); }
    "skip" { return createSymbol(Symbols.T_skip); }
    "tail" { return createSymbol(Symbols.T_tail); }
    "true" { return createSymbol(Symbols.T_true); }

    {id} { return createSymbol(Symbols.T_id, yytext()); }

    {int_const} {
        try {
            return createSymbol(Symbols.T_int_const, Integer.parseInt(yytext()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Integer overflow '" + yytext() + "' at line " + (yyline + 1));
        }
    }

    {char_const} {
        String s = yytext();
        s = s.substring(1, s.length() - 1);
        return createSymbol(Symbols.T_char_const, resolveChar(s));
    }

    {string_literal} {
        String s = yytext();
        s = s.substring(1, s.length()-1);
        return createSymbol(Symbols.T_string_literal, resolveString(s));
    }

    "+" { return createSymbol(Symbols.T_plus); }
    "-" { return createSymbol(Symbols.T_minus); }
    "*" { return createSymbol(Symbols.T_times); }
    "/" { return createSymbol(Symbols.T_div); }
    "#" { return createSymbol(Symbols.T_hash); }
    "=" { return createSymbol(Symbols.T_eq); }
    "<>" { return createSymbol(Symbols.T_neq); }
    "<" { return createSymbol(Symbols.T_lt); }
    ">" { return createSymbol(Symbols.T_gt); }
    "<=" { return createSymbol(Symbols.T_le); }
    ">=" { return createSymbol(Symbols.T_ge); }

    "(" { return createSymbol(Symbols.T_lparen); }
    ")" { return createSymbol(Symbols.T_rparen); }
    "[" { return createSymbol(Symbols.T_lbracket); }
    "]" { return createSymbol(Symbols.T_rbracket); }
    "," { return createSymbol(Symbols.T_comma); }
    ";" { return createSymbol(Symbols.T_semicolon); }
    ":" { return createSymbol(Symbols.T_colon); }
    ":=" { return createSymbol(Symbols.T_assign); }

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
