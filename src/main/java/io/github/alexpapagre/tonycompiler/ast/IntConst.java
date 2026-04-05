package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IntConst extends Expr {
    private final int value;
}
