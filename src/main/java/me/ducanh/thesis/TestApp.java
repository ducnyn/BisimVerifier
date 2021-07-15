package me.ducanh.thesis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;

public class TestApp {
    private static final ArrayList<Integer> iList = new ArrayList<>();

    public static void main(String[] args){
        Integer[] a = {1,2,3,4,4};
        iList.addAll(Arrays.asList(a));

        ObservableList<Integer> obsList = FXCollections.observableList(iList);
        int counter = 0;
        while(counter < 19){
            System.out.println(iList);
            obsList.add(counter);
            counter++;
        }
    }
}
