package io.github.alexpapagrigoriou.tonycompiler.runtime;

import java.io.IOException;
import java.util.Scanner;

public final class TonyIO {

    private TonyIO() {
    }

    public static void puti(int n) {
        System.out.print(n);
    }

    public static void putb(boolean b) {
        System.out.print(b);
    }

    public static void putc(char c) {
        System.out.print(c);
    }

    public static void puts(char[] s) {
        System.out.print(new String(s));
    }

    public static int geti() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static boolean getb() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextBoolean();
    }

    public static char getc() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next().charAt(0);
    }

    public static void gets(int n, char[] s) {
        int c;
        int i = 0;

        try {
            while (i < n - 1 && (c = System.in.read()) != '\n' && c != -1) {
                s[i++] = (char) c;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        s[i] = '\0';
    }
}
