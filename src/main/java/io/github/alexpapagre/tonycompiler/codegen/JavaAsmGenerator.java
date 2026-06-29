package io.github.alexpapagre.tonycompiler.codegen;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import io.github.alexpapagre.tonycompiler.ast.*;
import io.github.alexpapagre.tonycompiler.symbol.*;
import io.github.alexpapagre.tonycompiler.visitor.TraversalVisitor;

public class JavaAsmGenerator extends TraversalVisitor<Void> {
    private final String className;
    private final String runtimePath;

    private ClassWriter cw;
    private MethodVisitor mv;

    private int nextLocal;

    private FunctionSymbol currentFunction;

    public JavaAsmGenerator(String className, String runtimePath) {
        this.className = className;
        this.runtimePath = runtimePath;
    }

    public byte[] generate(Program program) {
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);

        MethodVisitor init = cw.visitMethod(Opcodes.ACC_PUBLIC,
                "<init>", "()V", null, null);

        init.visitCode();
        init.visitVarInsn(Opcodes.ALOAD, 0);
        init.visitMethodInsn(Opcodes.INVOKESPECIAL,
                "java/lang/Object", "<init>", "()V", false);
        init.visitInsn(Opcodes.RETURN);
        init.visitMaxs(1, 1);
        init.visitEnd();

        program.accept(this);

        FunctionSymbol main = program.getMainFunction().getFunction();

        mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                "main", "([Ljava/lang/String;)V", null, null);

        mv.visitCode();

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, className,
                main.getName(), buildDescriptor(main), false);

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }

    @Override
    public Void visit(FuncDef node) {
        MethodVisitor savedMv = mv;
        FunctionSymbol savedFunction = currentFunction;
        int savedNextLocal = nextLocal;

        FunctionSymbol function = node.getFunction();
        currentFunction = function;
        nextLocal = 0;

        for (VariableSymbol param : function.getParameters()) {
            param.setSlot(nextLocal++);
        }

        mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                function.getName(), buildDescriptor(function), null, null);

        mv.visitCode();

        for (Decl d : node.getDeclarations()) {
            d.accept(this);
        }

        for (Stmt s : node.getStatements()) {
            s.accept(this);
        }

        Type returnType = function.getReturnType();
        if (returnType instanceof IntType || returnType instanceof BoolType) {
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitInsn(Opcodes.IRETURN);
        } else if (returnType instanceof ArrayType || returnType instanceof CharType) {
            mv.visitInsn(Opcodes.ACONST_NULL);
            mv.visitInsn(Opcodes.ARETURN);
        } else {
            mv.visitInsn(Opcodes.RETURN);
        }

        mv.visitMaxs(0, 0);
        mv.visitEnd();

        mv = savedMv;
        currentFunction = savedFunction;
        nextLocal = savedNextLocal;

        return null;
    }

    @Override
    public Void visit(VarDef node) {
        for (VariableSymbol var : node.getVariables()) {
            var.setSlot(nextLocal++);
        }

        return null;
    }

    @Override
    public Void visit(VarExpr node) {
        VariableSymbol var = node.getVariable();
        int slot = var.getSlot();

        Type type = var.getType();

        if (type instanceof IntType || type instanceof BoolType) {
            mv.visitVarInsn(Opcodes.ILOAD, slot);
        } else {
            mv.visitVarInsn(Opcodes.ALOAD, slot);
        }

        return null;
    }

    @Override
    public Void visit(AssignStmt node) {
        if (node.getTarget() instanceof VarExpr expr) {
            node.getValue().accept(this);

            VariableSymbol var = expr.getVariable();

            int slot = var.getSlot();

            Type type = var.getType();

            if (type instanceof IntType || type instanceof BoolType) {
                mv.visitVarInsn(Opcodes.ISTORE, slot);
            } else {
                mv.visitVarInsn(Opcodes.ASTORE, slot);
            }
        } else if (node.getTarget() instanceof ArrayAccess access) {
            access.getArray().accept(this);
            access.getIndex().accept(this);
            node.getValue().accept(this);

            Type elem = ((ArrayType) access.getArray().getType()).getElementType();

            if (elem instanceof IntType || elem instanceof BoolType) {
                mv.visitInsn(Opcodes.IASTORE);
            } else if (elem instanceof CharType) {
                mv.visitInsn(Opcodes.CASTORE);
            }
        }

        return null;
    }

    @Override
    public Void visit(ExitStmt node) {
        mv.visitInsn(Opcodes.RETURN);
        return null;
    }

    @Override
    public Void visit(NilExpr node) {
        mv.visitInsn(Opcodes.ACONST_NULL);
        return null;
    }

    @Override
    public Void visit(CallExpr node) {
        FunctionSymbol function = node.getFunction();

        for (Expr arg : node.getArgs()) {
            arg.accept(this);
        }

        if (function.isBuiltin()) {
            emitRuntimeCall(function);
        } else {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, className,
                    function.getName(), buildDescriptor(function), false);
        }

        return null;
    }

    @Override
    public Void visit(BinaryExpr node) {
        switch (node.getOp()) {
            case ADD -> {
                node.getLeft().accept(this);
                node.getRight().accept(this);
                mv.visitInsn(Opcodes.IADD);
            }
            case SUB -> {
                node.getLeft().accept(this);
                node.getRight().accept(this);
                mv.visitInsn(Opcodes.ISUB);
            }
            case MUL -> {
                node.getLeft().accept(this);
                node.getRight().accept(this);
                mv.visitInsn(Opcodes.IMUL);
            }
            case DIV -> {
                node.getLeft().accept(this);
                node.getRight().accept(this);
                mv.visitInsn(Opcodes.IDIV);
            }
            case MOD -> {
                node.getLeft().accept(this);
                node.getRight().accept(this);
                mv.visitInsn(Opcodes.IREM);
            }

            case EQ -> {
                Label trueLabel = new Label();
                Label endLabel = new Label();

                node.getLeft().accept(this);
                node.getRight().accept(this);

                mv.visitJumpInsn(Opcodes.IF_ICMPEQ, trueLabel);

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(trueLabel);
                mv.visitInsn(Opcodes.ICONST_1);

                mv.visitLabel(endLabel);
            }
            case NEQ -> {
                Label trueLabel = new Label();
                Label endLabel = new Label();

                node.getLeft().accept(this);
                node.getRight().accept(this);

                mv.visitJumpInsn(Opcodes.IF_ICMPNE, trueLabel);

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(trueLabel);
                mv.visitInsn(Opcodes.ICONST_1);

                mv.visitLabel(endLabel);
            }
            case LT -> {
                Label trueLabel = new Label();
                Label endLabel = new Label();

                node.getLeft().accept(this);
                node.getRight().accept(this);

                mv.visitJumpInsn(Opcodes.IF_ICMPLT, trueLabel);

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(trueLabel);
                mv.visitInsn(Opcodes.ICONST_1);

                mv.visitLabel(endLabel);
            }
            case GT -> {
                Label trueLabel = new Label();
                Label endLabel = new Label();

                node.getLeft().accept(this);
                node.getRight().accept(this);

                mv.visitJumpInsn(Opcodes.IF_ICMPGT, trueLabel);

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(trueLabel);
                mv.visitInsn(Opcodes.ICONST_1);

                mv.visitLabel(endLabel);
            }
            case LEQ -> {
                Label trueLabel = new Label();
                Label endLabel = new Label();

                node.getLeft().accept(this);
                node.getRight().accept(this);

                mv.visitJumpInsn(Opcodes.IF_ICMPLE, trueLabel);

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(trueLabel);
                mv.visitInsn(Opcodes.ICONST_1);

                mv.visitLabel(endLabel);
            }
            case GEQ -> {
                Label trueLabel = new Label();
                Label endLabel = new Label();

                node.getLeft().accept(this);
                node.getRight().accept(this);

                mv.visitJumpInsn(Opcodes.IF_ICMPGE, trueLabel);

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(trueLabel);
                mv.visitInsn(Opcodes.ICONST_1);

                mv.visitLabel(endLabel);
            }

            case AND -> {
                Label falseLabel = new Label();
                Label endLabel = new Label();

                node.getLeft().accept(this);
                mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);

                node.getRight().accept(this);
                mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);

                mv.visitInsn(Opcodes.ICONST_1);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(falseLabel);
                mv.visitInsn(Opcodes.ICONST_0);

                mv.visitLabel(endLabel);
            }
            case OR -> {
                Label trueLabel = new Label();
                Label endLabel = new Label();

                node.getLeft().accept(this);
                mv.visitJumpInsn(Opcodes.IFNE, trueLabel);

                node.getRight().accept(this);
                mv.visitJumpInsn(Opcodes.IFNE, trueLabel);

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(trueLabel);
                mv.visitInsn(Opcodes.ICONST_1);

                mv.visitLabel(endLabel);
            }

            default -> throw new RuntimeException("Unsupported binary operator: " + node.getOp());
        }

        return null;
    }

    @Override
    public Void visit(UnaryExpr node) {
        node.getExpr().accept(this);

        switch (node.getOp()) {
            case PLUS -> {
            }
            case MINUS -> {
                mv.visitInsn(Opcodes.INEG);
            }
            case NOT -> {
                Label trueLabel = new Label();
                Label endLabel = new Label();

                mv.visitJumpInsn(Opcodes.IFEQ, trueLabel);

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitJumpInsn(Opcodes.GOTO, endLabel);

                mv.visitLabel(trueLabel);
                mv.visitInsn(Opcodes.ICONST_1);

                mv.visitLabel(endLabel);
            }
        }

        return null;
    }

    @Override
    public Void visit(ReturnStmt node) {
        node.getExpr().accept(this);

        Type returnType = currentFunction.getReturnType();

        if (returnType instanceof IntType || returnType instanceof BoolType) {
            mv.visitInsn(Opcodes.IRETURN);
        } else {
            mv.visitInsn(Opcodes.ARETURN);
        }

        return null;
    }

    @Override
    public Void visit(IntConst node) {
        int value = node.getValue();

        switch (value) {
            case 0 -> mv.visitInsn(Opcodes.ICONST_0);
            case 1 -> mv.visitInsn(Opcodes.ICONST_1);
            case 2 -> mv.visitInsn(Opcodes.ICONST_2);
            case 3 -> mv.visitInsn(Opcodes.ICONST_3);
            case 4 -> mv.visitInsn(Opcodes.ICONST_4);
            case 5 -> mv.visitInsn(Opcodes.ICONST_5);
            default -> mv.visitLdcInsn(value);
        }

        return null;
    }

    @Override
    public Void visit(BoolConst node) {
        mv.visitInsn(node.isValue() ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
        return null;
    }

    @Override
    public Void visit(CharConst node) {
        mv.visitLdcInsn(node.getValue());
        return null;
    }

    @Override
    public Void visit(StringLiteral node) {
        char[] chars = node.getValue().toCharArray();

        mv.visitLdcInsn(chars.length + 1);
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_CHAR);

        for (int i = 0; i < chars.length; i++) {
            mv.visitInsn(Opcodes.DUP);
            mv.visitLdcInsn(i);
            mv.visitLdcInsn((int) chars[i]);
            mv.visitInsn(Opcodes.CASTORE);
        }

        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn(chars.length);
        mv.visitLdcInsn(0);
        mv.visitInsn(Opcodes.CASTORE);

        return null;
    }

    @Override
    public Void visit(IfStmt node) {
        Label endLabel = new Label();

        Label nextLabel = new Label();

        node.getCondition().accept(this);
        mv.visitJumpInsn(Opcodes.IFEQ, nextLabel);

        for (Stmt stmt : node.getThenBody()) {
            stmt.accept(this);
        }

        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        mv.visitLabel(nextLabel);

        for (Elsif elsif : node.getElsifList()) {
            Label elsifFalseLabel = new Label();

            elsif.getCondition().accept(this);
            mv.visitJumpInsn(Opcodes.IFEQ, elsifFalseLabel);

            for (Stmt stmt : elsif.getBody()) {
                stmt.accept(this);
            }

            mv.visitJumpInsn(Opcodes.GOTO, endLabel);

            mv.visitLabel(elsifFalseLabel);
        }

        for (Stmt stmt : node.getElseBody()) {
            stmt.accept(this);
        }

        mv.visitLabel(endLabel);

        return null;
    }

    @Override
    public Void visit(ForStmt node) {
        for (Simple simple : node.getInit()) {
            simple.accept(this);
        }

        Label loopStart = new Label();
        Label loopEnd = new Label();

        mv.visitLabel(loopStart);

        node.getCondition().accept(this);
        mv.visitJumpInsn(Opcodes.IFEQ, loopEnd);

        for (Stmt stmt : node.getBody()) {
            stmt.accept(this);
        }

        for (Simple simple : node.getUpdate()) {
            simple.accept(this);
        }

        mv.visitJumpInsn(Opcodes.GOTO, loopStart);

        mv.visitLabel(loopEnd);

        return null;
    }

    @Override
    public Void visit(NewArrayExpr node) {
        node.getSize().accept(this);

        Type elem = node.getType();

        if (elem instanceof IntType || elem instanceof BoolType) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
        } else if (elem instanceof CharType) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_CHAR);
        } else {
            throw new RuntimeException("Unsupported array type");
        }

        return null;
    }

    @Override
    public Void visit(ArrayAccess node) {
        node.getArray().accept(this);
        node.getIndex().accept(this);

        Type elem = ((ArrayType) node.getArray().getType()).getElementType();

        if (elem instanceof IntType || elem instanceof BoolType)
            mv.visitInsn(Opcodes.IALOAD);
        else if (elem instanceof CharType)
            mv.visitInsn(Opcodes.CALOAD);
        else
            throw new RuntimeException();

        return null;
    }

    private String buildDescriptor(FunctionSymbol fn) {

        StringBuilder sb = new StringBuilder();
        sb.append("(");

        for (VariableSymbol p : fn.getParameters()) {
            sb.append(toJvmType(p.getType()));
        }

        sb.append(")");
        sb.append(toJvmType(fn.getReturnType()));

        return sb.toString();
    }

    private String toJvmType(Type type) {
        if (type == null || type instanceof NilType) {
            return "V";
        }

        if (type instanceof IntType || type instanceof BoolType) {
            return "I";
        }

        if (type instanceof CharType) {
            return "C";
        }

        if (type instanceof ArrayType array) {
            return "[" + toJvmType(array.getElementType());
        }

        throw new RuntimeException("Unsupported type: " + type);
    }

    private void emitRuntimeCall(FunctionSymbol function) {
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC, runtimePath,
                function.getName(), buildDescriptor(function), false);
    }
}
