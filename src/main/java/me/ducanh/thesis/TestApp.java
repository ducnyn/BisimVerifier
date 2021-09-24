package me.ducanh.thesis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import me.ducanh.thesis.formula.FormulaLexer;

import java.util.TreeSet;

public class TestApp {
    private static ObservableSet<Integer> intSet = FXCollections.observableSet(new TreeSet<>());
    private static ObservableSet<Integer> int2Set = FXCollections.unmodifiableObservableSet(intSet);

public static void main(String[] args){

    System.out.println(FormulaLexer.generateTokenList("(<asadat>)|[b]ff"));

//    Set<Character> whiteSpace = Set.of('\t', ' ', '\n');
//    String texte = "2 1  3   242 2";
//    CharacterIterator iter = new StringCharacterIterator(texte);
//
//
//    while(iter.current()!=CharacterIterator.DONE){
//        if (whiteSpace.contains(iter.current())){
//            iter.next();
//        } else {
//
//            System.out.print(iter.current());
//            iter.next();
//        }
//
//    }
//


//    System.out.println("[a]whatever".matches("^\\[[a-z]].*"));
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
