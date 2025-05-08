package me.ducanh.thesis;

public class Controller {
    Model model;
    public Controller(Model model) {
        this.model = model;
    }

    public void addVertex(Integer vLabel){
        model.addVertex(new Vertex(vLabel));
    }
    public void addVertex(Integer vLabel, double posX, double posY){
        model.addVertex(new Vertex(vLabel, posX, posY));
    }
    public void addVertex(){
        Integer vLabel = model.smallestFreeLabel();
        model.addVertex(new Vertex(vLabel));
    }
    public void addVertex(double posX, double posY){
        Integer vLabel = model.smallestFreeLabel();
        model.addVertex(new Vertex(vLabel, posX, posY));
    }

    public void removeVertex(Integer id) {
        model.removeVertex(new Vertex(id));
    }
    public void requestPrint(String string){
        model.requestPrint(string);
    }

    public void addEdge(int source, String label, int target) {
        model.addEdge(new Vertex(source), label, new Vertex(target));
    }
    public void clear(){
        model.clear();
    }
}
