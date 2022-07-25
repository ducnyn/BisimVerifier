package me.ducanh.thesis.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import me.ducanh.thesis.*;

import java.util.HashSet;


public class Canvas{
    private final HashSet<VertexView> drawnVertices = new HashSet<>();
    private final ObservableSet<EdgeView> drawnEdgeViews = FXCollections.observableSet();
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane anchorPane;
    @FXML
    StackPane centerBox;
    @FXML
    Group group;
    @FXML
    Label bisimToggleLabel;

    private Controller controller;

//TODO scroll pane when item dragged past boundaries

    public void inject(CanvasVM canvasVM, Controller controller) {
        this.controller = controller;
        anchorPane.setMinHeight(600);
        anchorPane.setMinWidth(900);

        canvasVM.addVertexViewListener(vertexView->{
            if(vertexView.wasAdded()){
//
                Platform.runLater(() -> addChild(vertexView.getValueAdded()));
                drawnVertices.add(vertexView.getValueAdded());
            } else {
                Platform.runLater(() -> removeChild(vertexView.getValueRemoved()));
                drawnVertices.remove(vertexView.getValueRemoved());
            }
        });

        canvasVM.addEdgeViewListener(edgeView->{

            if(edgeView.wasAdded()){
                EdgeView edgeViewAdded = edgeView.getValueAdded();
                //TODO why are random edges not following this rule
                //TODO update curve when edges are removed
                if(!drawnEdgeViews.contains(edgeViewAdded)){
                    long parallelEdgeCount =
                            drawnEdgeViews.stream()
                                            .filter(eView-> eView.getTarget().equals(edgeViewAdded.getTarget())
                                            &&  eView.getSource().equals(edgeViewAdded.getSource())
                                            ).count();
//TODO                            JUnit System.out.println("there are already " + parallelEdgeCount + "parallel Edges");
//                            for(int i =0;i<parallelEdgeCount;i++){
                    edgeViewAdded.changeDegree(10 + parallelEdgeCount*10);
                    drawnEdgeViews.add(edgeViewAdded);
                    Platform.runLater(() -> {
                        addChild(edgeViewAdded);
                        edgeViewAdded.toBack();
                    });
                }

//                            HashMap<Integer,HashMap<Integer,Edge>> adjMatrix = new HashMap<>(new HashMap<>());
//                            if(adjMatrix.containsKey(1) && adjMatrix.get(1).containsKey(2)){
//                                adjMatrix.get(1).get(2).
//                            }


            } else {
                EdgeView edgeViewRemoved = edgeView.getValueRemoved();
                drawnEdgeViews.remove(edgeViewRemoved);
                Platform.runLater(()->removeChild(edgeView.getValueRemoved()));
            }
        });



        anchorPane.setOnMousePressed(pressed -> {
            checkCtrlClick(pressed);
        });

        canvasVM.addColorModeListener((observable, changeToFalse, changeToTrue) -> {
            colorToggleIndicator(changeToTrue);
            if(changeToFalse){
                Platform.runLater(() -> {
//                    Thread taskThread = new Thread(() -> {
                    for (VertexView vertex : drawnVertices) {
                        vertex.setFill(vertex.getDefaultFill());
                    }
//                    });
//                    taskThread.setDaemon(true);
//                    taskThread.start();
                });
            }

        });



        centerBox.setOnScrollStarted(scrollStart -> {

        });
        centerBox.setOnScroll(scroll -> {

            double zoom;

            if (scroll.getDeltaY() == 0) zoom = 1;
            else if (scroll.getDeltaY() > 0) zoom = 1.05;
            else zoom = 0.95;
            System.out.println("delta " + scroll.getDeltaY());


            //calculate pixel offsets from [0,1] range
            double offsetX = scrollPane.getHvalue() * (group.getLayoutBounds().getWidth() - scrollPane.getViewportBounds().getWidth());
            double offsetY = scrollPane.getVvalue() * (group.getLayoutBounds().getHeight() - scrollPane.getViewportBounds().getHeight());

            anchorPane.setScaleX(anchorPane.getScaleX() * zoom); // might cause problems because you didnt use scaleValue = scaleValue(0.7)*zoomFactor
            anchorPane.setScaleY(anchorPane.getScaleY() * zoom);
            scrollPane.layout(); //refresh ScrollPane scroll positions @ target bounds

            //convert absMousePos to relMousePos(anchorPane)
            Point2D anchorMousePos = anchorPane.sceneToLocal(new Point2D(scroll.getSceneX(), scroll.getSceneY()));
            //zoom towards mouse position
            Point2D delta = anchorPane.getLocalToParentTransform().deltaTransform(anchorMousePos.multiply(zoom - 1));

            scrollPane.setHvalue((offsetX + delta.getX()) / (group.getBoundsInLocal().getWidth() - scrollPane.getViewportBounds().getWidth()));
            scrollPane.setVvalue((offsetY + delta.getY()) / (group.getBoundsInLocal().getHeight() - scrollPane.getViewportBounds().getHeight()));
            //inspired by this over-engineered version with no practical difference : https://stackoverflow.com/questions/39827911/javafx-8-scaling-zooming-scrollpane-relative-to-mouse-position
        });
    }
//    private void resetSpawnPos(){
//        spawnPosX= Integer.DEFAULT_RADIUS;
//        spawnPosY= Integer.DEFAULT_RADIUS;
//    }
    private void checkCtrlClick(MouseEvent pressed) {
        if (pressed.getButton().equals(MouseButton.PRIMARY) && (pressed.isControlDown())) {
            controller.addVertex(pressed.getX()-VertexView.DEFAULT_RADIUS,pressed.getY()-VertexView.DEFAULT_RADIUS);
            pressed.consume();
        }
    }
    private void colorToggleIndicator(Boolean toggle) {

        if (toggle) {
            bisimToggleLabel.setText("Color bisimilar vertices: On");
        } else {
            bisimToggleLabel.setText("Color bisimilar vertices: Off");
        }
    }
    private void addChild(Node node){
        anchorPane.getChildren().add(node);
    }
    private void removeChild(Node node){
        anchorPane.getChildren().remove(node);
    }
}
