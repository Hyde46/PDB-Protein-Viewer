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
package views.utils;

import javafx.collections.ListChangeListener;
import models.AAcid;
import models.ANode;
import models.APeptide;
import models.selectionModels.ASelectionModel;
import views.graphviews.structureviews.AcidTextView;
import views.graphviews.structureviews.AcidTextViewContainer;

public class StructureSelectionHandler {

    private final ASelectionModel<ANode> selectionModel;

    public StructureSelectionHandler(ASelectionModel<ANode> selectionModel){
        this.selectionModel = selectionModel;
    }

    public void registerListener(APeptide peptide, AcidTextViewContainer container){
        this.selectionModel.getSelectedItems().addListener((ListChangeListener<ANode>) c -> {
            while (c.next()) {
                for (ANode node : c.getRemoved()) {
                    AAcid deselectedAcid = peptide.getAcidForNode(node);
                    if(deselectedAcid != null){
                        AcidTextView view = container.getAcidViewForId(deselectedAcid.getAcidId());
                        if(view!= null)
                            view.setCSSStyle(AcidTextView.deSelectedCSS);
                    }
                }
                for (ANode node : c.getAddedSubList()) {
                    AAcid selected = peptide.getAcidForNode(node);
                    if(selected != null){
                        AcidTextView view = container.getAcidViewForId(selected.getAcidId());
                        if(view!= null)
                            view.setCSSStyle(AcidTextView.selectedCSS);
                    }
                }
            }
        });
    }
}
