package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CallExpr extends Atom {
    private final String name;
    private final List<Expr> args;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
