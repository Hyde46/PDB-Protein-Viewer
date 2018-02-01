package presenters.commandManagers.commands;

import presenters.PDBPresenter;
import views.graphviews.AGraphView3D;

public class ColorNodesCommand extends ACommand{


    private final PDBPresenter presenter;
    private final AGraphView3D.ColorRenderMode renderMode;
    private AGraphView3D.ColorRenderMode oldRenderMode;

    public ColorNodesCommand(PDBPresenter presenter, AGraphView3D.ColorRenderMode renderMode){
        this.presenter = presenter;
        this.renderMode = renderMode;
        setName("Set Render Mode: "+renderMode);
    }
    @Override
    public void execute() throws Exception {
        if(presenter.getGraphModel().isEmptyProperty().getValue() ||
                presenter.getGraphView().getColorRenderMode() == renderMode)
            return;
        oldRenderMode = presenter.getGraphView().setColorRenderMode(renderMode);
        presenter.getUiHandler().colorViewNodes();
    }

    @Override
    public void undo() throws Exception {
        presenter.getGraphView().setColorRenderMode(oldRenderMode);
        presenter.getUiHandler().colorViewNodes();
    }

    @Override
    public void redo() throws Exception {
        presenter.getGraphView().setColorRenderMode(renderMode);
        presenter.getUiHandler().colorViewNodes();
    }
}
