package utils.parser;

import java.util.ArrayList;

public class ParseAcidContainer {

    private final ArrayList<AtomContainer> backbone = new ArrayList<>();
    private final ArrayList<AtomContainer> sideChain = new ArrayList<>();

    private final String acid;
    private final String acidId;

    ParseAcidContainer(String acid, String id ){
        this.acid = acid;
        this.acidId = id;
    }

    public String getAcidId() {
        return acidId;
    }

    void addBackbone(AtomContainer a){
        backbone.add(a);
    }

    void addSideChain(AtomContainer a){ sideChain.add(a);}

    public ArrayList<AtomContainer> getBackbone() {
        return backbone;
    }

    public String getAcid() {
        return acid;
    }

    public ArrayList<AtomContainer> getSideChain() {
        return sideChain;
    }
}
