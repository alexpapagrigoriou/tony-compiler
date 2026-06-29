package io.github.alexpapagrigoriou.tonycompiler.ast;

import io.github.alexpapagrigoriou.tonycompiler.symbol.FunctionSymbol;
import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FuncDecl extends Decl {
    private final Header header;

    private FunctionSymbol function;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    public void setFunction(FunctionSymbol function) {
        this.function = function;
    }
}
