package me.ducanh.thesis.model;

public class Edge {
private String label;
private Node source;
private Node target;

public Edge(String label, Node source, Node target) {
    this.label = label;
    this.source = source;
    this.target = target;
}

@Override
public String toString(){
    return source.toString() + " -" + label + "> " + target.toString();
}
public Node getTarget() {
    return target;
}

public Node getSource() {
    return source;
}

public String getLabel() {
    return label;
}
}
