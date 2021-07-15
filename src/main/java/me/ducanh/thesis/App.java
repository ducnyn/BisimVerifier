package me.ducanh.thesis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
  private double relDivPos;
  final private double absDivPos = 400;

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

    FXMLLoader menuLoader = new FXMLLoader(getClass().getResource(FXMLPATH.ALTMENU.getFileName()));
    FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource(FXMLPATH.SIDEBAR.getFileName()));
    FXMLLoader textEditorLoader = new FXMLLoader(getClass().getResource(FXMLPATH.TEXTEDITOR.getFileName()));
    FXMLLoader visEditorLoader = new FXMLLoader(getClass().getResource(FXMLPATH.VISEDITOR.getFileName()));

    Region visEditorView = visEditorLoader.load();


//    visEditorView.setPrefHeight(splitPane.getHeight());

    rootVBox.getChildren().addAll(menuLoader.load(), mainHBox);
    mainHBox.getChildren().addAll(sidebarLoader.load(), splitPane);
    splitPane.getItems().add(textEditorLoader.load());
    splitPane.getItems().add(visEditorView);

    splitPane.prefWidthProperty().bind(mainHBox.widthProperty());
    visEditorView.prefHeightProperty().bind(splitPane.heightProperty());
    visEditorView.prefWidthProperty().bind(splitPane.widthProperty());

    AltMenu altMenu = menuLoader.getController();
    SideBar sideBar = sidebarLoader.getController();
    TextEditor textEditor = textEditorLoader.getController();
    VisEditor visEditor = visEditorLoader.getController();

    scene = new Scene(rootVBox, 1600, 900);
    scene.getStylesheets().add(cssPath);
    stage.setScene(scene);
    stage.setTitle("Graph Theory Tools");
    stage.setMinWidth(440);
    stage.show();
    textEditor.intialize(model);
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
      splitPane.getItems().get(1).prefWidth(((Double) newV-splitPane.getDividers().get(0).getPosition()));
    });


  }
}
