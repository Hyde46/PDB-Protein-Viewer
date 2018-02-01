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

import javafx.animation.Transition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Point3D;
import javafx.util.Duration;
import views.graphviews.AGraphView3D;

public class MyRotateTransition extends Transition {

    private final static String TURN_OFF_DISPLAY = "Disable Automatic Rotation";
    private final static String TURN_ON_DISPLAY = "Enable Automatic Rotation";

    private final AGraphView3D toRotate;

    private final SimpleBooleanProperty isAutoRotatingProperty;
    private final SimpleStringProperty toggleDescription;

    public MyRotateTransition(AGraphView3D toRotate,Duration d){
        isAutoRotatingProperty = new SimpleBooleanProperty(true);
        toggleDescription = new SimpleStringProperty(TURN_OFF_DISPLAY);
        setCycleDuration(d);
        this.toRotate = toRotate;
    }

    @Override
    protected void interpolate(double frac) {
        if(isAutoRotatingProperty.getValue())
            SuperMath.rotateGraphElement(toRotate,0.4,new Point3D(0.0,1.0,0.0));
    }

    public void toggleAutoRotate(){
        isAutoRotatingProperty.setValue(!isAutoRotatingProperty.getValue());
        toggleDescription.setValue(isAutoRotatingProperty.getValue()?TURN_OFF_DISPLAY:TURN_ON_DISPLAY);
    }

    public SimpleStringProperty toggleDescriptionProperty() {
        return toggleDescription;
    }

    public SimpleBooleanProperty isAutoRotatingProperty() {
        return isAutoRotatingProperty;
    }
}
