package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArrayAccess extends Atom {
    private final Expr array;
    private final Expr index;
}
