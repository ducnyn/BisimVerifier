package me.ducanh.thesis.parser;

import me.ducanh.thesis.model.Vertex;

public class Or implements FormulaTree {

  FormulaTree leftChild;
  FormulaTree rightChild;
  @Override
  public Boolean evaluate(Vertex vertex) {
    return leftChild.evaluate(vertex) || rightChild.evaluate(vertex);
  }

  @Override
  public String getString(){return "("+leftChild.toString() + " || " + rightChild.toString()+")";}

  public FormulaTree getLeftChild() {
    return leftChild;
  }
  public FormulaTree getRightChild() {
    return rightChild;
  }
}
