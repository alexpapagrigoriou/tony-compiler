package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuncDef extends Decl {
    private final Header header;
    private final List<Decl> declarations;
    private final List<Stmt> statements;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
