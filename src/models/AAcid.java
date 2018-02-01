package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AAcid {

    private final SimpleStringProperty acidId;
    private final SimpleStringProperty acidName;

    private final ObservableList<ANode> backbone = FXCollections.observableArrayList();
    private final ObservableList<ANode> sideChain = FXCollections.observableArrayList();

    AAcid(String acidID, String acidName){
        this.acidId = new SimpleStringProperty(acidID);
        this.acidName = new SimpleStringProperty(acidName);
    }

    void addNodesToBackbone(ANode... nodes){
        this.backbone.addAll(nodes);
    }

    void addNodesToSideChain(ANode... nodes){ this.sideChain.addAll(nodes);}

    public String getAcidName() {
        return acidName.get();
    }

    public String getAcidId() {
        return acidId.get();
    }

    public ObservableList<ANode> getBackbone() {
        return backbone;
    }

    public ObservableList<ANode> getSideChain() {
        return sideChain;
    }

    @SuppressWarnings("unused")
    public SimpleStringProperty acidIdProperty() {
        return acidId;
    }

    @SuppressWarnings("unused")
    public SimpleStringProperty acidNameProperty() {
        return acidName;
    }

}
