package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IfStmt extends Stmt {
    private final Expr condition;
    private final List<Stmt> thenBody;
    private final List<Elsif> elsifList;
    private final List<Stmt> elseBody;
}
