package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BinaryExpr extends Expr {
    private final BinaryOp op;
    private final Expr left;
    private final Expr right;
}
