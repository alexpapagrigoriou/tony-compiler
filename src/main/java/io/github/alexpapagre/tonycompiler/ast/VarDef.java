package io.github.alexpapagre.tonycompiler.ast;

import java.util.ArrayList;
import java.util.List;

import io.github.alexpapagre.tonycompiler.symbol.VariableSymbol;
import io.github.alexpapagre.tonycompiler.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VarDef extends Decl {
    private final Type type;
    private final List<String> names;

    private final List<VariableSymbol> variables = new ArrayList<>();

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
