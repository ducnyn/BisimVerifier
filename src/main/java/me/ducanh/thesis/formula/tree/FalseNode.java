package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.CustomVertex;

public class FalseNode implements TreeNode {
  @Override
  public Boolean evaluate(CustomVertex vertex) {
    return false;
  }

  @Override
  public String toString() {
    return "ff";
  }
}
