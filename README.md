# Tony Compiler

 A compiler for the Tony programming language that generates JVM bytecode.

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
./tony.sh build <file.tony>
```

#### Run the compiled program
```bash
./tony.sh run
```

#### Clean the output directory
```bash
./tony.sh clean
```

#### Example
```bash
./tony.sh build src/main/resources/tony-samples/reverse.tony
./tony.sh run
```

### Manually

#### Compile a Tony source file
```bash
java -jar target/tony-compiler.jar <file.tony>
```

#### Run the compiled program
```bash
java -cp out Main
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

## Output

Compiled classes are written to the `out/` directory and can be run with `./tony.sh run` or `java -cp out Main`.
