package me.ducanh.thesis.model;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class Graph {
    Set<Vertex> vertices;
    Set<Edge> edges;
    int currentVertex;

    public void addVertices(Vertex v){
        vertices.add(v);
    }

    public void addVertex(){
        Vertex vertex = new Vertex(this, currentVertex++);
        vertices.add(vertex);
    }


    public  <C extends Collection<Vertex>> void addVertices(C vCollection){
        for(Vertex v : vCollection){
            this.addVertices(v);
        }
    }

public void addEdge(Vertex source, String label, Vertex target){
    edges.add(new Edge(this, source,label,target));
}




/** These are just for convenience and testing, might delete again*/

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


}
