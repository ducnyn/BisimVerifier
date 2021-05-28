package me.ducanh.thesis.model;

import java.util.Set;

public class BisimTree {
    Set<Vertex> block;

    BisimTree splitChild;
    BisimTree restChild;

    String splitter;

    public BisimTree(V value){
        this.value = value;
    }
}

