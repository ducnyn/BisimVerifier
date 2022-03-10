package me.ducanh.thesis.formula;

import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.Edge;
import java.util.Map;
import java.util.Set;

public class FalseNode implements TreeNode {
  @Override
  public Boolean evaluate(Vertex vertex, Model model) {
    return false;
  }

  @Override
  public String toString() {
    return "ff";
  }
}
