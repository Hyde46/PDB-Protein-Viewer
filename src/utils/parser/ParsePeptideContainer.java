package utils.parser;

import javafx.util.Pair;
import utils.AcidTranslator;

import java.util.ArrayList;

public class ParsePeptideContainer {

    private String primaryStructure;
    private String secondaryStructure;
    private final ArrayList<ParseEdgeContainer> edges = new ArrayList<>();
    private final ArrayList<ParseAcidContainer> acids = new ArrayList<>();
    private final ArrayList<HelixContainer> helixes = new ArrayList<>();
    private final ArrayList<SheetContainer> sheets = new ArrayList<>();
    private final ArrayList<AtomContainer> atoms = new ArrayList<>();

    boolean addParsedConstruct(ParsedConstruct construct){
        switch (construct.getType()){
            case Atom:
                if(((AtomContainer)construct).getChain().equals("A") &&
                        ((AtomContainer)construct).getAlternate().equals("A") &&
                        ((AtomContainer)construct).getiCode().equals(""))
                    atoms.add((AtomContainer)construct);
                else
                    return false;
                break;
            case Sheet:
                sheets.add((SheetContainer)construct);
                break;
            case Helix:
                helixes.add((HelixContainer)construct);
                break;
            case Ter:
                return true;
                default:
                    return false;
        }
        return false;
    }

    private void generateSecondaryStructure(){
        StringBuilder b = new StringBuilder("");
        for(int i = 0; i < primaryStructure.length(); ++i)
            b.append("-");
        secondaryStructure = b.toString();
        generateHelixStructure();
        generateSheetStructure();
    }

    private void generateHelixStructure(){
        for(HelixContainer helix : helixes){
            char[] secStruc = secondaryStructure.toCharArray();
            for(int i = 0; i < secStruc.length; ++i)
                if(i >= helix.getInitSeqNum() && i<= helix.getEndSeqNum())
                    secStruc[i] = 'H';
            secondaryStructure = new String(secStruc);
        }
    }

    private void generateSheetStructure(){
        for(SheetContainer sheet : sheets){
            char[] secStruc = secondaryStructure.toCharArray();
            for(int i = 0; i < secStruc.length; ++i)
                if(i >= sheet.getInitSeqNum() && i<= sheet.getEndSeqNum())
                    secStruc[i] = 'E';
            secondaryStructure = new String(secStruc);
        }
    }

    private void generatePrimaryStructure(){
        StringBuilder builder = new StringBuilder("");
        acids.forEach(s->builder.append(s.getAcid()));
        primaryStructure = builder.toString();
    }

    void buildPeptide(){
        if(atoms.size()== 0){
            System.err.println("Something went wrong while parsing");
        }
        String currentBuildingAcidId = atoms.get(0).getAcidId();
        ParseAcidContainer acid = new ParseAcidContainer(atoms.get(0).getAcid(),atoms.get(0).getAcidId());

        AtomContainer lastAtom = null;
        int lastId = -1;

        for(AtomContainer atom : atoms){
            String atomAcidId = atom.getAcidId();

            if(!atomAcidId.equals(currentBuildingAcidId)){ //Starting a new Acid
                acids.add(acid);
                if(lastAtom!=null)
                    lastId = lastAtom.getId();
                buildEdges(acid,lastId);
                lastAtom = acid.getBackbone().get(acid.getBackbone().size()-2);
                acid = new ParseAcidContainer(atom.getAcid(),atom.getAcidId());
                currentBuildingAcidId = atomAcidId;
            }
            if(atom.isSideChain()) { //always starts with CB, should catch the N in sidechain
                atom.setIsSideChain(true);
                acid.addSideChain(atom);
            }else
                acid.addBackbone(atom);

        }
        acids.add(acid);
        assert lastAtom != null;
        lastId = lastAtom.getId();
        buildEdges(acid,lastId);
        generatePrimaryStructure();
        generateSecondaryStructure();
    }

    private void buildEdges(ParseAcidContainer currentAcid, int lastAtomID){
        ArrayList<AtomContainer> atoms = new ArrayList<>(currentAcid.getBackbone());
        atoms.addAll(currentAcid.getSideChain());
        ArrayList<Pair<String,String>> connections = AcidTranslator.getSideChainConnections(currentAcid.getAcid());
        AtomContainer previousAtom = atoms.get(0);

        if(lastAtomID != -1)
            edges.add(new ParseEdgeContainer(lastAtomID,previousAtom.getId()));

        for(AtomContainer atom : atoms) {
            if(atom.getAtomName().equals("OXT"))
                atom.setAtomName("O");
            for (Pair<String, String> connection : connections) {
                if (connection.getValue().equals(atom.getAtomName())) {
                    //noinspection ConstantConditions
                    previousAtom = atoms.stream().filter(atomContainer -> atomContainer.getAtomName().equals(connection.getKey())).findFirst().get();
                    edges.add(new ParseEdgeContainer(previousAtom.getId(), atom.getId()));
                }
            }
        }
    }

    public ArrayList<ParseAcidContainer> getAtoms() {
        return acids;
    }

    public ArrayList<ParseEdgeContainer> getEdges() {
        return edges;
    }

    public String getPrimaryStructure() {
        return primaryStructure;
    }

    public String getSecondaryStructure() {
        return secondaryStructure;
    }

    public ArrayList<HelixContainer> getHelixContainer(){ return helixes;}

    public ArrayList<SheetContainer> getSheets() {
        return sheets;
    }
}
