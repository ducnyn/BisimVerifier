package me.ducanh.thesis;

import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import me.ducanh.thesis.formula.FormulaParser;
import me.ducanh.thesis.formula.NoMatchingTokenException;
import me.ducanh.thesis.formula.SyntaxErrorException;
import me.ducanh.thesis.formula.tree.TreeNode;
import me.ducanh.thesis.model.*;
import me.ducanh.thesis.util.StringUtils;

import java.util.*;

import static java.lang.Integer.parseInt;

public class SideBar {

    private Model model;

    public void initialize(Model model) {
        this.model = model;
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

    if(numberOK.isPresent()){
        int finalNumber = parseInt(number);
        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< finalNumber; i++){
                    model.addNextIDVertex();
                }
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
//        Block rootBlock = Algorithms.bisim(model.getVertices()).getKey();
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
            sourceDia.setHeaderText("source VisVertex (int):");
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
                inputTarget.setHeaderText("target VisVertex (int):");
                targetOK = inputTarget.showAndWait();
                target = inputTarget.getEditor().getText();
            } while (targetOK.isPresent() && (StringUtils.notInteger(target) || target.isBlank() || target.isEmpty()));
        }


        if (targetOK.isPresent()) {
            model.addEdge(parseInt(source), label, parseInt(target));
        }

    }

    @FXML
    private void randomEdges() {
        model.removeAllEdges();
        ArrayList<CustomVertex> vertices = new ArrayList<>(model.getVertices());
        for (CustomVertex vertex : vertices) {
            String alphabet = "abc";

            Random random = new Random();
            int edgesOutDegree = random.nextInt(4);

            for (int j = 0; j < edgesOutDegree; j++) {
                String randomLabel = String.valueOf(alphabet.charAt(random.nextInt(alphabet.length())));
                CustomVertex randomVertex = vertices.get(random.nextInt(vertices.size() - 1));
                model.addEdge(vertex.getID(), randomLabel, randomVertex.getID());
            }
        }
    }

    @FXML
    private void bisimulation() throws InterruptedException {


        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Set<Block> partition = Algorithms.bisim(model.getVertices()).getValue();
                model.setOutputString("\n Equivalence classes (Bisimulation): \n" + partition.toString());
                model.updatePartition(partition);
                System.out.println("Vertices: " + model.getVertices());
                System.out.println("Edges: " + model.getEdges());
                System.out.println(Thread.currentThread() + " should be backGroundThread");
            }
        });
        taskThread.setDaemon(true);
        taskThread.start();

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
                CustomVertex firstVertex = model.getVertex(parseInt(first));
                CustomVertex secondVertex = model.getVertex(parseInt(second));
                Thread taskThread = new Thread(() -> {
                    try {
                        model.setOutputString( "\n Distinguishing Formula: " +
                                Algorithms.getDeltaFormula(firstVertex, secondVertex, model.getVertices())
                        );
                    } catch (NoDistinguishingFormulaException e) {
                        model.setOutputString(e.getMessage());
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

        String first = "";
        String second = "";

        do {
            TextInputDialog firstPrompt = new TextInputDialog();
            firstPrompt.setHeaderText("Which CustomVertex should be checked on formula satisfaction?");
            firstOK = firstPrompt.showAndWait();
            first = firstPrompt.getEditor().getText();
        } while (firstOK.isPresent() && (!StringUtils.notInteger(first) && model.getVertex(parseInt(first)) == null || StringUtils.notInteger(first) || first.isBlank() || first.isEmpty()));

        if (firstOK.isPresent()) {
            TreeNode formulaTree = null;
            do {
                TextInputDialog secondPrompt = new TextInputDialog();
                secondPrompt.setHeaderText("Which Formula should be evaluated?");
                secondOK = secondPrompt.showAndWait();
                second = secondPrompt.getEditor().getText();
                try {
                    formulaTree = FormulaParser.parse(second);
                } catch (SyntaxErrorException | NoMatchingTokenException e) {
                    System.out.println(e.getMessage());
                    formulaTree = null;
                }
            } while (secondOK.isPresent() && (formulaTree==null));

            if (secondOK.isPresent()) {
                final TreeNode finalTree = formulaTree;
                CustomVertex vertex = model.getVertex(parseInt(first));
                String formula = second;
                Thread taskThread = new Thread(() -> {
                    if (finalTree.evaluate(vertex)) {
                        model.setOutputString("CustomVertex " + vertex + " satisfies the formula: " + formula);
                    } else {
                        model.setOutputString("CustomVertex " + vertex + " doesn't satisfy the formula: " + formula);
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
