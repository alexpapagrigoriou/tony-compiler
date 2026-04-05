package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnaryExpr extends Expr {
    private final UnaryOp op;
    private final Expr expr;
}
