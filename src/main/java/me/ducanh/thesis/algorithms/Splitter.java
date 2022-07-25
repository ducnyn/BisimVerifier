package me.ducanh.thesis.algorithms;

import java.util.Objects;

public class Splitter {
  public final String label;
  public final Block targetBlock;



  public Splitter(String label, Block targetBlock){
    this.label = label;
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
    Splitter splitter = (Splitter) o;
    return label.equals(splitter.label) && targetBlock.equals(splitter.targetBlock);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, targetBlock);
  }
}
