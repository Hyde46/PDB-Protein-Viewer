package presenters;

import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;
import models.selectionModels.ASelectionModel;
import utils.PDBQuery;
import utils.SuperMath;
import views.graphviews.AEdgeView3D;
import views.graphviews.AGraphView3D;
import views.graphviews.ANodeView3D;
import views.graphviews.structureviews.AcidTextView;
import views.graphviews.structureviews.AcidTextViewContainer;
import views.utils.BoundingBoxes2D;
import views.utils.PDBViewCam;
import views.utils.StructureSelectionHandler;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class PDBPresenter {

    private static final SceneAntialiasing DEFAULT_ALIASING = SceneAntialiasing.BALANCED;

    //Model
    private AGraph graphModel;
    private APeptide peptide;
    private HashMap<Integer,String> nodeIdToStructureTypeMap = new HashMap<>();
    private HashMap<Integer,String> nodeIdToAcidIdMap = new HashMap<>();

    //Presenter
    private UIHandler uiHandler;
    private ViewLoader viewLoader;
    private ContextMenuHandler contextMenu;
    private PDBConnector connector;
    private BorderPane scenePane;
    private ASelectionModel<ANode> selectionModel;
    private FileChooser pdbFileChooser;
    private CommandRegister commandRegister;

    //View
    private AGraphView3D graphView;
    private AcidTextViewContainer acidTextView;
    private PDBViewCam viewCam;
    private SubScene protSubScene;
    private BoundingBoxes2D bb2D;
    private StructureSelectionHandler structureSelectionHandler;
    private Stage primaryStage;
    private ObservableList<String> allPeptidesList;
    private ObservableList<String> filteredQueryList;

    public PDBPresenter(){
        graphModel = new AGraph();
        graphView = new AGraphView3D();
        selectionModel = new ASelectionModel<>();
        contextMenu = new ContextMenuHandler();
        commandRegister = new CommandRegister(this);
        viewCam = new PDBViewCam();
        uiHandler = new UIHandler(this);
        allPeptidesList = FXCollections.observableArrayList();
        filteredQueryList = FXCollections.observableArrayList();
        structureSelectionHandler= new StructureSelectionHandler(selectionModel);
        viewLoader = new ViewLoader(this);
    }

    public void setSceneFields(BorderPane bp,PDBConnector c) {
        scenePane = bp;
        connector = c;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void setUpScene(){
        setupListeners();
        setup3DSubScene(DEFAULT_ALIASING);
        setupPaneListener();
        createFileChooser();
        setupContextMenu();
        startPDBListQuery();
        setupTextStructureField();
        uiHandler.setupTooltipBinding();
    }

    private void setupContextMenu(){
        contextMenu.setPresenter(this);
        contextMenu.setupContextMenu();
    }

    private void setupTextStructureField(){
        acidTextView = new AcidTextViewContainer();
        connector.getStructurePane().setContent(acidTextView);
        connector.getStructurePane().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        disableVerticalScrollingSecondaryStructure();
        setupTextStructureViewProperties();
    }

    private void disableVerticalScrollingSecondaryStructure(){
        connector.getStructurePane().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });
    }

    private void setupTextStructureViewProperties(){
        acidTextView.prefHeightProperty().bind(connector.getStructurePane().prefHeightProperty());
        acidTextView.prefWidthProperty().bind(connector.getStructurePane().prefWidthProperty());
        acidTextView.maxHeightProperty().bind(connector.getStructurePane().maxHeightProperty());
        acidTextView.maxWidthProperty().bind(connector.getStructurePane().maxWidthProperty());
    }

    private void startPDBListQuery(){
        //noinspection unchecked
        connector.getListView().setItems(filteredQueryList);
        PDBQuery queryService = new PDBQuery();
        queryService.setUrl(PDBQuery.PDB_QUERY_URL);
        queryService.setIsPeptide(false);
        queryService.setOnSucceeded(t ->{
            //noinspection unchecked
            allPeptidesList.addAll((List<String>)t.getSource().getValue());
                    filteredQueryList.addAll(allPeptidesList);
        } );
        queryService.start();
    }
    private void startDownloadQueryWorker(String url){
        PDBQuery queryService = new PDBQuery();
        queryService.setUrl(url);
        queryService.setIsPeptide(true);
        queryService.setOnSucceeded(viewLoader::loadProteinFromPDB);
        queryService.start();
    }

    private void handlePDBListSelection(Observable obs, String oldValue, String newValue ){
        if(newValue != null){
            String selectedProtein = newValue.split(" ")[0];
            String queryURL = "https://files.rcsb.org/download/"+selectedProtein+".pdb";
            startDownloadQueryWorker(queryURL);
        }
    }

    private void setupListeners() {
        //Nodes
        graphModel.getNodes().addListener((ListChangeListener<? super ANode>) this::handleModelNodeChange);
        graphModel.getEdges().addListener((ListChangeListener<? super AEdge>) this::handleModelEdgeChange);
        graphView.getEdgeViews().addListener((ListChangeListener<? super Node>) this::handleViewEdgeChange);
        //noinspection unchecked
        connector.getListView().getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) this::handlePDBListSelection);
        connector.getSearchField().textProperty().addListener(uiHandler::handleFilterList);
    }

    private void setup3DSubScene(SceneAntialiasing aliasing){
        viewCam.setupCamera();
        generateSubScene(aliasing);
    }

    private void generateSubScene(SceneAntialiasing sceneAntialiasing){
        protSubScene = new SubScene(graphView,730,460,true,sceneAntialiasing);
        protSubScene.setCamera(viewCam.getCam());
        bindPaneLayoutSizes();
        setUpBoundingBoxes(connector.getBotPane(),graphView,selectionModel, viewCam.getCam().translateXProperty(),
                viewCam.getCam().translateZProperty(),
                viewCam.getCam().translateYProperty(),
                graphView.getTransformProperty(),
                protSubScene.widthProperty(),
                protSubScene.heightProperty());
        setupPaneChildren();
    }

    private void setupPaneChildren(){
        connector.getTopPane().getChildren().add(bb2D.getRectangles());
        connector.getBotPane().getChildren().add(protSubScene);

        connector.getTopPane().maxHeightProperty().bind(protSubScene.heightProperty());
        connector.getTopPane().maxWidthProperty().bind(protSubScene.widthProperty());
        connector.getTopPane().prefWidthProperty().bind(protSubScene.widthProperty());
        connector.getTopPane().prefHeightProperty().bind(protSubScene.heightProperty());

    }

    private void setUpBoundingBoxes(Pane paneToTrack, AGraphView3D viewToTrack, ASelectionModel<ANode> modelToTrack, Property... properties){
        bb2D = new BoundingBoxes2D(paneToTrack,viewToTrack,modelToTrack,properties);
    }

    private void bindPaneLayoutSizes(){
        protSubScene.heightProperty().bind(connector.getBotPane().heightProperty());
        protSubScene.widthProperty().bind(connector.getBotPane().widthProperty());
    }

    private void setupPaneListener(){
        uiHandler.setupPaneListener();
        connector.getBotPane().setOnScroll(viewCam::zoomCamera);
    }

    private void focusCameraOnGraphCenter(){
        //Focus on x/y plane
        Point3D centerOfGraph = graphView.getCenterOfGeometry();
        focusCam(centerOfGraph,graphView.getViewHeight());
    }

    private void focusCam(Point3D centerPoint, double selectionHeight ){
        Point3D cameraLookAt = viewCam.lookAt();
        Point3D delta = centerPoint.subtract(cameraLookAt);
        viewCam.translateCamera(delta.getX(),delta.getY());
        //set focal distance
        Double focalDistance = SuperMath.focalDistance(viewCam.getFieldOfView(),selectionHeight,connector.getBotPane().getPrefHeight());
        viewCam.setFocalDistance(-focalDistance);
    }

    private void createFileChooser() {
        pdbFileChooser = new FileChooser();
        pdbFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDB(.pdb)","*.pdb"));
    }


    public void setPeptide(APeptide peptide){
        this.peptide = peptide;
        graphView.resetMeshes();
        setupNodeMaps();
        setBackBoneValues();
        uiHandler.colorViewNodes();
        fillStructureView();
        structureSelectionHandler.registerListener(peptide,acidTextView);
        uiHandler.addMeshToView(viewLoader.loadMeshes(peptide));
        viewLoader.loadCartoonView();
    }

    private void fillStructureView(){
        resetAcidTextView();
        for(AAcid acid : peptide.getAminoAcids()){
            AcidTextView textView = acidTextView.addAcidTextView(acid.getAcidName(),peptide.getStructureTypeForNode(acid.getBackbone().get(0)),acid.getAcidId());
            textView.setOnMouseClicked(event -> uiHandler.handleAcidStructureSelection(event,acid.getAcidId()));
        }
    }

    private void resetAcidTextView() {
        acidTextView.resetBindings();
        acidTextView.getChildren().clear();
    }

    private void setupNodeMaps(){
        nodeIdToAcidIdMap.clear();
        nodeIdToStructureTypeMap.clear();
        for(ANode node : graphModel.getNodes()){
            String structType = peptide.getStructureTypeForNode(node);
            AAcid acid = peptide.getAcidForNode(node);
            nodeIdToStructureTypeMap.put(node.getId(),structType);
            nodeIdToAcidIdMap.put(node.getId(),acid.getAcidName());
        }
    }

    private void setBackBoneValues(){
        graphView.getNodeViews().forEach(n -> {
            ANodeView3D node = ((ANodeView3D)n);
            try {
                node.setIsBackBone(peptide.isAtomInBackBone(graphModel.getNodeForId(((ANodeView3D) n).getViewId(),"")));
            } catch (AGraph.InvalidAGraphException ignored) {
            }
        });
    }

    private void clearScene(){
        graphModel.resetGraph();
        graphView.getChildren().clear();
        connector.getBotPane().getChildren().clear();
        nodeIdToStructureTypeMap.clear();
        nodeIdToAcidIdMap.clear();
    }

    private void handleModelNodeChange(ListChangeListener.Change<? extends ANode> n) {
        while (n.next()) {
            uiHandler.stopRotation();
            for (ANode addItem : n.getAddedSubList()) {
                viewLoader.loadNode(addItem);
                selectionModel.setItems(graphModel.getNodes().toArray(new ANode[graphModel.getNodes().size()]));
            }
            for (ANode removeItem : n.getRemoved()) {
                graphView.removeNodeById(removeItem.getId());
            }
            graphView.calculateCenterOfBB();
            focusCameraOnGraphCenter();
        }
        uiHandler.restartAnimation();
    }

    private void handleModelEdgeChange(ListChangeListener.Change<? extends AEdge> e) {
        while (e.next()) {
            for (AEdge additem : e.getAddedSubList()) {
                viewLoader.connectNodesWithEdge(additem.getId(),additem.getSourceNode().getId(),additem.getTargetNode().getId());
            }
            for (AEdge removeItem : e.getRemoved()) {
                graphView.removeEdgeById(removeItem.getId());
            }

        }
    }

    private void handleViewEdgeChange(ListChangeListener.Change<? extends Node> e){
        while (e.next()) {
            for( Node removeItem : e.getRemoved()){
                graphModel.removeEdgeByEdgeId(((AEdgeView3D)removeItem).getViewId());
            }
        }
    }

    void handleResetTransform(){
        graphView.resetTransform();
        focusCameraOnGraphCenter();
    }

    APeptide getPeptide() {
        return peptide;
    }

    public FileChooser getPdbFileChooser() {
        return pdbFileChooser;
    }

    public PDBConnector getConnector() {
        return connector;
    }

    public AGraph getGraphModel() {
        return graphModel;
    }

    public AGraphView3D getGraphView() {
        return graphView;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    ASelectionModel<ANode> getSelectionModel() {
        return selectionModel;
    }

    CommandRegister getCommandRegister() {
        return commandRegister;
    }

    PDBViewCam getViewCam() {
        return viewCam;
    }

    ObservableList<String> getAllPeptidesList() {
        return allPeptidesList;
    }

    ObservableList<String> getFilteredQueryList() {
        return filteredQueryList;
    }

    public UIHandler getUiHandler() {
        return uiHandler;
    }

    public BorderPane getScenePane() {
        return scenePane;
    }

    HashMap<Integer, String> getNodeIdToAcidIdMap() {
        return nodeIdToAcidIdMap;
    }

    HashMap<Integer, String> getNodeIdToStructureTypeMap() {
        return nodeIdToStructureTypeMap;
    }
}