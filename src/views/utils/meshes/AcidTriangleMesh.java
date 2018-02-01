package views.utils.meshes;

import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

public class AcidTriangleMesh extends TriangleMesh {

    private final ArrayList<Integer> alternateFaces = new ArrayList<>();

    public AcidTriangleMesh(){
        super();
    }

    public ArrayList<Integer> getAlternateFaces(){
        return alternateFaces;
    }

    public void toggleFaces() {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (int i = 0; i < getFaces().size(); ++i)
            tmp.add(getFaces().get(i));
        getFaces().clear();
        for(Integer i : alternateFaces)
            getFaces().addAll(i);
        alternateFaces.clear();
        alternateFaces.addAll(tmp);
    }
}
