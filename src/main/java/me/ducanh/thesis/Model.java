package me.ducanh.thesis;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;

import java.util.*;
import java.util.stream.Collectors;

public class Model {
  private String username = "User";

//  private final ObservableMap<Integer, Vertex> vertices = FXCollections.observableMap(new HashMap<>());
  private final ObservableMap<Vertex,ObservableSet<Edge>> adjacencyList = FXCollections.observableMap(new HashMap<>());
  private final ObservableSet<Block> partition = FXCollections.observableSet(new HashSet<>());
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final BooleanProperty colorToggle = new SimpleBooleanProperty(false);
  private final BooleanProperty printRequest = new SimpleBooleanProperty();
  private  String printString = "";

//  private boolean addedByVis = false;


  {//initiator
    adjacencyList.addListener((MapChangeListener<Vertex, ObservableSet<Edge>>) mapChange -> {

      if (mapChange.wasRemoved()){
        Vertex removed = mapChange.getKey();
        deletedIDs.add(removed.getLabel());
        mapChange.getValueRemoved().clear();
        for(ObservableSet<Edge> edgeSet : adjacencyList.values()){
          edgeSet.removeIf(edge -> edge.getSource().equals(removed) ||edge.getTarget().equals(removed));
        }
      }
    });



  }

  public void addEdge(Vertex source, String label, Vertex target) {
    adjacencyList.get(source).add(new Edge(source, label, target));
  }

  public Boolean addVertex(Vertex vertex) {
    if (adjacencyList.containsKey(vertex))
      return false;

    adjacencyList.put(vertex,FXCollections.observableSet(new HashSet<>()));
    return true;
  }

 public Integer smallestFreeLabel(){
    return Objects.requireNonNullElse(deletedIDs.pollFirst(), getMaxID() + 1);
 }

  public void clear(){
    for(Vertex vertex : adjacencyList.keySet()){
      removeVertex(vertex);
    }
  }



  public Integer getMaxID() {
    return adjacencyList.keySet().stream()
            .max(Vertex::compareTo)
            .map(Vertex::getLabel)
            .orElse(0);
  }

  public void removeAllEdges() {
    for(Set<Edge> edgeSet: adjacencyList.values()){
      for(Edge edge:edgeSet){
        edgeSet.remove(edge);
      }
    }
  }

  public Boolean removeVertex(Vertex vertex) {
    if (adjacencyList.containsKey(vertex)){
      adjacencyList.remove(vertex);
      return true;
    }
    return false;
  }

  public void addVertexListener(MapChangeListener<Vertex, ObservableSet<Edge>> mapChangeListener) {
    adjacencyList.addListener(mapChangeListener);
  }

  public void addEdgeListener(SetChangeListener<Edge> edgeSetListener){
    for(ObservableSet<Edge> edgeSet : adjacencyList.values()){
      edgeSet.addListener(edgeSetListener);
    }
  }


  public Set<Vertex> getVertices() {
    return Set.copyOf(adjacencyList.keySet());
  }

  public Set<Edge> getEdges() {
    return Set.copyOf(
            adjacencyList.values().stream()
            .flatMap(ObservableSet::stream)
            .collect(Collectors.toSet())
    );
  }
  public Set<Edge> getEdges(Vertex vertex){
    return Set.copyOf(adjacencyList.get(vertex));
  }

  public BooleanProperty getColorToggleProperty(){
    return colorToggle;
  }

  public ObservableSet<Block> getPartition() {
    return partition;
  }

  public void setPartition(Set<Block> newPartition) {
    this.partition.clear();
    this.partition.addAll(newPartition);
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

  public void setUsername(String name){
    this.username = name;
  }

  public String getUserName() {
    return username;
  }

}




