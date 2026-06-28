package io.github.alexpapagre.tonycompiler.ast;

import io.github.alexpapagre.tonycompiler.symbol.FunctionSymbol;
import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FuncDecl extends Decl {
    private final Header header;

    private FunctionSymbol symbol;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    public void setSymbol(FunctionSymbol symbol) {
        this.symbol = symbol;
    }
}
