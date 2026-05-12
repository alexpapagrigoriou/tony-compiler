package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IfStmt extends Stmt {
    private final Expr condition;
    private final List<Stmt> thenBody;
    private final List<Elsif> elsifList;
    private final List<Stmt> elseBody;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
