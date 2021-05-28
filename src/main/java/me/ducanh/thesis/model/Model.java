package me.ducanh.thesis.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public class Model {
    private final ObservableSet<Vertex> vertices = FXCollections.observableSet();
    private final ObservableSet<Edge> edges = FXCollections.observableSet();

    public void addVertex(Vertex vertex){
        vertices.add(vertex);
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public ObservableSet<Vertex> getVertices(){
        return vertices;
    }

    public ObservableSet<Edge> getEdges() {
        return edges;
    }


    //Outputs Graph in Dotfile Notation String for Graphviz and Dot2Tex
    public String currentToDot() { //TODO : How about toDot for sub graphs
        StringBuilder graphString = new StringBuilder();
        graphString
                .append("digraph {");

        for (Vertex vertex : vertices) {
            graphString
                    .append("\n\t")
                    .append(vertex.getId());
        }
        graphString.append("\n");
        for (Edge edge : edges) {
            graphString
                    .append("\n\t")
                    .append(edge.getSource().getId())
                    .append(" -> ")
                    .append(edge.getTarget().getId())
                    .append("[label = ")
                    .append(edge.getLabel())
                    .append("]");
        }
        return graphString.append("\n}").toString();
    }
}




