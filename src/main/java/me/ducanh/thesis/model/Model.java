package me.ducanh.thesis.model;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;

import java.util.*;

public class Model {
  private final ObservableMap<Integer, Vertex> vertices = FXCollections.observableMap(new TreeMap<>());
  private final ObservableSet<Edge> edges = FXCollections.observableSet(new TreeSet<>());

  private final ObservableSet<Block> partition = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<Integer> selectedVertices = FXCollections.observableSet();
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final StringProperty dotString = new SimpleStringProperty("digraph {\n\n}");
  private final StringProperty alertString = new SimpleStringProperty("");
  private final BooleanProperty updated = new SimpleBooleanProperty(false);
  private final StringProperty printRequest = new SimpleStringProperty();
  private final BooleanProperty bisimToggle = new SimpleBooleanProperty(false);
  private String username = "User";
//  private boolean addedByVis = false;


  {//initiator
    vertices.addListener((MapChangeListener<Integer, Vertex>) vertexChange -> {
      int vertexID = vertexChange.getKey();

      if (vertexChange.wasAdded()) { //vertex added
        vertexChange.getValueAdded().getEdges().addListener((SetChangeListener<Edge>) edgeSetChange -> {

          if(edgeSetChange.wasAdded()){
            edges.add(edgeSetChange.getElementAdded());
          } else {
            edges.remove(edgeSetChange.getElementRemoved());
          }
        });

        edges.addAll(vertexChange.getValueAdded().getEdges());

      }

      else { //vertex removed
        edges.removeAll(vertexChange.getValueRemoved().getEdges());
        System.out.println(vertexChange.getValueRemoved());
        deletedIDs.add(vertexID);
        selectedVertices.remove(vertexID);
        //remove Edge if source is removed  TODO :what about targets?
        for (Edge edge : getEdges()) {
          if (edge.getTarget().getID().equals(vertexChange.getKey())) {
            if (vertices.containsKey(edge.getSource().getID()))
              vertices.get(edge.getSource().getID()).getEdges().remove(edge);
          }
        }

      }
      Set<Block> partition = Algorithms.bisim(getVertices()).getValue();
      requestPrint("\n Equivalence classes (Bisimulation): \n" + partition.toString());

    });


    edges.addListener((SetChangeListener<? super Edge>) change -> {
      System.out.println(edges + " in obsEdgeSet now");
      Set<Block> partition = Algorithms.bisim(getVertices()).getValue();
      requestPrint("\n Equivalence classes (Bisimulation): \n" + partition.toString());
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

  public  Vertex addVertex(int ID) {
    if (!vertices.containsKey(ID)) vertices.put(ID, new Vertex(ID));
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
    vertices.remove(vertexID);
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
    this.printRequest.setValue(string);
  }

  public void setUsername(String name){
    this.username = name;
  }

  public void listenToPrintRequest(StringProperty stringproperty){
    stringproperty.bind(printRequest);
  }

  public String getUserName() {
    return username;
  }

}




