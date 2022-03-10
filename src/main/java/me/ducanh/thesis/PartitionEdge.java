package me.ducanh.thesis;

public class PartitionEdge {
  private String label;
  private Partition targetPartition;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Partition getTargetBlock() {
    return targetPartition;
  }

  public void setTargetBlock(Partition targetPartition) {
    this.targetPartition = targetPartition;
  }

  public PartitionEdge(String label, Partition targetPartition){
    this.label = label;
    this.targetPartition = targetPartition;
  }

  @Override
  public String toString() {
    return "-"+label+">"+ targetPartition.toString();
  }
}
