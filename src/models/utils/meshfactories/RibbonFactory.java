package models.utils.meshfactories;

import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import models.ANode;
import models.APeptide;
import utils.ArrayHelper;
import views.graphviews.AGraphView3D;
import views.utils.meshes.AcidTriangleMesh;
import views.utils.textures.AcidTextureLoader;

import java.util.ArrayList;

public class RibbonFactory{

    public static AcidTriangleMesh generateMesh(ObservableList<ANode> nodes, APeptide peptide) {
        if(nodes.size() == 0)
            return null;
        ArrayList<Float> pointList = new ArrayList<>();
        ArrayList<Integer> facesList = new ArrayList<>();
        ArrayList<Integer> alternateFacesList = new ArrayList<>();
        ArrayList<ANode> filteredList = new ArrayList<>();
        nodes.stream().
                filter(aNode -> ((aNode.getAtomName().equals("CB")||
                        aNode.getAtomName().equals("CA")) &&
                        !aNode.getAcidType() .equals("G") )||
                    (aNode.getAtomName().equals("C")||
                                aNode.getAtomName().equals("O")) &&
                        aNode.getAcidType() .equals("G") ).
                forEach(filteredList::add);
        fillPointList(pointList, filteredList);
        fillFacesList(pointList, facesList,alternateFacesList,filteredList,peptide);
        int[] smoothGroup = new int[facesList.size()/6];
        for (int i = 0; i < smoothGroup.length; i+=8) {
            int frontSide = i%2==0?1:2;
            int backSide = i%2!=0?2:1;
            smoothGroup[i] = frontSide;
            smoothGroup[i+1] = frontSide;
            smoothGroup[i+2] = frontSide;
            smoothGroup[i+3] = frontSide;

            smoothGroup[i+4] = backSide;
            smoothGroup[i+5] = backSide;
            smoothGroup[i+6] = backSide;
            smoothGroup[i+7] = backSide;
        }

        float[] texCoords= AcidTextureLoader.getAcidTexCoordinates();
        AcidTriangleMesh m = new AcidTriangleMesh();
        m.getPoints().addAll(ArrayHelper.toPrimitiveFloatArray(pointList));
        m.getFaces().addAll(facesList.stream().mapToInt(i -> i).toArray());
        m.getAlternateFaces().addAll(alternateFacesList);
        m.getTexCoords().addAll(texCoords);
        m.getFaceSmoothingGroups().addAll(smoothGroup);
        return m;
    }

    private static void fillFacesList(ArrayList<Float> pointList, ArrayList<Integer> facesList,
                                      ArrayList<Integer> alternateFacesList,ArrayList<ANode> filteredList,
                                      APeptide peptide) {
        boolean isRenderedOnce = false;
        int counter = 0;
        for(int i = 0 ; i < (pointList.size()/3.0) - 5; i+=3){

            //<Magic>
            if(isRenderedOnce){
                isRenderedOnce = false;
                counter += 2;
            }else
                isRenderedOnce = true;
            //</Magic>
            String currentAcidType = filteredList.get(counter).getAcidType();
            String currentStructureType = peptide.getStructureTypeForNode(filteredList.get(counter));
            currentStructureType = enlargeStructureString(currentStructureType);
            counter++;
            int[] texCoords = AcidTextureLoader.getAcidTextureStartId(currentAcidType);
            int[] structureTexCoords = AcidTextureLoader.getAcidTextureStartId(currentStructureType);

            //clockwise acidType
            addPointToFaces(facesList,i,i+2,i+3,texCoords);
            addPointToFaces(facesList,i,i+5,i+1,texCoords);
            addPointToFaces(facesList,i+2,i+4,i+3,texCoords);
            addPointToFaces(facesList,i,i+3,i+5,texCoords);
            //clockwise structureType
            addPointToFaces(alternateFacesList,i,i+2,i+3,structureTexCoords);
            addPointToFaces(alternateFacesList,i,i+5,i+1,structureTexCoords);
            addPointToFaces(alternateFacesList,i+2,i+4,i+3,structureTexCoords);
            addPointToFaces(alternateFacesList,i,i+3,i+5,structureTexCoords);

            //counter clockwise
            addPointToFaces(facesList,i,i+3,i+2,texCoords);
            addPointToFaces(facesList,i,i+1,i+5,texCoords);
            addPointToFaces(facesList,i+2,i+3,i+4,texCoords);
            addPointToFaces(facesList,i,i+5,i+3,texCoords);
            //counter clockwise structure type
            addPointToFaces(alternateFacesList,i,i+3,i+2,structureTexCoords);
            addPointToFaces(alternateFacesList,i,i+1,i+5,structureTexCoords);
            addPointToFaces(alternateFacesList,i+2,i+3,i+4,structureTexCoords);
            addPointToFaces(alternateFacesList,i,i+5,i+3,structureTexCoords);
        }
    }

    private static void addPointToFaces(ArrayList<Integer>facesList,int first,int second, int third, int[] texCoords){
        facesList.add(first);
        facesList.add(texCoords[0]);
        facesList.add(second);
        facesList.add(texCoords[1]);
        facesList.add(third);
        facesList.add(texCoords[2]);
    }

    private static void fillPointList(ArrayList<Float> pointList, ArrayList<ANode> filteredList) {
        int idx = 0;
        for(ANode node : filteredList){
            Point3D currentPoint = new Point3D(node.getxCoord(),node.getyCoord(),node.getzCoord());
            addPointToList(pointList,currentPoint.multiply(AGraphView3D.VIEW_SCALING));
            if(!node.getAcidType().equals("G")) {
                if (node.getAtomName().equals("CB")) {
                    ANode prevNode;
                    if (idx == 0)
                        prevNode = filteredList.get(idx);
                    else
                        prevNode = filteredList.get(idx - 1);

                    Point3D cA = new Point3D(prevNode.getxCoord(),
                            prevNode.getyCoord(),
                            prevNode.getzCoord());
                    Point3D cB = new Point3D(node.getxCoord(), node.getyCoord(), node.getzCoord());
                    addPointToList(pointList, (cA.add(cA.subtract(cB))).multiply(AGraphView3D.VIEW_SCALING));
                }
            }else{
                if (node.getAtomName().equals("O")) {
                    ANode prevNode;
                    if (idx == 0)
                        prevNode = filteredList.get(idx);
                    else
                        prevNode = filteredList.get(idx - 1);

                    Point3D cA = new Point3D(prevNode.getxCoord(),
                            prevNode.getyCoord(),
                            prevNode.getzCoord());
                    Point3D cB = new Point3D(node.getxCoord(), node.getyCoord(), node.getzCoord());
                    addPointToList(pointList, (cA.add(cA.subtract(cB))).multiply(AGraphView3D.VIEW_SCALING));
                }
            }
            idx++;
        }
    }

    private static void addPointToList(ArrayList<Float> l, Point3D p){
        l.add((float) p.getX());
        l.add((float) p.getY());
        l.add((float) p.getZ());
    }

    private  static String enlargeStructureString(String structure){
        switch (structure){
            case "H":
                return "Helix";
            case "E":
                return "Sheet";
            case "-":
                return "Bone";
                default:
                    return "Bone";
        }
    }
}
