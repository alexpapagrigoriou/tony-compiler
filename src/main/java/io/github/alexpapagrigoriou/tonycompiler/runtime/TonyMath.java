package io.github.alexpapagrigoriou.tonycompiler.runtime;

public final class TonyMath {

    private TonyMath() {
    }

    public static int abs(int n) {
        return Math.abs(n);
    }

    public static int ord(char c) {
        return (int) c;
    }

    public static char chr(int n) {
        return (char) n;
    }
}
