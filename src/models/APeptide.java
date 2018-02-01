package models;

import utils.AcidTranslator;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "ConstantConditions"})
public class APeptide {
    private ArrayList<AAcid> aminoAcids;
    private String primaryStructure;
    private String secondaryStructure;
    private ArrayList<AHelixContainer> helixes;
    private ArrayList<ASheetContainer> sheets;


    APeptide(ArrayList<AAcid> acids, String primaryStructure, String secondaryStructure, ArrayList<AHelixContainer> h,
             ArrayList<ASheetContainer> s){
        this.aminoAcids = acids;
        this.primaryStructure = primaryStructure;
        this.secondaryStructure = secondaryStructure;
        this.helixes = h;
        this.sheets = s;
    }

    public AAcid getAcidForNode(ANode node){
        for(AAcid acid : aminoAcids){
            if(acid.getAcidId().equals(node.getAcidId()))
                return acid;
        }
        return null;
    }

    public ArrayList<AAcid> getAminoAcids() {
        return aminoAcids;
    }

    public String getSequence(){
        StringBuilder builder = new StringBuilder();
        for(AAcid acid : aminoAcids){
            builder.append(acid.getAcidName());
        }
        return builder.toString();
    }

    public AAcid getAcidById(String acidId){
        return aminoAcids.stream().filter(aAcid -> aAcid.getAcidId().equals(acidId)).findFirst().get();
    }

    public String getStructureTypeForNode(ANode node){
        AAcid acid = getAcidForNode(node);
        int id = Integer.parseInt(acid.getAcidId());
        for(AHelixContainer helixContainer : helixes){
           if(id >= helixContainer.getInitSeqNum() && id <= helixContainer.getEndSeqNum())
               return "H";
        }
        for(ASheetContainer sheetContainer : sheets){
            if(id >= sheetContainer.getInitSeqNum() &&id <= sheetContainer.getEndSeqNum())
                return "E";
        }
        return "-";
    }

    public boolean isAtomInBackBone(ANode node){
        for(AAcid acid : aminoAcids){
            if(acid.getBackbone().stream().anyMatch(aNode -> aNode.getId() == node.getId()))
                return true;
        }
        return false;
    }

    public AAcid getNextAcid(String acidId){
        for(int i = 0; i<  aminoAcids.size(); ++i){
            if(aminoAcids.get(i).getAcidId().equals(acidId)){
                if(i<aminoAcids.size()-1){
                    return aminoAcids.get(i+1);
                }
            }
        }
        return null;
    }

    public int getAcidCount(String type){
        if(type.length() != aminoAcids.get(0).getAcidName().length())
            type = AcidTranslator.getOneLetterCode(type);
        final String filterType = type;
        return (int)aminoAcids.stream().filter(aAcid ->  aAcid.getAcidName().equals(filterType)).count();
    }

    private List<List<AAcid>> getSubStructure(String structure){
        List<List<AAcid>> subStructure = new ArrayList<>();
        List<AAcid> struc = new ArrayList<>();
        for(AAcid acid : aminoAcids){
            ANode node = acid.getBackbone().get(0);
            if(getStructureTypeForNode(node).equals(structure)){
                struc.add(acid);
            }else{
                if(struc.size() != 0){
                    subStructure.add(new ArrayList<>(struc));
                    struc.clear();
                }
            }

        }
        if(struc.size() != 0){
            subStructure.add(new ArrayList<>(struc));
            struc.clear();
        }
        return subStructure;
    }

    public List<List<AAcid>> getLoopAcids(){ return getSubStructure("-");}

    public List<List<AAcid>> getHelixAcids(){
       return getSubStructure("H");
    }

    public List<List<AAcid>> getSheetAcids(){
        return getSubStructure("E");
    }


    public String getPrimaryStructure() {
        return primaryStructure;
    }

    public String getSecondaryStructure() {
        return secondaryStructure;
    }

    public ArrayList<AHelixContainer> getHelixContainer() {
        return helixes;
    }

    public ArrayList<ASheetContainer> getSheetContainer() {
        return sheets;
    }
}

