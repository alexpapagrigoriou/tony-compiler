package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ForStmt extends Stmt {
    private final List<Simple> init;
    private final Expr condition;
    private final List<Simple> update;
    private final List<Stmt> body;
}
