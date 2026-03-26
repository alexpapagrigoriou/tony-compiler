package io.github.alexpapagre.tony;

%%

%class Lexer
%unicode
%integer
%line
%column

%{
    private int blockComments = 0;

    public static class Token {
        public static final int T_and = 1;
        public static final int T_bool = 2;
        public static final int T_char = 3;
        public static final int T_decl = 4;
        public static final int T_def = 5;
        public static final int T_else = 6;
        public static final int T_elseif = 7;
        public static final int T_end = 8;
        public static final int T_exit = 9;
        public static final int T_false = 10;
        public static final int T_for = 11;
        public static final int T_head = 12;
        public static final int T_if = 13;
        public static final int T_int = 14;
        public static final int T_list = 15;
        public static final int T_mod = 16;
        public static final int T_new = 17;
        public static final int T_nil = 18;
        public static final int T_nilQ = 19;
        public static final int T_not = 20;
        public static final int T_or = 21;
        public static final int T_ref = 22;
        public static final int T_return = 23;
        public static final int T_skip = 24;
        public static final int T_tail = 25;
        public static final int T_true = 26;

        public static final int T_id = 27;
        public static final int T_int_const = 28;
        public static final int T_char_const = 29;
        public static final int T_string_literal = 30;

        public static final int T_plus = 31;
        public static final int T_minus = 32;
        public static final int T_times = 33;
        public static final int T_div = 34;
        public static final int T_hash = 35;
        public static final int T_eq = 36;
        public static final int T_neq = 37;
        public static final int T_lt = 38;
        public static final int T_gt = 39;
        public static final int T_le = 40;
        public static final int T_ge = 41;

        public static final int T_lparen = 42;
        public static final int T_rparen = 43;
        public static final int T_lbracket = 44;
        public static final int T_rbracket = 45;
        public static final int T_comma = 46;
        public static final int T_semicolon = 47;
        public static final int T_colon = 48;
        public static final int T_assign = 49;
    };
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
    "and" { return Token.T_and; }
    "bool" { return Token.T_bool; }
    "char" { return Token.T_char; }
    "decl" { return Token.T_decl; }
    "def" { return Token.T_def; }
    "else" { return Token.T_else; }
    "elseif" { return Token.T_elseif; }
    "end" { return Token.T_end; }
    "exit" { return Token.T_exit; }
    "false" { return Token.T_false; }
    "for" { return Token.T_for; }
    "head" { return Token.T_head; }
    "if" { return Token.T_if; }
    "int" { return Token.T_int; }
    "list" { return Token.T_list; }
    "mod" { return Token.T_mod; }
    "new" { return Token.T_new; }
    "nil" { return Token.T_nil; }
    "nilQ" { return Token.T_nilQ; }
    "not" { return Token.T_not; }
    "or" { return Token.T_or; }
    "ref" { return Token.T_ref; }
    "return" { return Token.T_return; }
    "skip" { return Token.T_skip; }
    "tail" { return Token.T_tail; }
    "true" { return Token.T_true; }

    {id} { return Token.T_id; }
    {int_const} { return Token.T_int_const; }
    {char_const} { return Token.T_char_const; }
    {string_literal} { return Token.T_string_literal; }

    "+" { return Token.T_plus; }
    "-" { return Token.T_minus; }
    "*" { return Token.T_times; }
    "/" { return Token.T_div; }
    "#" { return Token.T_hash; }
    "=" { return Token.T_eq; }
    "<>" { return Token.T_neq; }
    "<" { return Token.T_lt; }
    ">" { return Token.T_gt; }
    "<=" { return Token.T_le; }
    ">=" { return Token.T_ge; }

    "(" { return Token.T_lparen; }
    ")" { return Token.T_rparen; }
    "[" { return Token.T_lbracket; }
    "]" { return Token.T_rbracket; }
    "," { return Token.T_comma; }
    ";" { return Token.T_semicolon; }
    ":" { return Token.T_colon; }
    ":=" { return Token.T_assign; }

    {ws} {}
    {line_comment} {}
    "<*" { blockComments = 1; yybegin(BLOCK_COMMENT); }
}

<BLOCK_COMMENT> {
    "<*" { blockComments++; }
    "*>" { blockComments--; if (blockComments == 0) yybegin(YYINITIAL); }
    [^] {}
}
