package me.ducanh.thesis.util;

import me.ducanh.thesis.model.Graph;

public class DummyGraph {
    //Creates a dummy graph

    public Graph defaultGraph(){
        Graph graph = new Graph();
        graph.addVertices(1,2);
        graph.getVertex(1);
        return graph;
    }

}
