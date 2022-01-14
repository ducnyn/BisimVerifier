package me.ducanh.thesis.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Vertex implements com.brunomnsilva.smartgraph.graph.Vertex {
  private final ObservableSet<Edge> edgeSet;
  private final Integer ID;

  public Vertex(int id, ObservableSet<Edge> edgeSet){
    this.ID = id;
    this.edgeSet = edgeSet;
  }
  public Vertex(int id){
    this.ID = id;
    this.edgeSet = FXCollections.observableSet(new HashSet<>());
  }
  public ObservableSet<Edge> getEdges(){
    return edgeSet;//do listeners get notified by underlying changes?
  }

  public Set<Vertex> getTargets(String label){
    return edgeSet.stream()
            .filter(edge->edge.getLabel().equals(label))
            .map(Edge::getTarget)
            .collect(Collectors.toSet());
  }

  @Override
  public String toString(){
    return String.valueOf(this.ID);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Vertex vertex = (Vertex) o;

    return ID.equals(vertex.ID);
  }

  @Override
  public int hashCode() {
    return ID;
  }

  public Integer getID() {
    return ID;
  }

  @Override
  public Integer element() {
    return ID;
  }
}
