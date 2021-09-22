package me.ducanh.thesis.parser;

import me.ducanh.thesis.model.Vertex;

public class ForAll implements FormulaTree {

  private String action;
  private FormulaTree child;

  public ForAll(String action, FormulaTree child){
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

  public FormulaTree getChild() {
    return child;
  }
}
