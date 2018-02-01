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

import models.AGraph;
import models.APeptide;
import presenters.PDBPresenter;

import java.io.File;
import java.util.List;

public class LoadFileCommand extends ACommand{

    private final PDBPresenter presenter;
    private List<String> pdbLoadedList;

    public LoadFileCommand(PDBPresenter p, List<String> pdbLoadedList){
        this.setName("Load File");
        this.presenter = p;
        if(pdbLoadedList != null)
            this.pdbLoadedList = pdbLoadedList;
    }

    @Override
    public void execute() throws Exception {
        if(pdbLoadedList == null) {
            File file = presenter.getPdbFileChooser().showOpenDialog(presenter.getPrimaryStage());
            if (file != null) {
                APeptide pep;
                try {
                    pep = presenter.getGraphModel().readPDB(file.getPath());
                   loadPeptide(pep);
                } catch (AGraph.InvalidAGraphException err) {
                    handleInvalidPDBFile();
                }
            }
        }else{
            APeptide pep;
            try {
                pep = presenter.getGraphModel().readPDBFromList(pdbLoadedList);
                loadPeptide(pep);
            } catch (AGraph.InvalidAGraphException err) {
                handleInvalidPDBFile();
            }
        }
    }

    @Override
    public void undo() throws Exception {

    }

    @Override
    public void redo() throws Exception {

    }

    @Override
    public boolean isUndoable(){ return false; }

    @Override
    public boolean isRedoable(){ return false; }

    private void loadPeptide(APeptide pep){
        if (pep != null)
            presenter.setPeptide(pep);
        presenter.getUiHandler().setupTooltips();
        presenter.getGraphView().setFormRenderMode(presenter.getGraphView().getFormRenderMode());
    }

    private void handleInvalidPDBFile() {
        presenter.getUiHandler().showAlertMessage("Invalid PDB File", "PDB File not Valid");
        presenter.getGraphModel().resetGraph();
    }

}
