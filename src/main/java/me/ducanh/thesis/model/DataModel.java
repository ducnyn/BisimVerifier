package me.ducanh.thesis.model;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mvp.model.Person;

import java.io.File;
import java.util.ArrayList;

public class DataModel {
    private final ObservableList<Node> nodeList = FXCollections.observableArrayList();
    private final ObservableList<Edge> edgeList = FXCollections.observableArrayList();

    public void addNode(Node node){
        nodeList.add(node);
    }

    public void addEdge(Edge edge){
        edgeList.add(edge);
    }

    public ObservableList<Node> getNodes(){
        return nodeList;
    }

    public ObservableList<Edge> getEdges(){
        return edgeList;
    }
    public String toDot() { //TODO : How about toDot for sub graphs
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


//
//private final ObservableList<Graph> graphList = FXCollections.observableArrayList(graph ->
//        new Observable[]{graph.getNodes(), graph.getEdges()});

//private final ObjectProperty<Graph> currentGraph = new SimpleObjectProperty<>(null);
//
//public ObjectProperty<Graph> currentGraphProperty() {
//    return currentGraph;
//}


//public ObservableList<Graph> getPersonList() {
//    return graphList;
//}

//    public void loadData(File file) {
//        // mock...
//        graphList.setAll(
//                new Person("Jacob", "Smith", "jacob.smith@example.com"),
//                new Person("Isabella", "Johnson","isabella.johnson@example.com"),
//                new Person("Ethan", "Williams", "ethan.williams@example.com"),
//                new Person("Emma", "Jones", "emma.jones@example.com"),
//                new Person("Michael", "Brown", "michael.brown@example.com")
//        );
//    }

//    public void saveData(File file) { }

