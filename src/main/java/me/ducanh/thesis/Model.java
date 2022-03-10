package me.ducanh.thesis;


import com.google.common.collect.Sets;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import javafx.util.Pair;
import me.ducanh.thesis.algorithms.Algorithms;
import me.ducanh.thesis.algorithms.NoDistinguishingFormulaException;
import me.ducanh.thesis.formula.*;

import java.util.*;
import java.util.stream.Collectors;


public class Model {
  private String username = "User";

//  private final ObservableMap<Integer, Vertex> vertices = FXCollections.observableMap(new HashMap<>());
  private final ObservableMap<Vertex,ObservableSet<Edge>> adjacencyList = FXCollections.observableMap(new HashMap<>());
  private final TreeSet<Integer> deletedIDs = new TreeSet<>();
  private final BooleanProperty colorModeProperty = new SimpleBooleanProperty(false);
  private final BooleanProperty printRequest = new SimpleBooleanProperty();
  private  String printString = "";

//  private boolean addedByVis = false;


  {//initiator
    adjacencyList.addListener((MapChangeListener<Vertex, ObservableSet<Edge>>) vertex -> {

      if (vertex.wasRemoved()){
        Vertex removedVertex = vertex.getKey();
        deletedIDs.add(removedVertex.getLabel());
        vertex.getValueRemoved().clear();
        for(ObservableSet<Edge> edgeSet : vertex.getMap().values()){
          edgeSet.removeIf(edge -> edge.getSource().equals(removedVertex) ||edge.getTarget().equals(removedVertex));
        }
      } else {

      }
    });



  }

  public void addEdge(Vertex source, String label, Vertex target) {
    adjacencyList.get(source).add(new Edge(source, label, target));
  }

  public Boolean addVertex(Vertex vertex) {
    if (adjacencyList.containsKey(vertex))
      return false;

    adjacencyList.put(vertex,FXCollections.observableSet(new HashSet<>()));
    return true;
  }

 public Integer smallestFreeLabel(){
    return Objects.requireNonNullElse(deletedIDs.pollFirst(), getMaxID() + 1);
 }

  public void clear(){
    for(Vertex vertex : adjacencyList.keySet()){
      removeVertex(vertex);
    }
  }



  public Integer getMaxID() {
    return adjacencyList.keySet().stream()
            .max(Vertex::compareTo)
            .map(Vertex::getLabel)
            .orElse(0);
  }

  public void removeAllEdges() {
    for(Set<Edge> edgeSet: adjacencyList.values()){
      for(Edge edge:edgeSet){
        edgeSet.remove(edge);
      }
    }
  }

  public Boolean removeVertex(Vertex vertex) {
    if (adjacencyList.containsKey(vertex)){
      adjacencyList.remove(vertex);
      return true;
    }
    return false;
  }

  public void addVertexListener(MapChangeListener<Vertex, ObservableSet<Edge>> mapChangeListener) {
    adjacencyList.addListener(mapChangeListener);
  }

  public void addEdgeListener(SetChangeListener<Edge> edgeSetListener){
    for(ObservableSet<Edge> edgeSet : adjacencyList.values()){
      edgeSet.addListener(edgeSetListener);
    }
  }


 public ObservableMap<Vertex,ObservableSet<Edge>> getAdjacencyList(){
      return adjacencyList;
 }
  public Set<Vertex> getVertices() {
    return Set.copyOf(adjacencyList.keySet());
  }

  public Set<Edge> getEdges() {
    return Set.copyOf(
            adjacencyList.values().stream()
            .flatMap(ObservableSet::stream)
            .collect(Collectors.toSet())
    );
  }
  public Set<Edge> getEdges(Vertex vertex){
    return Set.copyOf(adjacencyList.get(vertex));
  }

  public BooleanProperty getColorModeListener(){
    return colorModeProperty;
  }
  public Boolean getColorMode(){
      return colorModeProperty.get();
  }

