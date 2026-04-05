package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Elsif extends AstNode {
    private final Expr condition;
    private final List<Stmt> body;
}
