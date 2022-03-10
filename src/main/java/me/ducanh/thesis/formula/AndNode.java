package me.ducanh.thesis.formula;

import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;

import java.util.Map;
import java.util.Set;

public class AndNode implements TreeNode {

  TreeNode leftChild;
  TreeNode rightChild;

  public AndNode(TreeNode leftChild, TreeNode rightChild){
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  @Override
  public Boolean evaluate(Vertex vertex, Model model) {
    return leftChild.evaluate(vertex, model) && rightChild.evaluate(vertex, model);
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
