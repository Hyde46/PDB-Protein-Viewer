package utils.parser;

abstract class ParsedConstruct {


    enum ConstructType {Atom,Helix,Sheet,Ter}

    private final ConstructType type;

    ParsedConstruct(ConstructType t){
        type = t;
    }

    ConstructType getType() {
        return type;
    }

    abstract boolean setValuesBySplit(String[] line);

}
