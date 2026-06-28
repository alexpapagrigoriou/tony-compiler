package io.github.alexpapagre.tonycompiler.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Expr extends AstNode {
    private Type type;
}
