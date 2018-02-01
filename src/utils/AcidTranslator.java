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

import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AcidTranslator {

    private static final String[] acidTypes = {"ALA","ARG", "ASN", "ASP", "CYS", "GLN", "GLU", "GLY", "HIS", "ILE",
            "LEU", "LYS", "MET", "PHE", "PRO", "SER", "THR", "TRP", "TYR", "VAL", "STP"};


    public static List<String> getAcidTypeAsList(){
        return Arrays.asList(acidTypes);
    }

    public static String getOneLetterCode(String resName) {
        switch (resName) {
            case "ALA":
                return "A";
            case "ARG":
                return "R";
            case "ASN":
                return "N";
            case "ASP":
                return "D";
            case "CYS":
                return "C";
            case "GLN":
                return "Q";
            case "GLU":
                return "E";
            case "GLY":
                return "G";
            case "HIS":
                return "H";
            case "ILE":
                return "I";
            case "LEU":
                return "L";
            case "LYS":
                return "K";
            case "MET":
                return "M";
            case "PHE":
                return "F";
            case "PRO":
                return "P";
            case "SER":
                return "S";
            case "THR":
                return "T";
            case "TRP":
                return "W";
            case "TYR":
                return "Y";
            case "VAL":
                return "V";
            case "STP":
                return "*";
            default:
                return "X";
        }
    }

    public static Color getAminoColorByType(String type){
        type = type.toUpperCase();
        switch (type) {
            case "K":
            case "R":
                return Color.rgb( 20, 90,255);
            case "Q":
            case "N":
                return Color.rgb( 0,220,220);
            case "D":
            case "E":
                return Color.rgb(230,230, 10);
            case "C":
            case "M":
                return Color.rgb(230,230,  0);
            case "G":
                return Color.rgb(235,235,235);
            case "H":
                return Color.rgb(130,130,210);
            case "V":
            case "L":
            case "I":
                return Color.rgb( 15,130, 15);
            case "F":
            case "Y":
                return Color.rgb( 50, 50,170);
            case "P":
                return Color.rgb(220,150,130);
            case "T":
            case "S":
                return Color.rgb(250,150,  0);
            case "W":
                return Color.rgb(180, 90,180);
            case "A":
                return Color.rgb( 200, 200, 200);
            case "*":
                return Color.rgb(190,160,110);
            default:
                return Color.rgb(190,160,110);
        }
    }

    public static ArrayList<Pair<String,String>> getSideChainConnections(String type){
        switch (type) {
            case "A":
                return  new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB")));
            case "R":
                return  new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "CD"), new Pair<>("CD", "NE"),new Pair<>("NE", "CZ"),new Pair<>("CZ", "NH1"),
                        new Pair<>("CZ", "NH2")))   ;
            case "N":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "OD1"), new Pair<>("CG", "ND2")));
            case "D":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"),new Pair<>("CG", "OD1"),new Pair<>("CG", "OD2")));
            case "C":
                return  new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "SG")));
            case "Q":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "CD"), new Pair<>("CD", "OE1"), new Pair<>("CD", "NE2")));
            case "E":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "CD"), new Pair<>("CD", "OE1"), new Pair<>("CD", "OE2")));
            case "G":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O")));
            case "H":
                return  new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "ND1"), new Pair<>("CG", "CD2"), new Pair<>("ND1", "CE1"),
                        new Pair<>("CD2", "NE2"), new Pair<>("CE1", "NE2")
                ));
            case "I":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG1"), new Pair<>("CB", "CG2"), new Pair<>("CG1", "CD1")));
            case "L":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "CD1"), new Pair<>("CG", "CD2")));
            case "K":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "CD"), new Pair<>("CD", "CE"), new Pair<>("CE", "NZ")));
            case "M":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "SD"), new Pair<>("SD", "CE")));
            case "F":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "CD1"), new Pair<>("CG", "CD2"), new Pair<>("CD1", "CE1"),
                        new Pair<>("CD2", "CE2"), new Pair<>("CE1", "CZ"), new Pair<>("CE2", "CZ")));
            case "P":
                return  new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"),new Pair<>("CG", "CD"),new Pair<>("CD", "N")));
            case "S":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"), new Pair<>("CB", "OG")));
            case "T":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "OG1"), new Pair<>("CB", "CG2")));
            case "W":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "CD1"), new Pair<>("CG", "CD2"), new Pair<>("CD1", "NE1"),
                        new Pair<>("CD2", "CE2"), new Pair<>("NE1", "CE2"), new Pair<>("CD2", "CE3"), new Pair<>("CE2", "CZ2"),
                        new Pair<>("CE3", "CZ3"), new Pair<>("CZ2", "CH2"), new Pair<>("CZ3", "CH2")));
            case "Y":
                return  new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG"), new Pair<>("CG", "CD1"), new Pair<>("CG", "CD2"), new Pair<>("CD1", "CE1"),
                        new Pair<>("CD2", "CE2"), new Pair<>("CE1", "CZ"), new Pair<>("CE2", "CZ"), new Pair<>("CZ", "OH")));
            case "V":
                return new ArrayList<>(Arrays.asList(new Pair<>("N", "CA"), new Pair<>("CA", "C"), new Pair<>("C", "O"), new Pair<>("CA", "CB"),
                        new Pair<>("CB", "CG1"), new Pair<>("CB", "CG2")));
            case "*":
                return new ArrayList<>();
            default:
                return new ArrayList<>();
        }
    }

}
