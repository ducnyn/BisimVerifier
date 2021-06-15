package me.ducanh.thesis.model;

import java.util.HashSet;
import java.util.Set;

public class BisimTree {
Set<Vertex> vertices;

BisimTree trueChild;
BisimTree falseChild;
boolean predicate;
String splitter;

public BisimTree(Set<Vertex> vertices) {
    this.vertices = new HashSet<>(vertices);
}

public BisimTree getTrueChild() {
    return trueChild;
}

public void setTrueChild(BisimTree bisimTree) {
    this.trueChild = bisimTree;
}

public BisimTree getFalseChild() {
    return falseChild;
}

public Set<Vertex> getVertices() {
    return vertices;
}

public void setVertices(Set<Vertex> vertices) {
    this.vertices = vertices;
}

public void setFalseChild(BisimTree bisimTree) {
    this.falseChild = bisimTree;
}
}

