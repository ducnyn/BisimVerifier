package me.ducanh.thesis.model;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

public class BlockNode implements Iterable<Vertex>{
  Set<Vertex> vertices;
  BlockEdge splitter;
  BlockNode leftChild;
  BlockNode rightChild;

  public BlockNode(Set<Vertex> vertices, BlockEdge splitter, BlockNode leftChild, BlockNode rightChild) {
    this.vertices = vertices;
    this.splitter = splitter;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  public BlockNode(Set<Vertex> vertices) {
    this.vertices = vertices;
  }

  public void printTree(){
    BlockNode rootBlockNode = this;
    int currentLayer = 0;
    ArrayList<Queue<BlockNode>> treeLayers = new ArrayList<>();
    Queue<BlockNode> queue = new LinkedList<>();
    treeLayers.add(queue);
    queue.add(rootBlockNode);

    while(true){
      Queue<BlockNode> childQueue = new LinkedList<>();
      for(BlockNode tBlockNode : treeLayers.get(currentLayer)){
        if (tBlockNode.left()!=null) {
          childQueue.add(tBlockNode.left());
        }
        if (tBlockNode.right()!=null) {
          childQueue.add(tBlockNode.right());
        }
        if(tBlockNode.getVertices().size()==1){
          childQueue.add(tBlockNode);
        }
      }
      if (childQueue.isEmpty()){
        break;
      } else if (childQueue.stream().allMatch(b->b.getVertices().size() ==1)){
        break;
      }
      treeLayers.add(childQueue);
      currentLayer = treeLayers.indexOf(childQueue);
    }
    for(Queue<BlockNode> layer : treeLayers){
      System.out.println(layer);
//      for(BlockNode blockNode : layer){
//        if(blockNode.getVertices()!=null)
//        System.out.println("splitter for " + blockNode + "is" + blockNode.getSplitter());
//      }
    }
//    treeLayers.add(queue);
//    queue.add(rootBlockNode);
//    while(!queue.isEmpty()){
//      rootBlockNode = queue.remove();
//      System.out.print(rootBlockNode.vertices + " ");
//
//      if(rootBlockNode.left()!=null){
//        queue.add(rootBlockNode.left());
//      }
//      if(rootBlockNode.right()!=null){
//        queue.add(rootBlockNode.right());
//      }
//    }
  }

  @Override
  public String toString() {
    return vertices.toString();
  }

  public Set<Vertex> getVertices() {
    return vertices;
  }

  public BlockEdge getSplitter() {
    return splitter;
  }

  public void setSplitter(BlockEdge splitter) {
    this.splitter = splitter;
  }

  public BlockNode left() {
    return leftChild;
  }

  public void setLeftChild(BlockNode leftChild) {
    this.leftChild = leftChild;
  }

  public BlockNode right() {
    return rightChild;
  }

  public void setRightChild(BlockNode rightChild) {
    this.rightChild = rightChild;
  }

  public Stream<Vertex> stream(){
    return vertices.stream();
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BlockNode blockNode = (BlockNode) o;

    return vertices.equals(blockNode.vertices);
  }

  @Override
  public int hashCode() {
    return vertices.hashCode();
  }

  @Override
  public Iterator<Vertex> iterator() {
    return vertices.iterator();
  }

  public boolean contains(Vertex vertex) {
    return vertices.contains(vertex);
  }

  public boolean containsAll(Vertex... v) {
    return Arrays.stream(v).allMatch(vertices::contains);
//    return this.vertices.containsAll(Arrays.stream(i).collect(Collectors.toList()));
  }

}
