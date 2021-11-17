/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ducanh.thesis;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.ducanh.thesis.model.CustomDigraph;
import me.ducanh.thesis.model.CustomEdge;
import me.ducanh.thesis.model.CustomVertex;

import java.util.HashMap;

/**
 *
 * @author brunomnsilva
 */
public class ExampleMain extends Application {
    CustomDigraph customDigraph = new CustomDigraph();
    HashMap<Integer,CustomVertex> vertexMap = new HashMap<>();

    public void insertEdge(int source, int target, String label){
        try{
            customDigraph.insertEdge(source,target,label);
        } catch (InvalidEdgeException e) {
            System.out.println("Edge already exists.");
        }
    }

    public void insertVertex(){
        //TODO make it turn on auto mode
    }
    @Override
    public void start(Stage ignored) {

        insertEdge(1,2,"a");
        insertEdge(2,3,"b");
        insertEdge(2,3,"d");
        insertEdge(2,3,"e");

        insertEdge(2,3,"f");

        insertEdge(2,3,"g");









        insertEdge(2,3,"c");
        insertEdge(4,5,"a");
        insertEdge(4,6,"a");
        insertEdge(5,7,"b");
        insertEdge(6,7,"c");


        /* Only Java 15 allows for multi-line strings */
        String customProps = "edge.label = true" + "\n" + "edge.arrow = true\n" +
                "layout.repulsive-force = 250000\n" +
                "layout.attraction-force = 20\n" +
                "layout.attraction-scale = 20\n" +
                "vertex.radius = 50";
                
        SmartGraphProperties properties = new SmartGraphProperties(customProps);
        
        SmartGraphPanel<Integer, String> graphView = new SmartGraphPanel<>(customDigraph, properties, new SmartCircularSortedPlacementStrategy());
        Button button = new Button("a");

        SmartGraphDemoContainer container = new SmartGraphDemoContainer(graphView);
        VBox vbox = new VBox();
        vbox.getChildren().add(container);
        vbox.getChildren().add(button);

        Scene scene = new Scene(vbox, 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph City Distances");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
        
        graphView.init();
        
        graphView.setAutomaticLayout(false);

        
        /* You can mannualy place vertices at any time. However, these are
        absolute coordinates inside the container panel. */

        int pos = 0;
        for(Vertex<Integer> v : customDigraph.vertices()){
            graphView.setVertexPosition(v,pos,pos);
            graphView.getStylableLabel(v).setStyle("-fx-stroke: red; -fx-fill: red;");
            pos += 100;
        }
        final int[] i = {100};
        scene.setOnMousePressed(e->{
            customDigraph.insertVertex(i[0]++);
            graphView.update();
        });


    }
    
    public static void main(String[] args) {
        launch(args);
    }
}