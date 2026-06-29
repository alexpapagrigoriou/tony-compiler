package io.github.alexpapagrigoriou.tonycompiler.semantics;

import io.github.alexpapagrigoriou.tonycompiler.ast.*;
import io.github.alexpapagrigoriou.tonycompiler.symbol.*;
import io.github.alexpapagrigoriou.tonycompiler.visitor.TraversalVisitor;

import java.util.ArrayList;
import java.util.List;

public class TypeChecker extends TraversalVisitor<Type> {

    public TypeChecker() {
    }

    @Override
    public Type visit(Program node) {
        return node.getMainFunction().accept(this);
    }

    @Override
    public Type visit(FuncDef node) {
        FunctionSymbol function = node.getFunction();

        for (Decl decl : node.getDeclarations()) {
            decl.accept(this);
        }

        for (Stmt stmt : node.getStatements()) {
            stmt.accept(this);
        }

        return null;
    }

    @Override
    public Type visit(FuncDecl node) {
        return null;
    }

    @Override
    public Type visit(VarDef node) {
        return null;
    }

    @Override
    public Type visit(AssignStmt node) {
        Type left = node.getTarget().accept(this);
        Type right = node.getValue().accept(this);

        if (!sameType(left, right)) {
            throw new SemanticException("Type mismatch in assignment: expected " + typeToString(left) +
                    " but found " + typeToString(right));
        }

        return null;
    }

    @Override
    public Type visit(IfStmt node) {
        Type cond = node.getCondition().accept(this);

        if (!(cond instanceof BoolType)) {
            throw new SemanticException("if condition requires bool");
        }

        for (Stmt stmt : node.getThenBody())
            stmt.accept(this);
        for (Elsif e : node.getElsifList())
            e.accept(this);
        for (Stmt stmt : node.getElseBody())
            stmt.accept(this);

        return null;
    }

    @Override
    public Type visit(Elsif node) {
        Type cond = node.getCondition().accept(this);

        if (!(cond instanceof BoolType)) {
            throw new SemanticException("elsif condition requires bool");
        }

        for (Stmt stmt : node.getBody()) {
            stmt.accept(this);
        }

        return null;
    }

    @Override
    public Type visit(ForStmt node) {
        Type cond = node.getCondition().accept(this);

        if (!(cond instanceof BoolType)) {
            throw new SemanticException("for condition requires bool");
        }

        for (Simple s : node.getInit())
            s.accept(this);
        for (Simple s : node.getUpdate())
            s.accept(this);
        for (Stmt stmt : node.getBody())
            stmt.accept(this);

        return null;
    }

    @Override
    public Type visit(CallExpr node) {
        FunctionSymbol function = node.getFunction();
        List<VariableSymbol> params = function.getParameters();
        List<Expr> args = node.getArgs();

        if (params.size() != args.size()) {
            throw new SemanticException("Wrong number of arguments in call to " + function.getName());
        }

        for (int i = 0; i < params.size(); i++) {
            VariableSymbol param = params.get(i);
            Expr arg = args.get(i);

            Type actual = arg.accept(this);
            Type expected = param.getType();

            if (param.isRef()) {
                if (!(arg instanceof VarExpr) && !(arg instanceof ArrayAccess)) {
                    throw new SemanticException("ref parameter requires lvalue");
                }
            }

            if (!sameType(expected, actual)) {
                throw new SemanticException("Argument type mismatch in call to " + function.getName());
            }
        }

        return function.getReturnType();
    }

    @Override
    public Type visit(VarExpr node) {
        Type result = node.getVariable().getType();
        node.setType(result);
        return result;
    }

    @Override
    public Type visit(ArrayAccess node) {
        Type arrayType = node.getArray().accept(this);
        Type indexType = node.getIndex().accept(this);

        if (!(indexType instanceof IntType)) {
            throw new SemanticException("array index requires int");
        }

        if (!(arrayType instanceof ArrayType array)) {
            throw new SemanticException("cannot index non-array type");
        }

        Type result = array.getElementType();
        node.setType(result);
        return result;
    }

