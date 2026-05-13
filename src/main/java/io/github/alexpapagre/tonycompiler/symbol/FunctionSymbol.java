package io.github.alexpapagre.tonycompiler.symbol;

import java.util.List;

import io.github.alexpapagre.tonycompiler.ast.Type;
import lombok.Getter;

@Getter
public class FunctionSymbol extends Symbol {
    private final Type returnType;
    private final List<VariableSymbol> parameters;

    public FunctionSymbol(String name, Type returnType, List<VariableSymbol> parameters) {
        super(name);
        this.returnType = returnType;
        this.parameters = parameters;
    }
}
