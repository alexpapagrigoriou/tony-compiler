package io.github.alexpapagrigoriou.tonycompiler.ast;

import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CallStmt extends Simple {
    private final CallExpr call;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
