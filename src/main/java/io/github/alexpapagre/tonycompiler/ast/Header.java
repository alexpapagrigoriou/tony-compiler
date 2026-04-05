package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Header extends AstNode {
    private final Type returnType;
    private final String name;
    private final List<Formal> parameters;
}
