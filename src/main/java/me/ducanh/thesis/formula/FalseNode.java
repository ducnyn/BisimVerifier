package me.ducanh.thesis.formula;

public class FalseNode implements TreeNode {
  @Override
  public Boolean evaluate(Integer vertex) {
    return false;
  }

  @Override
  public String toString() {
    return "ff";
  }
}
