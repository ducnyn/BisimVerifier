package me.ducanh.thesis.model;

public class BlockEdge {
  private String label;
  private BlockNode targetBlockNode;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public BlockNode getTargetBlock() {
    return targetBlockNode;
  }

  public void setTargetBlock(BlockNode targetBlockNode) {
    this.targetBlockNode = targetBlockNode;
  }

  public BlockEdge(String label, BlockNode targetBlockNode){
    this.label = label;
    this.targetBlockNode = targetBlockNode;
  }

  @Override
  public String toString() {
    return "-"+label+">"+ targetBlockNode.toString();
  }
}
