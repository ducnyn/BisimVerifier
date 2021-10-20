package me.ducanh.thesis.model;

import com.brunomnsilva.smartgraph.graph.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomVertex implements Vertex<Integer> {
  private final ObservableSet<CustomEdge> edgeSet;
  private final Integer ID;

  public CustomVertex(int id, ObservableSet<CustomEdge> edgeSet){
    this.ID = id;
    this.edgeSet = edgeSet;
  }
  public CustomVertex(int id){
    this.ID = id;
    this.edgeSet = FXCollections.observableSet(new HashSet<>());
  }
  public ObservableSet<CustomEdge> getEdges(){
    return edgeSet;//do listeners get notified by underlying changes?
  }

  public Set<CustomVertex> getTargets(String label){
    return edgeSet.stream()
            .filter(edge->edge.getLabel().equals(label))
            .map(CustomEdge::getTarget)
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

    CustomVertex vertex = (CustomVertex) o;

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
