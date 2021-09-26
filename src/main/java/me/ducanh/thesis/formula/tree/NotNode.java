package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.Vertex;

public class NotNode implements TreeNode
{
  TreeNode child;
  public NotNode(TreeNode child){
    this.child = child;
  }

  @Override
  public Boolean evaluate(Vertex vertex) {
    return !child.evaluate(vertex);
  }

  @Override
  public String toString() {
    return "-"+child.toString();
  }
}
