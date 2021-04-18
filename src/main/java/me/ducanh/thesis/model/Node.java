package me.ducanh.thesis.model;

import java.util.ArrayList;

public class Node {
    private String name;


public Node(String name){
    this.name = name;
}

public String getName() {
    return name;
}

@Override
    public String toString(){
        return name;
    }



}
