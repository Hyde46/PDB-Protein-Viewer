package models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import utils.parser.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@SuppressWarnings({"unused", "ConstantConditions"})
public class AGraph {

    private ObservableList<ANode> nodes = FXCollections.observableArrayList();
    private ObservableList<AEdge> edges = FXCollections.observableArrayList();
    private SimpleBooleanProperty isEmptyProperty;
    private SimpleStringProperty sizeProperty;

    public AGraph() {
        isEmptyProperty = new SimpleBooleanProperty(true);
        sizeProperty = new SimpleStringProperty("0");
        setupEmptyListener();
    }

    public void connectTwoNodes(ANode source, ANode target, String label) throws InvalidAGraphException {
        addEdges(new AEdge(source, target, label));
    }

    private void connectTwoNodesById(int sourceId, int targetID) throws InvalidAGraphException {
        ANode sourceNode = getNodeForId(sourceId,"");
        ANode targetNode = getNodeForId(targetID,"");
        addEdges(new AEdge(sourceNode,targetNode,""));
    }

    public void disconnectTwoNodes(ANode source, ANode target) {
        AEdge edgeToRemove = getEdgeByNode(source, target);
        if (edgeToRemove != null)
            removeEdges(edgeToRemove);
    }

    public AEdge getEdgeByNodeIds(int sourceID, int targetID) {
        try {
            return edges.stream().filter(edge -> (edge.getSourceNode().getId() == sourceID) && (edge.getTargetNode().getId() == targetID)).findFirst().get();
        } catch (NoSuchElementException ignored) {
        }
        return null;
    }

    private void removeEdges(AEdge... edgesToRemove) {
        edges.removeAll(edgesToRemove);
        nodes.forEach(node -> {
            node.removeIncomingEdges(edgesToRemove);
            node.removeLeavingEdges(edgesToRemove);
        });
    }

    public void removeEdgeByEdgeId(int edgeID){
        try {
            removeEdges(edges.stream().filter(edge -> (edge.getId() == edgeID)).findFirst().get());
        } catch (NoSuchElementException ignored) {
        }
    }

    public void removeEdgeByNodeIds(int sourceID, int targetID) {
        try {
            removeEdges(edges.stream().filter(edge -> (edge.getSourceNode().getId() == sourceID) && (edge.getTargetNode().getId() == targetID)).findFirst().get());
        } catch (NoSuchElementException ignored) {
        }
    }

    public void removeNodeById(int nodeId) {
        try {
            removeNodes(nodes.stream().filter(n -> n.getId() == nodeId).findFirst().get());
        } catch (NoSuchElementException ignored) {
        }
    }

    public APeptide readPDBFromList(List<String> pdbList) throws InvalidAGraphException{
        resetGraph();
        ParsePeptideContainer parsePeptideContainer = PDBParser.parsePDB(pdbList);
        return combinePeptideFields(parsePeptideContainer);
    }

    public APeptide readPDB(String path) throws InvalidAGraphException{
        resetGraph();
        ParsePeptideContainer parsePeptideContainer = PDBParser.parseFile(path);
        return combinePeptideFields(parsePeptideContainer);
    }

    private APeptide combinePeptideFields(ParsePeptideContainer parsePeptideContainer) throws InvalidAGraphException{
        ArrayList<AAcid> aminoAcids = buildPeptide(parsePeptideContainer);
        ArrayList<AHelixContainer> helixes = getHelixContainers(parsePeptideContainer);
        ArrayList<ASheetContainer> sheets = getSheetContainers(parsePeptideContainer);
        return new APeptide(aminoAcids,parsePeptideContainer.getPrimaryStructure(),parsePeptideContainer.getSecondaryStructure(),helixes,sheets);
    }

