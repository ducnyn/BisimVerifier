package me.ducanh.thesis.formula;

import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Graph;
import me.ducanh.thesis.Vertex;

public interface TreeNode {

   Boolean evaluate(Vertex vertex, Graph<Vertex, Edge> graph);

   String toString();


}
