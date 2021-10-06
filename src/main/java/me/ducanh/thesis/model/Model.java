package me.ducanh.thesis.model;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import me.ducanh.thesis.util.DotService;

import java.util.*;

public class Model {
  private final ObservableMap<Integer, ObservableSet<Edge>> obsGraph = FXCollections.observableMap(new TreeMap<>());
  private final ObservableMap<Integer,Vertex> obsVertices= FXCollections.observableMap(new TreeMap<>());
  private final ObservableSet<Edge> obsEdgeSet = FXCollections.observableSet(new TreeSet<>());

  private final ObservableSet<BlockNode> partition = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<Integer> selectedVertices = FXCollections.observableSet();
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final StringProperty dotString = new SimpleStringProperty("digraph {\n\n}");
  private final StringProperty alertString = new SimpleStringProperty("");
  private final BooleanProperty newAlert = new SimpleBooleanProperty(false);
  private final StringProperty outputString = new SimpleStringProperty();
//  private boolean addedByVis = false;


  {//initiator

    obsVertices.addListener((MapChangeListener<Integer, Vertex>) mapEntry -> {
      int vertexID = mapEntry.getKey();

      if (mapEntry.wasAdded()) {
        mapEntry.getValueAdded().getEdges().addListener((SetChangeListener<Edge>) edgeSetChange -> {

          dotString.set(DotService.write(obsVertices.keySet(),getFlatEdges()));
          if(edgeSetChange.wasAdded()){

            obsEdgeSet.add(edgeSetChange.getElementAdded());
          } else {
            obsEdgeSet.remove(edgeSetChange.getElementRemoved());
          }
        });

        obsEdgeSet.addAll(mapEntry.getValueAdded().getEdges());

      } else { //mapEntry removed
        obsEdgeSet.removeAll(mapEntry.getValueRemoved().getEdges());
        System.out.println(mapEntry.getValueRemoved());
        deletedIDs.add(vertexID);
        selectedVertices.remove(vertexID);
        //remove Edge if source is removed  TODO :what about targets?
        for (Edge edge : getFlatEdges()) {
          if (edge.getTarget().getID().equals(mapEntry.getKey())) {
            if (obsVertices.containsKey(edge.getSource().getID()))
              obsVertices.get(edge.getSource().getID()).getEdges().remove(edge);
          }
        }
      }
      dotString.set(DotService.write(obsVertices.keySet(), getFlatEdges()));

    });
    obsEdgeSet.addListener((SetChangeListener<? super Edge>) change -> System.out.println(obsEdgeSet + " in obsEdgeSet now"));
  }

  public void addEdge(int source, String label, int target) {
    Vertex sourceVertex;
    Vertex targetVertex;

    if (obsVertices.containsKey(target)) {
      targetVertex = obsVertices.get(target);
    }  else {
      targetVertex = new Vertex(target);
      addVertex(targetVertex);
    }

    if (obsVertices.containsKey(source)) {
      sourceVertex = obsVertices.get(source);
    }else {
      sourceVertex = new Vertex(source);
      addVertex(sourceVertex);
    }
    System.out.println("sourceV = " +obsVertices.get(source));
    System.out.println("targetV = " +targetVertex);
    System.out.println("label = " +label);
    System.out.println("source = " +source);
    System.out.println("target = " +target);
    obsVertices.get(source).getEdges().add(new Edge(sourceVertex, label, targetVertex));
  }

//public boolean removeEdge(int edgeID) {
//    edges.get(edgeID);jh
//}

  public int addVertex() {
//    if(addedByVis) this.addedByVis = false;
    int id = Objects.requireNonNullElse(deletedIDs.pollFirst(), getMaxID() + 1);
    addVertex(new Vertex(id));
    return id;
  }

//  public boolean isAddedByVis(){
//    return this.addedByVis;
//  }
//  public void setAddedByVis(boolean addedByVis){
//      this.addedByVis = addedByVis;
//
//  }

  public boolean addVertex(Vertex vertex) {
    if (obsVertices.containsKey(vertex.getID()))
      return false;
    else
      obsVertices.put(vertex.getID(), vertex);
    return true;
  }

  public Integer getMaxID() {
    Optional<Integer> max = obsVertices.keySet().stream()
            .max(Integer::compare);
    return max.orElse(0);
  }

  public Set<Edge> getEdges(Integer vertexID) {
    return obsVertices.get(vertexID).getEdges();
  }

  public Set<Vertex> getTargets(Integer vertexID, String label) {
    return obsVertices.get(vertexID).getTargets(label);
  }

  public TreeSet<Integer> getDeletedIDs() {
    return deletedIDs;
  }

  public void removeAllEdges() {
    for(Vertex vertex: obsVertices.values()){
      vertex.getEdges().clear();
    }
  }

  public void removeVertex(int vertexID) {
    obsVertices.remove(vertexID);


  }

  public void addGraphListener(MapChangeListener<Integer,Vertex> mapChangeListener) {
    obsVertices.addListener(mapChangeListener);
  }

  public void addDotListener(ChangeListener<String> stringListener) {
    dotString.addListener(stringListener);
  }

  public ObservableSet<Integer> getSelectedVertices() {
    return selectedVertices;
  }

  public Vertex getVertex(int id){
    return obsVertices.getOrDefault(id,null);
  }
  public Set<Vertex> getVertices() {
    return Set.copyOf(obsVertices.values());
  }

  public ObservableSet<Edge> getFlatEdges() {
    return FXCollections.unmodifiableObservableSet(obsEdgeSet);
  }

  public StringProperty getAlertString() {
    return alertString;
  }


  public BooleanProperty getNewAlert() {
    return newAlert;
  }

  public void alert() {
    newAlert.set(true);
    newAlert.set(false);
  }

  public ObservableSet<BlockNode> getPartition() {
    return partition;
  }

  public void updatePartition(Set<BlockNode> newPartition) {

    this.partition.clear();
    this.partition.addAll(newPartition);


  }

  public String getDot() {
    return dotString.getValue();
  }

  public void setOutputString(String s){
    this.outputString.setValue(s);
  }
  public void appendOutputString(String s){this.outputString.setValue(this.outputString.getValue() +"\n"+ s);}

  public void bindOutputString(StringProperty sp){
    sp.bind(this.outputString);
  }


}




