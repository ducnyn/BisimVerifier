package me.ducanh.thesis.model;

public class Edge {
private String label;
private Vertex source;
private Vertex target;

public Edge(String label, Vertex source, Vertex target) {
    this.label = label;
    this.source = source;
    this.target = target;
}

@Override
public String toString(){
    return source.toString() + " -" + label + "> " + target.toString();
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
