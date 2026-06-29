package io.github.alexpapagrigoriou.tonycompiler.visitor;

import io.github.alexpapagrigoriou.tonycompiler.ast.*;

public interface Visitor<T> {

    T visit(Program node);

    T visit(FuncDef node);

    T visit(FuncDecl node);

    T visit(VarDef node);

    T visit(Header node);

    T visit(Formal node);

    T visit(Elsif node);

    T visit(AssignStmt node);

    T visit(CallStmt node);

    T visit(IfStmt node);

    T visit(ForStmt node);

    T visit(ReturnStmt node);

    T visit(ExitStmt node);

    T visit(SkipStmt node);

    T visit(BinaryExpr node);

    T visit(UnaryExpr node);

    T visit(CallExpr node);

    T visit(VarExpr node);

    T visit(ArrayAccess node);

    T visit(IntConst node);

    T visit(CharConst node);

    T visit(BoolConst node);

    T visit(StringLiteral node);

    T visit(NewArrayExpr node);

    T visit(NilExpr node);

    T visit(NilCheckExpr node);

    T visit(HashExpr node);

    T visit(HeadExpr node);

    T visit(TailExpr node);

    T visit(IntType node);

    T visit(BoolType node);

    T visit(CharType node);

    T visit(ArrayType node);

    T visit(ListType node);

    T visit(NilType node);
}
