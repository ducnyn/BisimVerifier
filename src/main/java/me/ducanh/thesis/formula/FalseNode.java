package me.ducanh.thesis.formula;

import me.ducanh.thesis.Vertex;

public class FalseNode implements TreeNode {
  @Override
  public Boolean evaluate(Vertex vertex) {
    return false;
  }

  @Override
  public String toString() {
    return "ff";
  }
}
