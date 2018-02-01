package presenters;

import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.beans.Observable;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.AAcid;
import models.AGraph;
import models.ANode;
import utils.Delta;
import utils.MyRotateTransition;
import utils.SuperMath;
import views.charts.AcidChartFactory;
import views.graphviews.AGraphView3D;
import views.graphviews.ANodeView3D;
import views.graphviews.viewobjects.ALine3D;
import views.utils.meshes.AcidTriangleMesh;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UIHandler {

    private static final double ROTATION_VELOCITY = 0.25;
    private static final String DEFAULT_BG_CSS = "-fx-background-color:BLACK;";
    private static final String ALTETNATE_BG_CSS = "-fx-background-color:AZURE;";
    private final PDBPresenter presenter;
    private final Delta dragDelta;
    private Delta oldPositions;

    private Transition autoRotateAnimation;
    private Transition waitAnimation;
    private String currentBGCss;

    UIHandler(PDBPresenter presenter){
        this.presenter = presenter;
        dragDelta = new Delta();
        oldPositions = new Delta();
        oldPositions = new Delta();
        waitAnimation = new PauseTransition();
        setupAnimation();
        currentBGCss = DEFAULT_BG_CSS;
    }

    void showCharts(){
        Stage stage = new Stage();
        stage.setTitle("Amino Acid Charts");
        stage.setScene(new Scene(AcidChartFactory.generateBarChart(presenter.getPeptide()), 640, 640));
        stage.show();
    }

    void addNodeToView(int nodeIdToAdd, Double x, Double y, Double z, String atomSymbol){
        ANodeView3D addedNode = presenter.getGraphView().addNodeByCoordinates(nodeIdToAdd,
                x* AGraphView3D.VIEW_SCALING,
                y* AGraphView3D.VIEW_SCALING,
                z* AGraphView3D.VIEW_SCALING,
                atomSymbol);
        addedNode.setOnMouseClicked(e->presenter.getUiHandler().handleMouseSelection(e,addedNode));
    }

    void addMeshToView(ArrayList<MeshView> views){
        views.forEach(meshView -> presenter.getGraphView().addMesh(meshView));
    }

    void addCartoonMeshToView(ArrayList<MeshView> views){
        views.forEach(meshView -> presenter.getGraphView().addCartoonMesh(meshView));
    }

    void addTubesToCartoon(List<ALine3D> tubes){
        if(tubes != null)
            tubes.forEach(t -> presenter.getGraphView().addTubeToCartoonView(t));
    }

    public void colorViewNodes(){
        if(presenter.getGraphView().getColorRenderMode() == AGraphView3D.ColorRenderMode.Structure)
            presenter.getNodeIdToStructureTypeMap().forEach((integer, s) -> presenter.getGraphView().colorNode(integer, s));
        else
            presenter.getNodeIdToAcidIdMap().forEach(((integer, s) -> presenter.getGraphView().colorNode(integer,s)));
        presenter.getGraphView().colorEdges();
        presenter.getGraphView().getMeshViews().forEach(m->((AcidTriangleMesh)((MeshView)m).getMesh()).toggleFaces());
        presenter.getGraphView().getCartoonMeshViews().forEach(m->{
            try{((AcidTriangleMesh)((MeshView)m).getMesh()).toggleFaces();}catch (ClassCastException ignore){}
        });
    }

    private void rotateViewGraph(){
        double angle = (new Point3D(dragDelta.x, dragDelta.y, 0.0)).magnitude() * ROTATION_VELOCITY;
        @SuppressWarnings("SuspiciousNameCombination")
        Point3D rotationAxis = new Point3D(-dragDelta.y, dragDelta.x, 0.0);
        SuperMath.rotateGraphElement(presenter.getGraphView(),angle,rotationAxis);
    }

    private void updateDragValues(MouseEvent event){
        dragDelta.x =  oldPositions.x -event.getSceneX();
        dragDelta.y =  oldPositions.y - event.getSceneY();
        oldPositions.x = event.getSceneX();
        oldPositions.y = event.getSceneY();
    }

    void setupTooltipBinding(){
        presenter.getConnector().getAtomCountField().textProperty().bind(presenter.getGraphModel().getSizeProperty());
        presenter.getConnector().getFormField().textProperty().bind(presenter.getGraphView().formModePropertyProperty());
        presenter.getConnector().getColoringField().textProperty().bind(presenter.getGraphView().colorModePropertyProperty());
    }

    private void updateAutomaticRotationAnimation(){
        autoRotateAnimation.pause();
        waitAnimation.play();
        waitAnimation.setOnFinished(e -> autoRotateAnimation.play());
    }

    void setupPaneListener(){
        presenter.getConnector().getBotPane().setOnMousePressed(e->{
            oldPositions.x = e.getSceneX();
            oldPositions.y = e.getSceneY();
        });
        presenter.getConnector().getBotPane().setOnMouseDragged(this::handlePaneDragging);
    }

    private void setupAnimation(){
        autoRotateAnimation = new MyRotateTransition(presenter.getGraphView(), Duration.seconds(10000));
        autoRotateAnimation.setCycleCount(10);
        waitAnimation = new PauseTransition(Duration.seconds(5));
    }

    void stopRotation(){
        autoRotateAnimation.stop();
    }

    void restartAnimation(){
        autoRotateAnimation.playFromStart();
        waitAnimation.playFromStart();
        waitAnimation.stop();
    }

    public void setupTooltips() {
        for (AAcid acid : presenter.getPeptide().getAminoAcids()) {
            for (ANode node : acid.getBackbone()) {
                (presenter.getGraphView().getNodeById(node.getId())).setTooltip(acid.getAcidId(), node.getAtomName());
            }
            for(ANode node : acid.getSideChain()) {
                (presenter.getGraphView().getNodeById(node.getId())).setTooltip(acid.getAcidId(), node.getAtomName());
            }
        }
    }
    private void handlePaneDragging(MouseEvent event){
        if(!event.isShiftDown() && presenter.getGraphModel().isEmptyProperty().not().getValue()){
            updateAutomaticRotationAnimation();
            updateDragValues(event);
            if(event.isSecondaryButtonDown()){
                presenter.getViewCam().translateCamera(dragDelta.x,dragDelta.y);
            }else {
                rotateViewGraph();
            }
        }
    }

    private void handleMouseSelection(MouseEvent event, ANodeView3D addedNode){
        try {
            ANode clickedNode = presenter.getGraphModel().getNodeForId(addedNode.getViewId(),"");
            AAcid clickedAcid = presenter.getPeptide().getAcidById(clickedNode.getAcidId());
            presenter.getCommandRegister().executeSelectNode(event.isShiftDown(),clickedAcid);
        } catch (AGraph.InvalidAGraphException e) {
            e.printStackTrace();
        }
        event.consume();
    }

    @SuppressWarnings("unused")
    void handleFilterList(Observable change){
        String filter = presenter.getConnector().getSearchField().textProperty().getValue().toLowerCase();
        presenter.getFilteredQueryList().setAll((presenter.getAllPeptidesList().stream().filter(s -> s.toLowerCase().contains(filter)).collect(Collectors.toList())));
    }

    void handleAcidStructureSelection(MouseEvent event, String selectedAcidId){
        presenter.getCommandRegister().executeSelectNode(event.isShiftDown(),presenter.getPeptide().getAcidById(selectedAcidId));
    }

    void handleBGColorToggle(){
        if(currentBGCss.equals(DEFAULT_BG_CSS)) {
            presenter.getConnector().getBotPane().setStyle(ALTETNATE_BG_CSS);
            currentBGCss = ALTETNATE_BG_CSS;
        }else {
            presenter.getConnector().getBotPane().setStyle(DEFAULT_BG_CSS);
            currentBGCss = DEFAULT_BG_CSS;
        }
    }

    public void showAlertMessage(String title,String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    Transition getAutoRotateAnimation() {
        return autoRotateAnimation;
    }

}
