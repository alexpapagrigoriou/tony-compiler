package io.github.alexpapagre.tony;

import java_cup.runtime.Symbol;

%%

%class Lexer
%unicode
%line
%column
%cup

%eofval{
    return createSymbol(Symbols.EOF);
%eofval}

%{
    private int blockComments = 0;

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
    {int_const} { return createSymbol(Symbols.T_int_const, yytext()); }
    {char_const} { return createSymbol(Symbols.T_char_const, yytext()); }
    {string_literal} { return createSymbol(Symbols.T_string_literal, yytext()); }

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
}

<BLOCK_COMMENT> {
    "<*" { blockComments++; }
    "*>" { blockComments--; if (blockComments == 0) yybegin(YYINITIAL); }
    [^] {}
}
