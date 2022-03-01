package me.ducanh.thesis.formula;

import me.ducanh.thesis.Vertex;

public class TrueNode implements TreeNode {
  @Override
  public Boolean evaluate(Vertex vertex) {
    return true;
  }

  @Override
  public String toString() {
    return "tt";
  }
}
