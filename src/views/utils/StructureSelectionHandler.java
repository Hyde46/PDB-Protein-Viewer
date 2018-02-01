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
