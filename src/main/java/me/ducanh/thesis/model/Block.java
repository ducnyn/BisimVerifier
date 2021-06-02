package me.ducanh.thesis.model;

import java.util.HashSet;
import java.util.Set;

public class Block {
Set<Vertex> vertices;

Block trueChild;
Block falseChild;
boolean predicate;
String splitter;

public Block(Set<Vertex> vertices) {
    this.vertices = new HashSet<>(vertices);
}

public Block getTrueChild() {
    return trueChild;
}

public void setTrueChild(Block block) {
    this.trueChild = block;
}

public Block getFalseChild() {
    return falseChild;
}

public Set<Vertex> getVertices() {
    return vertices;
}

public void setVertices(Set<Vertex> vertices) {
    this.vertices = vertices;
}

public void setFalseChild(Block block) {
    this.falseChild = block;
}
}

