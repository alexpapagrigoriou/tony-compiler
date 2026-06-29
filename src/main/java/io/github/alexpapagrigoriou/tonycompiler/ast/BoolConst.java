package io.github.alexpapagrigoriou.tonycompiler.ast;

import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoolConst extends Expr {
    private final boolean value;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
