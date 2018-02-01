package views.graphviews.structureviews;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class AcidTextViewContainer extends HBox {

    public AcidTextView addAcidTextView(String primary, String secondary, String acidID){
        AcidTextView toAdd = new AcidTextView(primary,secondary,acidID);
        if(getChildren().add(toAdd))
            return toAdd;
        else
            return null;
    }

    public void resetBindings(){
        for(Node node : getChildren()){
            node.onMouseClickedProperty().unbind();
        }
    }
    public AcidTextView getAcidViewForId(String acidId){
        for(Node n : getChildren()){
            AcidTextView acidView = (AcidTextView)n;
            if(acidView.getAcidId().equals(acidId))
                return acidView;
        }
        return null;
    }
}
