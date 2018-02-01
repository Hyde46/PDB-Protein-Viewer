/*
 This file is part of PDB-Protein-Viewer
 PDB-Protein-Viewer is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 PDB-Protein-Viewer is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with PDB-Protein-Viewer.  If not, see <http://www.gnu.org/licenses/>.
 */
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


