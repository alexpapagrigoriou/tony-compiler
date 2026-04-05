package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NewArrayExpr extends Expr {
    private final Type type;
    private final Expr size;
}
