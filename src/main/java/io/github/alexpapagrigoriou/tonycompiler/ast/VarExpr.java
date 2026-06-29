package io.github.alexpapagrigoriou.tonycompiler.ast;

import io.github.alexpapagrigoriou.tonycompiler.symbol.VariableSymbol;
import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VarExpr extends Atom {
    private final String name;

    private VariableSymbol variable;

    public void setVariable(VariableSymbol variable) {
        this.variable = variable;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
