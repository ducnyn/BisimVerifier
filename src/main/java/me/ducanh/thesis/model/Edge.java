package me.ducanh.thesis.model;

public class Edge {
private String label;
private Vertex source;
private Vertex target;
private Graph graph;

public Edge(Graph graph, Vertex source, String label, Vertex target) {
    this.graph = graph;
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
