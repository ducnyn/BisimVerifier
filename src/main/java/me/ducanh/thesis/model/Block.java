package me.ducanh.thesis.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toSet;

public class Block implements Iterable<Integer> {
  Set<Integer> vertices;
  BlockEdge splitter;
  Block leftChild;
  Block rightChild;

  public Block(Set<Integer> vertices, BlockEdge splitter, Block leftChild, Block rightChild) {
    this.vertices = vertices;
    this.splitter = splitter;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  public Block(Set<Integer> vertices) {
    this.vertices = vertices;
  }



  @Override
  public String toString() {
    return vertices.toString();
  }

  public Set<Integer> getVertices() {
    return vertices;
  }

  public BlockEdge getSplitter() {
    return splitter;
  }

  public void setSplitter(BlockEdge splitter) {
    this.splitter = splitter;
  }

  public Block getLeftChild() {
    return leftChild;
  }

  public void setLeftChild(Block leftChild) {
    this.leftChild = leftChild;
  }

  public Block getRightChild() {
    return rightChild;
  }

  public void setRightChild(Block rightChild) {
    this.rightChild = rightChild;
  }

  public Stream<Integer> stream(){
    return vertices.stream();
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Block block = (Block) o;

    return vertices.equals(block.vertices);
  }

  @Override
  public int hashCode() {
    return vertices.hashCode();
  }

  @Override
  public Iterator<Integer> iterator() {
    return vertices.iterator();
  }

  public boolean contains(Integer integer) {
    return vertices.contains(integer);
  }
}
