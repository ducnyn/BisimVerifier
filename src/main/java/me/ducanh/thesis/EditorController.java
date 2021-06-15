package me.ducanh.thesis;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import me.ducanh.thesis.model.Model;
import me.ducanh.thesis.util.DummyGraph;


public class EditorController {

    @FXML
    private TextArea editorTextArea;
    private Model data;

    public void inject(Model dataModel){
        this.data = dataModel;
//        DummyGraph.setExampleGraph(data);
        editorTextArea.setText(dataModel.currentToDot());

        editorTextArea.setOnKeyTyped(a->{
            editorTextArea.appendText(a.getCharacter());
        });

    }

}





