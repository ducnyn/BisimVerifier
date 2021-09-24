package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.Vertex;

public class FalseNode implements TreeNode {
  @Override
  public Boolean evaluate(Vertex vertex) {
    return false;
  }

  @Override
  public String getString() {
    return "ff";
  }
}
