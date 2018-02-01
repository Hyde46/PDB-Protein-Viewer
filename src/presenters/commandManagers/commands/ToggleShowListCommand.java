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
