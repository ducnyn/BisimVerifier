package me.ducanh.thesis.formula;

import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.Edge;
import java.util.Map;
import java.util.Set;

public class NotNode implements TreeNode
{
  TreeNode child;
  public NotNode(TreeNode child){
    this.child = child;
  }

  @Override
  public Boolean evaluate(Vertex vertex, Model model) {
    return !child.evaluate(vertex, model);
  }

  @Override
  public String toString() {
    return "!"+child.toString();
  }
}
