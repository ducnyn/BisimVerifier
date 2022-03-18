package me.ducanh.thesis.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import me.ducanh.thesis.Controller;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.formula.parser.FormulaParser;
import me.ducanh.thesis.formula.parser.NoMatchingTokenException;
import me.ducanh.thesis.formula.parser.SyntaxErrorException;
import me.ducanh.thesis.formula.TreeNode;
import me.ducanh.thesis.algorithms.Algorithms;
import me.ducanh.thesis.Model;
import me.ducanh.thesis.algorithms.NoDistinguishingFormulaException;
import me.ducanh.thesis.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class Sidebar {

    private Model model;
    private Controller controller;

    public void inject(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }

    @FXML
    private void vertices() {
        Optional<String> numberOK = null;
        String number = "";

        do {
            TextInputDialog sourceDia = new TextInputDialog();
            sourceDia.setHeaderText("Number of vertices to spawn:");
            numberOK = sourceDia.showAndWait();
            number = sourceDia.getEditor().getText();
        } while (numberOK.isPresent() && (StringUtils.notInteger(number) || number.isBlank() || number.isEmpty()));

        if (numberOK.isPresent()) {
            int finalNumber = parseInt(number);
            Thread taskThread = new Thread(() -> {
                for (int i = 0; i < finalNumber; i++) {
                    controller.addVertex();
                }
            });
            taskThread.setDaemon(true);
            taskThread.start();
        }
//        model.addEdge(1, "a", 2);
//        model.addEdge(2, "b", 3);
//        model.addEdge(2, "c", 3);
//
//        model.addEdge(4, "a", 5);
//
//        model.addEdge(4, "a", 6);
//
//        model.addEdge(5, "b", 7);
//        model.addEdge(6, "c", 7);
//
//
//        BlockNode rootBlock = Algorithms.bisim(model.getVertices()).getKey();
//        System.out.println("The deltaFormula is " + Algorithms.getDeltaFormula(model.getVertex(1), model.getVertex(4), rootBlock));
//
//        for (int i = 1; i < 7; i++) {
//            boolean a =
//                    model.getEdges(i).stream()
//                            .anyMatch(e -> e.getLabel().equals("a")
//                                    && e.getTarget().getEdges().stream()
//                                    .noneMatch(e2 -> e2.getLabel().equals("b")));
//
//
//            System.out.println("vertex " + i + " satisfies <a><~b>? -> " + a);
//        }
//
    }

    @FXML
    private void edges() {
        Optional<String> sourceOK;
        Optional<String> labelOK;
        Optional<String> targetOK = Optional.empty();

        String source = "";
        String label = "";
        String target = "";

        do {
            TextInputDialog sourceDia = new TextInputDialog();
            sourceDia.setHeaderText("source Vertex (int):");
            sourceOK = sourceDia.showAndWait();
            source = sourceDia.getEditor().getText();
        } while (sourceOK.isPresent() && (StringUtils.notInteger(source) || source.isBlank() || source.isEmpty()));

        if (sourceOK.isPresent()) {
            do {
                TextInputDialog inputLabel = new TextInputDialog();

                inputLabel.setHeaderText("Label (String):");
                labelOK = inputLabel.showAndWait();
                label = inputLabel.getEditor().getText();
            } while (labelOK.isPresent() && (label.isBlank() || label.isEmpty()));
        } else {
            labelOK = Optional.empty();
        }
        if (labelOK.isPresent()) {
            do {
                TextInputDialog inputTarget = new TextInputDialog();
                inputTarget.setHeaderText("target Vertex (int):");
                targetOK = inputTarget.showAndWait();
                target = inputTarget.getEditor().getText();
            } while (targetOK.isPresent() && (StringUtils.notInteger(target) || target.isBlank() || target.isEmpty()));
        }


        if (targetOK.isPresent()) {
            controller.addEdge(parseInt(source), label, parseInt(target));
        }

    }

    @FXML
    private void randomEdges() {
        model.removeAllEdges();
        ArrayList<Vertex> vertices = new ArrayList<>(model.getVertices());
        for (Vertex vertex : vertices) {
            String alphabet = "abc";

            Random random = new Random();
            int edgesOutDegree = random.nextInt(4);

            for (int j = 0; j < edgesOutDegree; j++) {
                String randomLabel = String.valueOf(alphabet.charAt(random.nextInt(alphabet.length())));
                Vertex randomVertex = vertices.get(random.nextInt(vertices.size() - 1));
                controller.addEdge(vertex.getLabel(), randomLabel, randomVertex.getLabel());
            }
        }
    }

    @FXML
    private void bisimulation() throws InterruptedException {

        model.getColorModeProperty().set(!model.getColorModeProperty().get());
//        Thread taskThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Set<BlockNode> partition = Algorithms.bisim(model.getVertices()).getValue();
//                model.setOutputString("\n Equivalence classes (Bisimulation): \n" + partition.toString());
//                model.updatePartition(partition);
//                System.out.println("Vertices: " + model.getVertices());
//                System.out.println("Edges: " + model.getEdges());
//                System.out.println(Thread.currentThread() + " should be backGroundThread");
//            }
//        });
//        taskThread.setDaemon(true);
//        taskThread.start();

    }

    @FXML
    private void deltaFormula() {
        Optional<String> firstOK;
        Optional<String> secondOK;

        String first = "";
        String second = "";

        do {
            TextInputDialog firstPrompt = new TextInputDialog();
            firstPrompt.setHeaderText("first vertex (int):");
            firstOK = firstPrompt.showAndWait();
            first = firstPrompt.getEditor().getText();
        } while (firstOK.isPresent() && (!StringUtils.notInteger(first) && model.getVertex(parseInt(first)) == null || StringUtils.notInteger(first) || first.isBlank() || first.isEmpty()));

        if (firstOK.isPresent()) {
            do {
                TextInputDialog secondPrompt = new TextInputDialog();
                secondPrompt.setHeaderText("second vertex (int):");
                secondOK = secondPrompt.showAndWait();
                second = secondPrompt.getEditor().getText();
            } while (secondOK.isPresent() && (!StringUtils.notInteger(second) && model.getVertex(parseInt(second)) == null || StringUtils.notInteger(second) || second.isBlank() || second.isEmpty()));

            if (secondOK.isPresent()) {
                Vertex firstVertex = model.getVertex(parseInt(first));
                Vertex secondVertex = model.getVertex(parseInt(second));
                Thread taskThread = new Thread(() -> {
                    try {
                        model.requestPrint("\n Distinguishing Formula: " +
                                Algorithms.getDeltaFormula(firstVertex, secondVertex, model) +
                                " is satisfied by " + firstVertex + " but not by " + secondVertex
                        );
                    } catch (NoDistinguishingFormulaException e) {
                        model.requestPrint(e.getMessage());
                    }
                });
                taskThread.setDaemon(true);
                taskThread.start();
            }

        }
    }

    @FXML
    private void formulaSat() {
        Optional<String> firstOK;
        Optional<String> secondOK;

        String first;
        String second;

        do {
            TextInputDialog firstPrompt = new TextInputDialog();
            firstPrompt.setHeaderText("Please define a start vertex:");
            firstOK = firstPrompt.showAndWait();
            first = firstPrompt.getEditor().getText();
        } while (firstOK.isPresent() && (!StringUtils.notInteger(first) && model.getVertex(parseInt(first)) == null || StringUtils.notInteger(first) || first.isBlank() || first.isEmpty()));

        if (firstOK.isPresent()) {
            TreeNode formulaTree = null;
            do {
                TextInputDialog secondPrompt = new TextInputDialog();
                secondPrompt.setHeaderText("Please enter the formula to be checked:");
                secondOK = secondPrompt.showAndWait();
                second = secondPrompt.getEditor().getText();
                try {
                    formulaTree = FormulaParser.parse(second);
                } catch (SyntaxErrorException | NoMatchingTokenException e) {
                    System.out.println(e.getMessage());
                    formulaTree = null;
                }
            } while (secondOK.isPresent() && (formulaTree == null));

            if (secondOK.isPresent()) {
                final TreeNode finalTree = formulaTree;
                Vertex vertex = model.getVertex(parseInt(first));
                String formula = second;
                Thread taskThread = new Thread(() -> {
                    if (finalTree.evaluate(vertex, model)) {
                        model.requestPrint("Vertex " + vertex + " satisfies the formula: " + formula);
                    } else {
                        model.requestPrint("Vertex " + vertex + " doesn't satisfy the formula: " + formula);
                    }

//                String deltaFormula = Algorithms.getDeltaFormula(
//                        firstVertex,secondVertex,model, Algorithms.bisim(model.getVertices()).getKey());
//                model.setOutputString("\n Distinguishing Formula: " + deltaFormula);
                });
                taskThread.setDaemon(true);
                taskThread.start();
            }
        }
    }

}
