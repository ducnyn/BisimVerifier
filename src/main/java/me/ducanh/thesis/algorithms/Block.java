package me.ducanh.thesis.algorithms;

import me.ducanh.thesis.Vertex;

import java.util.*;

import static java.util.stream.Collectors.partitioningBy;

public class Block {
  public final List<Vertex> vertices = new ArrayList<>();
  public Splitter splitter;
  public Block left;
  public Block right;

  public Block(){

  }

  @Override
  public boolean equals(Object o) {
    if ( this == o ) return true;
    if ( o == null || this.getClass() != o.getClass() ) return false;
    Block block = (Block) o;
    return Objects.equals(this.vertices, block.vertices) && Objects.equals(this.splitter, block.splitter) && Objects.equals(this.left, block.left) && Objects.equals(this.right, block.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vertices, left, right);
  }

  public Block(Collection<Vertex> vertices){
    this.vertices.addAll(vertices);
  }
}




//  public void printTree(){
//    Block rootBlock = this;
//    int currentLayer = 0;
//    ArrayList<Queue<Block>> treeLayers = new ArrayList<>();
//    Queue<Block> queue = new LinkedList<>();
//    treeLayers.add(queue);
//    queue.add(rootBlock);
//
//    while(true){
//      Queue<Block> childQueue = new LinkedList<>();
//      for(Block tBlock : treeLayers.get(currentLayer)){
//        if (tBlock.getLeft()!=null) {
//          childQueue.add(tBlock.getLeft());
//        }
//        if (tBlock.getRight()!=null) {
//          childQueue.add(tBlock.getRight());
//        }
//        if(tBlock.size()==1){
//          childQueue.add(tBlock);
//        }
//      }
//      if (childQueue.isEmpty()){
//        break;
//      } else if (childQueue.stream().allMatch(b->b.size() ==1)){
//        break;
//      }
//      treeLayers.add(childQueue);
//      currentLayer = treeLayers.indexOf(childQueue);
//    }
//    for(Queue<Block> layer : treeLayers){
//      System.out.println(layer);
////      for(BlockNode blockNode : layer){
////        if(blockNode.getVertices()!=null)
////        System.out.println("splitter for " + blockNode + "is" + blockNode.getSplitter());
////      }
//    }
////    treeLayers.add(queue);
////    queue.add(rootBlock);
////    while(!queue.isEmpty()){
////      rootBlock = queue.remove();
////      System.out.print(rootBlock.vertices + " ");
////
////      if(rootBlock.left()!=null){
////        queue.add(rootBlock.left());
////      }
////      if(rootBlock.right()!=null){
////        queue.add(rootBlock.right());
////      }
////    }
//  }