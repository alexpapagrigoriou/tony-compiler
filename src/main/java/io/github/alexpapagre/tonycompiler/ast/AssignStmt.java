package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AssignStmt extends Simple {
    private final Atom target;
    private final Expr value;
}
