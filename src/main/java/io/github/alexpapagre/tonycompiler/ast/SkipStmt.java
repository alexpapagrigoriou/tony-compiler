package io.github.alexpapagre.tonycompiler.ast;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;

public class SkipStmt extends Simple {

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
