package me.ducanh.thesis.formula;

import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.Edge;
import java.util.Map;
import java.util.Set;

public class DiamondNode implements TreeNode {
  String action;
  TreeNode child;

    public DiamondNode(String action, TreeNode child) {
      this.action = action;
      this.child = child;
    }

    @Override
  public Boolean evaluate(Vertex vertex, Model model) {
    return model.getTargets(vertex,action).stream().anyMatch(targetVertex->child.evaluate(targetVertex, model));
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
