package me.ducanh.thesis;

import java.util.*;

import static java.util.stream.Collectors.partitioningBy;

public class Block extends LinkedList<Vertex>{
//  List<Vertex> vertices = new LinkedList<>();
  BlockEdge splitter;
  Block leftChild;
  Block rightChild;
  {
  }
//  public Block(Set<Vertex> vertices, BlockEdge splitter, Block leftChild, Block rightChild) {
//    this.vertices = vertices;
//    this.splitter = splitter;
//    this.leftChild = leftChild;
//    this.rightChild = rightChild;
//  }

//  public Partition(){
//    vertices = new LinkedList<>();
//  }i
  public Block(){

  }
  public Block(Collection<Vertex> vertices) {
   addAll(vertices);

  }

//  public void add(Vertex vertex){
//    this.vertices.add(vertex);
//  }

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
        if(tBlock.size()==1){
          childQueue.add(tBlock);
        }
      }
      if (childQueue.isEmpty()){
        break;
      } else if (childQueue.stream().allMatch(b->b.size() ==1)){
        break;
      }
      treeLayers.add(childQueue);
      currentLayer = treeLayers.indexOf(childQueue);
    }
    for(Queue<Block> layer : treeLayers){
      System.out.println(layer);
//      for(BlockNode blockNode : layer){
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

//  @Override
//  public String toString() {
//    return vertices.toString();
//  }

//  public List<Vertex> getVertices() {
//    return vertices;
//  }

  public BlockEdge getSplitter() {
    return splitter;
  }

  public Block setSplitter(BlockEdge splitter) {
    this.splitter = splitter;
    return this;
  }

  public Block left() {
    return leftChild;
  }

  public Block setLeftChild(Block leftChild) {
    this.leftChild = leftChild;
    return this;
  }

  public Block right() {
    return rightChild;
  }

  public Block setRightChild(Block rightChild) {
    this.rightChild = rightChild;
    return this;
  }

//  public Stream<Vertex> stream(){
//    return vertices.stream();
//  }
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//
//    Partition partition = (Partition) o;
//
//    return vertices.equals(partition.vertices);
//  }
//
//  @Override
//  public int hashCode() {
//    return vertices.hashCode();
//  }
//
//  @Override
//  public Iterator<Vertex> iterator() {
//    return vertices.iterator();
//  }

//  public boolean contains(Vertex vertex) {
//    return vertices.contains(vertex);
//  }
  public boolean hasSplitter(){
    return !(this.splitter==null);
  }
  public boolean containsAll(Vertex... v) {
//    for(Vertex vertex : v){
//      if (!this.contains(vertex)) return false;
//    }
//    return true;
    return Arrays.stream(v).allMatch(this::contains);
//    return this.vertices.containsAll(Arrays.stream(i).collect(Collectors.toList()));
  }

}
