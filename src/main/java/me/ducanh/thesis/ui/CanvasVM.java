package me.ducanh.thesis.ui;

import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;

import java.util.HashMap;

public class CanvasVM {
    Model model;
    private final ObservableMap<Edge, EdgeView> edgeViews = FXCollections.observableMap(new HashMap<>());
    private final ObservableMap<Vertex, VertexView>vertexViews = FXCollections.observableMap(new HashMap<>());
    public CanvasVM(Model model){
        this.model = model;

        model.addVertexListener(vertexChange->{
            if(vertexChange.wasAdded()) addVertexView(vertexChange.getKey());
            else removeVertexView(vertexChange.getKey());
        });
        model.addEdgeListener(edgeChange->{
            if(edgeChange.wasAdded()) addEdgeView(edgeChange.getElementAdded());
            else removeEdgeView(edgeChange.getElementRemoved());
        });
    }


    private void addVertexView(Vertex vertex){
        vertexViews.put(vertex, new VertexView(this,vertex));
    }
    private void removeVertexView(Vertex vertex){
        vertexViews.remove(vertex);
    }
    private void addEdgeView(Edge edge){
        edgeViews.put(edge,new EdgeView(edge));
    }
    private void removeEdgeView(Edge edge) {
        edgeViews.remove(edge);
    }

    public void addVertexViewListener(MapChangeListener<Vertex, VertexView> listener){
        vertexViews.addListener(listener);
    }
    public void addEdgeViewListener(MapChangeListener<Edge,EdgeView> listener){
        edgeViews.addListener(listener);
    }
    public void addColorToggleListener(ChangeListener<? super Boolean> listener){
        model.getColorToggleProperty().addListener(listener);
    }
}
