package com.dbms.model;

public class Pair<T, U> {
    public final T key;
    public final U value;

    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }
}