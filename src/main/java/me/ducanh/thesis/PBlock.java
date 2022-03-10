package me.ducanh.thesis;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

public class Partition extends LinkedList<Vertex>{
//  List<Vertex> vertices = new LinkedList<>();
  PartitionEdge splitter;
  Partition leftChild;
  Partition rightChild;
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
  public Partition(){

  }
  public Partition(Collection<Vertex> vertices) {
   addAll(vertices);

  }

//  public void add(Vertex vertex){
//    this.vertices.add(vertex);
//  }

  public void printTree(){
    Partition rootPartition = this;
    int currentLayer = 0;
    ArrayList<Queue<Partition>> treeLayers = new ArrayList<>();
    Queue<Partition> queue = new LinkedList<>();
    treeLayers.add(queue);
    queue.add(rootPartition);

    while(true){
      Queue<Partition> childQueue = new LinkedList<>();
      for(Partition tPartition : treeLayers.get(currentLayer)){
        if (tPartition.left()!=null) {
          childQueue.add(tPartition.left());
        }
        if (tPartition.right()!=null) {
          childQueue.add(tPartition.right());
        }
        if(tPartition.size()==1){
          childQueue.add(tPartition);
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
    for(Queue<Partition> layer : treeLayers){
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

  public PartitionEdge getSplitter() {
    return splitter;
  }

  public Partition setSplitter(PartitionEdge splitter) {
    this.splitter = splitter;
    return this;
  }

  public Partition left() {
    return leftChild;
  }

  public Partition setLeftChild(Partition leftChild) {
    this.leftChild = leftChild;
    return this;
  }

  public Partition right() {
    return rightChild;
  }

  public Partition setRightChild(Partition rightChild) {
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
