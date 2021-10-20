package me.ducanh.thesis;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import me.ducanh.thesis.model.CustomEdge;

public class VisEdge {
  @FXML
  public Line line = new Line();
  @FXML
  private AnchorPane anchorPane = new AnchorPane();
  private CustomEdge edge;
  public void init(CustomEdge edge){
    this.edge = edge;
    anchorPane.setPickOnBounds(false);

  }

  public Node getRoot(){
    return anchorPane;
  }
  public DoubleProperty startXProperty(){
    return line.startXProperty();
  }

  public DoubleProperty startYProperty(){
    return line.startYProperty();
  }

  public DoubleProperty endXProperty(){
    return line.endXProperty();
  }

  public DoubleProperty endYProperty(){
    return line.endYProperty();
  }
  @Override
  public String toString(){
    return edge.toString();
  }

  public void toBack() {
    anchorPane.toBack();
  }
}
