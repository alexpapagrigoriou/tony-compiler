package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HashExpr extends Expr {
    private final Expr left;
    private final Expr right;
}
