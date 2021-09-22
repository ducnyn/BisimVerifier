package me.ducanh.thesis.parser;

import me.ducanh.thesis.model.Vertex;

public class Exists implements FormulaTree {
  String action;
  FormulaTree child;
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

  public FormulaTree getChild() {
    return child;
  }
}
