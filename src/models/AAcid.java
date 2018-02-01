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
