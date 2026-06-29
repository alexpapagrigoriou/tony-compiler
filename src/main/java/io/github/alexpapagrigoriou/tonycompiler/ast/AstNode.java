package io.github.alexpapagrigoriou.tonycompiler.ast;

import io.github.alexpapagrigoriou.tonycompiler.visitor.Visitor;

public abstract class AstNode {

    public abstract <T> T accept(Visitor<T> visitor);
}
