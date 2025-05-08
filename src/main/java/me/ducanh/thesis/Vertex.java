package me.ducanh.thesis;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Vertex implements Comparable<Vertex>{
    public final Integer id;
    public final DoubleProperty layoutXProperty;
    public final DoubleProperty layoutYProperty;

    public Vertex(Integer id) {
        this(id, 0, 0);
    }
    public Vertex(Integer id, double x, double y){
        this.id = id;
        this.layoutXProperty = new SimpleDoubleProperty(x);
        this.layoutYProperty = new SimpleDoubleProperty(y);
    }

    @Override
    public int compareTo(Vertex vertex) {
        return this.id.compareTo(vertex.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return id.equals(vertex.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    @Override
    public String toString(){
        return this.id.toString();
    }

    public static int compare(Vertex vertex1, Vertex vertex2) {
        return Integer.compare(vertex1.id,vertex2.id);
    }
    

}
