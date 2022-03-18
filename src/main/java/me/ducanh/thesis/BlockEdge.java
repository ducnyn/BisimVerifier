package me.ducanh.thesis;

public class BlockEdge {
  private final String label;
  private final Block targetBlock;



  public BlockEdge(String label, Block targetBlock){
    this.label = label;
    this.targetBlock = targetBlock;
  }
  public String getLabel() {
    return label;
  }


  public Block getTargetBlock() {
    return targetBlock;
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
