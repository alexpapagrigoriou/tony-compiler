package io.github.alexpapagre.tonycompiler.semantics;

import io.github.alexpapagre.tonycompiler.ast.*;
import io.github.alexpapagre.tonycompiler.symbol.*;
import io.github.alexpapagre.tonycompiler.visitor.TraversalVisitor;

import java.util.ArrayList;
import java.util.List;

public class TypeChecker extends TraversalVisitor<Type> {
    private final ScopeStack scopes = new ScopeStack();

    private Type currentReturnType;

    public TypeChecker() {
        BuiltinInstaller.install(scopes);
    }

    @Override
    public Type visit(Program node) {
        return node.getMainFunction().accept(this);
    }

    @Override
    public Type visit(FuncDef node) {
        String name = node.getHeader().getName();

        List<VariableSymbol> parameters = new ArrayList<>();
        for (Formal formal : node.getHeader().getParameters()) {
            for (String parameterName : formal.getNames()) {
                parameters.add(new VariableSymbol(parameterName, formal.isRef(), formal.getType()));
            }
        }

        FunctionSymbol function = new FunctionSymbol(name, node.getHeader().getReturnType(), parameters);

        scopes.declare(function);

        scopes.enterScope();

        for (VariableSymbol parameter : parameters) {
            scopes.declare(parameter);
        }

        Type previousReturnType = currentReturnType;
        currentReturnType = node.getHeader().getReturnType();

        for (Decl decl : node.getDeclarations()) {
            decl.accept(this);
        }

        for (Stmt stmt : node.getStatements()) {
            stmt.accept(this);
        }

        currentReturnType = previousReturnType;

        scopes.exitScope();

        return null;

    }

    @Override
    public Type visit(FuncDecl node) {
        String name = node.getHeader().getName();

        List<VariableSymbol> parameters = new ArrayList<>();
        for (Formal formal : node.getHeader().getParameters()) {
            for (String parameterName : formal.getNames()) {
                parameters.add(new VariableSymbol(parameterName, formal.isRef(), formal.getType()));
            }
        }

        FunctionSymbol function = new FunctionSymbol(name, node.getHeader().getReturnType(), parameters);

        scopes.declare(function);

        return null;
    }

    @Override
    public Type visit(VarDef node) {
        for (String name : node.getNames()) {
            scopes.declare(new VariableSymbol(name, false, node.getType()));
        }

        return null;
    }

    @Override
    public Type visit(Elsif node) {
        Type cond = node.getCondition().accept(this);

        if (!(cond instanceof BoolType)) {
            throw new SemanticException("elsif condition requires bool");
        }

        return super.visit(node);
    }

    @Override
    public Type visit(AssignStmt node) {
        Type left = node.getTarget().accept(this);
        Type right = node.getValue().accept(this);

        if (!sameType(left, right)) {
            throw new SemanticException("Type mismatch in assignment: expected " + typeToString(left) + " but found "
                    + typeToString(right));
        }

        return null;
    }

    @Override
    public Type visit(IfStmt node) {
        Type cond = node.getCondition().accept(this);

        if (!(cond instanceof BoolType)) {
            throw new SemanticException("if condition requires bool");
        }

        return super.visit(node);
    }

    @Override
    public Type visit(ForStmt node) {
        Type cond = node.getCondition().accept(this);

        if (!(cond instanceof BoolType)) {
            throw new SemanticException("for condition requires bool");
        }

        return super.visit(node);
    }

