package io.github.alexpapagrigoriou.tonycompiler.symbol;

import java.util.ArrayDeque;
import java.util.Deque;

public class ScopeStack {
    private final Deque<Scope> stack = new ArrayDeque<>();

    public ScopeStack() {
        enterScope();
    }

    public void enterScope() {
        stack.push(new Scope());
    }

    public void exitScope() {
        stack.pop();
    }

    public boolean declare(Symbol symbol) {
        return stack.peek().declare(symbol);
    }

    public Symbol resolve(String name) {
        for (Scope scope : stack) {
            Symbol symbol = scope.resolve(name);
            if (symbol != null) {
                return symbol;
            }
        }

        return null;
    }
}
