package me.ducanh.thesis;

import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class Edge implements Comparable<Edge> {

    private final Vertex source;
    private final Vertex target;
    private final String label;


    public Edge(Vertex source, String label, Vertex target) {
        this.source = source;
        this.target = target;
        this.label = label;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return label.equals(edge.label) && source.equals(edge.source) && target.equals(edge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, source, target);
    }

    @Override
    public String toString() {
        return source + " -" + label + "> " + target;
    }

    @Override
    public int compareTo(Edge o) { //sorting priority source > label > target

        if (!this.getSource().equals(o.getSource())) {
            return Vertex.compare(this.getSource(), o.getSource()); // better to do this in Verteex Class
        }
        if (!this.getLabel().equals(o.getLabel())) {
            return this.getLabel().compareTo(o.getLabel());
        }
        if (!this.getTarget().equals(o.getTarget())) {
            return java.lang.Integer.compare(this.getTarget().hashCode(), o.getTarget().hashCode());
        }
        return 0;
    }
}


