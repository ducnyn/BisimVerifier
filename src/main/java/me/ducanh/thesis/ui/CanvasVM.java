package me.ducanh.thesis.ui;

import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import javafx.scene.paint.Paint;
import me.ducanh.thesis.*;
import me.ducanh.thesis.util.Colors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CanvasVM {
    private final Model model;
    private final Controller controller;
    private final ObservableMap<Edge, EdgeView> edgeViews = FXCollections.observableMap(new HashMap<>());
    private final ObservableMap<Vertex, VertexView>vertexViews = FXCollections.observableMap(new HashMap<>());
    private final ObservableSet<Block> partition = FXCollections.observableSet(new HashSet<>());
    int colorID = 0;

    //    private final BooleanProperty colorUpdate = new SimpleBooleanProperty();
    public CanvasVM(Model model, Controller controller){
        this.model = model;
        this.controller = controller;

        model.getEdgesByVertex().addListener((MapChangeListener<Vertex,ObservableSet<Edge>>)edgeList->{
            if(edgeList.wasAdded()) {
                addVertexView(edgeList.getKey());
                edgeList.getValueAdded().addListener((SetChangeListener<Edge>)edge->{
                    if(edge.wasAdded()) addEdgeView(edge.getElementAdded());
                    else removeEdgeView(edge.getElementRemoved());
                    updateColorIfColorMode();
                });
            }
            else removeVertexView(edgeList.getKey());
            updateColorIfColorMode();

        });

        partition.addListener((SetChangeListener<Block>) block->{

            if (block.wasAdded()) {
                if (block.getElementAdded().size() > 1) {
                    Paint color = Colors.array[colorID++ % 120];
                    System.out.println("block added (colorListener" + block.getElementAdded());

                    for (Vertex vertex : block.getElementAdded()) {
                        vertexViews.get(vertex).setFill(color);
                    }
                } else if (block.getElementAdded().size() == 1) {
                    vertexViews.get(block.getElementAdded().iterator().next()).resetFill();
                }
            } else {
                if (block.getElementRemoved().size() > 1) {
                    System.out.println("block removed (colorListener" + block.getElementRemoved());

                    colorID--;
                }
            } //TODO doesn't remove color when clearing the partition list
        });
        addColorModeListener((obs,falseV,trueV)->{
            updateColorIfColorMode();

        });
    }




    private void addVertexView(Vertex vertex){
        vertexViews.put(vertex, new VertexView(controller,vertex));
    }
    private void removeVertexView(Vertex vertex){
        vertexViews.remove(vertex);
    }
    private void addEdgeView(Edge edge){
        edgeViews.put(edge,new EdgeView(edge.getLabel(),vertexViews.get(edge.getSource()),vertexViews.get(edge.getTarget())));
    }
    private void removeEdgeView(Edge edge) {
        edgeViews.remove(edge);
    }
    public void updateColorIfColorMode(){
        if(model.getColorMode()){
            this.partition.clear();
            this.partition.addAll(model.getBisimulation());
            model.requestPrint("\n Equivalence classes (Bisimulation): \n" + partition);
            //TODO not sure if this belongs here, maybe canvasVM should also be injected to Editor or EditorVM should have the same listeners

        }
    }

    public void addVertexViewListener(MapChangeListener<Vertex, VertexView> listener){
        vertexViews.addListener(listener);
    }
    public void addEdgeViewListener(MapChangeListener<Edge,EdgeView> listener){
        edgeViews.addListener(listener);
    }

    public void addColorModeListener(ChangeListener<? super Boolean> listener){
        model.getColorModeProperty().addListener(listener);
    }
}
