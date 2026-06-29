package io.github.alexpapagrigoriou.tonycompiler.ast;

import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;

public class NilType extends Type {

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
