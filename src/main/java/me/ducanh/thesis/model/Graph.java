package me.ducanh.thesis.model;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class Graph {
    Set<Vertex> vertices;
    Set<Edge> edges;

    public void addVertices(Vertex vertex){
        if (vertices.stream().noneMatch(v -> v.getId() == vertex.getId())){
            vertices.add(vertex);
        }
    }

    public  <C extends Collection<Vertex>> void addVertices(C vertexCollection){
        for(Vertex vertex:vertexCollection){
            this.addVertices(vertex);
        }
    }

    public void addVertices(int id){
        addVertices(new Vertex(id));
    }

    public void addVertices(int... ids){
        for(int id : ids){
            addVertices(new Vertex(id));
        }
    }

    public Vertex getVertex(int id){
          for(Vertex vertex : vertices){
              if (vertex.getId() == id){
                  return vertex;
              }
          }
          return null;
    }

    public void addEdge(String source, String label, String target){
        edges.add(new Edge(source,label,target));
    }
}
