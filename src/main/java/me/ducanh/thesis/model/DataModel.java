package me.ducanh.thesis.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {
    private final ObservableList<Node> nodeList = FXCollections.observableArrayList();
    private final ObservableList<Edge> edgeList = FXCollections.observableArrayList();

    public void addNode(Node node){
        nodeList.add(node);
    }

    public void addEdge(Edge edge){
        edgeList.add(edge);
    }

    public ObservableList<Node> getNodeList(){
        return nodeList;
    }

    public ObservableList<Edge> getEdgeList() {
        return edgeList;
    }


    //Outputs Graph in Dotfile Notation String for Graphviz and Dot2Tex
    public String currentToDot() { //TODO : How about toDot for sub graphs
        StringBuilder graphString = new StringBuilder();
        graphString
                .append("digraph {");

        for (Node node : nodeList) {
            graphString
                    .append("\n\t")
                    .append(node.getName());
        }
        graphString.append("\n");
        for (Edge edge : edgeList) {
            graphString
                    .append("\n\t")
                    .append(edge.getSource().getName())
                    .append(" -> ")
                    .append(edge.getTarget().getName())
                    .append("[label = ")
                    .append(edge.getLabel())
                    .append("]");
        }
        return graphString.append("\n}").toString();
    }
}




