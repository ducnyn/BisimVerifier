package me.ducanh.thesis.model;

public class Edge {
private String label;
private int source;
private int target;

public Edge(int source, String label, int target) {
    this.label = label;
    this.source = source;
    this.target = target;
}

@Override
public String toString(){
    return source + " -" + label + "> " + target;
}
public Vertex getTarget() {
    return target;
}

public Vertex getSource() {
    return source;
}

public String getLabel() {
    return label;
}
}