    @Override
    public Type visit(BinaryExpr node) {
        Type left = node.getLeft().accept(this);
        Type right = node.getRight().accept(this);

        switch (node.getOp()) {
            case ADD, SUB, MUL, DIV, MOD -> {
                if (!(left instanceof IntType) || !(right instanceof IntType)) {
                    throw new SemanticException("arithmetic requires int");
                }
                Type result = new IntType();
                node.setType(result);
                return result;
            }

            case AND, OR -> {
                if (!(left instanceof BoolType) || !(right instanceof BoolType)) {
                    throw new SemanticException("logical requires bool");
                }
                Type result = new BoolType();
                node.setType(result);
                return result;
            }

            case EQ, NEQ -> {
                if (!sameType(left, right)) {
                    throw new SemanticException("comparison requires same type");
                }
                Type result = new BoolType();
                node.setType(result);
                return result;
            }

            case LT, GT, LEQ, GEQ -> {
                if (!(left instanceof IntType) || !(right instanceof IntType)) {
                    throw new SemanticException("comparison requires int");
                }
                Type result = new BoolType();
                node.setType(result);
                return result;
            }

            default -> throw new SemanticException("unknown operator");
        }

    }

    @Override
    public Type visit(UnaryExpr node) {
        Type t = node.getExpr().accept(this);

        switch (node.getOp()) {
            case PLUS, MINUS -> {
                if (!(t instanceof IntType)) {
                    throw new SemanticException("unary requires int");
                }
                Type result = new IntType();
                node.setType(result);
                return result;
            }
            case NOT -> {
                if (!(t instanceof BoolType)) {
                    throw new SemanticException("not requires bool");
                }
                Type result = new BoolType();
                node.setType(result);
                return result;
            }
            default -> throw new SemanticException("unknown unary op");
        }
    }

    @Override
    public Type visit(IntConst node) {
        return new IntType();
    }

    @Override
    public Type visit(BoolConst node) {
        return new BoolType();
    }

    @Override
    public Type visit(CharConst node) {
        return new CharType();
    }

    @Override
    public Type visit(StringLiteral node) {
        return new ArrayType(new CharType());
    }

    @Override
    public Type visit(NewArrayExpr node) {
        Type size = node.getSize().accept(this);

        if (!(size instanceof IntType)) {
            throw new SemanticException("array size must be int");
        }

        return new ArrayType(node.getType());
    }

    @Override
    public Type visit(NilExpr node) {
        return new NilType();
    }

    @Override
    public Type visit(NilCheckExpr node) {
        Type t = node.getExpr().accept(this);

        if (!(t instanceof ListType) && !(t instanceof NilType)) {
            throw new SemanticException("nil? expects list");
        }

        return new BoolType();
    }

    @Override
    public Type visit(HashExpr node) {
        Type head = node.getLeft().accept(this);
        Type tail = node.getRight().accept(this);

        if (!(tail instanceof ListType list)) {
            throw new SemanticException("right operand must be list");
        }

        if (!sameType(head, list.getElementType())) {
            throw new SemanticException("type mismatch in list construction");
        }

        return tail;
    }

    @Override
    public Type visit(HeadExpr node) {
        Type t = node.getExpr().accept(this);

        if (!(t instanceof ListType list)) {
            throw new SemanticException("head expects list");
        }

        return list.getElementType();
    }

    @Override
    public Type visit(TailExpr node) {
        Type t = node.getExpr().accept(this);

        if (!(t instanceof ListType list)) {
            throw new SemanticException("tail expects list");
        }

        return list;
    }

    private boolean sameType(Type a, Type b) {
        if (a instanceof NilType && b instanceof ListType) {
            return true;
        }

        if (a instanceof ListType && b instanceof NilType) {
            return true;
        }

        if (a.getClass() != b.getClass()) {
            return false;
        }

        if (a instanceof ArrayType aa && b instanceof ArrayType bb) {
            return sameType(aa.getElementType(), bb.getElementType());
        }

        if (a instanceof ListType la && b instanceof ListType lb) {
            return sameType(la.getElementType(), lb.getElementType());
        }

        return true;
    }

    private String typeToString(Type type) {
        if (type instanceof IntType) {
            return "int";
        }

        if (type instanceof BoolType) {
            return "bool";
        }

        if (type instanceof CharType) {
            return "char";
        }

        if (type instanceof ArrayType arrayType) {
            return typeToString(arrayType.getElementType()) + "[]";
        }

        if (type instanceof ListType listType) {
            return "list[" + typeToString(listType.getElementType()) + "]";
        }

        if (type instanceof NilType) {
            return "nil";
        }

        return type.getClass().getSimpleName();
    }
}
