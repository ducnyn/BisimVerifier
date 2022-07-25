package me.ducanh.thesis;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import me.ducanh.thesis.algorithms.Algorithms;
import me.ducanh.thesis.algorithms.Block;

import java.util.*;
import java.util.stream.Collectors;


public class Model implements Graph<Vertex,Edge>{
  private String username = "User";

  private final ObservableMap<Vertex, ObservableSet<Edge>> edgesByVertex = FXCollections.observableMap(new HashMap<>());
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final BooleanProperty coloringIsEnabled = new SimpleBooleanProperty(false);
  private final BooleanProperty printRequest = new SimpleBooleanProperty();
  private  String printString = "";

//  private boolean addedByVis = false;


  {//initiator
    edgesByVertex.addListener((MapChangeListener<Vertex,ObservableSet<Edge>>) entry -> {

      if (entry.wasRemoved()) {
          deletedIDs.add(entry.getKey().label);
          entry.getValueRemoved().clear();
          for (ObservableSet<Edge> edgeSet : entry.getMap().values()) {
              edgeSet.removeIf(edge -> edge.source.equals(entry.getKey()) || edge.target.equals(entry.getKey()));
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
      try{
          return Collections.max(edgesByVertex.keySet()).label;
      } catch (NoSuchElementException e) {
          return 0;
      }

//    return graph.getMap().keySet().stream()
//            .max(Vertex::compareTo)
//            .map(Vertex::getLabel)
//            .orElse(0);
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
      return edgesByVertex.get(vertex).stream()
              .filter(edge->edge.label.equals(action))
              .map(edge->edge.target)
              .collect(Collectors.toSet());
    }

    public Vertex getVertex(int vertexInt){
      return new Vertex(vertexInt);
    }


}




