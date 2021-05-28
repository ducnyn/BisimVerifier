package me.ducanh.thesis;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import me.ducanh.thesis.model.Model;
import me.ducanh.thesis.util.DummyGraph;


public class EditorController {

    @FXML
    private TextArea editorTextArea;


    private Model data;
    public void inject(Model data){
        this.data = data;
        DummyGraph.setExampleGraph(data);
        System.out.println(data.currentToDot());
        editorTextArea.setText(data.currentToDot());

        editorTextArea.setOnKeyTyped(a->{
            editorTextArea.appendText(a.getCharacter());
        });

    }

}





