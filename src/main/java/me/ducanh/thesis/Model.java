package me.ducanh.thesis;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;

import java.util.*;

public class Model {
  private String username = "User";

  private final ObservableMap<Integer, Vertex> vertices = FXCollections.observableMap(new TreeMap<>());
  private final ObservableSet<Edge> edges = FXCollections.observableSet(new TreeSet<>());
  private final ObservableSet<Block> partition = FXCollections.observableSet(new HashSet<>());
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final BooleanProperty bisimToggle = new SimpleBooleanProperty(false);
  private final BooleanProperty printRequest = new SimpleBooleanProperty();
  private  String printString = "";

//  private boolean addedByVis = false;


  {//initiator
    vertices.addListener((MapChangeListener<Integer, Vertex>) vertexChange -> {;

      if (vertexChange.wasAdded()) { //vertex added
        vertexChange.getValueAdded().getEdges().addListener((SetChangeListener<Edge>) edgeSetChange -> {

          if(edgeSetChange.wasAdded()){
            edges.add(edgeSetChange.getElementAdded());
          } else {
            edges.remove(edgeSetChange.getElementRemoved());
          }
        });
        edges.addAll(vertexChange.getValueAdded().getEdges());
      } else {
        int removedID = vertexChange.getValueRemoved().getID();
        for (Vertex vertex : getVertices()){
          vertex.getEdges().removeIf(edge -> edge.getSource().getID().equals(removedID) ||edge.getTarget().getID().equals(removedID));
        }
        deletedIDs.add(removedID);
      }
    });


    edges.addListener((SetChangeListener<? super Edge>) change -> {
      if(change.wasAdded())
      requestPrint(change.getElementAdded() + " was added.");
      else requestPrint(change.getElementRemoved() + " was removed.");
    });

  }

  private String getSystemName() {
    return "System";
  }

  public void addEdge(int source, String label, int target) {
    Vertex sourceVertex;
    Vertex targetVertex;

    if (vertices.containsKey(target)) {
      targetVertex = vertices.get(target);
    }  else {
      targetVertex = addVertex(target);
    }

    if (vertices.containsKey(source)) {
      sourceVertex = vertices.get(source);
    }else {
      sourceVertex = addVertex(source);
    }
    System.out.println("sourceV = " + vertices.get(source));
    System.out.println("targetV = " +targetVertex);
    System.out.println("label = " +label);
    System.out.println("source = " +source);
    System.out.println("target = " +target);
    vertices.get(source).getEdges().add(new Edge(sourceVertex, label, targetVertex));
  }

  public int addNextIDVertex() {
//    if(addedByVis) this.addedByVis = false;
    int id = Objects.requireNonNullElse(deletedIDs.pollFirst(), getMaxID() + 1);
    addVertex(id);
    return id;
  }

  public void clear(){
    vertices.clear();
    requestPrint(TerminalMessage.CLEAR.getMessage());
  }
  public  Vertex addVertex(int ID) {
    if (!vertices.containsKey(ID)) {
      Vertex vertex = new Vertex(this,ID);
      vertices.put(ID,vertex);

      vertex.getEdges().addListener((SetChangeListener<Edge>) edgeSetChange -> {

        if(edgeSetChange.wasAdded()){
          edges.add(edgeSetChange.getElementAdded());
        } else {
          edges.remove(edgeSetChange.getElementRemoved());
        }
      });
      edges.addAll(vertex.getEdges());
    }
    return vertices.get(ID);
  }

  public Integer getMaxID() {
    Optional<Integer> max = vertices.keySet().stream()
            .max(Integer::compare);
    return max.orElse(0);
  }

  public TreeSet<Integer> getDeletedIDs() {
    return deletedIDs;
  }

  public void removeAllEdges() {
    for(Vertex vertex: vertices.values()){
      vertex.getEdges().clear();
    }
  }

  public void removeVertex(int vertexID) {
//    for (Vertex vertex : getVertices()){
//      vertex.getEdges().removeIf(edge -> edge.getSource().getID().equals(vertexID) ||edge.getTarget().getID().equals(vertexID));
//    }
    vertices.remove(vertexID);
//    deletedIDs.add(vertexID);

  }

  public void addVertexListener(MapChangeListener<Integer, Vertex> mapChangeListener) {
    vertices.addListener(mapChangeListener);
  }

  public Vertex getVertex(int id){
    return vertices.getOrDefault(id,null);
  }
  public Set<Vertex> getVertices() {
    return Set.copyOf(vertices.values());
  }

  public ObservableSet<Edge> getEdges() {
    return edges;
  }

  public BooleanProperty getBisimToggle(){
    return bisimToggle;
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




