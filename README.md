# Tony

## Build

```bash
./mvnw clean package
```

## Usage

There are two ways to run the program:

### 1. With a file argument

Reads input from a file:

```bash
java -jar target/tony.jar <file>
```

Example:

```bash
java -jar target/tony.jar src/main/resources/tony-samples/reverse.tony
```

### 2. Without a file argument

Reads input from standard input (`System.in`):

```bash
java -jar target/tony.jar
```
