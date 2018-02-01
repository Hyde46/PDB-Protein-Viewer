package utils.parser;

import java.util.ArrayList;

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
