package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Formal extends AstNode {
    private final boolean isRef;
    private final Type type;
    private final List<String> names;
}
