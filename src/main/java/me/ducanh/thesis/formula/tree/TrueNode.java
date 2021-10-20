package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.CustomVertex;

public class TrueNode implements TreeNode {
  @Override
  public Boolean evaluate(CustomVertex vertex) {
    return true;
  }

  @Override
  public String toString() {
    return "tt";
  }
}
