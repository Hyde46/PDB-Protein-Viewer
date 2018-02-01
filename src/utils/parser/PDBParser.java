package utils.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utils.parser.PDBParser.ParseMode.*;

public class PDBParser {

    protected enum ParseMode {Idle, Meta, Atom,Helix,Sheet, End, TER}

    public static ParsePeptideContainer parseFile(String pathToFile){
        // read file and convert to arrayList<String>
        ArrayList<String> list = new ArrayList<>();
        String singleLine;
        FileReader fileReader;
        try {
            fileReader = new FileReader(pathToFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((singleLine = bufferedReader.readLine()) != null) {
               list.add(singleLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Parse the actual list
        return parsePDB(list);
    }

    public static ParsePeptideContainer parsePDB(List<String> pdb){
        ParsePeptideContainer peptideContainer = new ParsePeptideContainer();
        for(String line : pdb){
            ParsedConstruct construct = parseSingleLine(line);
            if(construct != null)
                if(peptideContainer.addParsedConstruct(construct))
                    break;
        }
        peptideContainer.buildPeptide();
        return peptideContainer;
    }

    private static ParsedConstruct parseSingleLine(String line){
        if(line.equals(""))
            return null;
        switch (checkMode(line)){
            case Atom:
                return parseConstruct(line,AtomContainer.splitIndices,AtomContainer.class);
            case Helix:
                return parseConstruct(line, HelixContainer.splitIndices,HelixContainer.class);
            case Sheet:
                return parseConstruct(line, SheetContainer.splitIndices, SheetContainer.class);
            case TER:
                return null;
            default:
                return null;
        }
    }

    private static ParsedConstruct parseConstruct(String line, int[][] splitIndices, Class<? extends ParsedConstruct> constructClass){
        try {
            ParsedConstruct construct = constructClass.newInstance();
            String split[] = splitLine(line,splitIndices);
            if(construct.setValuesBySplit(split))
                return construct;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String[] splitLine(String line, int[][] splitIndices){
        String[] split = new String[splitIndices.length];
        for(int i = 0; i < splitIndices.length; ++i){
            if(splitIndices[i][0] <= line.length()-1 && splitIndices[i][1] <= line.length()-1)
                split[i] = line.substring(splitIndices[i][0],splitIndices[i][1]).trim();
        }
        return split;
    }

    private static ParseMode checkMode(String l){
        String[] line = l.split(" ");
        switch (line[0]) {
            case "HEADER":
                return Meta;
            case "ATOM":
                return Atom;
            case "END":
                return End;
            case "TER":
                return TER;
            case "HELIX":
                return Helix;
            case "SHEET":
                return Sheet;
        }
        return Idle;
    }

}
