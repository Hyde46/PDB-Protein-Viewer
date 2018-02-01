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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import presenters.PDBPresenter;

import java.io.IOException;
import java.io.InputStream;
public class ApplicationContainer extends Application {

    private double prefHeight;
    private double prefWidth;
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        try(InputStream ins=getClass().getResource("views/fxmlviews/PDBViewer.fxml").openStream()){
            BorderPane root = new BorderPane();
            fxmlLoader.setRoot(root);
            root=fxmlLoader.load(ins);
            root.getStylesheets().add("views/fxmlviews/style.css");
            //Presenter
            PDBPresenter pdbPresenter = new PDBPresenter();
            pdbPresenter.setSceneFields(root,fxmlLoader.getController());
            pdbPresenter.setPrimaryStage(primaryStage);
            pdbPresenter.setUpScene();


            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            prefHeight = primaryScreenBounds.getHeight();
            prefWidth = primaryScreenBounds.getWidth();

            Scene mainScene = new Scene(root, 860, 860);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Totally Awesome Protein Viewer");
            primaryStage.setFullScreen(true);
            primaryStage.getIcons().add(new Image("/views/fxmlviews/images/main_logo.png"));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
