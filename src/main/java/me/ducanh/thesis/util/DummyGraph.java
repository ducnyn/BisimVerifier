package me.ducanh.thesis.util;

import me.ducanh.thesis.model.Graph;
import me.ducanh.thesis.model.Vertex;

import java.util.HashSet;
import java.util.stream.Collectors;

public class DummyGraph {
    //Creates a dummy graph

    public static Graph defaultGraph(){
        Graph graph = new Graph();
        graph.addVertices(8);
        graph.addEdge(1,"a", 2);
        graph.addEdge(2,"b", 3);
        graph.addEdge(2,"c", 3);
        graph.addEdge(4,"a", 5);
        graph.addEdge(4,"a", 6);
        graph.addEdge(5,"b", 7);
        graph.addEdge(6,"c", 7);

        return graph;
    }

}
