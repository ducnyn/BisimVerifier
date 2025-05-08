package me.ducanh.thesis;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import me.ducanh.thesis.algorithms.Algorithms;
import me.ducanh.thesis.algorithms.Block;

import java.util.*;
import java.util.stream.Collectors;


public class Model implements Graph<Vertex,Edge>{

  private final ObservableMap<Vertex, ObservableSet<Edge>> adjacencyMap = FXCollections.observableMap(new HashMap<>());
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final BooleanProperty coloringIsEnabled = new SimpleBooleanProperty(false);
  private final BooleanProperty printRequest = new SimpleBooleanProperty();
  private  String printString = "";


  {//initiator
    adjacencyMap.addListener((MapChangeListener<Vertex,ObservableSet<Edge>>) entry -> {

      if (entry.wasRemoved()) { //vertex removed
          deletedIDs.add(entry.getKey().id);
          entry.getValueRemoved().clear();
          for (ObservableSet<Edge> edgeSet : entry.getMap().values()) {
              edgeSet.removeIf(edge -> edge.source.equals(entry.getKey()) || edge.target.equals(entry.getKey()));
          }
      }
    });
  }

public void addEdge(Vertex source, String label, Vertex target) {
    if(!adjacencyMap.containsKey(target))
        addVertex(target);
    if(!adjacencyMap.containsKey(source))
        addVertex(source);
    adjacencyMap.get(source).add(new Edge(source, label, target));
}

public void addVertex(Vertex vertex) {
        adjacencyMap.putIfAbsent(vertex,FXCollections.observableSet());
}

 public Integer smallestFreeLabel(){
    return Objects.requireNonNullElse(deletedIDs.pollFirst(), getMaxID() + 1);
 }

  public void clear(){
      adjacencyMap.keySet().clear();
  }



  public Integer getMaxID() {
      try{
          return Collections.max(adjacencyMap.keySet()).id;
      } catch (NoSuchElementException e) {
          return 0;
      }
 }

  public void removeAllEdges() {
    for(Set<Edge> edgeSet: adjacencyMap.values()){
      edgeSet.clear();
    }
  }

  public void removeVertex(Vertex vertex) {
    if (adjacencyMap.containsKey(vertex)){
      adjacencyMap.remove(vertex);
    }
  }

  public void addGraphListener(MapChangeListener<Vertex, ObservableSet<Edge>> mapChangeListener) {
    adjacencyMap.addListener(mapChangeListener);
  }

 public ObservableMap<Vertex,ObservableSet<Edge>> getAdjacencyMap(){
      return adjacencyMap;
 }
  public Set<Vertex> getVertices() {
    return Set.copyOf(adjacencyMap.keySet());
  }

  public Set<Edge> getEdges() {
    return Set.copyOf(
            adjacencyMap.values().stream()
            .flatMap(ObservableSet::stream)
            .collect(Collectors.toSet())
    );
  }
  public Set<Edge> getEdges(Vertex vertex){
    return Set.copyOf(adjacencyMap.get(vertex));
  }

  public BooleanProperty coloringToggle(){
    return coloringIsEnabled;
  }
  public Boolean coloringIsEnabled(){
      return coloringIsEnabled.get();
  }

  public List<Block> getBisimulation(){
      return Algorithms.partitionByBisimilarity(this).getValue();
  }



  public void requestPrint(String string){
    this.printString = string;
    this.printRequest.setValue(false);
    this.printRequest.setValue(true);
  }
  public BooleanProperty printRequestedProperty(){
    return printRequest;
  }

  public String getPrintString(){
    return printString;
  }

    public Set<Vertex> getTargets(Vertex vertex, String action) {
      return adjacencyMap.get(vertex).stream()
              .filter(edge->edge.label.equals(action))
              .map(edge->edge.target)
              .collect(Collectors.toSet());
    }

    public Vertex getVertex(int vertexInt){
      return new Vertex(vertexInt);
    }


}




