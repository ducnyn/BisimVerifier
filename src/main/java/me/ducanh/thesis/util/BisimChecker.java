package me.ducanh.thesis.util;

import me.ducanh.thesis.model.Edge;
import me.ducanh.thesis.model.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class BisimChecker {

    private Set<Node> split (Set<Node> source, String label, Set<Node> target){
        ArrayList<Set<Node>> blockPair =  new ArrayList<Set<Node>>();
    }

    public static void bisim(Set<Node> nodes, Set<String> labels, Set<Edge> edges) {
        Collection<Node> nodes1 = nodes;
        Collection<Node> nodes2 = Collections.emptySet();
        while (!nodes1.equals(nodes2)) {
            nodes2 = nodes1;
            nodes1 = Collections.emptySet();

            for (Node node : nodes2) {
                nodes1 = nodes1.addAll(split(node, trans, transNode));
            }
        }
    }
}
