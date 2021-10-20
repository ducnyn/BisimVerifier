package me.ducanh.thesis.model;

import com.brunomnsilva.smartgraph.graph.*;

import java.util.*;

public class CustomDigraph implements Digraph<Integer,String> {
    private final Map<Integer,CustomVertex> vertices;
    private final ArrayList<Edge<String,Integer>> edges;

    public CustomDigraph(){
        vertices = new HashMap<>();
        edges = new ArrayList<>();
    }

    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        return edges.size();
    }

    @Override
    public Collection<Vertex<Integer>> vertices() {
        return new ArrayList<>(vertices.values());
    }

    @Override
    public Collection<Edge<String, Integer>> edges() {
        return new ArrayList<>(edges);
    }

    @Override
    public Collection<Edge<String, Integer>> incidentEdges(Vertex<Integer> target) throws InvalidVertexException {
        List<Edge<String, Integer>> incidentEdges = new ArrayList<>();
        for (Edge<String,Integer> edge: edges) {
            if (((CustomEdge) edge).getTarget() == target) {
                incidentEdges.add(edge);
            }
        }
        return incidentEdges;
    }

    @Override
    public Vertex<Integer> opposite(Vertex<Integer> vertex, Edge<String, Integer> edge) throws InvalidVertexException, InvalidEdgeException {
        checkVertex(vertex);
        checkEdge(edge);


        if (edge.vertices()[0] == vertex) {
            return edge.vertices()[1];
        } else if (edge.vertices()[1] == vertex){
            return edge.vertices()[0];
        } else {
            return null;
        }

    }
    private CustomVertex checkVertex(Vertex<Integer> v) throws InvalidVertexException {
        if(v == null) throw new InvalidVertexException("Null vertex.");

        CustomVertex vertex;
        try {
            vertex = (CustomVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsKey(vertex.element())) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private CustomEdge checkEdge(Edge<String,Integer> e) throws InvalidEdgeException {
        if(e == null) throw new InvalidEdgeException("Null edge.");

        CustomEdge edge;
        try {
            edge = (CustomEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an edge.");
        }

        if (!edges.contains(edge)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }

    @Override
    public Collection<Edge<String, Integer>> outboundEdges(Vertex<Integer> source) throws InvalidVertexException {
            List<Edge<String, Integer>> outboundEdges = new ArrayList<>();
            for (Edge<String,Integer> edge: edges) {
                if (((CustomEdge) edge).getSource() == source) {
                    outboundEdges.add(edge);
                }
            }
            return outboundEdges;
        }

    @Override
    public boolean areAdjacent(Vertex<Integer> source, Vertex<Integer> target) throws InvalidVertexException {
            checkVertex(source);
            checkVertex(target);
            /* find and edge that goes source ---> target */
            return edges.stream().anyMatch(edge->((CustomEdge) edge).getSource() == source && ((CustomEdge) edge).getTarget() == target);
    }

    @Override
    public CustomVertex insertVertex(Integer id) throws InvalidVertexException {
        if (vertices.containsKey(id)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }
        CustomVertex newVertex = new CustomVertex(id);
        vertices.put(id,newVertex);
        return newVertex;
    }

    @Override
    public Edge<String, Integer> insertEdge(Vertex<Integer> source, Vertex<Integer> target, String s) throws InvalidVertexException, InvalidEdgeException {
        CustomVertex customSource = checkVertex(source);
        CustomVertex customTarget = checkVertex(target);
        CustomEdge newEdge = new CustomEdge(customSource,s,customTarget);
        if(edges.contains(newEdge)){
            throw new InvalidEdgeException("Edge already exists.");
        }
        edges.add(newEdge);
        return newEdge;
    }

    @Override
    public Edge<String, Integer> insertEdge(Integer sourceID, Integer targetID, String s) throws InvalidEdgeException {
        try{
            insertVertex(sourceID);
        } catch (InvalidVertexException e){
            System.out.println("Vertex with ID " + sourceID + " already exists.");
        }
        try {
            insertVertex(targetID);
        } catch (InvalidVertexException e){
            System.out.println("Vertex with ID " + targetID + " already exists.");
        }

        return insertEdge(vertices.get(sourceID),vertices.get(targetID),s);
    }

    @Override
    public Integer removeVertex(Vertex<Integer> vertex) throws InvalidVertexException {
        checkVertex(vertex);
        Integer ID = vertex.element();
        edges.removeAll(incidentEdges(vertex));
        edges.removeAll(outboundEdges(vertex));
        vertices.remove(ID);
        return ID;
    }

    @Override
    public String removeEdge(Edge<String, Integer> edge) throws InvalidEdgeException {
        checkEdge(edge);
        String ID = edge.element();
        edges.remove(edge);
        return ID;
    }

    @Override
    public Integer replace(Vertex<Integer> oldVertex, Integer newID) throws InvalidVertexException {

        if (vertices.containsKey(newID)) {
            throw new InvalidVertexException("There's already a vertex with this ID.");
        }

        CustomVertex customVertex = checkVertex(oldVertex);
        Integer oldID = oldVertex.element();

        CustomVertex newVertex = insertVertex(newID);

        for(Edge<String,Integer> inEdge : incidentEdges(oldVertex)){
            if(inEdge.vertices()[0] != inEdge.vertices()[1]){
                insertEdge(inEdge.vertices()[0],newVertex,inEdge.element());
            } else {
                insertEdge(newVertex,newVertex,inEdge.element());
            }
        }
        for(Edge<String,Integer> outEdge : outboundEdges(oldVertex)){
            if(outEdge.vertices()[0]!=outEdge.vertices()[1]){
                insertEdge(newVertex,outEdge.vertices()[1],outEdge.element());
            } else {
                insertEdge(newVertex,newVertex,outEdge.element());
            }
        }

        removeVertex(oldVertex);
        return oldID;
    }

    @Override
    public String replace(Edge<String, Integer> oldEdge, String newLabel) throws InvalidEdgeException {
        String oldID = oldEdge.element();
        insertEdge(oldEdge.vertices()[0],oldEdge.vertices()[1],newLabel);
        removeEdge(oldEdge);
        return oldID;
    }
}