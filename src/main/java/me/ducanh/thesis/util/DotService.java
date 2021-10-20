package me.ducanh.thesis.util;

import me.ducanh.thesis.model.CustomEdge;
import me.ducanh.thesis.model.Model;

import java.util.Collection;

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
public static String write(Collection<Integer> vertices, Collection<CustomEdge> edges){



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


        for (CustomEdge edge : edges){
            graphString.append("\n\t");

                //        graphString
//                .append("\n\t")
//                .append(CustomEdge.getSource())
//                .append(" -> ")
//                .append(CustomEdge.getTarget())
//                .append("[label = ")
//                .append(CustomEdge.getLabel())
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
