package me.ducanh.thesis;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import static javafx.scene.control.Alert.AlertType;

import me.ducanh.thesis.model.Graph;
import me.ducanh.thesis.model.Model;
import me.ducanh.thesis.util.BisimChecker;
import me.ducanh.thesis.util.StringCheck;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

public class SidebarController {

private Model model;

public void inject(Model model) {
    this.model = model;
}

@FXML
private void addNode() {
    TextInputDialog td = new TextInputDialog();
    td.setHeaderText("number of vertices to add");
    td.showAndWait();
    String  number = td.getEditor().getText();
    model.addVertices(Integer.parseInt(number));
}

@FXML
private void addEdge() throws Exception {
    TextInputDialog sourceDia = new TextInputDialog();
    TextInputDialog inputLabel = new TextInputDialog();
    TextInputDialog inputTarget = new TextInputDialog();
    Optional<String> sourceOK = null;
    Optional<String> labelOK = null;
    Optional<String> targetOK = null;

    String source = "";
    String label = "";
    String target = "";

    do{ sourceDia.setHeaderText("source Vertex (int):");
        sourceOK = sourceDia.showAndWait();
        source = sourceDia.getEditor().getText();
    }while(sourceOK.isPresent() && (!StringCheck.isInteger(source) || source.isBlank() || source.isEmpty()));

    if (sourceOK.isPresent()){
        do{
            inputLabel.setHeaderText("Label (String):");
            labelOK = inputLabel.showAndWait();
            label = inputLabel.getEditor().getText();
        } while(labelOK.isPresent() && (label.isBlank() || label.isEmpty()));
    } else {
        labelOK = Optional.empty();
    }
    if (labelOK.isPresent()){
        do {
            inputTarget.setHeaderText("target Vertex (int):");
            targetOK = inputTarget.showAndWait();
            target = inputTarget.getEditor().getText();
        } while(targetOK.isPresent() && (!StringCheck.isInteger(target) || target.isBlank() || target.isEmpty()));
    }


    if (targetOK != null && targetOK.isPresent()){
        model.addEdge(Integer.parseInt(source),label,Integer.parseInt(target));
    }












}

@FXML
private void init(){
    Graph graph = new Graph();
    int cap = 100;

    for(int i = 0; i<cap;i++){
        char randomChar;
        String alphabet = "abcd";

        int random3 = new Random().nextInt(5);

        for(int j = 0; j<random3;j++){
            Random random2 = new Random();

            randomChar = alphabet.charAt(random2.nextInt(alphabet.length()));
            int random = new Random().nextInt(cap-1);
            graph.addEdge(i,Character.toString(randomChar),random);
        }

        }

    model.setGraph(graph);

//    model.updateDotString(graph);
}
@FXML
private void part(){
    model.setAppendString("\n Grouped by bisimilar vertices: \n" + BisimChecker.bisim(new HashSet<>(model.getGraph().getVertices())));
}
}
