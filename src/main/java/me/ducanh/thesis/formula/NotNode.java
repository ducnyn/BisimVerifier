package me.ducanh.thesis.formula;

public class NotNode implements TreeNode
{
  TreeNode child;
  public NotNode(TreeNode child){
    this.child = child;
  }

  @Override
  public Boolean evaluate(Integer vertex) {
    return !child.evaluate(vertex);
  }

  @Override
  public String toString() {
    return "!"+child.toString();
  }
}
