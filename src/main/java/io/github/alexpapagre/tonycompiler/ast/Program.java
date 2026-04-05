package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Program extends AstNode {
    private final FuncDef mainFunction;
}
