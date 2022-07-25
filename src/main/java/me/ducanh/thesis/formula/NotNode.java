package me.ducanh.thesis.formula;

import me.ducanh.thesis.Graph;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.Edge;

public class NotNode implements TreeNode
{
  TreeNode child;
  public NotNode(TreeNode child){
    this.child = child;
  }

  @Override
  public Boolean evaluate(Vertex vertex, Graph<Vertex, Edge> graph) {
    return !child.evaluate(vertex, graph);
  }

  @Override
  public String toString() {
    return "!"+child.toString();
  }
}
