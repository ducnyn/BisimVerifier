package me.ducanh.thesis;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import me.ducanh.thesis.model.Edge;

public class VisEdge {
  @FXML
  public Line line = new Line();
  @FXML
  public AnchorPane pane = new AnchorPane();
  int sourceID;
  int targetID;
  String label;

  public void init(){

  }
  public VisEdge(Edge edge){
    this.sourceID = edge.getSource();
    this.label = edge.getLabel();
    this.targetID = edge.getTarget();
  }
  public VisEdge(){
    super();
  }
  @Override
  public String toString(){
    StringBuilder stringbuilder = new StringBuilder();
    stringbuilder.append(sourceID);
    stringbuilder.append("-");
    stringbuilder.append(label);
    stringbuilder.append(">");
    stringbuilder.append(targetID);
    return stringbuilder.toString();
  }
}
