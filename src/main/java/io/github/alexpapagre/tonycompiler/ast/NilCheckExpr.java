package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NilCheckExpr extends Expr {
    private final Expr expr;
}
