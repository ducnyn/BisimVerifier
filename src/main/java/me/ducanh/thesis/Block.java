package me.ducanh.thesis;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

public class Block implements Iterable<Vertex>{
  Set<Vertex> vertices;
  BlockEdge splitter;
  Block leftChild;
  Block rightChild;

  public Block(Set<Vertex> vertices, BlockEdge splitter, Block leftChild, Block rightChild) {
    this.vertices = vertices;
    this.splitter = splitter;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  public Block(Set<Vertex> vertices) {
    this.vertices = vertices;
  }


  public void printTree(){
    Block rootBlock = this;
    int currentLayer = 0;
    ArrayList<Queue<Block>> treeLayers = new ArrayList<>();
    Queue<Block> queue = new LinkedList<>();
    treeLayers.add(queue);
    queue.add(rootBlock);

    while(true){
      Queue<Block> childQueue = new LinkedList<>();
      for(Block tBlock : treeLayers.get(currentLayer)){
        if (tBlock.left()!=null) {
          childQueue.add(tBlock.left());
        }
        if (tBlock.right()!=null) {
          childQueue.add(tBlock.right());
        }
        if(tBlock.getVertices().size()==1){
          childQueue.add(tBlock);
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
    for(Queue<Block> layer : treeLayers){
      System.out.println(layer);
//      for(Block blockNode : layer){
//        if(blockNode.getVertices()!=null)
//        System.out.println("splitter for " + blockNode + "is" + blockNode.getSplitter());
//      }
    }
//    treeLayers.add(queue);
//    queue.add(rootBlock);
//    while(!queue.isEmpty()){
//      rootBlock = queue.remove();
//      System.out.print(rootBlock.vertices + " ");
//
//      if(rootBlock.left()!=null){
//        queue.add(rootBlock.left());
//      }
//      if(rootBlock.right()!=null){
//        queue.add(rootBlock.right());
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

  public Stream<Vertex> stream(){
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
