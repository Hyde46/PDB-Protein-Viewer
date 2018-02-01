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
package views.utils.meshes;

import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
public class AcidTriangleMesh extends TriangleMesh {

    private final ArrayList<Integer> alternateFaces = new ArrayList<>();

    public AcidTriangleMesh(){
        super();
    }

    public ArrayList<Integer> getAlternateFaces(){
        return alternateFaces;
    }

    public void toggleFaces() {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (int i = 0; i < getFaces().size(); ++i)
            tmp.add(getFaces().get(i));
        getFaces().clear();
        for(Integer i : alternateFaces)
            getFaces().addAll(i);
        alternateFaces.clear();
        alternateFaces.addAll(tmp);
    }
}
