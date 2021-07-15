package me.ducanh.thesis;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import me.ducanh.thesis.model.Model;

public class VisVertex {

  @FXML
  StackPane rootPane = new StackPane();
  @FXML
  Circle vertexCircle = new Circle(40);
  @FXML
  Text vertexLabel = new Text();
  @FXML
  int id;
  Paint dragFill = Paint.valueOf("lightcoral");

  DoubleProperty centerX = new SimpleDoubleProperty();
  DoubleProperty centerY = new SimpleDoubleProperty();


  public void init(Model model, int id) {
    init(model, id, 0, 0);
  }

  public void init(Model model, int id, double initX, double initY) {
    rootPane.layoutXProperty().addListener((ChangeListener<? super Number>) (change, oldV, newV) -> {
      centerX.setValue((Double) newV + 40.0);
    });
    rootPane.layoutYProperty().addListener((ChangeListener<? super Number>) (change, oldV, newV) -> {
      centerY.setValue((Double) newV + 40.0);
    });


    rootPane.setLayoutX(initX);
    rootPane.setLayoutY(initY);
    rootPane.setPickOnBounds(false);
    this.id = id;
    this.vertexLabel.setText(String.valueOf(id));
    this.vertexCircle.setFill(Paint.valueOf("lightgray"));

    final double[] mouseOrigin = {0, 0};


    rootPane.setOnMousePressed(press -> {
      if (press.isAltDown()) {
        model.removeVertex(this.id);
      }

      rootPane.toFront();
      mouseOrigin[0] = press.getScreenX();
      mouseOrigin[1] = press.getScreenY();
    });


    rootPane.setOnMouseDragged((drag) -> {

      if (drag.getButton().equals(MouseButton.PRIMARY) &&!drag.isAltDown()) {
        drag.consume();
        double deltaX = rootPane.getTranslateX() + (drag.getScreenX() - mouseOrigin[0])/rootPane.getParent().getScaleX();
        double deltaY = rootPane.getTranslateY() + (drag.getScreenY() - mouseOrigin[1])/rootPane.getParent().getScaleY();
        rootPane.setTranslateX(deltaX);
        rootPane.setTranslateY(deltaY);
        rootPane.setLayoutX(rootPane.getLayoutX()+rootPane.getTranslateX());
        rootPane.setLayoutY(rootPane.getLayoutY()+rootPane.getTranslateY());
        rootPane.setTranslateX(0);
        rootPane.setTranslateY(0);
        mouseOrigin[0] = drag.getScreenX();
        mouseOrigin[1] = drag.getScreenY();

        System.out.print("\r" + "nodeID: " + id);
        System.out.print(" absMousePosX: " + drag.getSceneX());
        System.out.print(" absMousePosY:" + drag.getSceneY());
        System.out.print(" relNodePosX: " + rootPane.getLayoutX());
        System.out.print(" relNodePosY: " + rootPane.getLayoutY());






      }
    });


    rootPane.setOnMouseReleased(released -> {
      if (released.isDragDetect()) {
        if (model.getSelectedVertices().contains(id)) {
          model.getSelectedVertices().remove(id);
          vertexCircle.setStroke(Paint.valueOf("gray"));
        } else {
          model.getSelectedVertices().add(id);
          vertexCircle.setStroke(Paint.valueOf("black"));
        }
      }
    });

    rootPane.addEventHandler(MouseEvent.ANY, Event::consume);
  }

  public StackPane getRootPane() {
    return rootPane;
  }

  int getID() {
    return id;
  }

  public void setFill(Paint fill) {
    this.vertexCircle.setFill(fill);
  }

  public void setLayout(double relPosX, double relPosY) {
    rootPane.setLayoutX(relPosX);
    rootPane.setLayoutY(relPosY);
  }

}
