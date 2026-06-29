package io.github.alexpapagrigoriou.tonycompiler.ast;

import java.util.List;

import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
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
