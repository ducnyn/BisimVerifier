package me.ducanh.thesis.parser;

import me.ducanh.thesis.model.Vertex;

public interface FormulaTree {

  public Boolean evaluate(Vertex vertex);

  public String getString();
}
