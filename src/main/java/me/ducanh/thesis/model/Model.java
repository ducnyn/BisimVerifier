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
  private final ObservableMap<Integer, CustomVertex> vertices = FXCollections.observableMap(new TreeMap<>());
  private final ObservableSet<CustomEdge> edges = FXCollections.observableSet(new TreeSet<>());

  private final ObservableSet<Block> partition = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<Integer> selectedVertices = FXCollections.observableSet();
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final StringProperty dotString = new SimpleStringProperty("digraph {\n\n}");
  private final StringProperty alertString = new SimpleStringProperty("");
  private final BooleanProperty updated = new SimpleBooleanProperty(false);
  private final StringProperty outputString = new SimpleStringProperty();
  private final BooleanProperty bisimToggle = new SimpleBooleanProperty(false);
//  private boolean addedByVis = false;


  {//initiator
    vertices.addListener((MapChangeListener<Integer, CustomVertex>) mapEntry -> {
      int vertexID = mapEntry.getKey();

      if (mapEntry.wasAdded()) { //mapentry added
        mapEntry.getValueAdded().getEdges().addListener((SetChangeListener<CustomEdge>) edgeSetChange -> {

          dotString.set(DotService.write(vertices.keySet(), getEdges()));
          if(edgeSetChange.wasAdded()){
            edges.add(edgeSetChange.getElementAdded());
          } else {
            edges.remove(edgeSetChange.getElementRemoved());
          }
        });

        edges.addAll(mapEntry.getValueAdded().getEdges());

      } else { //mapEntry removed
        edges.removeAll(mapEntry.getValueRemoved().getEdges());
        System.out.println(mapEntry.getValueRemoved());
        deletedIDs.add(vertexID);
        selectedVertices.remove(vertexID);
        //remove CustomEdge if source is removed  TODO :what about targets?
        for (CustomEdge edge : getEdges()) {
          if (edge.getTarget().getID().equals(mapEntry.getKey())) {
            if (vertices.containsKey(edge.getSource().getID()))
              vertices.get(edge.getSource().getID()).getEdges().remove(edge);
          }
        }

      }
      Set<Block> partition = Algorithms.bisim(getVertices()).getValue();
      setOutputString("\n Equivalence classes (Bisimulation): \n" + partition.toString());




      dotString.set(DotService.write(vertices.keySet(), getEdges()));
      broadcastUpdate();

    });


    edges.addListener((SetChangeListener<? super CustomEdge>) change -> {
      System.out.println(edges + " in obsEdgeSet now");
      dotString.set(DotService.write(vertices.keySet(), getEdges()));
      Set<Block> partition = Algorithms.bisim(getVertices()).getValue();
      setOutputString("\n Equivalence classes (Bisimulation): \n" + partition.toString());


      broadcastUpdate();
    });





  }

  public void addEdge(int source, String label, int target) {
    CustomVertex sourceVertex;
    CustomVertex targetVertex;

    if (vertices.containsKey(target)) {
      targetVertex = vertices.get(target);
    }  else {
      targetVertex = new CustomVertex(target);
      addVertex(targetVertex);
    }

    if (vertices.containsKey(source)) {
      sourceVertex = vertices.get(source);
    }else {
      sourceVertex = new CustomVertex(source);
      addVertex(sourceVertex);
    }
    System.out.println("sourceV = " + vertices.get(source));
    System.out.println("targetV = " +targetVertex);
    System.out.println("label = " +label);
    System.out.println("source = " +source);
    System.out.println("target = " +target);
    vertices.get(source).getEdges().add(new CustomEdge(sourceVertex, label, targetVertex));
  }

  public int addNextIDVertex() {
//    if(addedByVis) this.addedByVis = false;
    int id = Objects.requireNonNullElse(deletedIDs.pollFirst(), getMaxID() + 1);
    addVertex(new CustomVertex(id));
    return id;
  }

  public boolean addVertex(CustomVertex vertex) {
    if (vertices.containsKey(vertex.getID()))
      return false;
    else
      vertices.put(vertex.getID(), vertex);
    return true;
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
    for(CustomVertex vertex: vertices.values()){
      vertex.getEdges().clear();
    }
  }

  public void removeVertex(int vertexID) {
    vertices.remove(vertexID);


  }

  public void addGraphListener(MapChangeListener<Integer, CustomVertex> mapChangeListener) {
    vertices.addListener(mapChangeListener);
  }

  public void addDotListener(ChangeListener<String> stringListener) {
    dotString.addListener(stringListener);
  }

  public ObservableSet<Integer> getSelectedVertices() {
    return selectedVertices;
  }

  public CustomVertex getVertex(int id){
    return vertices.getOrDefault(id,null);
  }
  public Set<CustomVertex> getVertices() {
    return Set.copyOf(vertices.values());
  }

  public ObservableSet<CustomEdge> getEdges() {
    return edges;
  }

  public StringProperty getAlertString() {
    return alertString;
  }


  public BooleanProperty updatedProperty() {
    return updated;
  }
  public BooleanProperty getBisimToggle(){
    return bisimToggle;
  }
  public void broadcastUpdate() {
    updated.set(true);
    updated.set(false);
  }

  public ObservableSet<Block> getPartition() {
    return partition;
  }

  public void updatePartition(Set<Block> newPartition) {

    this.partition.clear();
    this.partition.addAll(newPartition);


  }

  public String getDot() {
    return dotString.getValue();
  }

  public void setOutputString(String s){
    this.outputString.setValue(s);
  }
//  public void appendOutputString(String s){this.outputString.setValue(this.outputString.getValue() +"\n"+ s);}

  public void bindOutputString(StringProperty sp){
    sp.bind(this.outputString);
  }


}




