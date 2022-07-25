package me.ducanh.thesis.algorithms;

import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlgorithmsTest {

    Model graph;

    @BeforeEach
    void setUp() {
        graph = new Model();
        graph.addEdge(new Vertex(0), "a", new Vertex(1));
        graph.addEdge(new Vertex(1), "b", new Vertex(2));
        graph.addEdge(new Vertex(1), "c", new Vertex(2));
        graph.addEdge(new Vertex(3), "a", new Vertex(4));
        graph.addEdge(new Vertex(3), "a", new Vertex(5));
        graph.addEdge(new Vertex(4), "b", new Vertex(6));
        graph.addEdge(new Vertex(5), "c", new Vertex(7));
    }

    @Test
    @DisplayName("split should split")
    //TODO create random test cases
    void splitTest() {
        Block toSplit = new Block(graph.getVertices());
        Block toSplitOriginal = new Block(graph.getVertices());
        Block afterSplit = Algorithms.split(toSplit, new Splitter("a", toSplit), graph);
        assert toSplit.equals(toSplitOriginal);
        assert (afterSplit.vertices.equals(toSplit.vertices));
        assert ((afterSplit.splitter != null && afterSplit.left != null && afterSplit.right != null) ||
                afterSplit.splitter == null && afterSplit.left == null && afterSplit.right == null);

        //TODO create cases that should split and cases that shouldn't
        if ( afterSplit.splitter != null ) {
            assert (
                    afterSplit.left.vertices.stream()
                            .flatMap(vertex -> graph.getEdges(vertex).stream())
                            .anyMatch(edge -> edge.label.equals(afterSplit.splitter.label) && afterSplit.splitter.targetBlock.vertices.contains(edge.target))
            );
            assert (
                    afterSplit.right.vertices.stream()
                            .flatMap(vertex -> graph.getEdges(vertex).stream())
                            .noneMatch(edge -> edge.label.equals(afterSplit.splitter.label) && afterSplit.splitter.targetBlock.vertices.contains(edge.target))
            );
        }
    }

    @Test
    void partitionByBisimulation() {
    }

    @Test
    void getDeltaFormula() {
    }
}