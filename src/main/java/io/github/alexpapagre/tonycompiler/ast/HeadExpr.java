package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HeadExpr extends Expr {
    private final Expr expr;
}
