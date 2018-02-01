package views.graphviews.structureviews;
/*
 This file is part of PDB-Protein-Viewer
 PDB-Protein-Viewer is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 PDB-Protein-Viewer is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with PDB-Protein-Viewer.  If not, see <http://www.gnu.org/licenses/>.
 */
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
