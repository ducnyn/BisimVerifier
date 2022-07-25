package me.ducanh.thesis.formula;

import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Graph;
import me.ducanh.thesis.Vertex;

public class TrueNode implements TreeNode {
  @Override
  public Boolean evaluate(Vertex vertex, Graph<Vertex, Edge> graph) {
    return true;
  }

  @Override
  public String toString() {
    return "tt";
  }
}
