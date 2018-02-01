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
package views.graphviews;

import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;

public class ANodeView3D extends Group {

    private static final float SPHERE_RADIUS = 10.0f;


    private final int id;
    private Tooltip t;
    private final Sphere shape;
    private final PhongMaterial material;
    private final DoubleProperty currentScale;
    private final String atomSymbol;
    private boolean isBackBone;

    ANodeView3D(@NamedArg("id") int id,
                @NamedArg("x") Double x,
                @NamedArg("y") Double y,
                @NamedArg("z") Double z,
                String atomSymbol){
       this.id = id;
        this.atomSymbol = atomSymbol;
        shape = new Sphere();
        shape.setRadius(SPHERE_RADIUS);
        shape.setDrawMode(DrawMode.FILL);
        material = new PhongMaterial();
        material.setDiffuseColor(Color.LIMEGREEN);
        material.setSpecularColor(Color.LIMEGREEN);
        shape.setMaterial(material);
        translateXProperty().setValue(x);
        translateYProperty().setValue(y);
        translateZProperty().setValue(z);
        this.getChildren().add(shape);
        this.currentScale = new SimpleDoubleProperty(getAtomScale(this.atomSymbol));
        setScale(currentScale.getValue());
        this.isBackBone = true;
    }

    public int getViewId(){
        return id;
    }


    public void setTooltip(String lhs, String rhs){
        if(t != null){
            removeTooltip();
        }
        t = new Tooltip(lhs+":"+rhs);
        Tooltip.install(this, t);
    }

    void removeTooltip(){
        Tooltip.uninstall(this,t);
    }

    void setSphereRadius(Double r){
        shape.setRadius(r*0.9);
    }

    String getAtomSymbol(){
        return atomSymbol;
    }

    public void setIsBackBone(boolean t){
        isBackBone = t;
    }

    boolean isBackBone(){return isBackBone;}

    void setMainAtomFormRenderModeRadius(){
        shape.setRadius(SPHERE_RADIUS);
    }

    void setSpaceFillingFormRenderModeRadius(){
        shape.setRadius(AGraphView3D.VIEW_SCALING);
    }

    DrawMode setDrawMode(DrawMode mode){
        DrawMode oldMode = shape.getDrawMode();
        if(mode == DrawMode.FILL || mode == DrawMode.LINE)
            shape.drawModeProperty().setValue(mode);
        return oldMode;
    }

    Double changeSize(Double scale){
        currentScale.setValue(scaleXProperty().getValue());
        scaleXProperty().setValue(scaleXProperty().getValue() * scale);
        scaleYProperty().setValue(scaleYProperty().getValue() * scale);
        scaleZProperty().setValue(scaleZProperty().getValue() * scale);
        return currentScale.getValue();
    }

    Double setScale(Double scaling){
        currentScale.setValue(scaleXProperty().getValue());
        scaleXProperty().setValue(scaling);
        scaleYProperty().setValue(scaling);
        scaleZProperty().setValue(scaling);
        return currentScale.getValue();

    }

    public void setColor(Color c){
        material.setDiffuseColor(c);
        material.setSpecularColor(c);
    }

    public Color getColor(){ return material.getDiffuseColor();}

    private static Double getAtomScale(String atomType){
        switch (atomType){
            case "N": //70 pm
                return 1.2;
            case "C": //155 pm
                return 1.4;
            case "O": //60 pm
                return 1.0;
            default:
                return 2.0;
        }
    }

    public Sphere getShape() {
        return shape;
    }
}
