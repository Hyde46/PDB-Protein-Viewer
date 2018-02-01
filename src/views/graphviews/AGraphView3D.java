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

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import models.ANode;
import utils.AcidTranslator;
import views.graphviews.viewobjects.ALine3D;

import java.util.*;

@SuppressWarnings({"unused", "ConstantConditions"})
public class AGraphView3D extends Group {

    public static final Double VIEW_SCALING = 55.0;
    private static final Double DEFAULT_NODE_SCALE = 1.0;

    private Group cartoonMeshViewGroup;
    private Group ribbonViewGroup;
    private Group nodeViewGroup;
    private Group edgeViewGroup;

    private Property<Transform> transformProperty = new SimpleObjectProperty<>(new Rotate());
    private Point3D centerOfGeometry;

    private ColorRenderMode colorRenderMode;
    private FormRenderMode formRenderMode;

    private StringProperty colorModeProperty;
    private StringProperty formModeProperty;

    private HashMap<String,Color> colorRenderModeMap = new HashMap<>();

    private DoubleProperty currentNodesScaling;
    private DoubleProperty currentBondScaling;

    public AGraphView3D(){
        nodeViewGroup = new Group();
        edgeViewGroup = new Group();
        ribbonViewGroup = new Group();
        cartoonMeshViewGroup = new Group();
        getChildren().add(edgeViewGroup);
        getChildren().add(nodeViewGroup);
        getChildren().add(ribbonViewGroup);
        getChildren().add(cartoonMeshViewGroup);
        transformProperty.addListener((c,o,n)->this.getTransforms().setAll(n));
        centerOfGeometry = new Point3D(0.0,0.0,0.0);
        colorRenderMode = ColorRenderMode.Type;
        formRenderMode = FormRenderMode.Default;
        formModeProperty = new SimpleStringProperty(formRenderMode.toString());
        colorModeProperty = new SimpleStringProperty(colorRenderMode.toString());
        setFormRenderMode(formRenderMode);
        setupModeMaps();
        currentNodesScaling = new SimpleDoubleProperty(DEFAULT_NODE_SCALE);
        currentBondScaling = new SimpleDoubleProperty(DEFAULT_NODE_SCALE);
        setupScalingListener();
    }

    private void setupModeMaps(){
        colorRenderModeMap.put("H",Color.DEEPPINK);
        colorRenderModeMap.put("E",Color.ORANGE);
        colorRenderModeMap.put("-",Color.LINEN);
    }

    public ColorRenderMode setColorRenderMode(ColorRenderMode mode){
        ColorRenderMode oldMode = colorRenderMode;
        colorModeProperty.setValue(colorRenderMode.toString());
        this.colorRenderMode = mode;
        return oldMode;
    }

    private void setupScalingListener(){
        currentNodesScaling.addListener((observable)->scaleNodeSizes());
        currentBondScaling.addListener(observable -> scaleBondSizes());
    }

    public Property<Transform> getTransformProperty(){
        return transformProperty;
    }

    public Point3D getCenterOfGeometry(){ return centerOfGeometry;}

    public void colorNode(int nodeId, String mode){
        if(colorRenderMode == ColorRenderMode.Structure)
            getNodeById(nodeId).setColor(colorRenderModeMap.get(mode));
        else if( colorRenderMode == ColorRenderMode.Type) {
            getNodeById(nodeId).setColor(AcidTranslator.getAminoColorByType(mode));
        }
    }

    public void colorEdges(){
        for( Node e : edgeViewGroup.getChildren()){
            AEdgeView3D edge = ((AEdgeView3D)e);
            edge.setColor(edge.getSource().getColor());
        }
    }

    public FormRenderMode setFormRenderMode(FormRenderMode newMode){
        FormRenderMode oldMode = formRenderMode;
        hideEverything();
        if(currentNodesScaling != null && currentBondScaling != null){
            currentNodesScaling.setValue(DEFAULT_NODE_SCALE);
            currentBondScaling.setValue(DEFAULT_NODE_SCALE);
        }
        switch (newMode) {
            case Default:
                showMainAtomFormRenderMode();
                break;
            case SpaceFilling:
                showSpaceFillingFormRenderMode();
                break;
            case Backbone:
                showBackBoneFormRenderMode();
                break;
            case Ribbon:
                showSimpleRibbons();
                break;
            case Cartoon:
                showCartoonMode();
                break;
        }
        formRenderMode = newMode;
        formModeProperty.setValue(formRenderMode.toString());
        return oldMode;
    }

    private void hideEverything(){
        showRenderElement(false,edgeViewGroup);
        showRenderElement(false,nodeViewGroup);
        showRenderElement(false,ribbonViewGroup);
        showRenderElement(false,cartoonMeshViewGroup);
        hideHiddenEdges(true);
        hideSideChain(true);
    }

