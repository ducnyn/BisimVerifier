package me.ducanh.thesis.util;

import javafx.collections.ObservableSet;
import me.ducanh.thesis.model.Edge;
import me.ducanh.thesis.model.Model;

import java.util.Collection;
import java.util.List;

public class DotService {
private Model model;

public static void parse(String string) {

}
//public StringProperty getDotStringProperty() {
//    return dotString;
//}
//
//public final String getDotString() {
//    return dotString.get();
//}
public static String write(Collection<Integer> vertices, Collection<Edge> edges){



        StringBuilder graphString = new StringBuilder();
        graphString
                .append("digraph {\n\t");

        int count = 0;
        for (Integer vertex : vertices) {
            if (count % 6 == 0) {
                graphString.append("\n")
                        .append("\t");

            }
            count++;
            graphString
                    .append(vertex)
                    .append("; ");

        }
        graphString.append("\n");


        for (Edge edge : edges){
            graphString.append("\n\t");

                //        graphString
//                .append("\n\t")
//                .append(Edge.getSource())
//                .append(" -> ")
//                .append(Edge.getTarget())
//                .append("[label = ")
//                .append(Edge.getLabel())
//                .append("]");
                graphString
                        .append("\n\t")
                        .append(edge.getSource())
                        .append(" -").append(edge.getLabel()).append("> ")
                        .append(edge.getTarget());

        }



        graphString.append("\n}");
        return graphString.toString();


}
private Model Program() {
    return this.model;
}
}
