package me.ducanh.thesis;

import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import me.ducanh.thesis.model.Block;
import me.ducanh.thesis.model.Model;
import me.ducanh.thesis.model.Algorithms;
import me.ducanh.thesis.util.StringCheck;

import java.util.*;

import static java.lang.Integer.parseInt;

public class SideBar {

private Model model;

public void initialize(Model model) {
    this.model = model;
}

@FXML
private void vertices() {
//    Optional<String> numberOK = null;
//    String number = "";
//
//    do {
//        TextInputDialog sourceDia = new TextInputDialog();
//        sourceDia.setHeaderText("Number of vertices to spawn:");
//        numberOK = sourceDia.showAndWait();
//        number = sourceDia.getEditor().getText();
//    } while (numberOK.isPresent() && (StringCheck.notInteger(number) || number.isBlank() || number.isEmpty()));
//
//    if(numberOK.isPresent()){
//        int finalNumber = parseInt(number);
//        Thread taskThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i = 0; i< finalNumber; i++){
//                    model.addVertex();
//                }
//            }
//        });
//        taskThread.setDaemon(true);
//        taskThread.start();
//    }
    model.addEdge(1,"a",2);
    model.addEdge(2,"b",3);
    model.addEdge(2,"c",4);

    model.addEdge(5,"a",6);

    model.addEdge(5,"a",7);

    model. addEdge(6,"b",8);
    model.addEdge(7,"c",8);
}

@FXML
private void edges() throws Exception {
//    TextInputDialog sourceDia = new TextInputDialog();
    Optional<String> sourceOK = null;
    Optional<String> labelOK = null;
    Optional<String> targetOK = null;

    String source = "";
    String label = "";
    String target = "";

    do {
        TextInputDialog sourceDia = new TextInputDialog();
        sourceDia.setHeaderText("source VisVertex (int):");
        sourceOK = sourceDia.showAndWait();
        source = sourceDia.getEditor().getText();
    } while (sourceOK.isPresent() && (StringCheck.notInteger(source) || source.isBlank() || source.isEmpty()));

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
        } while (targetOK.isPresent() && (StringCheck.notInteger(target) || target.isBlank() || target.isEmpty()));
    }


    if (targetOK != null && targetOK.isPresent()) {
        model.addEdge(parseInt(source), label, parseInt(target));
    }

}

@FXML
private void randomEdges() throws InterruptedException {
    model.removeAllEdges();
    ArrayList<Integer> vertices = new ArrayList<>(model.getVertices());
    for (Integer vertex: vertices) {
        String alphabet = "abc";

        Random random = new Random();
        int edgesOutDegree = random.nextInt(4);

        for (int j = 0; j < edgesOutDegree; j++) {
            String randomLabel = String.valueOf(alphabet.charAt(random.nextInt(alphabet.length())));
            int randomVertex = vertices.get(random.nextInt(vertices.size()-1));
            model.addEdge(vertex, randomLabel, randomVertex);
        }
    }
}

@FXML
private void bisimulation() throws InterruptedException {


    Thread taskThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Set<Block> partition = Algorithms.bisim(model).getValue();
            model.setOutputString("\n Equivalence classes (Bisimulation): \n" + partition.toString());
            model.updatePartition(partition);
            System.out.println("Vertices: " + model.getVertices());
            System.out.println("Edges: " + model.getFlatEdges());
            System.out.println(Thread.currentThread() + " should be backGroundThread");
            }
        });
    taskThread.setDaemon(true);
    taskThread.start();

}

@FXML
    private void deltaFormula(){
    Optional<String> firstOK = null;
    Optional<String> secondOK = null;

    String first = "";
    String second = "";

    do {
        TextInputDialog firstPrompt = new TextInputDialog();
        firstPrompt.setHeaderText("first vertex (int):");
        firstOK = firstPrompt.showAndWait();
        first = firstPrompt.getEditor().getText();
    } while (!StringCheck.notInteger(first) && !model.getVertices().contains(parseInt(first)) || firstOK.isPresent() && (StringCheck.notInteger(first) || first.isBlank() || first.isEmpty()));

    if (firstOK.isPresent()) {
        do {
            TextInputDialog secondPrompt = new TextInputDialog();
            secondPrompt.setHeaderText("second vertex (int):");
            secondOK = secondPrompt.showAndWait();
            second = secondPrompt.getEditor().getText();
        } while (!StringCheck.notInteger(second) && !model.getVertices().contains(parseInt(second)) ||secondOK.isPresent() && (StringCheck.notInteger(second) || second.isBlank() || second.isEmpty()));
    }


    if (secondOK != null && secondOK.isPresent()) {
        String finalFirst = first;
        String finalSecond = second;
        Thread taskThread = new Thread(() -> {
            String deltaFormula = Algorithms.getDeltaFormula(
                    parseInt(finalFirst),parseInt(finalSecond),model, Algorithms.bisim(model).getKey());
            model.setOutputString("\n Distinguishing Formula: " + deltaFormula);
        });
        taskThread.setDaemon(true);
        taskThread.start();
    }

}
}
