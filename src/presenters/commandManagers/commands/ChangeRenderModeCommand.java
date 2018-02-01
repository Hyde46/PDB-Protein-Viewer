package presenters.commandManagers.commands;

import presenters.PDBPresenter;
import views.graphviews.AGraphView3D;

public class ChangeRenderModeCommand extends ACommand {

    private final PDBPresenter presenter;
    private final AGraphView3D.FormRenderMode renderMode;
    private AGraphView3D.FormRenderMode oldMode;

    public ChangeRenderModeCommand(PDBPresenter presenter, AGraphView3D.FormRenderMode newMode){
        this.presenter = presenter;
        this.renderMode = newMode;
        setName("Set Render Mode "+renderMode);
    }

    @Override
    public void execute() throws Exception {
        if(presenter.getGraphModel().isEmptyProperty().getValue() ||
                presenter.getGraphView().getFormRenderMode() == renderMode)
            return;
        oldMode = presenter.getGraphView().setFormRenderMode(renderMode);
    }

    @Override
    public void undo() throws Exception {
        presenter.getGraphView().setFormRenderMode(oldMode);
    }

    @Override
    public void redo() throws Exception {
        presenter.getGraphView().setFormRenderMode(renderMode);

    }
}
