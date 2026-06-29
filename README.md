# Tony Compiler

A compiler for the Tony programming language that generates JVM bytecode.

## Language Specification

The full Tony language specification (in Greek) is available in [the language specification](docs/tony-spec.pdf).

## Build the Compiler

Before compiling any Tony source files, build the compiler jar once:

```bash
./tony.sh compile
```

Or manually:

```bash
./mvnw clean package
```

> Run this again only when you make changes to the compiler itself.

## Usage

### Via Script

#### Build a Tony source file
```bash
./tony.sh build <inputFile.tony> [-o <outputName>]
```

If `-o <outputName>` is not provided, the generated class name defaults to `Main`.

#### Run the compiled program
```bash
./tony.sh run [<className>]
```

If `<className>` is not provided, the program defaults to running `Main`.

#### Clean the output directory
```bash
./tony.sh clean
```

#### Example
```bash
./tony.sh build src/main/resources/tony-samples/reverse.tony
./tony.sh run
```

or

```bash
./tony.sh build src/main/resources/tony-samples/reverse.tony -o Reverse
./tony.sh run Reverse
```

### Manually

#### Compile a Tony source file
```bash
java -jar target/tony-compiler.jar <file.tony> [-o <outputName>]
```

If `-o <outputName>` is not provided, the generated class name defaults to `Main`.

#### Run the compiled program
```bash
java -cp out <className>
```

#### Clean the output directory
```bash
rm -rf out
```

#### Example
```bash
java -jar target/tony-compiler.jar src/main/resources/tony-samples/reverse.tony
java -cp out Main
```

or

```bash
java -jar target/tony-compiler.jar src/main/resources/tony-samples/reverse.tony -o Reverse
java -cp out Reverse
```
