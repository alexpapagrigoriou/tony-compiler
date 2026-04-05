package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CallExpr extends Atom {
    private final String name;
    private final List<Expr> args;
}
