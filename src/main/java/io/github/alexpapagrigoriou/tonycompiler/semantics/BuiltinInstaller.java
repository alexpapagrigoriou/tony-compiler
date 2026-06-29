package io.github.alexpapagrigoriou.tonycompiler.semantics;

import io.github.alexpapagrigoriou.tonycompiler.ast.*;
import io.github.alexpapagrigoriou.tonycompiler.symbol.BuiltinClass;
import io.github.alexpapagrigoriou.tonycompiler.symbol.FunctionSymbol;
import io.github.alexpapagrigoriou.tonycompiler.symbol.ScopeStack;
import io.github.alexpapagrigoriou.tonycompiler.symbol.VariableSymbol;

import java.util.List;

public final class BuiltinInstaller {

    private BuiltinInstaller() {
    }

    public static void install(ScopeStack scopes) {
        scopes.declare(function(BuiltinClass.IO, "puti", null, parameter("n", new IntType())));
        scopes.declare(function(BuiltinClass.IO, "putb", null, parameter("b", new BoolType())));
        scopes.declare(function(BuiltinClass.IO, "putc", null, parameter("c", new CharType())));
        scopes.declare(function(BuiltinClass.IO, "puts", null, parameter("s", new ArrayType(new CharType()))));

        scopes.declare(function(BuiltinClass.IO, "geti", new IntType()));
        scopes.declare(function(BuiltinClass.IO, "getb", new BoolType()));
        scopes.declare(function(BuiltinClass.IO, "getc", new CharType()));
        scopes.declare(function(BuiltinClass.IO, "gets", null, parameter("n", new IntType()), parameter("s", new ArrayType(new CharType()))));

        scopes.declare(function(BuiltinClass.MATH, "abs", new IntType(), parameter("n", new IntType())));
        scopes.declare(function(BuiltinClass.MATH, "ord", new IntType(), parameter("c", new CharType())));
        scopes.declare(function(BuiltinClass.MATH, "chr", new CharType(), parameter("n", new IntType())));

        scopes.declare(function(BuiltinClass.STRING, "strlen", new IntType(), parameter("s", new ArrayType(new CharType()))));
        scopes.declare(function(BuiltinClass.STRING, "strcmp", new IntType(), parameter("s1", new ArrayType(new CharType())), parameter("s2", new ArrayType(new CharType()))));
        scopes.declare(function(BuiltinClass.STRING, "strcpy", null, parameter("trg", new ArrayType(new CharType())), parameter("src", new ArrayType(new CharType()))));
        scopes.declare(function(BuiltinClass.STRING, "strcat", null, parameter("trg", new ArrayType(new CharType())), parameter("src", new ArrayType(new CharType()))));
    }

    private static FunctionSymbol function(BuiltinClass kind, String name, Type returnType, VariableSymbol... parameters) {
        return new FunctionSymbol(name, returnType, List.of(parameters), kind);
    }

    private static VariableSymbol parameter(String name, Type type) {
        return new VariableSymbol(name, false, type);
    }
}
