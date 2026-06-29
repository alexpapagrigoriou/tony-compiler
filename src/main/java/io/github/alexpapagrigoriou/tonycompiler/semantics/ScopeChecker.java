package io.github.alexpapagrigoriou.tonycompiler.semantics;

import io.github.alexpapagrigoriou.tonycompiler.ast.*;
import io.github.alexpapagrigoriou.tonycompiler.symbol.*;
import io.github.alexpapagrigoriou.tonycompiler.visitor.TraversalVisitor;

import java.util.ArrayList;
import java.util.List;

public class ScopeChecker extends TraversalVisitor<Void> {
    private final ScopeStack scopes = new ScopeStack();

    public ScopeChecker() {
        BuiltinInstaller.install(scopes);
    }

    @Override
    public Void visit(FuncDef node) {
        String name = node.getHeader().getName();

        Symbol existing = scopes.resolve(name);

        FunctionSymbol function;

        if (existing == null) {
            List<VariableSymbol> parameters = new ArrayList<>();

            for (Formal formal : node.getHeader().getParameters()) {
                for (String parameterName : formal.getNames()) {
                    VariableSymbol parameter = new VariableSymbol(parameterName, formal.isRef(), formal.getType());

                    parameters.add(parameter);
                    formal.getVariables().add(parameter);
                }
            }

            function = new FunctionSymbol(name, node.getHeader().getReturnType(), parameters, null);
        } else if (existing instanceof FunctionSymbol fs) {
            function = fs;

            if (function.isDefined()) {
                throw new SemanticException("Function already defined: " + name);
            }
        } else {
            throw new SemanticException(name + " is not a function");
        }

        function.setDefined();

        scopes.declare(function);

        node.setFunction(function);

        scopes.enterScope();

        for (VariableSymbol parameter : function.getParameters()) {
            if (!scopes.declare(parameter)) {
                throw new SemanticException("Duplicate parameter: " + parameter.getName());
            }
        }

        for (Decl decl : node.getDeclarations()) {
            decl.accept(this);
        }

        for (Stmt stmt : node.getStatements()) {
            stmt.accept(this);
        }

        scopes.exitScope();

        return null;
    }

    @Override
    public Void visit(FuncDecl node) {
        String name = node.getHeader().getName();

        Symbol existing = scopes.resolve(name);

        if (existing != null) {
            throw new SemanticException("Function already declared: " + name);
        }

        List<VariableSymbol> parameters = new ArrayList<>();

        for (Formal formal : node.getHeader().getParameters()) {
            for (String parameter : formal.getNames()) {
                parameters.add(new VariableSymbol(parameter, formal.isRef(), formal.getType()));
            }
        }

        FunctionSymbol function = new FunctionSymbol(name, node.getHeader().getReturnType(), parameters, null);

        function.setDeclared();

        scopes.declare(function);
        node.setFunction(function);

        return null;
    }

    @Override
    public Void visit(VarDef node) {
        for (String name : node.getNames()) {
            VariableSymbol variable = new VariableSymbol(name, false, node.getType());

            if (!scopes.declare(variable)) {
                throw new SemanticException("Variable already declared in this scope: " + name);
            }

            node.getVariables().add(variable);
        }

        return null;
    }

    @Override
    public Void visit(IfStmt node) {
        node.getCondition().accept(this);

        scopes.enterScope();
        for (Stmt stmt : node.getThenBody()) {
            stmt.accept(this);
        }
        scopes.exitScope();

        for (Elsif elsif : node.getElsifList()) {
            elsif.accept(this);
        }

        scopes.enterScope();
        for (Stmt stmt : node.getElseBody()) {
            stmt.accept(this);
        }
        scopes.exitScope();

        return null;
    }

    @Override
    public Void visit(Elsif node) {
        node.getCondition().accept(this);

        scopes.enterScope();
        for (Stmt stmt : node.getBody()) {
            stmt.accept(this);
        }
        scopes.exitScope();

        return null;
    }

    @Override
    public Void visit(ForStmt node) {
        scopes.enterScope();
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
        scopes.exitScope();

        return null;
    }

    @Override
    public Void visit(VarExpr node) {
        Symbol symbol = scopes.resolve(node.getName());
        if (!(symbol instanceof VariableSymbol variable)) {
            throw new SemanticException("Undefined variable: " + node.getName());
        }

        node.setVariable(variable);

        return null;
    }

    @Override
    public Void visit(CallExpr node) {
        Symbol symbol = scopes.resolve(node.getName());

        if (symbol == null) {
            throw new SemanticException("Undefined function: " + node.getName());
        }

        if (!(symbol instanceof FunctionSymbol function)) {
            throw new SemanticException(node.getName() + " is not a function");
        }

        node.setFunction(function);

        for (Expr expr : node.getArgs()) {
            expr.accept(this);
        }

        return null;
    }
}
