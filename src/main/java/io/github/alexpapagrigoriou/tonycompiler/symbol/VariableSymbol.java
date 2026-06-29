package io.github.alexpapagrigoriou.tonycompiler.symbol;

import io.github.alexpapagrigoriou.tonycompiler.ast.Type;
import lombok.Getter;

@Getter
public class VariableSymbol extends Symbol {
    private final boolean isRef;
    private final Type type;

    private int slot;

    public VariableSymbol(String name, boolean isRef, Type type) {
        super(name);
        this.isRef = isRef;
        this.type = type;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
