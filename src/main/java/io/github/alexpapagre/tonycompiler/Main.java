package io.github.alexpapagre.tonycompiler;

import io.github.alexpapagre.tonycompiler.ast.Program;
import io.github.alexpapagre.tonycompiler.codegen.CodeGenerator;
import io.github.alexpapagre.tonycompiler.semantics.SemanticAnalyzer;

public class Main {

    public static void main(String[] args) {
        Program program = ProgramLoader.loadProgram(args);

        SemanticAnalyzer.analyze(program);

        CodeGenerator.generate(program);
    }
}
