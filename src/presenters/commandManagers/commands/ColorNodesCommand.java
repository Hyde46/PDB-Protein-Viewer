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
