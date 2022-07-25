package me.ducanh.thesis.formula;

import me.ducanh.thesis.Graph;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.Edge;

public class DiamondNode implements TreeNode {
  String action;
  TreeNode child;

    public DiamondNode(String action, TreeNode child) {
      this.action = action;
      this.child = child;
    }

    @Override
  public Boolean evaluate(Vertex vertex, Graph<Vertex, Edge> graph) {
    return graph.getTargets(vertex,action).stream().anyMatch(targetVertex->child.evaluate(targetVertex, graph));
  }

  @Override
  public String toString() {
    return  "<" + action + ">" + child.toString();
  }

  public String getAction() {
    return action;
  }

  public TreeNode getChild() {
    return child;
  }
}
