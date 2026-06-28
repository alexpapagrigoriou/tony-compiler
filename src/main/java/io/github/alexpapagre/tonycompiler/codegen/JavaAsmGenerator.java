package io.github.alexpapagre.tonycompiler.codegen;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import io.github.alexpapagre.tonycompiler.visitor.TraversalVisitor;

public class JavaAsmGenerator extends TraversalVisitor<Void> {
    private ClassWriter cw;
    private MethodVisitor mw;
}
