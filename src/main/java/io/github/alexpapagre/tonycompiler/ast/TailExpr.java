package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TailExpr extends Expr {
    private final Expr expr;
}
