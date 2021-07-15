package me.ducanh.thesis.model;

import java.util.Objects;

public class Edge implements Comparable<Edge>{
private final String label;
private final Integer source;
private final Integer target;

public Edge(Integer source, String label, Integer target) {
    this.label = label;
    this.source = source;
    this.target = target;
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

public Integer getTarget() {
    return target;
}

public Integer getSource() {
    return source;
}

public String getLabel() {
    return label;
}

@Override
public int compareTo(Edge o) {

    if(!this.getSource().equals(o.getSource())){
        return Integer.compare(this.getSource(),o.getSource());
    }
    if(!this.getLabel().equals(o.getLabel())){
        return this.getLabel().compareTo(o.getLabel());
    }
    if(!this.getTarget().equals(o.getTarget())){
        return Integer.compare(this.getTarget(),o.getTarget());
    }
    return 0;
}

}
