package views.graphviews;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import views.graphviews.viewobjects.ALine3D;

public class AEdgeView3D extends Group {

    private final int id;

    private final ALine3D shape;
    private final ANodeView3D source;
    private final ANodeView3D target;

    AEdgeView3D(int id, ANodeView3D src, ANodeView3D target){
        this.id = id;
        shape = new ALine3D(src.translateXProperty(),src.translateYProperty(),src.translateZProperty(),
                            target.translateXProperty(), target.translateYProperty(), target.translateZProperty());
        this.source = src;
        this.target = target;
        getChildren().add(shape);
    }



    public int getViewId(){
        return id;
    }

    public void setColor(Color c){
        shape.setColor(c);
    }

    double getRadius(){
        return shape.getCylinderRadius();
    }

    void changeSize(Double newScaling){
        shape.scaleRadius(newScaling);
    }

    void setScale(Double newScaling){
        shape.setRadiusScale(newScaling);
    }

    ANodeView3D getSource() {
        return source;
    }

    ANodeView3D getTarget() {
        return target;
    }
}
