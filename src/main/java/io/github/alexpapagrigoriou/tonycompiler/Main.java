package io.github.alexpapagrigoriou.tonycompiler;

import io.github.alexpapagrigoriou.tonycompiler.ast.Program;
import io.github.alexpapagrigoriou.tonycompiler.codegen.CodeGenerator;
import io.github.alexpapagrigoriou.tonycompiler.semantics.SemanticAnalyzer;

public class Main {

    public static void main(String[] args) {
        Program program = ProgramLoader.loadProgram(args);

        SemanticAnalyzer.analyze(program);

        CodeGenerator.generate(program);
    }
}
