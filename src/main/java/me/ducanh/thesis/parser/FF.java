package me.ducanh.thesis.parser;

import me.ducanh.thesis.model.Vertex;

public class FF implements FormulaTree {
  @Override
  public Boolean evaluate(Vertex vertex) {
    return false;
  }

  @Override
  public String getString() {
    return "ff";
  }
}
