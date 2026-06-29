package io.github.alexpapagrigoriou.tonycompiler.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Expr extends AstNode {
    private Type type;
}
