package me.ducanh.thesis;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Vertex implements Comparable<Vertex>{
    public final Integer label;
    public final DoubleProperty layoutXProperty;
    public final DoubleProperty layoutYProperty;

    public Vertex(Integer label) {
        this(label, 0, 0);
    }
    public Vertex(Integer label, double x, double y){
        this.label = label;
        this.layoutXProperty = new SimpleDoubleProperty(x);
        this.layoutYProperty = new SimpleDoubleProperty(y);
    }

    @Override
    public int compareTo(Vertex vertex) {
        return this.label.compareTo(vertex.label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return label.equals(vertex.label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }
    @Override
    public String toString(){
        return this.label.toString();
    }

    public static int compare(Vertex vertex1, Vertex vertex2) {
        return Integer.compare(vertex1.label,vertex2.label);
    }
    

}
