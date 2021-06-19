package me.ducanh.thesis.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Map;

public class Model {
private  ObservableList<Vertex> vertices = FXCollections.observableArrayList();
private  ObservableList<Edge> edges = FXCollections.observableArrayList(); //TODO Keep sorted by Source Vertex
private final StringProperty dotString = new SimpleStringProperty("digraph {\n\n}");
private final StringProperty appendString = new SimpleStringProperty("");
private Graph graph;// TODO make this observable.

public Model(){
    graph = new Graph();
}
public Graph getGraph(){
    return graph;
}

public void setGraph(Graph graph){
    this.graph = graph;
    copyGraph(graph);
    updateDotString(graph);

}
public void addVertices(int number){
    if (graph != null){
        graph.addVertices(number);
        copyGraph(graph);
    } else {
        System.out.println("this shouldn't happen, but null graph.");
    }
}

public void addEdge(int src, String lbl, int tar){
    graph.addEdge(src,lbl,tar);
        copyGraph(graph);

}
public StringProperty getAppendStringProperty() {
    return appendString;
}

public final void setAppendString(String string) {
    getAppendStringProperty().set(string);
}

public StringProperty getDotStringProperty() {
    return dotString;
}

public final String getDotString() {
    return getDotStringProperty().get();
}

public final void setDotString(String dotString) {
    getDotStringProperty().set(dotString);
}

public void updateDotString(Graph graph) {
    StringBuilder graphString = new StringBuilder();
    graphString
            .append("digraph {");

    for (Vertex vertex : graph.getVertices()) {
        if (vertex.getTransitions().isEmpty()){
            graphString
                    .append("\n\t")
                    .append(vertex.getId());
        }

    }
    graphString.append("\n");
    for (Edge edge : graph.getEdges()) {
        graphString
                .append("\n\t")
                .append(edge.getSource().getId())
                .append(" -> ")
                .append(edge.getTarget().getId())
                .append("[label = ")
                .append(edge.getLabel())
                .append("]");
    }
    graphString.append("\n}");
    getDotStringProperty().set(graphString.toString());
}

public void addVertex(Vertex vertex) {
    vertices.add(vertex);
}

public void addEdge(Edge edge) {
    edges.add(edge);
}

public void copyGraph(Graph graph) {
    vertices.clear();
    edges.clear();
    for (Vertex vertex : graph.getVertices()) {
        addVertex(vertex);
        for (Map.Entry<String, Vertex> transition : vertex.getTransitions().entries()) {
            Edge newEdge = new Edge(graph, vertex, transition.getKey(), transition.getValue());
            addEdge(newEdge);


        }
    }updateDotString(graph);
}

public ObservableList<Vertex> getVertices() {
    return vertices;
}

public ObservableList<Edge> getEdges() {
    return edges;
}


//Outputs Graph in Dotfile Notation String for Graphviz and Dot2Tex

}




