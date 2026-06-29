package io.github.alexpapagrigoriou.tonycompiler;

import io.github.alexpapagrigoriou.tonycompiler.ast.Program;
import io.github.alexpapagrigoriou.tonycompiler.cli.CommandLineParser;
import io.github.alexpapagrigoriou.tonycompiler.cli.CompilerOptions;
import io.github.alexpapagrigoriou.tonycompiler.codegen.CodeGenerator;
import io.github.alexpapagrigoriou.tonycompiler.semantics.SemanticAnalyzer;

public class Main {

    public static void main(String[] args) {
        CompilerOptions options = CommandLineParser.parse(args);

        Program program = ProgramLoader.loadProgram(options.getInputFile());

        SemanticAnalyzer.analyze(program);

        CodeGenerator.generate(program, options.getOutputName());
    }
}
