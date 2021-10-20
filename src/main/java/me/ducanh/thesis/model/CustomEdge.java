package me.ducanh.thesis.model;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import java.util.Objects;

public class CustomEdge implements Comparable<CustomEdge>, Edge<String, Integer> {
    private final String label;
    private final CustomVertex source;
    private final CustomVertex target;



    public CustomEdge(CustomVertex source, String label, CustomVertex target) {
        this.label = label;
        this.source = source;
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomEdge edge = (CustomEdge) o;
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

    public CustomVertex getTarget() {
        return target;
    }

    public CustomVertex getSource() {
        return source;
    }


    public String getLabel() {
        return label;
    }

    @Override
    public int compareTo(CustomEdge o) { //sorting priority source > label > target

        if (!this.getSource().equals(o.getSource())) {
            return Integer.compare(this.getSource().hashCode(), o.getSource().hashCode()); // better to do this in Verteex Class
        }
        if (!this.getLabel().equals(o.getLabel())) {
            return this.getLabel().compareTo(o.getLabel());
        }
        if (!this.getTarget().equals(o.getTarget())) {
            return Integer.compare(this.getTarget().hashCode(), o.getTarget().hashCode());
        }
        return 0;
    }

    @Override
    public String element() {
         return toString();
    }

    @Override
    public CustomVertex[] vertices() {
        CustomVertex[] vertices = new CustomVertex[2];
        vertices[0] = source;
        vertices[1] = target;
        return vertices;
    }
}
