package io.github.alexpapagrigoriou.tonycompiler.ast;

import java.util.List;

import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Header extends AstNode {
    private final Type returnType;
    private final String name;
    private final List<Formal> parameters;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
