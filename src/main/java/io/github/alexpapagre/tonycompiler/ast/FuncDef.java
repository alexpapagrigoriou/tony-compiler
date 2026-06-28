package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import io.github.alexpapagre.tonycompiler.symbol.FunctionSymbol;
import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FuncDef extends Decl {
    private final Header header;
    private final List<Decl> declarations;
    private final List<Stmt> statements;

    private FunctionSymbol symbol;

    public void setSymbol(FunctionSymbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
