package io.github.alexpapagrigoriou.tonycompiler.ast;

import java.util.ArrayList;
import java.util.List;

import io.github.alexpapagrigoriou.tonycompiler.symbol.VariableSymbol;
import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Formal extends AstNode {
    private final boolean isRef;
    private final Type type;
    private final List<String> names;

    private final List<VariableSymbol> variables = new ArrayList<>();

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
