#!/bin/bash

set -e

OUT_DIR=out
JAR=target/tony-compiler.jar

usage() {
    echo "Usage:"
    echo "  ./tony build <inputFile.tony> [-o <outputName>]"
    echo "  ./tony run [<className>]"
    echo "  ./tony clean"
}

CMD=$1

case "$CMD" in
compile)
    if [ "$#" -ne 1 ]; then
        echo "Error: compile takes no arguments"
        usage
        exit 1
    fi
    ./mvnw -q clean package
    ;;
build)
    if [ ! -f "$JAR" ]; then
        echo "Error: '$JAR' not found. Run './tony.sh compile' first."
        exit 1
    fi

    if [ $# -eq 2 ]; then
        INPUT_FILE=$2

        java -jar "$JAR" "$INPUT_FILE"

    elif [ $# -eq 4 ] && [ "$3" = "-o" ]; then
        INPUT_FILE=$2
        OUTPUT_NAME=$4

        java -jar "$JAR" "$INPUT_FILE" -o "$OUTPUT_NAME"
    else
        usage
        exit 1
    fi
    ;;
run)
    if [ ! -d "$OUT_DIR" ]; then
        echo "Error: '$OUT/' not found. Run 'tony build <inputFile.tony> [-o <outputName>]' first."
        exit 1
    fi

    CLASS_NAME="Main"

    if [ $# -gt 2 ]; then
        usage
        exit 1
    fi

    if [ -n "$2" ]; then
        CLASS_NAME=$2

        if ! [[ "$CLASS_NAME" =~ ^[A-Za-z][A-Za-z0-9]*$ ]]; then
            echo "Error: invalid class name '$CLASS_NAME'"
            echo "Output name must start with a letter and contain only letters and digits."
            exit 1
        fi

        FIRST_CHAR=$(echo "${CLASS_NAME:0:1}" | tr '[:lower:]' '[:upper:]')
        REST="${CLASS_NAME:1}"
        CLASS_NAME="${FIRST_CHAR}${REST}"
    fi

    java -cp "$OUT_DIR" "$CLASS_NAME"
    ;;
clean)
    if [ "$#" -ne 1 ]; then
        echo "Error: clean takes no arguments"
        usage
        exit 1
    fi

    rm -rf "$OUT_DIR"
    ;;
*)
    usage
    exit 1
    ;;
esac
