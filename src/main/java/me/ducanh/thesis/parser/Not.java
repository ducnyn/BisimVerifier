package me.ducanh.thesis.parser;

import me.ducanh.thesis.model.Vertex;

public class Not implements FormulaTree
{
  FormulaTree child;

  @Override
  public Boolean evaluate(Vertex vertex) {
    return !child.evaluate(vertex);
  }

  @Override
  public String getString() {
    return "-"+child.getString();
  }
}
