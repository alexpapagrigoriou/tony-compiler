package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CallStmt extends Simple {
    private final CallExpr call;
}
