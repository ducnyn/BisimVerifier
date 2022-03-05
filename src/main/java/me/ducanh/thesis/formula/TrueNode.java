package me.ducanh.thesis.formula;

public class TrueNode implements TreeNode {
  @Override
  public Boolean evaluate(Integer vertex) {
    return true;
  }

  @Override
  public String toString() {
    return "tt";
  }
}
