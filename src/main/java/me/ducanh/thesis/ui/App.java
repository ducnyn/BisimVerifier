package me.ducanh.thesis.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.ducanh.thesis.FXMLPATH;
import me.ducanh.thesis.Model;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    final private double absDivPos = 400;
    private double relDivPos;

    public static void main(String[] args) {

        launch();
    }

    static void setRoot(FXMLPATH FXMLPATH) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(FXMLPATH.getFileName()));
            scene.setRoot(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("FXMLPATH file " + FXMLPATH.getFileName() + " doesn't exist.");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        final String cssPath =
                Objects.requireNonNull(getClass().getResource("stylesheet.css"), "missing main stylesheet")
                        .toExternalForm();
        final Model model = new Model();

        VBox rootVBox = new VBox();
        HBox mainHBox = new HBox();
        SplitPane splitPane = new SplitPane();

        FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource(FXMLPATH.SIDEBAR.getFileName()));
        FXMLLoader textEditorLoader = new FXMLLoader(getClass().getResource(FXMLPATH.COMMANDLINE.getFileName()));
        FXMLLoader visEditorLoader = new FXMLLoader(getClass().getResource(FXMLPATH.CANVAS.getFileName()));
        Region visEditorView = visEditorLoader.load();


//    visEditorView.setPrefHeight(splitPane.getHeight());

        rootVBox.getChildren().addAll(mainHBox);
        mainHBox.getChildren().addAll(sidebarLoader.load(), splitPane);
        splitPane.getItems().add(textEditorLoader.load());
        splitPane.getItems().add(visEditorView);


//        editorOutputBox.getChildren().add(outputLoader.load());

        splitPane.prefWidthProperty().bind(mainHBox.widthProperty());
        visEditorView.prefHeightProperty().bind(splitPane.heightProperty());
        visEditorView.prefWidthProperty().bind(splitPane.widthProperty());

        SideBar sideBar = sidebarLoader.getController();
        InputOutput textEditor = textEditorLoader.getController();
        Canvas visEditor = visEditorLoader.getController();

        scene = new Scene(rootVBox, 1600, 900);
        scene.getStylesheets().add(cssPath);
        stage.setScene(scene);
        stage.setTitle("Graph Verifier");
        stage.setMinWidth(440);
        stage.show();
        textEditor.init(model);
        visEditor.initialize(model);
        sideBar.initialize(model);

        // These are to maintain split ratio while resizing windows

        relDivPos = absDivPos / splitPane.getWidth();
        splitPane.setDividerPositions(relDivPos);
        splitPane.getDividers().get(0).positionProperty().addListener((obs, oldV, newV) -> {
            relDivPos = newV.doubleValue() * splitPane.getWidth();
        });

        splitPane.widthProperty().addListener((observableV, oldV, newV) -> {
            double width = newV.doubleValue();
            System.out.println(splitPane.getWidth());
            splitPane.setDividerPosition(0, relDivPos / width); // splitPane.getWidth worked
            splitPane.getItems().get(1).prefWidth(((Double) newV - splitPane.getDividers().get(0).getPosition()));
        });


    }
}
