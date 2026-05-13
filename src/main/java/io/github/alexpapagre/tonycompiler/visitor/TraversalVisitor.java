package io.github.alexpapagre.tonycompiler.visitor;

import io.github.alexpapagre.tonycompiler.ast.*;

public abstract class TraversalVisitor<T> extends BaseVisitor<T> {

    @Override
    public T visit(Program node) {
        node.getMainFunction().accept(this);

        return null;
    }

    @Override
    public T visit(FuncDef node) {
        node.getHeader().accept(this);

        for (Decl decl : node.getDeclarations()) {
            decl.accept(this);
        }

        for (Stmt stmt : node.getStatements()) {
            stmt.accept(this);
        }

        return null;
    }

    @Override
    public T visit(FuncDecl node) {
        node.getHeader().accept(this);

        return null;
    }

    @Override
    public T visit(VarDef node) {
        node.getType().accept(this);

        return null;
    }

    @Override
    public T visit(Header node) {
        if (node.getReturnType() != null) {
            node.getReturnType().accept(this);
        }

        for (Formal formal : node.getParameters()) {
            formal.accept(this);
        }

        return null;
    }

    @Override
    public T visit(Formal node) {
        node.getType().accept(this);

        return null;
    }

    @Override
    public T visit(Elsif node) {
        node.getCondition().accept(this);

        for (Stmt stmt : node.getBody()) {
            stmt.accept(this);
        }

        return null;
    }

    @Override
    public T visit(AssignStmt node) {
        node.getTarget().accept(this);
        node.getValue().accept(this);

        return null;
    }

    @Override
    public T visit(CallStmt node) {
        node.getCall().accept(this);

        return null;
    }

    @Override
    public T visit(IfStmt node) {
        node.getCondition().accept(this);

        for (Stmt stmt : node.getThenBody()) {
            stmt.accept(this);
        }

        for (Elsif elsif : node.getElsifList()) {
            elsif.accept(this);
        }

        for (Stmt stmt : node.getElseBody()) {
            stmt.accept(this);
        }

        return null;
    }

    @Override
    public T visit(ForStmt node) {
        for (Simple simple : node.getInit()) {
            simple.accept(this);
        }

        node.getCondition().accept(this);

        for (Simple simple : node.getUpdate()) {
            simple.accept(this);
        }

        for (Stmt stmt : node.getBody()) {
            stmt.accept(this);
        }

        return null;
    }

    @Override
    public T visit(ReturnStmt node) {
        node.getExpr().accept(this);

        return null;
    }

    @Override
    public T visit(BinaryExpr node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);

        return null;
    }

    @Override
    public T visit(UnaryExpr node) {
        node.getExpr().accept(this);

        return null;
    }

    @Override
    public T visit(CallExpr node) {
        for (Expr expr : node.getArgs()) {
            expr.accept(this);
        }

        return null;
    }

    @Override
    public T visit(ArrayAccess node) {
        node.getArray().accept(this);
        node.getIndex().accept(this);

        return null;
    }

    @Override
    public T visit(NewArrayExpr node) {
        node.getType().accept(this);
        node.getSize().accept(this);

        return null;
    }

    @Override
    public T visit(NilCheckExpr node) {
        node.getExpr().accept(this);

        return null;
    }

    @Override
    public T visit(HashExpr node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);

        return null;
    }

    @Override
    public T visit(HeadExpr node) {
        node.getExpr().accept(this);

        return null;
    }

    @Override
    public T visit(TailExpr node) {
        node.getExpr().accept(this);

        return null;
    }

    @Override
    public T visit(ArrayType node) {
        node.getElementType().accept(this);

        return null;
    }

    @Override
    public T visit(ListType node) {
        node.getElementType().accept(this);

        return null;
    }
}
