package me.ducanh.thesis.formula;

import me.ducanh.thesis.Graph;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.Edge;

public class FalseNode implements TreeNode {
  @Override
  public Boolean evaluate(Vertex vertex, Graph<Vertex, Edge> graph) {
    return false;
  }

  @Override
  public String toString() {
    return "ff";
  }
}
