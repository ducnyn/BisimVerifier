package me.ducanh.thesis.model;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodeList;
    private List<Edge> edgeList;

    public Graph(List<Node> nodeList){
        {
            this.nodeList = nodeList;
        }
    }
public Graph(List<Node> nodeList, List<Edge> edgeList){
    {
        this.nodeList = nodeList;
        this.edgeList = edgeList;
    }
}

@Override
public String toString(){
    return nodeList.toString()+edgeList.toString();

}
public void addNode(Node node){
        nodeList.add(node);
}

/**
 *
 * @param noteID
 * @return
 */
private Node getNode(int noteID){
        return nodeList.get(noteID);
    }

public List<Node> getNodes() {
    return nodeList;
}

public List<Edge> getEdges() {
    return edgeList;
}
}
