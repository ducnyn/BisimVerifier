package me.ducanh.thesis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class TestApp {
    private static ObservableSet<Integer> intSet = FXCollections.observableSet(new TreeSet<Integer>());
    private static ObservableSet<Integer> int2Set = FXCollections.unmodifiableObservableSet(intSet);

public static void main(String[] args){
    System.out.println("[a]whatever".matches("^\\[[a-z]].*"));
//    int2Set.addListener((SetChangeListener<Integer>)s->{
//        if (s.wasAdded()){
//            System.out.println("this works: " + s.getElementAdded());
//        }
//    });
//
//    intSet.add(1);
//    int2Set.add(2);
}
//    private static final ArrayList<Integer> iList = new ArrayList<>();
//
//    public static void main(String[] args){
//        Integer[] a = {1,2,3,4,4};
//        iList.addAll(Arrays.asList(a));
//
//        ObservableList<Integer> obsList = FXCollections.observableList(iList);
//        int counter = 0;
//        while(counter < 19){
//            System.out.println(iList);
//            obsList.add(counter);
//            counter++;
//        }
//    }
}
