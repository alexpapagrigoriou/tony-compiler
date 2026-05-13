package io.github.alexpapagre.tonycompiler.symbol;

import java.util.List;

import io.github.alexpapagre.tonycompiler.ast.Type;
import lombok.Getter;

@Getter
public class FunctionSymbol extends Symbol {
    private final List<VariableSymbol> parameters;
    private final Type returnType;

    public FunctionSymbol(String name, List<VariableSymbol> parameters, Type returnType) {
        super(name);
        this.parameters = parameters;
        this.returnType = returnType;
    }
}
