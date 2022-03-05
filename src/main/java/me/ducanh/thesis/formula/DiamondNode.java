package me.ducanh.thesis.formula;

public class DiamondNode implements TreeNode {
  String action;
  TreeNode child;

    public DiamondNode(String action, TreeNode child) {
      this.action = action;
      this.child = child;
    }

    @Override
  public Boolean evaluate(Integer vertex) {
    return vertex.getTargets(action).stream().anyMatch(targetVertex->child.evaluate(targetVertex));
  }

  @Override
  public String toString() {
    return  "<" + action + ">" + child.toString();
  }

  public String getAction() {
    return action;
  }

  public TreeNode getChild() {
    return child;
  }
}
