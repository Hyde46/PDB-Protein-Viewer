package presenters.commandManagers.commands;

import presenters.PDBPresenter;

public class ToggleShowListCommand extends ACommand {

    @SuppressWarnings("FieldCanBeLocal")
    private final double LIST_VIEW_WIDTH = 250.0;

    private final PDBPresenter presenter;

    public ToggleShowListCommand(PDBPresenter p){
        setName("Toggle PDB List");
        this.presenter = p;
    }
    @Override
    public void execute() throws Exception {
        boolean lastVisibility = presenter.getConnector().getListBox().isVisible();
        presenter.getConnector().getListBox().setPrefWidth(lastVisibility?0: LIST_VIEW_WIDTH);
        presenter.getConnector().getListBox().setVisible(!lastVisibility);
    }

    @Override
    public void undo() throws Exception {
        execute();
    }

    @Override
    public void redo() throws Exception {
        execute();
    }
}
