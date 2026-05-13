package io.github.alexpapagre.tonycompiler.semantics;

import io.github.alexpapagre.tonycompiler.ast.*;
import io.github.alexpapagre.tonycompiler.symbol.FunctionSymbol;
import io.github.alexpapagre.tonycompiler.symbol.ScopeStack;
import io.github.alexpapagre.tonycompiler.symbol.VariableSymbol;

import java.util.List;

public final class BuiltinInstaller {

    private BuiltinInstaller() {
    }

    public static void install(ScopeStack scopes) {
        scopes.declare(function("puti", null, parameter("n", new IntType())));
        scopes.declare(function("putb", null, parameter("b", new BoolType())));
        scopes.declare(function("putc", null, parameter("c", new CharType())));
        scopes.declare(function("puts", null, parameter("s", new ArrayType(new CharType()))));

        scopes.declare(function("geti", new IntType()));
        scopes.declare(function("getb", new BoolType()));
        scopes.declare(function("getc", new CharType()));
        scopes.declare(function("gets", null, parameter("n", new IntType()), parameter("s", new ArrayType(new CharType()))));

        scopes.declare(function("abs", new IntType(), parameter("n", new IntType())));
        scopes.declare(function("ord", new IntType(), parameter("c", new CharType())));
        scopes.declare(function("chr", new CharType(), parameter("n", new IntType())));

        scopes.declare(function("strlen", new IntType(), parameter("s", new ArrayType(new CharType()))));
        scopes.declare(function("strcmp", new IntType(), parameter("s1", new ArrayType(new CharType())), parameter("s2", new ArrayType(new CharType()))));
        scopes.declare(function("strcpy", null, parameter("trg", new ArrayType(new CharType())), parameter("src", new ArrayType(new CharType()))));
        scopes.declare(function("strcat", null, parameter("trg", new ArrayType(new CharType())), parameter("src", new ArrayType(new CharType()))));
    }

    private static FunctionSymbol function(String name, Type returnType, VariableSymbol... parameters) {
        return new FunctionSymbol(name, returnType, List.of(parameters));
    }

    private static VariableSymbol parameter(String name, Type type) {
        return new VariableSymbol(name, false, type);
    }
}
