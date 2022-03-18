package me.ducanh.thesis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import me.ducanh.thesis.algorithms.Algorithms;
import me.ducanh.thesis.algorithms.NoDistinguishingFormulaException;

import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

public class TestApp {
    private static ObservableSet<Integer> intSet = FXCollections.observableSet(new TreeSet<>());
    private static ObservableSet<Integer> int2Set = FXCollections.unmodifiableObservableSet(intSet);

    public static void main(String[] args) throws NoDistinguishingFormulaException {
Model model = new Model();
model.addEdge(new Vertex(2), "a", new Vertex(2));
Algorithms.getDeltaFormula(new Vertex(1), new Vertex(2),model);
}}

