package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.CustomVertex;

public class NotNode implements TreeNode
{
  TreeNode child;
  public NotNode(TreeNode child){
    this.child = child;
  }

  @Override
  public Boolean evaluate(CustomVertex vertex) {
    return !child.evaluate(vertex);
  }

  @Override
  public String toString() {
    return "!"+child.toString();
  }
}
