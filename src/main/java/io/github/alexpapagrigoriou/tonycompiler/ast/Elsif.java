package io.github.alexpapagrigoriou.tonycompiler.ast;

import java.util.List;

import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Elsif extends AstNode {
    private final Expr condition;
    private final List<Stmt> body;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
