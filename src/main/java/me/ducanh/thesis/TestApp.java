package me.ducanh.thesis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import me.ducanh.thesis.algorithms.Algorithms;
import me.ducanh.thesis.algorithms.NoCommonBlockException;
import me.ducanh.thesis.algorithms.NoDistinguishingFormulaException;
import me.ducanh.thesis.algorithms.StateDisappearedDuringPartitioningException;

import java.util.TreeSet;

public class TestApp {
    private static ObservableSet<Integer> intSet = FXCollections.observableSet(new TreeSet<>());
    private static ObservableSet<Integer> int2Set = FXCollections.unmodifiableObservableSet(intSet);

    public static void main(String[] args) throws NoDistinguishingFormulaException, StateDisappearedDuringPartitioningException, NoCommonBlockException {
Model model = new Model();
model.addEdge(new Vertex(1), "a", new Vertex(2));
Algorithms.getDistinguishingFormula(new Vertex(1), new Vertex(2),model);
}}

