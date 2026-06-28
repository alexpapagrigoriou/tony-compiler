package io.github.alexpapagre.tonycompiler.ast;

import io.github.alexpapagre.tonycompiler.symbol.VariableSymbol;
import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VarExpr extends Atom {
    private final String name;

    private VariableSymbol symbol;

    public void setSymbol(VariableSymbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
