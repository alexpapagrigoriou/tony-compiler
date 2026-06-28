package io.github.alexpapagre.tonycompiler.runtime;

import java.io.IOException;
import java.util.Scanner;

public final class Runtime {

    private Runtime() {
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

    public static int abs(int n) {
        return Math.abs(n);
    }

    public static int ord(char c) {
        return (int) c;
    }

    public static char chr(int n) {
        return (char) n;
    }

    public static int strlen(char[] s) {
        int i = 0;

        while (s[i] != '\0') {
            i++;
        }

        return i;
    }

    public static int strcmp(char[] s1, char[] s2) {
        int i = 0;

        while (s1[i] != '\0' && s2[i] != '\0') {
            if (s1[i] != s2[i]) {
                return s1[i] - s2[i];
            }

            i++;
        }

        if (s1[i] != '\0' || s2[i] != '\0') {
            return s1[i] - s2[i];
        }

        return 0;
    }

    public static void strcpy(char[] trg, char[] src) {
        int i = 0;

        while (src[i] != '\0') {
            trg[i] = src[i];
            i++;
        }

        trg[i] = '\0';
    }

    public static void strcat(char[] trg, char[] src) {
        int i = 0;

        while (trg[i] != '\0') {
            i++;
        }

        int j = 0;

        while (src[j] != '\0') {
            trg[i++] = src[j++];
        }

        trg[i] = '\0';
    }
}
