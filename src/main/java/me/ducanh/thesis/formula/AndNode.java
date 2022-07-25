package me.ducanh.thesis.formula;

import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Graph;
import me.ducanh.thesis.Vertex;

public class AndNode implements TreeNode {

  TreeNode leftChild;
  TreeNode rightChild;

  public AndNode(TreeNode leftChild, TreeNode rightChild){
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  @Override
  public Boolean evaluate(Vertex vertex, Graph<Vertex, Edge> graph) {
    return leftChild.evaluate(vertex, graph) && rightChild.evaluate(vertex, graph);
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
