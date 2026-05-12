package io.github.alexpapagre.tonycompiler.ast;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BinaryExpr extends Expr {
    private final BinaryOp op;
    private final Expr left;
    private final Expr right;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
