package io.github.alexpapagrigoriou.tonycompiler.symbol;

import lombok.Getter;

@Getter
public abstract class Symbol {
    private final String name;

    public Symbol(String name) {
        this.name = name;
    }
}
