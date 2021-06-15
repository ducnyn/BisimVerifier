package me.ducanh.thesis.model;

import java.util.*;

public class Graph {
ArrayList<Vertex> vertices;
Set<Edge> edges;
private int vertexCount;

public Graph(){
    vertexCount = 0;
    this.vertices = new ArrayList<>();
    this.edges = new HashSet<>();
}

public int getVertexCount(){
    return vertexCount;
}
public List<Vertex> getVertices() {
    return vertices;
}

public Set<Edge> getEdges() {
    return edges;
}



public void addEdge(int source, String label, int target) { //this assumes all vertices actually exist already.

    if(source < vertexCount && target < vertexCount){
        edges.add(new Edge(this, vertices.get(source), label, vertices.get(target)));
        vertices.get(source).addTransition(label,vertices.get(target));
    }

}



public void addVertex() {
    Vertex vertex = new Vertex(this, vertexCount++);
    vertices.add(vertex);
}


public void addVertices(int id) {
    for(int i = 0; i<id; i++){
        addVertex();
    }
}

public void addVertices(Vertex v) {
    vertices.add(v);
}

public <C extends Collection<Vertex>> void addVertices(C vCollection) {
    for (Vertex v : vCollection) {
        this.addVertices(v);
    }
}
//public void addVertices(int... ids) {
//    for (int id : ids) {
//        addVertices(new Vertex(this, id));
//    }
//}

public Vertex getVertex(int id) {
    for (Vertex vertex : vertices) {
        if (vertex.getId() == id) {
            return vertex;
        }
    }
    return null;
}


}
