package io.github.alexpapagre.tonycompiler.ast;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NilCheckExpr extends Expr {
    private final Expr expr;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
