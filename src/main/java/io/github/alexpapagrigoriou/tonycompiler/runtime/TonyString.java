package io.github.alexpapagrigoriou.tonycompiler.runtime;

public final class TonyString {

    private TonyString() {
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