    private void showCartoonMode(){
        showRenderElement(true,cartoonMeshViewGroup);
    }

    private void showSimpleRibbons(){
        showRenderElement(true, ribbonViewGroup);
    }

    private void showMainAtomFormRenderMode(){
        hideHiddenEdges(false);
        hideSideChain(false);
        showRenderElement(true,edgeViewGroup);
        showRenderElement(true,nodeViewGroup);
        nodeViewGroup.getChildren().forEach(node ->
                ((ANodeView3D)node).setMainAtomFormRenderModeRadius());
    }

    private void showSpaceFillingFormRenderMode(){
        hideHiddenEdges(false);
        hideSideChain(false);
        nodeViewGroup.getChildren().forEach(node -> ((ANodeView3D)node).setSpaceFillingFormRenderModeRadius());
        showRenderElement(true,nodeViewGroup);
    }

    private void showBackBoneFormRenderMode(){
        hideHiddenEdges(true);
        hideSideChain(true);
        showRenderElement(true,edgeViewGroup);
        showRenderElement(true,nodeViewGroup);
        scaleNodeSizes();
    }

    private void showRenderElement(boolean isViewable, Group elements){
        elements.setVisible(isViewable);
        elements.setDisable(!isViewable);
    }

    private void hideSideChain(boolean isHiding){
        //Oxygen is not part of the sidechain, but we still hide it
        nodeViewGroup.getChildren().forEach(n -> {
            ANodeView3D node = (ANodeView3D)n;
            if(!node.isBackBone() || node.getAtomSymbol().equals("O")) {
                node.setVisible(!isHiding);
                node.setDisable(isHiding);
            }else{
                node.setSphereRadius(((AEdgeView3D)edgeViewGroup.getChildren().get(0)).getRadius());
            }
        });
    }

    private void hideHiddenEdges(boolean isHiding){
        edgeViewGroup.getChildren().forEach(e->{
            AEdgeView3D edge = (AEdgeView3D)e;
            if((!edge.getSource().isVisible())||(!edge.getTarget().isVisible())){
                edge.setVisible(!isHiding);
                edge.setDisable(isHiding);
            }
        });
    }

    private void scaleNodeSizes(){
        nodeViewGroup.getChildren().forEach(n-> ((ANodeView3D)n).setScale(currentNodesScaling.getValue()));
    }

    private void scaleBondSizes(){
        edgeViewGroup.getChildren().forEach(e->((AEdgeView3D)e).setScale(currentBondScaling.getValue()));
    }

    public void resetTransform(){
        transformProperty.setValue(new Rotate());
    }

    public ANodeView3D addNodeByCoordinates(int nodeIdToAdd,double x, double y, double z, String atomSymbol){
        ANodeView3D n = new ANodeView3D(nodeIdToAdd,x,y,z,atomSymbol);
        this.addNode(n);
        return n;
    }

    public void addEdgeByNodes(int edgeId, ANodeView3D n1, ANodeView3D n2){
        AEdgeView3D edge = new AEdgeView3D(edgeId,n1,n2);
        this.addEdge(edge);
    }

    public HashMap<Integer,DrawMode> setNodeDrawMode(DrawMode mode, ArrayList<Integer> nodeIds){
        HashMap<Integer, DrawMode> oldModes = new HashMap<>();
        for(Integer i : nodeIds){
            oldModes.put(i, getNodeById(i).setDrawMode(mode));
        }
        return oldModes;
    }

    public Double scaleAllEdges(Double newScaling,boolean isRelativeScaling){
        Double oldScale = ((AEdgeView3D) edgeViewGroup.getChildren().get(0)).getRadius();
        edgeViewGroup.getChildren().forEach(node -> {
            if(isRelativeScaling)
                ((AEdgeView3D)node).changeSize(newScaling);
            else
                ((AEdgeView3D)node).setScale(newScaling);
        });
        return oldScale;
    }

    public HashMap<Integer,Double> scaleNodes(Double newScaling, ArrayList<Integer> nodeIds,boolean isRelativeScaling){
        HashMap<Integer,Double> oldSizes = new HashMap<>();
        nodeIds.forEach(nId->{
            if(isRelativeScaling)
                oldSizes.put(nId, getNodeById(nId).changeSize(newScaling));
            else {
                currentNodesScaling.setValue(newScaling);
                oldSizes.put(nId, getNodeById(nId).setScale(newScaling));
            }
        });
        return oldSizes;
    }