    @Override
    public Type visit(BinaryExpr node) {
        Type left = node.getLeft().accept(this);
        Type right = node.getRight().accept(this);

        switch (node.getOp()) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
                if (!(left instanceof IntType) || !(right instanceof IntType)) {
                    throw new SemanticException("arithmetic requires int");
                }
                return new IntType();
            case AND:
            case OR:
                if (!(left instanceof BoolType) || !(right instanceof BoolType)) {
                    throw new SemanticException("logical requires bool");
                }
                return new BoolType();
            case EQ:
            case NEQ:
                if (!sameType(left, right)) {
                    throw new SemanticException("Comparison operands must have same type");
                }
                return new BoolType();
            case LT:
            case GT:
            case LEQ:
            case GEQ:
                if (!(left instanceof IntType) || !(right instanceof IntType)) {
                    throw new SemanticException("comparison requires int");
                }
                return new BoolType();
            default:
                throw new SemanticException("Unknown binary operator");
        }
    }

    @Override
    public Type visit(UnaryExpr node) {
        Type exprType = node.getExpr().accept(this);

        switch (node.getOp()) {
            case PLUS:
            case MINUS:
                if (!(exprType instanceof IntType)) {
                    throw new SemanticException("unary arithmetic requires int");
                }
                return new IntType();
            case NOT:
                if (!(exprType instanceof BoolType)) {
                    throw new SemanticException("logical not requires bool");
                }
                return new BoolType();
            default:
                throw new SemanticException("Unknown unary operator");
        }
    }

    @Override
    public Type visit(CallExpr node) {
        Symbol symbol = scopes.resolve(node.getName());

        if (!(symbol instanceof FunctionSymbol function)) {
            throw new SemanticException(node.getName() + " is not a function");
        }

        List<VariableSymbol> parameters = function.getParameters();
        List<Expr> args = node.getArgs();

        if (parameters.size() != args.size()) {
            throw new SemanticException("Wrong number of arguments in call to " + node.getName());
        }

        for (int i = 0; i < parameters.size(); i++) {
            VariableSymbol parameter = parameters.get(i);
            Expr arg = args.get(i);

            Type expected = parameter.getType();
            Type actual = arg.accept(this);

            if (parameter.isRef()) {
                if (!(arg instanceof VarExpr) && !(arg instanceof ArrayAccess)) {
                    throw new SemanticException("ref parameter requires lvalue");
                }
            }

            if (!sameType(expected, actual)) {
                throw new SemanticException("Argument type mismatch in call to " + node.getName());
            }
        }

        return function.getReturnType();
    }

    @Override
    public Type visit(VarExpr node) {
        Symbol symbol = scopes.resolve(node.getName());

        if (!(symbol instanceof VariableSymbol variable)) {
            throw new SemanticException("Undefined variable: " + node.getName());
        }

        return variable.getType();
    }

    @Override
    public Type visit(ArrayAccess node) {
        Type arrayType = node.getArray().accept(this);
        Type indexType = node.getIndex().accept(this);

        if (!(indexType instanceof IntType)) {
            throw new SemanticException("array index requires int");
        }

        if (!(arrayType instanceof ArrayType array)) {
            throw new SemanticException("cannot index value of type " + typeToString(arrayType));
        }

        return array.getElementType();
    }

    @Override
    public Type visit(IntConst node) {
        return new IntType();
    }

    @Override
    public Type visit(CharConst node) {
        return new CharType();
    }

    @Override
    public Type visit(BoolConst node) {
        return new BoolType();
    }

    @Override
    public Type visit(StringLiteral node) {
        return new ArrayType(new CharType());
    }

    @Override
    public Type visit(NewArrayExpr node) {
        Type sizeType = node.getSize().accept(this);

        if (!(sizeType instanceof IntType)) {
            throw new SemanticException("array size requires int");
        }

        return new ArrayType(node.getType());
    }

    @Override
    public Type visit(NilExpr node) {
        return new NilType();
    }

    @Override
    public Type visit(NilCheckExpr node) {
        Type exprType = node.getExpr().accept(this);

        if (!(exprType instanceof ListType) && !(exprType instanceof NilType)) {
            throw new SemanticException("nil? expects list");
        }

        return new BoolType();
    }

    @Override
    public Type visit(HashExpr node) {
        Type head = node.getLeft().accept(this);
        Type tail = node.getRight().accept(this);

        if (!(tail instanceof ListType list)) {
            throw new SemanticException("Right operand of # must be a list");
        }

        if (!sameType(head, list.getElementType())) {
            throw new SemanticException("Head type does not match list element type");
        }

        return tail;
    }

    @Override
    public Type visit(HeadExpr node) {
        Type exprType = node.getExpr().accept(this);

        if (!(exprType instanceof ListType list)) {
            throw new SemanticException("head expects list");
        }

        return list.getElementType();
    }

    @Override
    public Type visit(TailExpr node) {
        Type exprType = node.getExpr().accept(this);

        if (!(exprType instanceof ListType list)) {
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
