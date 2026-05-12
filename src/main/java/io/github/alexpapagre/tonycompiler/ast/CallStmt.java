package io.github.alexpapagre.tonycompiler.ast;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CallStmt extends Simple {
    private final CallExpr call;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
