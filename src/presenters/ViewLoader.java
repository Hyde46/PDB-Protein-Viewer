package presenters;

import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import models.AAcid;
import models.ANode;
import models.APeptide;
import models.ASheetContainer;
import models.utils.meshfactories.HelixMeshFactory;
import models.utils.meshfactories.LoopFactory;
import models.utils.meshfactories.RibbonFactory;
import views.graphviews.AGraphView3D;
import views.graphviews.ANodeView3D;
import views.graphviews.viewobjects.ALine3D;
import views.utils.meshes.AcidTriangleMesh;
import views.utils.textures.AcidTextureLoader;

import java.util.ArrayList;
import java.util.List;

class ViewLoader {

    private final PDBPresenter presenter;


    ViewLoader(PDBPresenter p){
        this.presenter = p;
    }

    void loadNode(ANode additem) {
        presenter.getGraphView().resetTransform();
        presenter.getUiHandler().addNodeToView(additem.getId(),
                additem.xCoordPropertyProperty().getValue(),
                additem.yCoordPropertyProperty().getValue(),
                additem.zCoordPropertyProperty().getValue(),additem.getAtomSymbol());
    }

    void connectNodesWithEdge(int edgeId, int firstNodeID, int secondNodeID){
        ANodeView3D firstNode = presenter.getGraphView().getNodeById(firstNodeID);
        ANodeView3D secondNode = presenter.getGraphView().getNodeById(secondNodeID);
        if(firstNode != null && secondNode != null){
            presenter.getGraphView().addEdgeByNodes(edgeId,firstNode,secondNode);
        }
    }

    ArrayList<MeshView> loadMeshes(APeptide pep){
        ArrayList<MeshView> meshViews = new ArrayList<>();
        ObservableList<ANode> points = presenter.getGraphModel().getNodes();
        AcidTriangleMesh simpleRibbonMesh = RibbonFactory.generateMesh(points,pep);
        MeshView simpleRibbonView = new MeshView(simpleRibbonMesh);
        simpleRibbonView.setMaterial(AcidTextureLoader.loadAcidTexture());
        meshViews.add(simpleRibbonView);
        colorMeshes();
        return meshViews;
    }

    void loadCartoonView(){
        ArrayList<MeshView> meshes = new ArrayList<>();
        List<Point3D> cAlphaPoints = new ArrayList<>();
        List<Point3D> cPoints = new ArrayList<>();
        List<Point3D> oPoints = new ArrayList<>();
        List<Point3D> nPoints = new ArrayList<>();
        List<ANode> helixPoints = new ArrayList<>();
        loadCartoonmesh(meshes, cAlphaPoints, cPoints, oPoints, nPoints, helixPoints,presenter.getPeptide().getHelixAcids(),"Helix");
        loadLoopCartoonLines(cAlphaPoints, cPoints, oPoints, nPoints, helixPoints);
        loadCartoonmesh(meshes, cAlphaPoints, cPoints, oPoints, nPoints, helixPoints,presenter.getPeptide().getSheetAcids(),"Sheet");
        //loadCartoonmesh(meshes);
        presenter.getUiHandler().addCartoonMeshToView(meshes);
        colorMeshes();
    }