    private ArrayList<ASheetContainer> getSheetContainers(ParsePeptideContainer pc){
        ArrayList<ASheetContainer> c = new ArrayList<>();
        for(SheetContainer sc : pc.getSheets()){
            ASheetContainer s = new ASheetContainer(sc.getRecordName(),sc.getStrand(),sc.getSheetID(),sc.getNumStrands(),
                    sc.getInitResName(),sc.getInitChainID(),sc.getInitSeqNum(),sc.getInitICode(),sc.getEndResName(),sc.getEndChainID(),
                    sc.getEndSeqNum(),sc.getEndICode(),sc.getSense(),sc.getCurAtom(),sc.getCurResName(),sc.getCurChainId(),
                    sc.getCurResSeq(),sc.getCurICode(),sc.getPrevAtom(),sc.getPrevResName(),sc.getPrevChainId(),sc.getPrevResSeq(),
                    sc.getPrevICode());
            c.add(s);
        }
        return c;
    }

    private ArrayList<AHelixContainer> getHelixContainers(ParsePeptideContainer pc){
        ArrayList<AHelixContainer> container = new ArrayList<>();
        for(HelixContainer hc : pc.getHelixContainer()){
            AHelixContainer h = new AHelixContainer(hc.getRecordName(),hc.getSerNum(),hc.getHelixID(),hc.getInitResName(),
                    hc.getInitChainID(),hc.getInitSeqNum(),hc.getInitICode(),hc.getEndResName(),hc.getEndChainID(),
                    hc.getEndSeqNum(),hc.getEndICode(),hc.getHelixClass(),hc.getComment(),hc.getLength());
            container.add(h);
        }
        return container;
    }

    private ArrayList<AAcid> buildPeptide(ParsePeptideContainer parsePeptideContainer) throws InvalidAGraphException {
        ArrayList<ParseAcidContainer> parsedAcids = parsePeptideContainer.getAtoms();
        ArrayList<ParseEdgeContainer> parseEdgeContainers = parsePeptideContainer.getEdges();
        ArrayList<AAcid> aminoAcids = new ArrayList<>();

        for(ParseAcidContainer pAcid : parsedAcids){
            AAcid acid = new AAcid(pAcid.getAcidId(),pAcid.getAcid());
            for(AtomContainer pAtom : pAcid.getBackbone()){
                ANode atom = new ANode(pAtom.getId(),
                        pAtom.getAtomSymbol(),
                        pAtom.getAtomName(),
                        pAtom.getCoordinate(),
                        pAtom.getAcidId(),
                        pAtom.getAcid());
                acid.addNodesToBackbone(atom);
                addNodes(atom);
            }
            for(AtomContainer sideAtom : pAcid.getSideChain()){
                ANode atom = new ANode(sideAtom.getId(),
                        sideAtom.getAtomSymbol(),
                        sideAtom.getAtomName(),
                        sideAtom.getCoordinate(),
                        sideAtom.getAcidId(),
                        sideAtom.getAcid());
                acid.addNodesToSideChain(atom);
                addNodes(atom);
            }
            aminoAcids.add(acid);
        }
        for(ParseEdgeContainer pEdge : parseEdgeContainers){
           connectTwoNodesById(pEdge.getSourceID(),pEdge.getTargetID());
        }
        return aminoAcids;
    }
    public SimpleBooleanProperty isEmptyProperty() {
        return isEmptyProperty;
    }

    public SimpleStringProperty getSizeProperty() {
        return sizeProperty;
    }

    public ObservableList<ANode> getNodes() {
        return nodes;
    }

    public ObservableList<AEdge> getEdges() {
        return edges;
    }

    public ObservableList<ANode> getNodesSublist(int startIndes, int endIndex){
        ObservableList<ANode> sublist = FXCollections.observableArrayList();
        nodes.stream().filter(aNode -> Integer.parseInt(aNode.getAcidId()) >= startIndes &&  Integer.parseInt(aNode.getAcidId()) <= endIndex).forEach(sublist::add);
        return sublist;
    }

    public class InvalidAGraphException extends Exception {
        InvalidAGraphException(int elementID) {
            super("Invalid AGraph. Can't add Node with duplicate ID " + elementID + ".");
        }

