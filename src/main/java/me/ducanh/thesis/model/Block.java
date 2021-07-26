package me.ducanh.thesis.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

public class Block implements Iterable<Integer>{
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

  public void printTree(){
    Block block = this;
    Queue<Block> queue = new LinkedList<>();
    queue.add(block);
    while(!queue.isEmpty()){
      block = queue.remove();
      System.out.print(block.vertices + " ");

      if(block.left()!=null){
        queue.add(block.left());
      }
      if(block.right()!=null){
        queue.add(block.right());
      }
    }
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

  public Block left() {
    return leftChild;
  }

  public void setLeftChild(Block leftChild) {
    this.leftChild = leftChild;
  }

  public Block right() {
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

  public boolean containsAll(Integer... i) {
    return this.vertices.containsAll(Arrays.stream(i).collect(Collectors.toList()));
  }

}
