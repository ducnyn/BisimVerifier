package me.ducanh.thesis;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import me.ducanh.thesis.model.CustomEdge;

public class VisEdge {

  @FXML
  private AnchorPane anchorPane = new AnchorPane();
  @FXML
  public Line line = new Line();
  @FXML
  public Text labelNode;
  private CustomEdge edge;
  private VisVertex source;
  private VisVertex target;

  public void init(CustomEdge edge, VisVertex source, VisVertex target){
    this.edge = edge;
    this.source = source;
    this.target = target;
    this.labelNode = new Text(edge.getLabel());
    bindLabelPosition();
    bindNodePositions();
    anchorPane.setPickOnBounds(false);

  }
  private void bindLabelPosition() {
    labelNode.xProperty().bind(this.startXProperty().add(this.endXProperty()).divide(2).subtract(this.labelNode.getLayoutBounds().getWidth() / 2.0D));
    labelNode.yProperty().bind(this.startYProperty().add(this.endYProperty()).divide(2).add(this.labelNode.getLayoutBounds().getHeight() / 1.5D));
  }

  private void bindNodePositions() {
    source.bindToCenter(startXProperty(),startYProperty());
    target.bindToCenter(endXProperty(),endYProperty());
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
