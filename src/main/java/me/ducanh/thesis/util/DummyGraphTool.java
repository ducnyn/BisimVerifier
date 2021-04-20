package me.ducanh.thesis.util;

import me.ducanh.thesis.model.DataModel;
import me.ducanh.thesis.model.Edge;
import me.ducanh.thesis.model.Node;

public class DummyGraphTool {
    //Creates a dummy graph
    public static void setExampleGraph(DataModel model) {
        for (int i = 0; i < 5; i++) {
            Node node = new Node(Integer.toString(i));
            if (i % 2 == 1) {
                Edge edge = new Edge("a", model.getNodeList().get(i - 1), node);
                model.addEdge(edge);
            } else if (i != 0) {
                Edge edge = new Edge("b", model.getNodeList().get(i - 1), node);
                model.addEdge(edge);
            }
            model.addNode(node);
        }
    }

}
