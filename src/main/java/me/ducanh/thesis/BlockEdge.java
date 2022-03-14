package me.ducanh.thesis;

public class BlockEdge {
  private String label;
  private Block targetBlock;



  public BlockEdge(String label, Block targetBlock){
    this.label = label;
    this.targetBlock = targetBlock;
  }
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
  @Override
  public String toString() {
    return "-"+label+">"+ targetBlock.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BlockEdge blockEdge = (BlockEdge) o;

    if (!label.equals(blockEdge.label)) return false;
    return targetBlock.equals(blockEdge.targetBlock);
  }

  @Override
  public int hashCode() {
    int result = label.hashCode();
    result = 31 * result + targetBlock.hashCode();
    return result;
  }
}
