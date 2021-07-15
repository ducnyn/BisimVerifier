package me.ducanh.thesis.model;

import java.util.HashSet;
import java.util.Set;

public class BisimTree {
Set<Integer> vertices;

BisimTree trueChild;
BisimTree falseChild;
boolean predicate;
String splitter;

public BisimTree(Set<Integer> vertices) {
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

public void setFalseChild(BisimTree bisimTree) {
    this.falseChild = bisimTree;
}

public Set<Integer> getVertices() {
    return vertices;
}

public void setVertices(Set<Integer> vertices) {
    this.vertices = vertices;
}
}

