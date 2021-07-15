package me.ducanh.thesis;

import javafx.application.Platform;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import me.ducanh.thesis.model.Edge;
import me.ducanh.thesis.model.Model;
import me.ducanh.thesis.util.Colors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

public class VisEditor {
  private final HashMap<Integer, VisVertex> drawnVertices = new HashMap<>();
  private final HashMap<Edge,VisEdge> drawnEdges = new HashMap<>();
  @FXML
  ScrollPane scrollPane ;
  @FXML
  AnchorPane anchorPane;
  @FXML
  VBox centerBox;
  @FXML
  Group group;

  private final int radius = 40;
  private int colorID = 0;
  double spawnPosX = 1;
  double spawnPosY = 1;
  Stack<Pair<Double,Double>> spawnPositions;
//  {spawnPositions.add(new Pair<Double,Double>(1.0,1.0));}

//TODO scroll pane when item dragged past boundaries
//TODO When you add/remove an edge, the edgeVis doesn't have to be drawn again, just add / remove a label
  public void initialize(Model model) {
    anchorPane.setMinHeight(600);
    anchorPane.setMinWidth(900);

//    anchorPane.minWidthProperty().bind(scrollPane.widthProperty());
//    anchorPane.minHeightProperty().bind(scrollPane.heightProperty());
    anchorPane.setOnMousePressed(pressed -> {
      if (pressed.getButton().equals(MouseButton.PRIMARY) && (pressed.isControlDown())) {
         spawnPosX = pressed.getX() - radius;
         spawnPosY = pressed.getY() - radius - 20;
         model.addVertex();
        pressed.consume();
      }
    });

//    anchorPane.setOnScroll(scroll -> {
//      if (scroll.getDeltaY() > 9) {
//        group.setScaleX(group.getScaleX() * scroll.getDeltaY());
//        group.setScaleY(group.getScaleY() * scroll.getDeltaY());
//      } else {
//        group.setScaleX(group.getScaleX() / scroll.getDeltaY());
//        group.setScaleY(group.getScaleY() / scroll.getDeltaY());
//      }
//    });

    model.getUnmodifiableEdgeSetObs().addListener((SetChangeListener<? super Edge>) change->{

      if(change.wasAdded()){
        Edge edge = change.getElementAdded();
        FXMLLoader edgeLoader = new FXMLLoader(getClass().getResource(FXMLPATH.VISEDGE.getFileName()));
        try {
          anchorPane.getChildren().add(edgeLoader.load());
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        VisEdge visEdge = edgeLoader.getController();
        drawnEdges.put(edge,visEdge);
        Line edgeLine = visEdge.line;
        edgeLine.startXProperty().bind(drawnVertices.get(change.getElementAdded().getSource()).centerX);
        edgeLine.startYProperty().bind(drawnVertices.get(change.getElementAdded().getSource()).centerY);
        edgeLine.endXProperty().bind(drawnVertices.get(change.getElementAdded().getTarget()).centerX);
        edgeLine.endYProperty().bind(drawnVertices.get(change.getElementAdded().getTarget()).centerY);
        visEdge.pane.toBack();


//      } else {//TODO Edge object
//        drawnEdges.remove()
      } else {
//        drawnEdges.stream()
//                .filter(visEdge->
//                  visEdge.sourceID == change.getElementRemoved().getSource()
//                  && visEdge.targetID == change.getElementRemoved().getTarget()
//                  && visEdge.label.equals(change.getElementRemoved().getLabel()))
//                .forEach(visEdge -> {
//                  anchorPane.getChildren().remove(visEdge);
//                  drawnEdges.remove(visEdge);
//                });
        anchorPane.getChildren().remove(drawnEdges.get(change.getElementRemoved()).pane);
        drawnEdges.remove(change.getElementRemoved());
      }
    });


    model.addGraphListener(vertex -> {

      int id = vertex.getKey();
      if (vertex.wasAdded()) {
        if (!drawnVertices.containsKey(id)){
          Platform.runLater(()->{
            drawnVertices.put(id,spawnVertex(model,id, spawnPosX, spawnPosY));
            this.spawnPosX = 1;
            this.spawnPosY = 1;
//            while(anchorPane.get)
          });

        }
      } else {
        anchorPane.getChildren().remove(drawnVertices.get(id).getRootPane());
        drawnVertices.remove(id);
      }
    });

    model.getPartition().addListener((SetChangeListener<Set<Integer>>) change -> {
      if (change.wasAdded()) {
        if (change.getElementAdded().size() > 1) {
          Paint color = Colors.array[colorID++ % 120];

          for (Integer vertex : change.getElementAdded()) {
            drawnVertices.get(vertex).setFill(color);
          }
        } else {
          drawnVertices.get(change.getElementAdded().iterator().next()).setFill(Color.LIGHTGRAY);
        }
      } else {
        if (change.getElementRemoved().size() > 1) {
          colorID--;
        }
      }
    });

//    scrollPane.setOnScroll((ScrollEvent event) -> {
//      // Adjust the zoom factor as per your requirement
//      double deltaY = event.getDeltaY();
//      double zoom = (deltaY > 0) ? 1.05 : 0.95;
//
//      anchorPane.setScaleX(anchorPane.getScaleX()*zoom);
//      anchorPane.setScaleY(anchorPane.getScaleY()*zoom);
//
//      System.out.println(anchorPane.getTransforms());
//    });


    centerBox.setOnScroll(scroll->{


        double zoom = (scroll.getDeltaY()>0)? 1.05 : 0.95;

        //calculate pixel offsets from [0,1] range
        double offsetX = scrollPane.getHvalue() * (group.getLayoutBounds().getWidth() - scrollPane.getViewportBounds().getWidth());
        double offsetY = scrollPane.getVvalue() * (group.getLayoutBounds().getHeight() - scrollPane.getViewportBounds().getHeight());

        anchorPane.setScaleX(anchorPane.getScaleX()*zoom); // might cause problems because you didnt use scakeValue = scaleValue(0.7)*zoomFactor
        anchorPane.setScaleY(anchorPane.getScaleY()*zoom);
        scrollPane.layout(); //refresh ScrollPane scroll positions @ target bounds

        //convert absMousePos to relMousePos(anchorPane)
        Point2D anchorMousePos = anchorPane.sceneToLocal(new Point2D(scroll.getSceneX(),scroll.getSceneY()));
        //zoom towards mouse position
        Point2D delta = anchorPane.getLocalToParentTransform().deltaTransform(anchorMousePos.multiply(zoom-1));

        scrollPane.setHvalue((offsetX + delta.getX()) / (group.getBoundsInLocal().getWidth() - scrollPane.getViewportBounds().getWidth()));
        scrollPane.setVvalue((offsetY + delta.getY()) / (group.getBoundsInLocal().getHeight() - scrollPane.getViewportBounds().getHeight()));
        //inspired by https://stackoverflow.com/questions/39827911/javafx-8-scaling-zooming-scrollpane-relative-to-mouse-position
      
    });
  }


  private VisVertex spawnVertex(Model model, int id, double mouseX, double mouseY) {
    Platform.runLater(()->{

    });
    FXMLLoader vertexLoader = new FXMLLoader(getClass().getResource(FXMLPATH.VERTEX.getFileName()));
    try {
      StackPane rootPane = vertexLoader.load();
      anchorPane.getChildren().add(rootPane);
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    VisVertex vertex = vertexLoader.getController();
    vertex.init(model, id, mouseX, mouseY);

    return vertex;

  }

}
