package io.github.alexpapagre.tonycompiler.visitor;

import io.github.alexpapagre.tonycompiler.ast.*;

public abstract class BaseVisitor<T> implements Visitor<T> {

    @Override
    public T visit(Program node) {
        return null;
    }

    @Override
    public T visit(FuncDef node) {
        return null;
    }

    @Override
    public T visit(FuncDecl node) {
        return null;
    }

    @Override
    public T visit(VarDef node) {
        return null;
    }

    @Override
    public T visit(Header node) {
        return null;
    }

    @Override
    public T visit(Formal node) {
        return null;
    }

    @Override
    public T visit(Elsif node) {
        return null;
    }

    @Override
    public T visit(AssignStmt node) {
        return null;
    }

    @Override
    public T visit(CallStmt node) {
        return null;
    }

    @Override
    public T visit(IfStmt node) {
        return null;
    }

    @Override
    public T visit(ForStmt node) {
        return null;
    }

    @Override
    public T visit(ReturnStmt node) {
        return null;
    }

    @Override
    public T visit(ExitStmt node) {
        return null;
    }

    @Override
    public T visit(SkipStmt node) {
        return null;
    }

    @Override
    public T visit(BinaryExpr node) {
        return null;
    }

    @Override
    public T visit(UnaryExpr node) {
        return null;
    }

    @Override
    public T visit(CallExpr node) {
        return null;
    }

    @Override
    public T visit(VarExpr node) {
        return null;
    }

    @Override
    public T visit(ArrayAccess node) {
        return null;
    }

    @Override
    public T visit(IntConst node) {
        return null;
    }

    @Override
    public T visit(CharConst node) {
        return null;
    }

    @Override
    public T visit(BoolConst node) {
        return null;
    }

    @Override
    public T visit(StringLiteral node) {
        return null;
    }

    @Override
    public T visit(NewArrayExpr node) {
        return null;
    }

    @Override
    public T visit(NilExpr node) {
        return null;
    }

    @Override
    public T visit(NilCheckExpr node) {
        return null;
    }

    @Override
    public T visit(HashExpr node) {
        return null;
    }

    @Override
    public T visit(HeadExpr node) {
        return null;
    }

    @Override
    public T visit(TailExpr node) {
        return null;
    }

    @Override
    public T visit(IntType node) {
        return null;
    }

    @Override
    public T visit(BoolType node) {
        return null;
    }

    @Override
    public T visit(CharType node) {
        return null;
    }

    @Override
    public T visit(ArrayType node) {
        return null;
    }

    @Override
    public T visit(ListType node) {
        return null;
    }

    @Override
    public T visit(NilType node) {
        return null;
    }
}
