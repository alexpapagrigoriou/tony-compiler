package io.github.alexpapagre.tonycompiler.ast;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VarDef extends Decl {
    private final Type type;
    private final List<String> names;
}
