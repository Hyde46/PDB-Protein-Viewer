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
package models;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;

import java.util.NoSuchElementException;

@SuppressWarnings("unused")
public class ANode {

    private SimpleIntegerProperty id;
    private SimpleStringProperty atomSymbol;
    private SimpleStringProperty atomName;
    private SimpleDoubleProperty xCoordProperty;
    private SimpleDoubleProperty yCoordProperty;
    private SimpleDoubleProperty zCoordProperty;
    private SimpleStringProperty acidId;
    private SimpleStringProperty acidType;
    private Object userData;

    private ObservableList<AEdge> leavingEdges = FXCollections.observableArrayList();
    private ObservableList<AEdge> incomingEdges = FXCollections.observableArrayList();

    public ANode(int id, String atomName, String atom, Double xCoord, Double yCoord, Double zCoord) {
        this.id = new SimpleIntegerProperty(id);
        this.atomSymbol = new SimpleStringProperty(atomName);
        this.atomName = new SimpleStringProperty(atom);
        this.xCoordProperty = new SimpleDoubleProperty(xCoord);
        this.yCoordProperty = new SimpleDoubleProperty(yCoord);
        this.zCoordProperty = new SimpleDoubleProperty(zCoord);
    }
    public ANode(int id, String atomName, String atom, Point3D coords, String acidId, String acidType) {
        this.id = new SimpleIntegerProperty(id);
        this.atomSymbol = new SimpleStringProperty(atomName);
        this.atomName = new SimpleStringProperty(atom);
        this.xCoordProperty = new SimpleDoubleProperty(coords.getX());
        this.yCoordProperty = new SimpleDoubleProperty(coords.getY());
        this.zCoordProperty = new SimpleDoubleProperty(coords.getZ());
        this.acidId  = new SimpleStringProperty(acidId);
        this.acidType = new SimpleStringProperty(acidType);
    }
    public ANode(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    void removeIncomingEdges(AEdge... toRemove) {
        incomingEdges.removeAll(toRemove);
    }

    void removeLeavingEdges(AEdge... toRemove) {
        leavingEdges.removeAll(toRemove);
    }

    void insertIncomingEdges(AEdge... toAdd) {
        incomingEdges.addAll(toAdd);
    }

    void insertLeavingEdges(AEdge... toAdd) {
        leavingEdges.addAll(toAdd);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public ObservableList<ANode> getNeighbors() {
        ObservableList<ANode> neighbors = FXCollections.observableArrayList();
        try {
            leavingEdges.forEach(lE -> neighbors.add(lE.getTargetNode()));
            incomingEdges.forEach(lE -> neighbors.add(lE.getSourceNode()));
        } catch (NoSuchElementException ignored) {

        }
        return neighbors;
    }

    public double getzCoord() {
        return zCoordProperty.get();
    }

    public SimpleDoubleProperty zCoordPropertyProperty() {
        return zCoordProperty;
    }

    public double getyCoord() {
        return yCoordProperty.get();
    }

    public SimpleDoubleProperty yCoordPropertyProperty() {
        return yCoordProperty;
    }

    public double getxCoord() {
        return xCoordProperty.get();
    }

    public SimpleDoubleProperty xCoordPropertyProperty() {
        return xCoordProperty;
    }

    public String getAtomName() {
        return atomName.get();
    }

    public SimpleStringProperty atomNameProperty() {
        return atomName;
    }

    public String getAtomSymbol() {
        return atomSymbol.get();
    }

    public SimpleStringProperty atomSymbolProperty() {
        return atomSymbol;
    }

    public String getAcidId() {
        return acidId.get();
    }

    public SimpleStringProperty acidIdProperty() {
        return acidId;
    }

    public String getAcidType() {
        return acidType.get();
    }

    public SimpleStringProperty acidTypeProperty() {
        return acidType;
    }
}
