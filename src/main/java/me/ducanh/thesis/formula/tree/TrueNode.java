package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.Vertex;

public class TrueNode implements TreeNode {
  @Override
  public Boolean evaluate(Vertex vertex) {
    return true;
  }

  @Override
  public String getString() {
    return "tt";
  }
}
