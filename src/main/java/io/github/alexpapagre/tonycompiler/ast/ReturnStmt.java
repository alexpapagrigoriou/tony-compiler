package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnStmt extends Stmt {
    private final Expr expr;
}
