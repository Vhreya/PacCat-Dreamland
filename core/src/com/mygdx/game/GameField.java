package com.mygdx.game;

import java.util.List;

public class GameField {

    private final List<Integer> data;

    public GameField(List<Integer> data) {
        this.data = data;
    }

    public int size() {
        return data.size();
    }

    public List<Integer> getData() {
        return data;
    }
}
