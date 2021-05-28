package me.ducanh.thesis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.ducanh.thesis.model.Model;


import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

//        scene = new Scene(loadFXML("Skeletton"), 900, 550);
//        scene.getStylesheets().add(App.class.getResource("stylesheet.css").toExternalForm());

        VBox rootVBox = new VBox();

        FXMLLoader menuLoader = createLoader(View.MENU);
        rootVBox.getChildren().add(menuLoader.load());
        MenuController menuController = menuLoader.getController();

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.35);
        rootVBox.getChildren().add(splitPane);

        FXMLLoader editorLoader = createLoader(View.EDITOR);
        splitPane.getItems().add(editorLoader.load());
        EditorController editorController = editorLoader.getController();

        FXMLLoader canvasLoader = createLoader(View.CANVAS);
        splitPane.getItems().add(canvasLoader.load());
        CanvasController canvasController = canvasLoader.getController();

        Model data = new Model();
        editorController.inject(data);

        scene = new Scene(rootVBox, 900, 550);
//        scene.getStylesheets();
        stage.setScene(scene);
        stage.setTitle("BisimVerifier");
        stage.show();



    }
    //TODO Don't just throw generic exception like this.
    static void setRoot(View view) throws Exception {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(view.getFileName()));
            scene.setRoot(fxmlLoader.load());
        }catch (Exception e) {
            System.out.println("FXML file " + view.getFileName() + " doesn't exist.");
        }


    }

    private FXMLLoader createLoader(View view)  {
        return new FXMLLoader(getClass().getResource(view.getFileName()));
    }

    public static void main(String[] args) {
           launch();
    }




}