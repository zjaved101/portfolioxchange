package com.example.frontend.model;

import java.util.ArrayList;

public class Search {
    private int index;
    private int length;
    private ArrayList<String> tags;

    public Search(int index, int length, ArrayList<String> tags) {
        this.index = index;
        this.length = length;
        this.tags = tags;
    }


    public int getIndex() {
        return index;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
