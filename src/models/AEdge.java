package models;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

@SuppressWarnings("unused")
public class AEdge {

    private static int idCounter = 0;

    private int id;
    private ANode sourceNode;
    private ANode targetNode;
    private SimpleStringProperty text;
    private SimpleFloatProperty weight;
    private Object userData;

    AEdge(ANode src, ANode trgt, String text) {
        this.id = ++idCounter;
        sourceNode = src;
        targetNode = trgt;
        this.text = new SimpleStringProperty(text);
        this.weight = new SimpleFloatProperty(0.0f);
        this.userData = new SimpleObjectProperty();
    }

    public ANode getSourceNode() {
        return sourceNode;
    }

    public ANode getTargetNode() {
        return targetNode;
    }

    String getText() {
        return text.get();
    }

    public int getId(){ return id; }

    public Object userDataProperty() {
        return userData;
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public SimpleStringProperty textProperty() {
        return text;
    }

    public SimpleFloatProperty weightProperty() {
        return weight;
    }
}
