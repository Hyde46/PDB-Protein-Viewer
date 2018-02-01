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
package views.graphviews.structureviews;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import utils.FontLoader;

import java.io.FileNotFoundException;

public class AcidTextView extends Group {

    private final Label structureLabel;
    private final String acidId;
    public static final String deSelectedCSS = "-fx-border-color: black;\n"
            + "-fx-border-insets: 1;\n"
            + "-fx-border-width: 1;\n"
            + "-fx-background-color:#FFFFFF;\n"
            + "-fx-padding:5;";
    public static final String selectedCSS = "-fx-border-color: black;\n"
            + "-fx-border-insets: 1;\n"
            + "-fx-border-width: 1;\n"
            + "-fx-background-color:#A0522D;\n"
            + "-fx-padding:5;";

    private static Font customFont;

    AcidTextView(String primary, String secondary, String id){
        if(customFont == null)
            loadCustomFont();
        structureLabel = new Label(primary+"\n"+secondary);
        structureLabel.setAlignment(Pos.CENTER);
        if(customFont != null){

            structureLabel.setFont(customFont);
            this.prefWidth(customFont.getSize());
        }
        structureLabel.setStyle(deSelectedCSS);
        this.acidId = id;
        super.getChildren().add(structureLabel);
    }

    public String getAcidId() {
        return acidId;
    }

    private static void loadCustomFont(){
        customFont = FontLoader.loadCustomFont("./res/fonts/Mechanical.otf", 15);
    }

    public void setCSSStyle(String style){
        if(structureLabel != null)
            structureLabel.setStyle(style);
    }
}
