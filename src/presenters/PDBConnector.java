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
package presenters;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class PDBConnector implements Initializable{

    @FXML
    private ToggleGroup structure;
    @FXML
    private ToggleGroup color;
    @FXML
    private ToolBar toolBar;
    @FXML
    private VBox listBox;

    @FXML
    private Pane topPane;
    @FXML
    private Pane botPane;
    @FXML
    private Pane centerPane;

    @FXML
    private StackPane tertiaryViewPane;

    @FXML
    private ListView listView;

    @FXML
    private ImageView logoView;
    @FXML
    private ScrollPane structurePane;
    @FXML
    private TextField searchField;
    @FXML
    private TextField acidNameField;
    @FXML
    private TextField atomCountField;
    @FXML
    private TextField acidCountField;
    @FXML
    private TextField coloringField;
    @FXML
    private TextField formField;

    @FXML
    private MenuItem loadFile;
    @FXML
    private MenuItem showList;
    @FXML
    private MenuItem undoButton;
    @FXML
    private MenuItem redoButton;
    @FXML
    private MenuItem closeButton;
    @FXML
    private MenuItem selectAllButton;
    @FXML
    private MenuItem selectNoneButton;
    @FXML
    private MenuItem autoRotateButton;
    @FXML
    private RadioMenuItem typeColorButton;
    @FXML
    private RadioMenuItem structureColorButton;
    @FXML
    private RadioMenuItem mainFormButton;
    @FXML
    private RadioMenuItem spaceFormButton;
    @FXML
    private RadioMenuItem backboneFormButton;
    @FXML
    private RadioMenuItem ribbonFormButton;
    @FXML
    private RadioMenuItem cartoonFormButton;
    @FXML
    private MenuItem showChartsItem;
    @FXML
    private MenuItem bgColorButton;
    @FXML
    private Button loadButton;
    @FXML
    private Button loadOnlineButton;
    @FXML
    private Button showChartsButton;
    @FXML
    private Button resetButton;

    @FXML
    private RadioButton structureTypeButton;
    @FXML
    private RadioButton acidTypeButton;
    @FXML
    private RadioButton mainAtomButton;
    @FXML
    private RadioButton spaceFillingButton;
    @FXML
    private RadioButton backBoneButton;
    @FXML
    private RadioButton ribbonButton;
    @FXML
    private RadioButton cartoonButton;

    @FXML
    private Slider nodeSizeSlider;
    @FXML
    private Slider bondSizeSlider;

    @FXML
    private CheckBox autoTransformCheckBox;

    Pane getBotPane() {
        return botPane;
    }

    Pane getTopPane() {
        return topPane;
    }

    ListView getListView() {
        return listView;
    }

    ImageView getLogoView(){ return logoView;}

    MenuItem getLoadFile() {
        return loadFile;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    CheckBox getAutoTransformCheckBox() { return autoTransformCheckBox; }

    MenuItem getShowList() {
        return showList;
    }

    Button getLoadOnlineButton() {
        return loadOnlineButton;
    }

    Button getLoadButton() {
        return loadButton;
    }

    MenuItem getRedoButton() {
        return redoButton;
    }

    MenuItem getUndoButton() {
        return undoButton;
    }

    MenuItem getCloseButton() {
        return closeButton;
    }

    MenuItem getSelectAllButton() {
        return selectAllButton;
    }

    MenuItem getSelectNoneButton() {
        return selectNoneButton;
    }

    MenuItem getAutoRotateButton() {
        return autoRotateButton;
    }

    TextField getSearchField() {
        return searchField;
    }

    public VBox getListBox() {
        return listBox;
    }

    RadioMenuItem getTypeColorButton() {
        return typeColorButton;
    }

    RadioMenuItem getStructureColorButton() {
        return structureColorButton;
    }
    RadioButton getStructureTypeButton(){ return structureTypeButton;}

    RadioButton getAcidTypeButton(){
         return acidTypeButton;
    }

    RadioButton getMainAtomButton(){ return mainAtomButton; }

    RadioButton getSpaceFillingButton(){ return spaceFillingButton; }

    RadioButton getBackBoneButton(){ return backBoneButton; }

    RadioButton getRibbonButton(){ return ribbonButton; }

    RadioButton getCartoonButton(){ return cartoonButton; }

    RadioMenuItem getMainFormButton() {
        return mainFormButton;
    }

    RadioMenuItem getSpaceFormButton() {
        return spaceFormButton;
    }

    RadioMenuItem getBackboneFormButton() {
        return backboneFormButton;
    }

    RadioMenuItem getRibbonFormButton() {
        return ribbonFormButton;
    }

    Button getShowChartsButton() {
        return showChartsButton;
    }

    MenuItem getShowChartsItem() {
        return showChartsItem;
    }

    ScrollPane getStructurePane() {
        return structurePane;
    }

    MenuItem getBgColorButton() {
        return bgColorButton;
    }

    RadioMenuItem getCartoonFormButton() {
        return cartoonFormButton;
    }

    Slider getNodeSizeSlider() {
        return nodeSizeSlider;
    }

    Slider getBondSizeSlider() {
        return bondSizeSlider;
    }

    public TextField getAcidNameField() {
        return acidNameField;
    }

    TextField getAtomCountField() {
        return atomCountField;
    }

    public TextField getAcidCountField() {
        return acidCountField;
    }

    TextField getColoringField() {
        return coloringField;
    }

    public void setColoringField(TextField coloringField) {
        this.coloringField = coloringField;
    }

    TextField getFormField() {
        return formField;
    }

    public Pane getCenterPane() {
        return centerPane;
    }

    public StackPane getTertiaryViewPane() {
        return tertiaryViewPane;
    }

    Button getResetButton(){ return  resetButton;}

}