  public Set<Partition> getBisimulation(){
      return Algorithms.bisim(this).getValue();
  }



  public void requestPrint(String string){
    this.printString = string;
    this.printRequest.setValue(false);
    this.printRequest.setValue(true);
  }
  public BooleanProperty printRequestedProperty(){
    return printRequest;
  }

  public String getPrintString(){
    return printString;
  }

  public void setUsername(String name){
    this.username = name;
  }

  public String getUserName() {
    return username;
  }

    public Set<Vertex> getTargets(Vertex vertex, String action) {
      return adjacencyList.get(vertex).stream()
              .filter(edge->edge.getLabel().equals(action))
              .map(Edge::getTarget)
              .collect(Collectors.toSet());
    }

    //True = states that have an action a, leading to a state in B' (= leading to the same state based on current bisimulation refinement)
    //False = the rest.
    public Partition split(Partition partition, PartitionEdge partitionEdge, Map<Vertex, ? extends Collection<Edge>> graph) {
//    System.out.println("attempting to split " + block);

//        Map<Boolean,Set<Vertex>> newPartitions =
//                partition.stream()
//                .collect(Collectors.partitioningBy(
//                        vertex -> graph.get(vertex).stream()
//                                .filter(edge->edge.getLabel().equals(partitionEdge.getLabel()))
//                                .map(Edge::getTarget)
//                                .anyMatch(partitionEdge.getTargetBlock()::contains)
//                        , toSet()));
//
        Partition result = new Partition(partition);
        Partition yes = new Partition();
        Partition no = new Partition();
        for (Vertex vertex : partition) {
            for (Edge edge : graph.get(vertex)) {
                if (partitionEdge.getLabel().equals(edge.getLabel()) &&
                        partitionEdge.getTargetBlock().contains(edge.getTarget()))
                    yes.add(vertex);
                else no.add(vertex);
            }
        }
        if (yes.isEmpty() || no.isEmpty()) return result;
        else return result.setLeftChild(yes).setRightChild(no).setSplitter(partitionEdge);
    }



    private Pair<Partition, List<Partition>> bisim(Map<Vertex, ? extends Collection<Edge>> graph) {

        HashMap<Vertex, Partition> containingBlock = new HashMap<>();
        List<Partition> nextPartitions = new LinkedList<>();
        List<Partition> currPartitions = new LinkedList<>();
        Partition rootPartition = new Partition(graph.keySet());
        nextPartitions.add(rootPartition);

        while (!nextPartitions.equals(currPartitions)) { //while partions can still be refined
//            System.out.println("current Partition: " + nextPartitions);
            currPartitions = nextPartitions;
            nextPartitions = new LinkedList<>();

            for (Partition partition : currPartitions) {
                for (Vertex vertex : partition) {
                    containingBlock.put(vertex, partition);
                }
            }

            for (Partition partition : currPartitions) {

                Optional<Partition> refinedPartition =
                        partition.stream()
                                .flatMap(vertex -> graph.get(vertex).stream())
                                .map(edge -> new PartitionEdge(edge.getLabel(), containingBlock.get(edge.getTarget())))
                                .map(pEdge -> split(partition, pEdge,graph))
                                .filter(Partition::hasSplitter)
                                .findFirst();

                if (refinedPartition.isPresent()) {

                    nextPartitions.addAll(List.of(refinedPartition.get().left(), refinedPartition.get().right()));
//                    System.out.println("\tblock "+ partition +" split by " + partition.getSplitter());
//                    System.out.println("\tinto " + partition.left() + " and " + partition.right());
                } else {
                    nextPartitions.add(partition);
//                    System.out.println("\tblock " + partition + " is not split");
                }
            }
        }
//    System.out.println("Final partition(bisimilar sets): " + nextPartitions);
        return new Pair<>(rootPartition, nextPartitions);
    }


