package me.ducanh.thesis.parser;

import me.ducanh.thesis.model.Vertex;

public class TT implements FormulaTree {
  @Override
  public Boolean evaluate(Vertex vertex) {
    return true;
  }

  @Override
  public String getString() {
    return "tt";
  }
}
