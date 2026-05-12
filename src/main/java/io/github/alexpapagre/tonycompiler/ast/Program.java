package io.github.alexpapagre.tonycompiler.ast;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Program extends AstNode {
    private final FuncDef mainFunction;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
