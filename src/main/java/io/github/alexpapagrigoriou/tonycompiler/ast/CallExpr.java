package io.github.alexpapagrigoriou.tonycompiler.ast;

import java.util.List;

import io.github.alexpapagrigoriou.tonycompiler.symbol.FunctionSymbol;
import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CallExpr extends Atom {
    private final String name;
    private final List<Expr> args;

    private FunctionSymbol function;

    public void setFunction(FunctionSymbol function) {
        this.function = function;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