        InvalidAGraphException(String edgeText, String error) {
            super("Invalid AGraph. Can't add Edge " + edgeText + ". " + error + ".");
        }
    }
    /*
    Checks if Node in the edge List already exists with the same source and target node as @edge
     */
    private void addNodes(ANode... nodesToAdd) throws InvalidAGraphException {
        for (ANode nodeToAdd : nodesToAdd) {
            //Check if id is unique
            if (nodes.stream().anyMatch(aNode -> aNode.getId() == nodeToAdd.getId())) {
                throw new InvalidAGraphException(nodeToAdd.getId());
            } else
                nodes.add(nodeToAdd);
        }
    }

    private void addEdges(AEdge... edgesToAdd) throws InvalidAGraphException {
        for (AEdge edgeToAdd : edgesToAdd) {
            if (isEdgeDuplicate(edgeToAdd)) {
                throw new InvalidAGraphException(edgeToAdd.getText(), "Edge already Exists or is Parallel Edge");
            } else if (isEdgeSelfLoop(edgeToAdd)) {
                throw new InvalidAGraphException(edgeToAdd.getText(), "Edge creates self loop");
            } else {
                edges.add(edgeToAdd);
                Arrays.stream(nodes.stream().filter(aNode -> edgeToAdd.getSourceNode().getId() == aNode.getId()).toArray(ANode[]::new)).forEach(sourceNode -> sourceNode.insertLeavingEdges(edgeToAdd));
                Arrays.stream(nodes.stream().filter(aNode -> edgeToAdd.getTargetNode().getId() == aNode.getId()).toArray(ANode[]::new)).forEach(sourceNode -> sourceNode.insertIncomingEdges(edgeToAdd));
            }
        }
    }

    private boolean isEdgeDuplicate(AEdge edgeToAdd) {
        return edges.stream().anyMatch(edge -> (edge.getTargetNode().getId() == edgeToAdd.getTargetNode().getId()) &&
                (edge.getSourceNode().getId() == edgeToAdd.getSourceNode().getId()));
    }

    private boolean isEdgeSelfLoop(AEdge edgeToAdd) {
        return edgeToAdd.getSourceNode().getId() == edgeToAdd.getTargetNode().getId();
    }

    public ANode getNodeForId(int id, String edgeLabel) throws InvalidAGraphException {
        for (ANode n : nodes)
            if (n.getId() == id)
                return n;
        throw new InvalidAGraphException(edgeLabel, "Node does not exist");
    }

    private void removeNodes(ANode... nodesToRemove) {
        nodes.removeAll(nodesToRemove);
        Arrays.stream(nodesToRemove).forEach(nodeToRemove -> removeEdges(edges.stream().
                filter(aEdge -> aEdge.getSourceNode() == nodeToRemove || aEdge.getTargetNode() == nodeToRemove).
                toArray(AEdge[]::new)));
    }

    private AEdge getEdgeByNode(ANode sourceID, ANode targetID) {
        try {
            return edges.stream().filter(edge -> (edge.getSourceNode().getId() == sourceID.getId()) && (edge.getTargetNode().getId() == targetID.getId())).findFirst().get();
        } catch (NoSuchElementException ignored) {
        }
        return null;
    }

    public void resetGraph() {
        removeNodes(nodes.toArray(new ANode[nodes.size()]));
    }

    private void setupEmptyListener(){
      nodes.addListener((ListChangeListener<ANode>) c -> isEmptyProperty.setValue(nodes.size() == 0));
      nodes.addListener((ListChangeListener<ANode>) c-> sizeProperty.setValue(nodes.size()+""));
    }

    public SimpleBooleanProperty isEmptyPropertyProperty() {
        return isEmptyProperty;
    }


    @Override
    public String toString() {
        StringBuilder collector = new StringBuilder("");
        if (nodes.size() == 0)
            return collector.append("Graph is Empty").toString();
        nodes.forEach(nodes -> {
            collector.append(nodes.toString());
            collector.append("\n");
        });
        return collector.toString();
    }
}
