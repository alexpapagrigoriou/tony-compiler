package io.github.alexpapagre.tonycompiler.ast;

import io.github.alexpapagre.tonycompiler.visitor.Visitor;

public abstract class AstNode {

    public abstract <T> T accept(Visitor<T> visitor);
}
