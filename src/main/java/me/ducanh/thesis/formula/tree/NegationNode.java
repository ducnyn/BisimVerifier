package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.Vertex;

public class NegationNode implements TreeNode
{
  TreeNode child;

  @Override
  public Boolean evaluate(Vertex vertex) {
    return !child.evaluate(vertex);
  }

  @Override
  public String toString() {
    return "-"+child.toString();
  }
}