    private Partition findLastBlock(Vertex state1, Vertex state2, Partition root) throws NoDistinguishingFormulaException {
        Partition partition = root;

        while (true) {
            if (!partition.hasSplitter()) {
                return partition;
            }
            if (partition.left().containsAll(state1, state2)) {
                partition = partition.left();
            } else if (partition.right().containsAll(state1, state2)) {
                partition = partition.right();
            } else {
                return partition; //neither left nor right contains all -> next split will separate
            }
        }

    }

    public TreeNode getDeltaFormula(Vertex state1, Vertex state2, Set<Vertex> vertexSet, Map<Vertex,Set<Edge>>graph) throws NoDistinguishingFormulaException {

        Partition rootPartition = bisim(getAdjacencyList()).getKey();

        TreeNode result = clevelandAlgo(state1, state2, rootPartition, graph);
        System.out.println(result);
        return result;
    }

    private TreeNode clevelandAlgo(Vertex state1, Vertex state2, Partition rootPartition,Map<Vertex,Set<Edge>>graph) throws NoDistinguishingFormulaException {

        Partition lastPartition = findLastBlock(state1, state2, rootPartition);
        if (lastPartition.getSplitter() == null) {
            throw new NoDistinguishingFormulaException(
                    "The states are bisimilar. No formula distinguishes them.");
        }
        if (!(rootPartition.contains(state1) & rootPartition.contains(state2))) {
            throw new RuntimeException("one or more of the compared states does not exist in the given set of vertices");
        }

        PartitionEdge splitter = lastPartition.getSplitter();
        String splitAction = splitter.getLabel();
        Set<Vertex> splitBlock = new HashSet<>((splitter.getTargetBlock()));

        Vertex leftState;
        Vertex rightState;
        boolean reversed;

        if (lastPartition.left().contains(state1) & lastPartition.right().contains(state2)) {
            leftState = state1;
            rightState = state2;
            reversed = false;
        } else if (lastPartition.left().contains(state2) & lastPartition.right().contains(state1)) {
            leftState = state2;
            rightState = state1;
            reversed = true;
        } else { //Test should never throw this
            throw new RuntimeException("state 1 and 2 are not split or not contained in lastBlock.");
        }


        Set<Vertex> SL = Sets.intersection(getTargets(leftState,graph, splitAction), splitBlock);
        Set<Vertex> SR = getTargets(rightState,graph,splitAction);
        int minFormulaSize = Integer.MAX_VALUE; //TODO long?
        TreeNode nextNode = new TrueNode();
//        TreeNode result = new DiamondNode(splitAction,nextNode);

        for (Vertex LTarget : SL) {
            List<TreeNode> formulas = new ArrayList<>(); //GAMMA

            for (Vertex RTarget : SR) {
                formulas.add(clevelandAlgo(LTarget, RTarget, rootPartition,graph));
            }

            for (TreeNode formula : formulas) {
                ArrayList<TreeNode> otherFormulas = new ArrayList<>(formulas);
                otherFormulas.remove(formula);
                System.out.println("All formulas for " + LTarget + formulas);

                if (SR.stream().noneMatch(vertex -> (!formula.evaluate(vertex,this )
                        && otherFormulas.stream().allMatch(otherFormula -> otherFormula.evaluate(vertex, this))))) {
                    formulas.remove(formula);
                    System.out.println("Formula removed: " + formula);
                }
            }

            if (formulas.size() < minFormulaSize) {
                minFormulaSize = formulas.size();
                if (formulas.size() == 1) {
                    nextNode = formulas.get(0);
                } else if (formulas.size() > 1) {
                    nextNode = formulas.get(0);
                    for (int i = 1; i < formulas.size(); i++) {
                        nextNode = new AndNode(nextNode, formulas.get(i));
                    }
                }
            }
        }
        TreeNode result = new DiamondNode(splitAction, nextNode);
        return reversed ? new NotNode(result) : result;
    } //TEST IT AGAIN


}




