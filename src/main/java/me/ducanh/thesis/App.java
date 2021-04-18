package me.ducanh.thesis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.ducanh.thesis.model.DataModel;
import me.ducanh.thesis.model.Edge;
import me.ducanh.thesis.model.Graph;
import me.ducanh.thesis.model.Node;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("Skeletton"), 900, 550);
        scene.getStylesheets().add(App.class.getResource("stylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        DataModel datamodel = new DataModel();
        setExampleGraph(datamodel);
        System.out.println(datamodel.toDot());
        launch();

    }

    private static void setExampleGraph(DataModel model){
        for (int i = 0; i < 5; i++) {
            Node node = new Node(Integer.toString(i));
            if (i % 2 == 1) {
                Edge edge = new Edge("a", model.getNodes().get(i - 1), node);
                model.addEdge(edge);
            } else if (i != 0) {
                Edge edge = new Edge("b", model.getNodes().get(i - 1), node);
                model.addEdge(edge);
            }
            model.addNode(node);
        }
//        Graph graph = new Graph(model.getNodes(), model.getEdges());
//        System.out.println(graph);
    }

}