    private void loadCartoonmesh(ArrayList<MeshView> meshes, List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints, List<Point3D> nPoints, List<ANode> helixPoints,List<List<AAcid>>acids, String type) {
        for(List<AAcid> acid :acids){
            prepareInputLists(acid,cAlphaPoints,cPoints,oPoints,nPoints,helixPoints);
            AcidTriangleMesh sheetMesh = HelixMeshFactory.generateHelixMesh(cAlphaPoints,cPoints,oPoints,nPoints,helixPoints,type,
                    5,15, 80, 10);
            assert sheetMesh != null;
            MeshView simpleRibbonView = new MeshView(sheetMesh);
            simpleRibbonView.setMaterial(AcidTextureLoader.loadAcidTexture());
            meshes.add(simpleRibbonView);
        }
    }
   /* private void loadCartoonmesh(ArrayList<MeshView> meshes) {
        for (ASheetContainer sheet : presenter.getPeptide().getSheetContainer()) {
            ObservableList<ANode> acidRibbonNodes = presenter.getGraphModel().getNodesSublist(sheet.getInitSeqNum(), sheet.getEndSeqNum() + 1);
            AcidTriangleMesh ribbonMesh = RibbonFactory.generateMesh(acidRibbonNodes, presenter.getPeptide());
            assert ribbonMesh != null;
            MeshView simpleRibbonView = new MeshView(ribbonMesh);
            simpleRibbonView.setMaterial(AcidTextureLoader.loadAcidTexture());
            meshes.add(simpleRibbonView);
        }
    }*/
    private void loadLoopCartoonLines(List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints, List<Point3D> nPoints, List<ANode> helixPoints) {
        for(List<AAcid> loop : presenter.getPeptide().getLoopAcids()){
            prepareInputLists(loop,cAlphaPoints,cPoints,oPoints,nPoints,helixPoints);
            List<ALine3D> tubes = LoopFactory.generateHelixMesh(cAlphaPoints,cPoints,oPoints,nPoints,5,30,25);
            presenter.getUiHandler().addTubesToCartoon(tubes);
        }
    }

    private void colorMeshes() {
        if(presenter.getGraphView().getColorRenderMode() == AGraphView3D.ColorRenderMode.Type)
            presenter.getUiHandler().colorViewNodes();
    }

    void loadProteinFromPDB(WorkerStateEvent event) {
        @SuppressWarnings("unchecked")
        List<String> downloadedPDB = (List<String>) event.getSource().getValue();
        presenter.getCommandRegister().executeLoadFileFromList(downloadedPDB);
    }

    private void prepareInputLists(List<AAcid> acids,List<Point3D> cAlphaPoints,List<Point3D> cPoints,List<Point3D> oPoints,List<Point3D> nPoints,List<ANode> helixPoints) {
        cAlphaPoints.clear();
        cPoints.clear();
        oPoints.clear();
        nPoints.clear();
        helixPoints.clear();
        int acidCounter = 0;
        for(AAcid acid: acids){
            addAcidAtomsToLists(cAlphaPoints, cPoints, oPoints, nPoints, helixPoints, acid);
            acidCounter++;

            if(acidCounter == acids.size()){
                AAcid lastAcid = presenter.getPeptide().getNextAcid(acid.getAcidId());
                if(lastAcid != null)
                    addAcidAtomsToLists(cAlphaPoints,cPoints,oPoints,nPoints,helixPoints,lastAcid);
            }
        }
    }

    private void addAcidAtomsToLists(List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints, List<Point3D> nPoints, List<ANode> helixPoints, AAcid acid) {
        for(ANode node : acid.getBackbone()){
            switch (node.getAtomName()){
                case "CA":
                    cAlphaPoints.add(new Point3D(node.getxCoord(),node.getyCoord(),node.getzCoord()).multiply(AGraphView3D.VIEW_SCALING));
                    helixPoints.add(node);
                    break;
                case "C":
                    cPoints.add(new Point3D(node.getxCoord(),node.getyCoord(),node.getzCoord()).multiply(AGraphView3D.VIEW_SCALING));
                    helixPoints.add(node);
                    break;
                case "N":
                    nPoints.add(new Point3D(node.getxCoord(),node.getyCoord(),node.getzCoord()).multiply(AGraphView3D.VIEW_SCALING));
                    helixPoints.add(node);
                    break;
                case "O":
                    oPoints.add(new Point3D(node.getxCoord(),node.getyCoord(),node.getzCoord()).multiply(AGraphView3D.VIEW_SCALING));
                    helixPoints.add(node);
                    break;
                default:
            }
        }
    }

}
