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
