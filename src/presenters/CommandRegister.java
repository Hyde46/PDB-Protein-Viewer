package presenters;

import models.AAcid;
import presenters.commandManagers.CommandManager;
import presenters.commandManagers.commands.*;
import views.graphviews.AGraphView3D;

import java.util.List;

class CommandRegister {


    private final PDBPresenter presenter;
    private final CommandManager commandManager;

    CommandRegister(PDBPresenter presenter){
        this.presenter = presenter;
        commandManager = new CommandManager();
    }

    private void executeCommand(ACommand command){
        try {
            commandManager.executeAndAdd(command);
        } catch (Exception e) {
            e.printStackTrace();
            showFailedAlertMessage("Command",e.toString());
        }
    }

    public void undo(){
        try {
            commandManager.undo();
        } catch (Exception e) {
            showFailedAlertMessage("Undo",e.getMessage());
        }
    }

    public void redo(){
        try {
            commandManager.redo();
        } catch (Exception e) {
            showFailedAlertMessage("Redo",e.getMessage());
        }
    }

    void executeLoadFile(){
        executeCommand( new LoadFileCommand(presenter,null));
    }

    void executeLoadFileFromList(List<String> downloadedPDB) {
        executeCommand( new LoadFileCommand(presenter,downloadedPDB));
    }

    void executeMainAtomFormRenderMode(){
        executeCommand( new ChangeRenderModeCommand(presenter, AGraphView3D.FormRenderMode.Default));
    }

    void executeSpaceFillingFormRenderMode(){
        executeCommand( new ChangeRenderModeCommand(presenter, AGraphView3D.FormRenderMode.SpaceFilling));
    }

    void executeBackboneFormRenderMode(){
        executeCommand( new ChangeRenderModeCommand(presenter, AGraphView3D.FormRenderMode.Backbone));
    }

    void executeRibbonFormRenderMode(){
        executeCommand( new ChangeRenderModeCommand(presenter, AGraphView3D.FormRenderMode.Ribbon));
    }

    void executeCartoonFormRenderMode(){
        executeCommand( new ChangeRenderModeCommand(presenter, AGraphView3D.FormRenderMode.Cartoon));
    }

    void executeColorStructureMode(){
        executeCommand( new ColorNodesCommand(presenter, AGraphView3D.ColorRenderMode.Structure));
    }

    void executeColorTypeMode(){
        executeCommand( new ColorNodesCommand(presenter, AGraphView3D.ColorRenderMode.Type));
    }

    void executeShowList(){
        executeCommand(new ToggleShowListCommand(presenter));
    }
    void executeSelectAll(){
        executeSelectNode(false, presenter.getPeptide().getAminoAcids().toArray(new AAcid[presenter.getPeptide().getAminoAcids().size()]));
    }

    void executeSelectNode(boolean isShiftDown, AAcid... clickedAcids){
        executeCommand(new SelectNodeCommand(presenter.getSelectionModel(),isShiftDown,clickedAcids));
    }

    void executeSelectNone(){
        executeSelectNode(false, (AAcid) null);
    }

    CommandManager getCommandManager() {
        return commandManager;
    }

    private void showFailedAlertMessage(String title, String message){
        presenter.getUiHandler().showAlertMessage("Could not execute " +title,message);
    }

}
