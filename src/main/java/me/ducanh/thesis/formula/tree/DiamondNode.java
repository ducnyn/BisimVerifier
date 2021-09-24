package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.Vertex;

public class DiamondNode implements TreeNode {
  String action;
  TreeNode child;

    public DiamondNode(String action, TreeNode child) {
      this.action = action;
      this.child = child;
    }

    @Override
  public Boolean evaluate(Vertex vertex) {
    return vertex.getTargets(action).stream().anyMatch(targetVertex->child.evaluate(targetVertex));
  }

  @Override
  public String getString() {
    return "(" + "<" + action + ">" + child.getString()+")";
  }

  public String getAction() {
    return action;
  }

  public TreeNode getChild() {
    return child;
  }
}
