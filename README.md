# Tony Compiler

 A compiler for the Tony programming language that generates JVM bytecode.

## Build
```bash
./mvnw clean package
```

## Usage

### Via Script

#### Build a Tony source file
```bash
./tony.sh build <file>
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
java -jar target/tony-compiler.jar <file>
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
