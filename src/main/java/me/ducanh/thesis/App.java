package me.ducanh.thesis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.ducanh.thesis.model.Model;


import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        final String cssPath = Objects.requireNonNull(getClass().getResource("stylesheet.css"),"missing main stylesheet").toExternalForm();
        final Model model = new Model();

        VBox rootVBox = new VBox();
        HBox mainHBox = new HBox();
        SplitPane splitPane = new SplitPane();

        FXMLLoader menuLoader = createLoader(View.MENU);
        FXMLLoader sidebarLoader = createLoader(View.SIDEBAR);
        FXMLLoader editorLoader = createLoader(View.EDITOR);
        FXMLLoader canvasLoader = createLoader(View.CANVAS);

        rootVBox.getChildren().add(menuLoader.load());
        rootVBox.getChildren().add(mainHBox);
        mainHBox.getChildren().add(sidebarLoader.load());
        mainHBox.getChildren().add(splitPane);
        splitPane.getItems().add(editorLoader.load());
        splitPane.getItems().add(canvasLoader.load());

        MenuController menuController = menuLoader.getController();
        SidebarController sideBarController = sidebarLoader.getController();
        EditorController editorController = editorLoader.getController();
        CanvasController canvasController = canvasLoader.getController();

        splitPane.setDividerPositions(0.35);

        editorController.inject(model);
        canvasController.inject(model);
        sideBarController.inject(model);

        scene = new Scene(rootVBox, 900, 550);
        scene.getStylesheets().add(cssPath);
        stage.setScene(scene);
        stage.setTitle("Bisimulation Check");
        stage.show();

    }
    static void setRoot(View view) {
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