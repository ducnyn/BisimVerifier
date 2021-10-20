package me.ducanh.thesis.model;

public class BlockEdge {
  private String label;
  private Block targetBlock;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Block getTargetBlock() {
    return targetBlock;
  }

  public void setTargetBlock(Block targetBlock) {
    this.targetBlock = targetBlock;
  }

  public BlockEdge(String label, Block targetBlock){
    this.label = label;
    this.targetBlock = targetBlock;
  }

  @Override
  public String toString() {
    return "-"+label+">"+ targetBlock.toString();
  }
}
