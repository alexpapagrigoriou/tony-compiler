package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuncDef extends Decl {
    private final Header header;
    private final List<Decl> declarations;
    private final List<Stmt> statements;
}
