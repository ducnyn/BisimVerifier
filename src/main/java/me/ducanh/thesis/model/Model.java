package me.ducanh.thesis.model;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import me.ducanh.thesis.util.DotService;

import java.util.*;
import java.util.stream.Collectors;

public class Model {
  private final ObservableMap<Integer, ObservableSet<Edge>> obsGraph = FXCollections.observableMap(new TreeMap<>());
  private final ObservableSet<Edge> obsEdgeSet = FXCollections.observableSet(new TreeSet<>());

  private final ObservableSet<Block> partition = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<Integer> selectedVertices = FXCollections.observableSet();
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final StringProperty dotString = new SimpleStringProperty("digraph {\n\n}");
  private final StringProperty alertString = new SimpleStringProperty("");
  private final BooleanProperty newAlert = new SimpleBooleanProperty(false);
  private final StringProperty outputString = new SimpleStringProperty();
  private boolean addedByVis = false;


  {//initiator

    obsGraph.addListener((MapChangeListener<Integer, ObservableSet<Edge>>) mapEntry -> {
      int vertex = mapEntry.getKey();

      if (mapEntry.wasAdded()) {
        mapEntry.getValueAdded().addListener((SetChangeListener<Edge>) edgeSetChange -> {
          dotString.set(DotService.write(obsGraph.keySet(), obsGraph.values()));
          if(edgeSetChange.wasAdded()){
            obsEdgeSet.add(edgeSetChange.getElementAdded());
          } else {
            obsEdgeSet.remove(edgeSetChange.getElementRemoved());
          }
        });

        obsEdgeSet.addAll(mapEntry.getValueAdded());

      } else { //mapEntry removed
        obsEdgeSet.removeAll(mapEntry.getValueRemoved());
        System.out.println(mapEntry.getValueRemoved());
        deletedIDs.add(vertex);
        selectedVertices.remove(vertex);
        for (Edge edge : getFlatEdges()) {
          if (edge.getTarget().equals(mapEntry.getKey())) {
            if (obsGraph.containsKey(edge.getSource())) obsGraph.get(edge.getSource()).remove(edge);
          }
        }

      }

      dotString.set(DotService.write(obsGraph.keySet(), obsGraph.values()));

    });


    obsEdgeSet.addListener((SetChangeListener<? super Edge>) change -> {
      System.out.println(obsEdgeSet + " in obsEdgeSet now");
    });





  }

  public ObservableSet<Edge> getUnmodifiableEdgeSetObs(){
    return FXCollections.unmodifiableObservableSet(obsEdgeSet);
  }

  public void addEdge(int source, String label, int target) {
    if (!obsGraph.containsKey(target)) {
      addVertex(target);
    }
    if (!obsGraph.containsKey(source)) {
      addVertex(source);
    }
    obsGraph.get(source).add(new Edge(source, label, target));

  }

//public boolean removeEdge(int edgeID) {
//    edges.get(edgeID);jh
//}

  public int addVertex() {
    if(addedByVis) this.addedByVis = true;
    int id = Objects.requireNonNullElse(deletedIDs.pollFirst(), getMaxID() + 1);
    addVertex(id);
    return id;
  }

//  public boolean isAddedByVis(){
//    return this.addedByVis;
//  }
//  public void setAddedByVis(boolean addedByVis){
//      this.addedByVis = addedByVis;
//
//  }

  public boolean addVertex(int id) {
    if (obsGraph.containsKey(id))
      return false;
    else
      obsGraph.put(id, FXCollections.observableSet(new TreeSet<>()));
    return true;
  }

  public Integer getMaxID() {
    Optional<Integer> max = obsGraph.keySet().stream()
            .max(Integer::compare);
    return max.orElse(0);
  }

  public Set<Edge> getEdges(Integer vertexID) {
    return obsGraph.get(vertexID);
  }

  public Set<Integer> getTargets(Integer vertexID, String label) {
    return obsGraph.get(vertexID).stream().filter(e -> label.equals(e.getLabel())).map(Edge::getTarget).collect(Collectors.toSet());
  }

  public TreeSet<Integer> getDeletedIDs() {
    return deletedIDs;
  }

  public void removeAllEdges() {
    for (Map.Entry<Integer, ObservableSet<Edge>> entry : obsGraph.entrySet()) {
      entry.getValue().clear();
    }
  }

  public void removeVertex(int vertexID) {
    obsGraph.remove(vertexID);


  }

  public void addGraphListener(MapChangeListener<Integer, ObservableSet<Edge>> mapChangeListener) {
    obsGraph.addListener(mapChangeListener);
  }

  public void addDotListener(ChangeListener<String> stringListener) {
    dotString.addListener(stringListener);
  }

  public ObservableSet<Integer> getSelectedVertices() {
    return selectedVertices;
  }

  public Set<Integer> getVertices() {
    return obsGraph.keySet();
  }

  public List<Edge> getFlatEdges() {
    return obsGraph.values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
  }

  public StringProperty getAlertString() {
    return alertString;
  }

  public final void setAlertString(String string) {
    this.alertString.set(string);
    alert();
  }

  public BooleanProperty getNewAlert() {
    return newAlert;
  }

  public void alert() {
    newAlert.set(true);
    newAlert.set(false);
  }

  public ObservableSet<Block> getPartition() {
    return partition;
  }

  public void updatePartition(Set<Block> newPartition) {

    this.partition.clear();
    this.partition.addAll(newPartition);


  }

  public boolean containsKey(int id) {
    return obsGraph.containsKey(id);
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




