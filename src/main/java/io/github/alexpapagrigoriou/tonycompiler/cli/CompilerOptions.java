package io.github.alexpapagrigoriou.tonycompiler.cli;

import lombok.Getter;

@Getter
public class CompilerOptions {
    private final String inputFile;
    private final String outputName;

    public CompilerOptions(String inputFile) {
        this(inputFile, "Main");
    }

    public CompilerOptions(String inputFile, String outputName) {
        this.inputFile = inputFile;
        this.outputName = outputName;
    }
}
