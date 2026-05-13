package io.github.alexpapagre.tonycompiler.symbol;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, Symbol> symbols = new HashMap<>();

    public Scope() {
    }

    public boolean declare(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) {
            return false;
        }

        symbols.put(symbol.getName(), symbol);
        return true;
    }

    public Symbol resolve(String name) {
        return symbols.get(name);
    }
}
