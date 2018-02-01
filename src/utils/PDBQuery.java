package utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PDBQuery extends Service<List<String>> {

    public static final String PDB_QUERY_URL = "http://www.rcsb.org/pdb/rest/customReport.csv?pdbids=*&customReportColumns=structureId,macromoleculeType,classification&format=csv&service=wsfile";

    private final StringProperty url = new SimpleStringProperty();
    private final BooleanProperty isPeptide = new SimpleBooleanProperty();

    public final void setUrl(String url){
        this.url.setValue(url);
    }

    public final void setIsPeptide(boolean isPeptide){
        this.isPeptide.setValue(isPeptide);
    }

    private static List<String> queryPDBServer(String url)throws IOException {
        final HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        final List<String> lines = new ArrayList<>();

        try(BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String line;
            while((line = rd.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static List<String> filterList(List<String> list){
        List<String> filteredList = new ArrayList<>();
        for(String s : list){
            String replaced = s.replaceAll("\"","");
            String[] split = replaced.split(",");
            if(split[1].equals("Protein")){
                String prot;
                if(split.length ==3)
                    prot = split[0]+"  "+split[2];
                else
                    prot = split[0]+"   "+split[1];
                filteredList.add(prot);
            }

        }
        return filteredList;
    }

    @Override
    protected Task<List<String>> createTask() {
        return new Task<List<String>>(){
            @Override
            protected  List<String> call() throws IOException{
                List<String> l= queryPDBServer(url.getValue());
                if(!isPeptide.getValue())
                    return filterList(l);
                else
                    return l;
            }
        };
    }
}


