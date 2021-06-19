package me.ducanh.thesis.model;

import com.google.common.collect.Multimap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
private ObservableList<Vertex> vertices = FXCollections.observableArrayList();
private ObservableList<Edge> edges = FXCollections.observableArrayList();
private int vertexCount = 0;
private Map<String,Integer> labelToID = new HashMap<>();
private Map<Integer,String> idToLabel = new HashMap<>();



public ObservableList<Vertex> getVertices() {
    return vertices;
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

//if the vertices are missing, they're added before the edge.
public void addEdge(int source, String label, int target)  {

        while (vertexCount<= source || vertexCount<=target) {
            addVertex();
        }
        vertices.get(source).addTransition(label,vertices.get(target));
        edges.add(new Edge(this,vertices.get(source),label,vertices.get(target)));
    }



public List<Edge> getEdges() {
//    return  vertices.stream()
//            .collect(Collectors.toMap(Vertex::getId,Vertex::getTransitions));
    return this.edges;
}

public Vertex getVertex(int id) {
    for (Vertex vertex : vertices) {
        if (vertex.getId() == id) {
            return vertex;
        }
    }
    return null;
}

}
