package models;

@SuppressWarnings({"unused", "WeakerAccess"})
public class AHelixContainer {
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

    AHelixContainer(String r, int s, String hId, String iRN, String iCID, int iSeqNum, String iICode, String eRName,
                    String eCID, int eSeqNum, String eICode, int hClass, String comment, int length){
        recordName = r;
        serNum = s;
        helixID = hId;
        initResName = iRN;
        initChainID = iCID;
        initSeqNum = iSeqNum;
        initICode = iICode;
        endResName = eRName;
        endChainID = eCID;
        endSeqNum = eSeqNum;
        endICode = eICode;
        helixClass = hClass;
        this.comment = comment;
        this.length = length;
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


