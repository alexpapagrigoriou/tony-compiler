package io.github.alexpapagrigoriou.tonycompiler.cli;

import java.util.regex.Pattern;

public final class CommandLineParser {
    private static final Pattern OUTPUT_NAME = Pattern.compile("^[A-Za-z][A-Za-z0-9]*$");

    private CommandLineParser() {
    }

    public static CompilerOptions parse(String[] args) {
        if (args.length != 1 && args.length != 3) {
            usage();
            System.exit(1);
        }

        String inputFile = args[0];

        if (!inputFile.endsWith(".tony")) {
            System.err.println("Input file must have the '.tony' extension.");
            System.exit(1);
        }

        if (args.length == 1) {
            return new CompilerOptions(inputFile);
        }

        if (!"-o".equals(args[1])) {
            usage();
            System.exit(1);
        }

        String outputName = args[2];

        if (!OUTPUT_NAME.matcher(outputName).matches()) {
            System.err.println("Output name must start with a letter and contain only letters and digits.");
            System.exit(1);
        }

        outputName = Character.toUpperCase(outputName.charAt(0)) + outputName.substring(1);

        return new CompilerOptions(inputFile, outputName);
    }

    private static void usage() {
        System.err.println("Usage: java -jar tony-compiler.jar <inputFile.tony> [-o <outputName>]");
    }
}
