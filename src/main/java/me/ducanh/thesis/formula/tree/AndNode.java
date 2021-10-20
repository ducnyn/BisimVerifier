package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.CustomVertex;

public class AndNode implements TreeNode {

  TreeNode leftChild;
  TreeNode rightChild;

  public AndNode(TreeNode leftChild, TreeNode rightChild){
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  @Override
  public Boolean evaluate(CustomVertex vertex) {
    return leftChild.evaluate(vertex) && rightChild.evaluate(vertex);
  }
  @Override
  public String toString(){return "("+leftChild.toString() + " && " + rightChild.toString()+")";}
  public TreeNode getLeftChild() {
    return leftChild;
  }
  public TreeNode getRightChild() {
    return rightChild;
  }
}
