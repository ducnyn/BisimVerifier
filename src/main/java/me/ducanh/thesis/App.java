package me.ducanh.thesis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.ducanh.thesis.model.DataModel;
import me.ducanh.thesis.model.Edge;
import me.ducanh.thesis.model.Node;
import mvp.editor.EditorController;
import mvp.list.ListController;
import mvp.menu.MenuController;

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

        BorderPane rootBorderPane = new BorderPane();
        SplitPane splitPane = new SplitPane();
        rootBorderPane.setCenter(splitPane);
        splitPane.getItems().add(loadFXML("Editor"));

//        FXMLLoader listLoader = new FXMLLoader(App.class.getResource("/mvp/list/list.fxml"));
//        rootBorderPane.setCenter(listLoader.load());
//        ListController listController = listLoader.getController();
//
//
//        mvp.model.DataModel model = new mvp.model.DataModel();
//        listController.initModel(model);

        scene = new Scene(rootBorderPane, 900, 550);
        stage.setScene(scene);
        stage.setTitle("BisimVerifier");
        stage.show();



    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(View.EDITOR));
    }

    private static Parent loadFXML(View view) throws IOException {
        return new FXMLLoader(App.class.getResource(view.getFileName())).load();
    }

    public static void main(String[] args) {
        DataModel datamodel = new DataModel();
        setExampleGraph(datamodel);
        System.out.println(datamodel.toDot());
        launch();

    }














    //Creates a dummy graph
    private static void setExampleGraph(DataModel model){
        for (int i = 0; i < 5; i++) {
            Node node = new Node(Integer.toString(i));
            if (i % 2 == 1) {
                Edge edge = new Edge("a", model.getNodeList().get(i - 1), node);
                model.addEdge(edge);
            } else if (i != 0) {
                Edge edge = new Edge("b", model.getNodeList().get(i - 1), node);
                model.addEdge(edge);
            }
            model.addNode(node);
        }
//        Graph graph = new Graph(model.getNodes(), model.getEdges());
//        System.out.println(graph);
    }

}