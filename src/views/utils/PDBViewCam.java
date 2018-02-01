package views.utils;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.ScrollEvent;

public class PDBViewCam {

    private static final Double FARCLIP = 20000.0;
    private static final Double NEARCLIP = 0.1;
    private static final Double ZOOM_MULTIPLIER = 1.8;
    private static final Double START_Z = -3350.0;
    private static final Double START_Y = 0.0;
    private static final Double START_X = 0.0;

    private PerspectiveCamera cam;


    public PDBViewCam(){
        cam = new PerspectiveCamera(true);
    }

    public void setupCamera(){
        cam = new PerspectiveCamera(true);
        cam.setFarClip(FARCLIP);
        cam.setNearClip(NEARCLIP);
        resetCamPosition();
    }

    public Point3D lookAt(){
        return new Point3D(cam.translateXProperty().getValue(),
                            cam.translateYProperty().getValue(),
                            0.0);
    }

    public void zoomCamera(ScrollEvent event) {
        double delta = event.getDeltaY() * ZOOM_MULTIPLIER;
        cam.setTranslateZ(cam.getTranslateZ()+delta);
    }

    public void translateCamera(double valueX, double valueY) {
        cam.setTranslateX(cam.getTranslateX()+valueX);
        cam.setTranslateY(cam.getTranslateY()+valueY);
    }

    public void setFocalDistance(Double val){
        cam.setTranslateZ(val);
    }

    private void resetCamPosition(){
        cam.setTranslateZ(START_Z);
        cam.setTranslateX(START_X);
        cam.setTranslateY(START_Y);
    }

    public Double getFieldOfView(){
        return cam.getFieldOfView();
    }

    public PerspectiveCamera getCam() {
        return cam;
    }
}
