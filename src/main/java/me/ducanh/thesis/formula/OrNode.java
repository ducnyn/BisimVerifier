package me.ducanh.thesis.formula;

import me.ducanh.thesis.Vertex;

public class OrNode implements TreeNode {


  TreeNode leftChild;
  TreeNode rightChild;

  public OrNode(TreeNode leftChild, TreeNode rightChild){
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  @Override
  public Boolean evaluate(Vertex vertex) {
    return leftChild.evaluate(vertex) || rightChild.evaluate(vertex);
  }

  @Override
  public String toString(){return "("+leftChild.toString() + " || " + rightChild.toString()+")";}

  public TreeNode getLeftChild() {
    return leftChild;
  }
  public TreeNode getRightChild() {
    return rightChild;
  }
}
