package models;

@SuppressWarnings("unused")
public class ASheetContainer {

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

    ASheetContainer(String recordName, int strand, String sheetID, int numStrands, String initResName,
                    String initChainID, int initSeqNum, String initICode, String endResName, String endChainID,
                    int endSeqNum, String endICode, int sense, String curAtom, String curResName,
                    String curChainId, String curResSeq, String curICode, String prevAtom, String prevResName,
                    String prevChainId, int prevResSeq, String prevICode) {
        this.recordName = recordName;
        this.strand = strand;
        this.sheetID = sheetID;
        this.numStrands = numStrands;
        this.initResName = initResName;
        this.initChainID = initChainID;
        this.initSeqNum = initSeqNum;
        this.initICode = initICode;
        this.endResName = endResName;
        this.endChainID = endChainID;
        this.endSeqNum = endSeqNum;
        this.endICode = endICode;
        this.sense = sense;
        this.curAtom = curAtom;
        this.curResName = curResName;
        this.curChainId = curChainId;
        this.curResSeq = curResSeq;
        this.curICode = curICode;
        this.prevAtom = prevAtom;
        this.prevResName = prevResName;
        this.prevChainId = prevChainId;
        this.prevResSeq = prevResSeq;
        this.prevICode = prevICode;
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