    @SuppressWarnings("UnusedReturnValue")
    public Point3D calculateCenterOfBB(){
        Point3D offset = new Point3D(nodeViewGroup.getTranslateX(),
                nodeViewGroup.getTranslateY(),
                nodeViewGroup.getTranslateZ());
        Bounds bounds = (nodeViewGroup.getLayoutBounds());
        centerOfGeometry = new Point3D((bounds.getMinX()+bounds.getMaxX())/2.0,
                (bounds.getMinY()+bounds.getMaxY())/2.0,
                (bounds.getMinZ()+bounds.getMaxZ())/2.0);
        centerOfGeometry = centerOfGeometry.add(offset);

        return centerOfGeometry;
    }

    public Double getViewHeight(){
        Bounds b = (nodeViewGroup.getLayoutBounds());
        return Math.max(Math.max(b.getMaxY()-b.getMinY(),b.getMaxX()-b.getMinX()),
                b.getMaxZ()-b.getMinZ());
    }

    public void resetMeshes(){
        ribbonViewGroup.getChildren().clear();
        cartoonMeshViewGroup.getChildren().clear();
    }

    private void addEdge(AEdgeView3D edge){ edgeViewGroup.getChildren().add(edge);}

    private void addNode(ANodeView3D node){
        nodeViewGroup.getChildren().add(node);
    }

    public void addMesh(MeshView mesh){ ribbonViewGroup.getChildren().add(mesh); }

    public void addCartoonMesh(MeshView mesh){ cartoonMeshViewGroup.getChildren().add(mesh);}

    public void addTubeToCartoonView(ALine3D tube ){ cartoonMeshViewGroup.getChildren().add(tube);}

    public ObservableList<Node> getNodeViews(){
        return nodeViewGroup.getChildren();
    }

    public ObservableList<Node> getEdgeViews(){
        return edgeViewGroup.getChildren();
    }

    public ObservableList<Node> getMeshViews() { return ribbonViewGroup.getChildren(); }
    public ObservableList<Node> getCartoonMeshViews() { return cartoonMeshViewGroup.getChildren(); }

    public ANodeView3D getNodeById(int id){
        ANodeView3D nodeRet = null;
        try{
            nodeRet = (ANodeView3D)nodeViewGroup.getChildren().stream().
                    filter(node -> ((ANodeView3D)node).getViewId() == id).
                    findFirst().
                    get();
        }catch(NoSuchElementException ignored){}
        return nodeRet;
    }

    public List<ANodeView3D> getNodesByIds(int... ids){
        List<ANodeView3D> nodes = new ArrayList<>();
        try{
            Arrays.stream(ids).forEach(id -> nodeViewGroup.getChildren().stream().
                    filter(node ->
                            ((ANodeView3D)node).getViewId()==id).
                    forEach(n->
                            nodes.add((ANodeView3D)n)));
        }catch (NoSuchElementException ignored){}
        return nodes;
    }

    private AEdgeView3D getEdgeById(int id){
        AEdgeView3D edge = null;
        try {
            edge = (AEdgeView3D) edgeViewGroup.getChildren().stream().
                    filter(e -> ((AEdgeView3D) e).getViewId() == id).
                    findFirst().
                    get();
        }catch(NoSuchElementException ignored){        }
        return edge;
    }

    public void removeEdgeById(int edgeId){
        AEdgeView3D edgeToRemove = getEdgeById(edgeId);
        edgeViewGroup.getChildren().remove(edgeToRemove);
    }

    public void removeNodeById(int id){
        ANodeView3D nodeToRemove = getNodeById(id);
        nodeToRemove.removeTooltip();
        nodeViewGroup.getChildren().remove(nodeToRemove);
    }

    public ANodeView3D getView(ANode n){
        Node nFiltered = nodeViewGroup.getChildren().stream().filter(node -> ((ANodeView3D)node).getViewId() == n.getId()).findFirst().get();
        return ((ANodeView3D)nFiltered);
    }
    public ColorRenderMode getColorRenderMode() {
        return colorRenderMode;
    }

    public FormRenderMode getFormRenderMode() {
        return formRenderMode;
    }

    public double getCurrentNodesScaling() {
        return currentNodesScaling.get();
    }

    public DoubleProperty currentNodesScalingProperty() {
        return currentNodesScaling;
    }

    public double getCurrentBondScaling() {
        return currentBondScaling.get();
    }

    public DoubleProperty currentBondScalingProperty() {
        return currentBondScaling;
    }

    public String getColorModeProperty() {
        return colorModeProperty.get();
    }

    public StringProperty colorModePropertyProperty() {
        return colorModeProperty;
    }

    public String getFormModeProperty() {
        return formModeProperty.get();
    }

    public StringProperty formModePropertyProperty() {
        return formModeProperty;
    }

    public enum ColorRenderMode{Structure, Type}
    public enum FormRenderMode{Default,SpaceFilling, Backbone,Ribbon,Cartoon}
}
