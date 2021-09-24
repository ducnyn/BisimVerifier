package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.Vertex;

public class BlockNode implements TreeNode {

  private String action;
  private TreeNode child;

  public BlockNode(String action, TreeNode child){
    this.action = action;
    this.child = child;
  }
  @Override
  public Boolean evaluate(Vertex vertex) { //yes allmatch does return true for empty streams
    return vertex.getTargets(action).stream().allMatch(targetVertex -> child.evaluate(targetVertex));
  }

  @Override
  public String getString() {
    return "(" + "[" + action + "]" + child.getString() + ")";
  }

  public String getAction() {
    return action;
  }

  public TreeNode getChild() {
    return child;
  }
}
