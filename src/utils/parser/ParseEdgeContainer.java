package utils.parser;

public class ParseEdgeContainer {

    private final int sourceID;
    private final int targetID;


    ParseEdgeContainer(int src, int target){
        this.sourceID = src;
        this.targetID = target;
    }

    public int getTargetID() {
        return targetID;
    }

    public int getSourceID() {
        return sourceID;
    }
}
