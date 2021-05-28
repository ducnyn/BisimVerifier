package me.ducanh.thesis.model;

import java.util.Collection;
import java.util.Set;

public class Graph {
    Set<Vertex> vertices;
    Set<Edge> edges;

    public void addVertices(Vertex vertex){
        if (vertices.stream().noneMatch(v -> v.getId().equals(vertex.getId()))){
            vertices.add(vertex);
        }
    }

    public  <C extends Collection<Vertex>> void addVertices(C vertexCollection){
        for(Vertex vertex:vertexCollection){
            this.addVertices(vertex);
        }
    }

    public void addVertices(String string){
        addVertices(new Vertex(string));
    }

    public void addVertices(String... strings){
        for(String string : strings){
            addVertices(new Vertex(string));
        }
    }
}
