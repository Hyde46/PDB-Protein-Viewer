package utils.parser;

public class SheetContainer extends ParsedConstruct {

    static final int[][] splitIndices = {
        {0,6},{7,10},{11,14},{14,16},
        {17,20},{21,22},{22,26},{26,27},
        {28,31},{32,33},{33,37},{37,38},
        {38,40},{41,45},{45,48},{49,50},
        {50,54},{54,55},{56,60},{60,63},
        {64,65},{65,69},{69,70}
    };

    private String recordName;
    private int strand;
    private String sheetID;
    private int numStrands;
    private String initResName;
    private String initChainID;
    private int initSeqNum;
    private String initICode;
    private String endResName;
    private String endChainID;
    private int endSeqNum;
    private String endICode;
    private int sense;
    private String curAtom;
    private String curResName;
    private String curChainId;
    private String curResSeq;
    private String curICode;
    private String prevAtom;
    private String prevResName;
    private String prevChainId;
    private int prevResSeq;
    private String prevICode;

    public SheetContainer() {
        super(ConstructType.Sheet);
    }

    @Override
    boolean setValuesBySplit(String[] line) {
        recordName = line[0];
        if(!line[1].equals(""))
            strand = Integer.parseInt(line[1]);
        sheetID = line[2];
        if(!line[3].equals(""))
            numStrands = Integer.parseInt(line[3]);
        initResName = line[4];
        initChainID = line[5];
        if(!line[6].equals(""))
            initSeqNum = Integer.parseInt(line[6]);
        initICode = line[7];
        endResName = line[8];
        endChainID = line[9];
        if(!line[10].equals(""))
            endSeqNum = Integer.parseInt(line[10]);
        endICode = line[11];
        if(!line[12].equals(""))
            sense = Integer.parseInt(line[12]);
        curAtom = line[13];
        curResName = line[14];
        curChainId = line[15];
        curResSeq = line[16];
        curICode = line[17];
        prevAtom = line[18];
        prevResName = line[19];
        prevChainId = line[20];
        if(!line[21].equals(""))
            prevResSeq = Integer.parseInt(line[21]);
        prevICode = line[22];

        return true;
    }

    public String getRecordName() {
        return recordName;
    }

    public int getStrand() {
        return strand;
    }

    public String getSheetID() {
        return sheetID;
    }

    public int getNumStrands() {
        return numStrands;
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

    public int getSense() {
        return sense;
    }

    public String getCurAtom() {
        return curAtom;
    }

    public String getCurResName() {
        return curResName;
    }

    public String getCurChainId() {
        return curChainId;
    }

    public String getCurResSeq() {
        return curResSeq;
    }

    public String getCurICode() {
        return curICode;
    }

    public String getPrevAtom() {
        return prevAtom;
    }

    public String getPrevResName() {
        return prevResName;
    }

    public String getPrevChainId() {
        return prevChainId;
    }

    public int getPrevResSeq() {
        return prevResSeq;
    }

    public String getPrevICode() {
        return prevICode;
    }
}
