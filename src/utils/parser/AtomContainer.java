package utils.parser;

import javafx.geometry.Point3D;
import utils.AcidTranslator;

import java.util.Arrays;

/**
 * Copyright 2018 Denis Heid(denis.heid@student.uni-tuebingen.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AtomContainer extends ParsedConstruct{

    static final int[][] splitIndices = {
            {0,6},{6,11},{12,16},
            {16,17},{17,20},{21,22},
            {22,26}, {26,27}, {30,38},
            {38,46}, {46,54}, {54,60},
            {60,66}, {72,76}, {76,78},
            {78,79}
    };
    private static final String[] validBackBoneAtoms = {"N","O","C","CA"};
    private static final String[] invalidAtoms ={"H"};

    private Integer id;
    private String atomName;
    private String atomSymbol;
    private Point3D coordinate;
    private String acid;
    private String acidId;

    private String alternate;
    private String chain;
    private String iCode;

    private boolean isSideChain;

    AtomContainer(){
        super(ConstructType.Atom);
    }

    @Override
    boolean setValuesBySplit(String[] split){
        id = Integer.parseInt(split[1]);
        atomName = split[2].trim();
        alternate = split[3].trim();
        //set default if not available
        if(alternate.equals(""))
            alternate="A";
        acid = AcidTranslator.getOneLetterCode(split[4].trim())+"";
        chain = split[5].trim();
        acidId = split[6].trim();
        iCode = split[7].trim();
        Double xCoord = Double.parseDouble(split[8]);
        Double yCoord = Double.parseDouble(split[9]);
        Double zCoord = Double.parseDouble(split[10]);
        atomSymbol = split[14].trim();
        coordinate = new Point3D(xCoord,yCoord,zCoord);
        isSideChain = !isAtomSymbolValid(atomName);
        return isAtomSymbolParsed(atomName);
        //return isAtomSymbolValid(atomName);
    }

    private static boolean isAtomSymbolValid(String symbol){
        return Arrays.stream(validBackBoneAtoms).anyMatch(s -> s.equals(symbol));
    }

    private static boolean isAtomSymbolParsed(String symbol){
        return Arrays.stream(invalidAtoms).noneMatch(s->s.equals(symbol));
    }

    void setAtomName(String name){
        atomName= name;
    }

    public Integer getId() {
        return id;
    }

    public Point3D getCoordinate() {
        return coordinate;
    }

    public String getAtomName() {
        return atomName;
    }

    public String getAtomSymbol() {
        return atomSymbol;
    }

    public String getAcid() {
        return acid;
    }

    public String getAcidId() {
        return acidId;
    }

    boolean isSideChain() {
        return isSideChain;
    }

    String getChain() {
        return chain;
    }

    void setIsSideChain(boolean b){
        isSideChain = b;
    }

    String getAlternate() {
        return alternate;
    }

    String getiCode() {
        return iCode;
    }
}
