package io.github.alexpapagre.tonycompiler.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArrayType extends Type {
    private final Type elementType;
}
