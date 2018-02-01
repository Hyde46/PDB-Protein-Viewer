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
package utils;

import java.util.ArrayList;

public class ArrayHelper {


    public static float[] toPrimitiveFloatArray(ArrayList<Float> list){
        float[] primitiveList = new float[list.size()];
        for(int i = 0; i < list.size();i++)
            primitiveList[i] = list.get(i);
        return primitiveList;
    }
}
