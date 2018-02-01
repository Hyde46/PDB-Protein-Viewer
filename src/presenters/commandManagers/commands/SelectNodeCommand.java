package presenters.commandManagers.commands;

import models.AAcid;
import models.ANode;
import models.selectionModels.ASelectionModel;

import java.util.Arrays;

public class SelectNodeCommand extends ACommand{

    private final ASelectionModel<ANode> selectionModel;
    private final AAcid[] selectedAcid;
    private final ANode[] previousSelectedNodes;
    private boolean isShiftDown;

    public SelectNodeCommand(ASelectionModel<ANode> selectionModel,boolean isShiftDown, AAcid... acidSelected){
        setName("Select Acid");
        this.selectionModel = selectionModel;
        this.selectedAcid = acidSelected;
        this.isShiftDown = isShiftDown;
        previousSelectedNodes = selectionModel.getSelectedItems().toArray(new ANode[selectionModel.getSelectedItems().size()]);
        if(acidSelected==null)
            this.isShiftDown=false;
    }

    @Override
    public void execute() throws Exception {
        if(!isShiftDown) {
            selectionModel.clearSelection();
        }
        if(selectedAcid==null)
            return;
        try {
            Arrays.stream(selectedAcid).forEach(acid ->{
                acid.getBackbone().forEach(selectionModel::select);
                acid.getSideChain().forEach(selectionModel::select);
            });
        }catch (Exception ignored){}
    }

    @Override
    public void undo() throws Exception {
        selectionModel.clearSelection();
        Arrays.stream(previousSelectedNodes).forEach(selectionModel::select);
    }

    @Override
    public void redo() throws Exception {
        execute();
    }
}
