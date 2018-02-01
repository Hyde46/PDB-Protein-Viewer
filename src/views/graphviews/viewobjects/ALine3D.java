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
package views.graphviews.viewobjects;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class ALine3D  extends Group {

    private static final double DEFAULT_RADIUS = 6.0;
    private double cylinderRadius = 6.0;
    private PhongMaterial m;
    private final Cylinder cylinder;
    private final DoubleProperty startXProperty;
    private final DoubleProperty startYProperty;
    private final DoubleProperty startZProperty;
    private final DoubleProperty endXProperty;
    private final DoubleProperty endYProperty;
    private final DoubleProperty endZProperty;

    public ALine3D(DoubleProperty startXProperty, DoubleProperty startYProperty, DoubleProperty startZProperty,
                   DoubleProperty endXProperty, DoubleProperty endYProperty, DoubleProperty endZProperty){
        cylinder = new Cylinder();
        cylinder.setRadius(DEFAULT_RADIUS);
        m = new PhongMaterial();
        m.diffuseColorProperty().setValue(Color.LIGHTGRAY);
        m.specularColorProperty().setValue(Color.WHITE);
        cylinder.setMaterial(m);

        translateXProperty().bind(startXProperty);
        translateYProperty().bind(startYProperty);
        translateZProperty().bind(startZProperty);
        getChildren().add(cylinder);
        this.startXProperty = startXProperty;
        this.startYProperty = startYProperty;
        this.startZProperty = startZProperty;
        this.endXProperty = endXProperty;
        this.endYProperty = endYProperty;
        this.endZProperty = endZProperty;
        rotateCylinder();
        setListeners();
    }

    private void rotateCylinder(){
        Point3D desiredDirection = new Point3D(endXProperty.getValue()-startXProperty.getValue(),
                                            endYProperty.getValue() - startYProperty.getValue(),
                                            endZProperty.getValue() - startZProperty.getValue());
        double cylinderHeight = desiredDirection.magnitude();
        Point3D desiredDirectionNormalized = desiredDirection.normalize();
        //since we are in local space
        Point3D normal = new Point3D(0,1,0);
        Point3D rotationAxis = desiredDirectionNormalized.crossProduct(normal);
        //since we normalize our vectors, we dont need to use the magnitude of the vectors
        double deltaAngle  = Math.acos(normal.dotProduct(desiredDirectionNormalized))*180/Math.PI;///(normal.magnitude()*desiredDirectionNormalized.magnitude()));
        cylinder.rotationAxisProperty().setValue(rotationAxis);
        cylinder.rotateProperty().setValue(-deltaAngle);
        cylinder.setHeight(cylinderHeight);
        cylinder.setTranslateX(desiredDirection.getX() * 0.5f);
        cylinder.setTranslateY(desiredDirection.getY() * 0.5f);
        cylinder.setTranslateZ(desiredDirection.getZ() * 0.5f);
        //cylinder.scaleYProperty().setValue(cylinderHeight);
    }

    private void setListeners(){
        startXProperty.addListener((observable, oldValue, newValue) -> rotateCylinder());
        startYProperty.addListener((observable, oldValue, newValue) -> rotateCylinder());
        startZProperty.addListener((observable, oldValue, newValue) -> rotateCylinder());

        endXProperty.addListener((observable, oldValue, newValue) -> rotateCylinder());
        endYProperty.addListener((observable, oldValue, newValue) -> rotateCylinder());
        endZProperty.addListener((observable, oldValue, newValue) -> rotateCylinder());
    }

    public double getCylinderRadius(){
        return cylinderRadius;
    }

    public void scaleRadius(Double newRadiusScale){
        cylinder.setRadius(cylinder.getRadius() * newRadiusScale);
        cylinderRadius = cylinder.getRadius();
    }

    public void setRadiusScale(Double newRadius){
        cylinder.setRadius(newRadius*DEFAULT_RADIUS);
        cylinderRadius = cylinder.getRadius();

    }

    public void setColor(Color c){
        m.setDiffuseColor(c);
        m.setSpecularColor(c);
    }
}
