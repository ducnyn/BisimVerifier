package me.ducanh.thesis;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import me.ducanh.thesis.algorithms.Algorithms;

import java.util.*;
import java.util.stream.Collectors;


public class Model {
  private String username = "User";

//  private final ObservableMap<Integer, Vertex> vertices = FXCollections.observableMap(new HashMap<>());
  private final ObservableMap<Vertex,ObservableSet<Edge>> edgesByVertex = FXCollections.observableMap(new HashMap<>());
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final BooleanProperty colorModeProperty = new SimpleBooleanProperty(false);
  private final BooleanProperty printRequest = new SimpleBooleanProperty();
  private  String printString = "";

//  private boolean addedByVis = false;


  {//initiator
    edgesByVertex.addListener((MapChangeListener<Vertex, ObservableSet<Edge>>) vertex -> {

      if (vertex.wasRemoved()) {
          Vertex removedVertex = vertex.getKey();
          deletedIDs.add(removedVertex.getLabel());
          vertex.getValueRemoved().clear();
          for (ObservableSet<Edge> edgeSet : vertex.getMap().values()) {
              edgeSet.removeIf(edge -> edge.getSource().equals(removedVertex) || edge.getTarget().equals(removedVertex));
          }
      }
    });



  }

  public void addEdge(Vertex source, String label, Vertex target) {
      if(!edgesByVertex.containsKey(target))
          addVertex(target);
      if(!edgesByVertex.containsKey(source))
          addVertex(source);
    edgesByVertex.get(source).add(new Edge(source, label, target));
  }

  public Boolean addVertex(Vertex vertex) {
    if (edgesByVertex.containsKey(vertex))
      return false;

    edgesByVertex.put(vertex,FXCollections.observableSet(new HashSet<>()));
    return true;
  }

 public Integer smallestFreeLabel(){
    return Objects.requireNonNullElse(deletedIDs.pollFirst(), getMaxID() + 1);
 }

  public void clear(){
      edgesByVertex.keySet().clear();

  }



  public Integer getMaxID() {
    return edgesByVertex.keySet().stream()
            .max(Vertex::compareTo)
            .map(Vertex::getLabel)
            .orElse(0);
  }

  public void removeAllEdges() {
    for(Set<Edge> edgeSet: edgesByVertex.values()){
      edgeSet.clear();
    }
  }

  public Boolean removeVertex(Vertex vertex) {
    if (edgesByVertex.containsKey(vertex)){
      edgesByVertex.remove(vertex);
      return true;
    }
    return false;
  }

  public void addGraphListener(MapChangeListener<Vertex, ObservableSet<Edge>> mapChangeListener) {
    edgesByVertex.addListener(mapChangeListener);
  }

 public ObservableMap<Vertex,ObservableSet<Edge>> getEdgesByVertex(){
      return edgesByVertex;
 }
  public Set<Vertex> getVertices() {
    return Set.copyOf(edgesByVertex.keySet());
  }

  public Set<Edge> getEdges() {
    return Set.copyOf(
            edgesByVertex.values().stream()
            .flatMap(ObservableSet::stream)
            .collect(Collectors.toSet())
    );
  }
  public Set<Edge> getEdges(Vertex vertex){
    return Set.copyOf(edgesByVertex.get(vertex));
  }

  public BooleanProperty getColorModeProperty(){
    return colorModeProperty;
  }
  public Boolean getColorMode(){
      return colorModeProperty.get();
  }

  public List<Block> getBisimulation(){
      return Algorithms.getBisimRootAndPartition(this).getValue();
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
      return edgesByVertex.get(vertex).stream()
              .filter(edge->edge.getLabel().equals(action))
              .map(Edge::getTarget)
              .collect(Collectors.toSet());
    }

    public Vertex getVertex(int vertexInt){
      return new Vertex(vertexInt);
    }


}




