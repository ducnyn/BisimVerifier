package me.ducanh.thesis.formula;

public class OrNode implements TreeNode {


  TreeNode leftChild;
  TreeNode rightChild;

  public OrNode(TreeNode leftChild, TreeNode rightChild){
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  @Override
  public Boolean evaluate(Integer vertex) {
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
