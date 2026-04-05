package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoolConst extends Expr {
    private final boolean value;
}
