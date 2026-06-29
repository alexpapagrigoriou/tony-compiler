package io.github.alexpapagre.tonycompiler.symbol;

import lombok.Getter;

@Getter
public enum BuiltinClass {
    IO("TonyIO"),
    MATH("TonyMath"),
    STRING("TonyString");

    private final String name;

    BuiltinClass(String name) {
        this.name = name;
    }
}
