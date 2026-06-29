package io.github.alexpapagrigoriou.tonycompiler.runtime;

public final class TonyList {
    public final int head;
    public final TonyList tail;

    public TonyList(int head, TonyList tail) {
        this.head = head;
        this.tail = tail;
    }
}
