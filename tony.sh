#!/bin/bash

set -e

CMD=$1
OUT=out

usage() {
    echo "Usage:"
    echo "  ./tony build <file.tony>"
    echo "  ./tony run"
    echo "  ./tony clean"
    exit 1
}

case "$CMD" in
build)
    if [ $# -ne 2 ]; then
        echo "Error: build requires exactly 1 argument"
        usage
    fi

    FILE=$2

    if [ ! -f "$FILE" ]; then
        echo "Error: file '$FILE' not found"
        exit 1
    fi

    rm -rf $OUT
    mkdir -p "$OUT"

    ./mvnw -q clean package
    java -jar target/tony-compiler.jar "$FILE"
    ;;

run)
    if [ "$#" -ne 1 ]; then
        echo "Error: run takes no arguments"
        usage
    fi

    if [ ! -d "$OUT" ]; then
        echo "Error: out/ not found. Run 'tony build <file>' first."
        exit 1
    fi

    if [ ! -f "$OUT/Main.class" ]; then
        echo "Error: Main.class not found in out/"
        exit 1
    fi

    java -cp "$OUT" Main
    ;;

clean)
    if [ "$#" -ne 1 ]; then
        echo "Error: clean takes no arguments"
        usage
    fi

    rm -rf "$OUT"
    ;;

*)
    usage
    ;;
esac
