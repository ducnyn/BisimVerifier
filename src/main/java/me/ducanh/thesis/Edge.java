package me.ducanh.thesis;

import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class Edge implements Comparable<Edge> {

    public final Vertex source;
    public final Vertex target;
    public final String label;


    public Edge(Vertex source, String label, Vertex target) {
        this.source = source;
        this.target = target;
        this.label = label;
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

        if (!this.source.equals(o.source)) {
            return Vertex.compare(this.source, o.source); // better to do this in Verteex Class
        }
        if (!this.label.equals(o.label)) {
            return this.label.compareTo(o.label);
        }
        if (!this.target.equals(o.target)) {
            return java.lang.Integer.compare(this.target.hashCode(), o.target.hashCode());
        }
        return 0;
    }
}


