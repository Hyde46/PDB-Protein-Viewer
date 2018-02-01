package utils.parser;

public class HelixContainer extends  ParsedConstruct{

    static final int[][] splitIndices = {
            {0,6},{7,10},{11,14},
            {15,18},{19,20},{21,25},
            {25,26},{27,30},{31,32},
            {33,37},{37,38},{38,40},
            {40,70},{71,76}
    };
    private String recordName;
    private int serNum;
    private String helixID;
    private String initResName;
    private String initChainID;
    private int initSeqNum;
    private String initICode;
    private String endResName;
    private String endChainID;
    private int endSeqNum;
    private String endICode;
    private int helixClass;
    private String comment;
    private int length;

    public HelixContainer() {
        super(ConstructType.Helix );
    }

    @Override
    boolean setValuesBySplit(String[] line) {
        recordName=line[0].trim();
        if(!line[1].equals(""))
            serNum = Integer.parseInt(line[1]);
        helixID = line[2];
        initResName = line[3];
        initChainID = line[4];
        if(!line[5].equals(""))
            initSeqNum = Integer.parseInt(line[5]);
        initICode = line[6];
        endResName = line[7];
        endChainID = line[8];
        if(!line[9].equals(""))
            endSeqNum = Integer.parseInt(line[9]);
        endICode = line[10];
        if(!line[11].equals(""))
            helixClass = Integer.parseInt(line[11]);
        comment = line[12];
        if(!line[12].equals(""))
            length = Integer.parseInt(line[13]);
        return true;
    }

    public String getRecordName() {
        return recordName;
    }

    public int getSerNum() {
        return serNum;
    }

    public String getHelixID() {
        return helixID;
    }

    public String getInitResName() {
        return initResName;
    }

    public String getInitChainID() {
        return initChainID;
    }

    public int getInitSeqNum() {
        return initSeqNum;
    }

    public String getInitICode() {
        return initICode;
    }

    public String getEndResName() {
        return endResName;
    }

    public String getEndChainID() {
        return endChainID;
    }

    public int getEndSeqNum() {
        return endSeqNum;
    }

    public String getEndICode() {
        return endICode;
    }

    public int getHelixClass() {
        return helixClass;
    }

    public String getComment() {
        return comment;
    }

    public int getLength() {
        return length;
    }
}
