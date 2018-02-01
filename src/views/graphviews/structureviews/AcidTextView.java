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
