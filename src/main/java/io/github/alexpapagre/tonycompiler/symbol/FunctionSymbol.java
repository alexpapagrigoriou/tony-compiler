package io.github.alexpapagre.tonycompiler.symbol;

import java.util.List;

import io.github.alexpapagre.tonycompiler.ast.Type;
import lombok.Getter;

@Getter
public class FunctionSymbol extends Symbol {
    private final Type returnType;
    private final List<VariableSymbol> parameters;
    private final BuiltinClass kind;

    private boolean declared = false;
    private boolean defined = false;;

    public FunctionSymbol(String name, Type returnType, List<VariableSymbol> parameters, BuiltinClass kind) {
        super(name);

        this.returnType = returnType;
        this.parameters = parameters;
        this.kind = kind;
    }

    public void setDeclared() {
        this.declared = true;
    }

    public void setDefined() {
        this.defined = true;
    }

    public boolean isBuiltin() {
        return kind != null;
    }

    public String getBuiltinName() {
        return kind.getName();
    }
}
