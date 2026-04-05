package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuncDecl extends Decl {
    private final Header header;
}
