package me.ducanh.thesis.formula;

import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;

import java.util.Map;
import java.util.Set;

public class TrueNode implements TreeNode {
  @Override
  public Boolean evaluate(Vertex vertex, Model model) {
    return true;
  }

  @Override
  public String toString() {
    return "tt";
  }
}
