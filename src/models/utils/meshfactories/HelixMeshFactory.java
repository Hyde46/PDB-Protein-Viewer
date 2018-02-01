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
package models.utils.meshfactories;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import models.ANode;
import utils.ArrayHelper;
import utils.SuperMath;
import views.utils.meshes.AcidTriangleMesh;
import views.utils.textures.AcidTextureLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Heid(denis.heid@student.uni-tuebingen.de),
 *          Friederike Hanssen (denis.heid@student.uni-tuebingen.de)
 */
public class HelixMeshFactory{

    private static final List<Point3D> mainHelixPoints = new ArrayList<>();
    private static final List<Point3D> subHelixPositivePoints = new ArrayList<>();
    private static final List<Point3D> subHelixNegativePoints = new ArrayList<>();

    public static AcidTriangleMesh generateHelixMesh(List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints,
                                                     List<Point3D> nPoints, List<ANode> nodes, String type, int windowSize, int subSegmentCount, int sheetLength, int smoothFactor){
        mainHelixPoints.clear();
        subHelixPositivePoints.clear();
        subHelixNegativePoints.clear();
        if(cAlphaPoints.size() != cPoints.size() || cAlphaPoints.size() !=oPoints.size() || cAlphaPoints.size() != nPoints.size()){
            System.err.println("Input is garbage.");
            return null;
        }else if(cAlphaPoints.size() == 0) {
            return null;
        }
        final int numHelixSegments = cAlphaPoints.size();
        double deltaAnglePerSegment;

        for(int helixSegment = 0; helixSegment  < numHelixSegments-1 ; ++helixSegment){
            final int startIndex = helixSegment < windowSize? 0: helixSegment-windowSize;
            final int endIndex = helixSegment >= (numHelixSegments-windowSize)? numHelixSegments : helixSegment + windowSize;
            final int startIndexNextSegment = helixSegment+1 < windowSize? startIndex+1: (helixSegment+1)-windowSize;
            final int endIndexNextSegment = helixSegment+1 > (numHelixSegments-windowSize)? numHelixSegments : helixSegment + 1 + windowSize;

            final Point3D tubeVectorNormal = calculateTubeNormal(oPoints.subList(startIndex,endIndex),
                                                    cPoints.subList(startIndex,endIndex));
            final Point3D tubeNextVectorNormal = calculateTubeNormal(oPoints.subList(startIndexNextSegment,endIndexNextSegment),
                                                                        cPoints.subList(startIndexNextSegment,endIndexNextSegment));

            final Point3D currentProjectedPoint = computeProjectedPoint(cAlphaPoints,cPoints,oPoints,nPoints,tubeVectorNormal,startIndex,endIndex,helixSegment);
            final Point3D nextProjectedPoint = computeProjectedPoint(cAlphaPoints,cPoints,oPoints,nPoints,tubeNextVectorNormal,startIndexNextSegment,endIndexNextSegment,helixSegment+1);

            final Point3D r = cAlphaPoints.get(helixSegment).subtract(currentProjectedPoint).normalize();
            final Point3D rPrime = cAlphaPoints.get(helixSegment+1).subtract(nextProjectedPoint).normalize();
            deltaAnglePerSegment = Math.acos(r.dotProduct(rPrime)) * 180.0/Math.PI;
            mainHelixPoints.addAll(calculateSegmentPoints(deltaAnglePerSegment, subSegmentCount,
                    currentProjectedPoint,nextProjectedPoint,cAlphaPoints.get(helixSegment), cAlphaPoints.get(helixSegment+1),tubeVectorNormal));

            subHelixPositivePoints.addAll(calculateSegmentPoints(deltaAnglePerSegment, subSegmentCount,
                    currentProjectedPoint.add(tubeVectorNormal.multiply(sheetLength)),
                    nextProjectedPoint.add(tubeNextVectorNormal.multiply(sheetLength)),
                    cAlphaPoints.get(helixSegment).add(tubeVectorNormal.multiply(sheetLength)),
                    cAlphaPoints.get(helixSegment+1).add(tubeNextVectorNormal.multiply(sheetLength)),
                    tubeVectorNormal));
            subHelixNegativePoints.addAll(calculateSegmentPoints(deltaAnglePerSegment, subSegmentCount,
                    currentProjectedPoint.add(tubeVectorNormal.multiply(-sheetLength)),
                    nextProjectedPoint.add(tubeNextVectorNormal.multiply(-sheetLength)),
                    cAlphaPoints.get(helixSegment).add(tubeVectorNormal.multiply(-sheetLength)),
                    cAlphaPoints.get(helixSegment+1).add(tubeNextVectorNormal.multiply(-sheetLength)),
                    tubeVectorNormal));
        }
        //mainHelixPoints.addAll(new ArrayList<>(subHelixPositivePoints));
        //return mainHelixPoints;
        for(int i = 0; i< smoothFactor;i++)  smoothPoints();
        return generateMeshFromPoints(nodes,type,subSegmentCount);
    }

    private static Point3D computeProjectedPoint(List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints,
                                                 List<Point3D> nPoints,Point3D normal,int startIndex, int endIndex, int helixSegment){
        final Point3D meanInTubePoint = SuperMath.calculateMeanForLists(cAlphaPoints.subList(startIndex,endIndex),
                cPoints.subList(startIndex,endIndex),
                oPoints.subList(startIndex,endIndex),
                nPoints.subList(startIndex,endIndex));
        Point3D aVector = cAlphaPoints.get(helixSegment).subtract(meanInTubePoint);

        final double aOneMagnitude = aVector.dotProduct(normal);
        return meanInTubePoint.add(normal.multiply(aOneMagnitude));
    }

    @SuppressWarnings("unused")
    private static List<Point3D> calculateSegmentPoints(double deltaAnglePerSegment, double subSegmentCount,
                                                        Point3D projectedPoint, Point3D nextProjectedPoint, Point3D cAlphaPoint,
                                                        Point3D nextCAlphaPoint, Point3D rotationAxis){
        final double degreesPerSegment = deltaAnglePerSegment / subSegmentCount;
        final List<Point3D> segmentPoints = new ArrayList<>();
        final Point3D segmentVector = rotationAxis.multiply(projectedPoint.subtract(nextProjectedPoint).magnitude()/(subSegmentCount-1));

        double alphaOffset = 0;
        double alphaSegment = 1;
        final double alphaStep = 1.0/(subSegmentCount);

        for(int i = 0; i < subSegmentCount-1; ++i,alphaOffset+=alphaStep, alphaSegment-=alphaStep){
            final Rotate rotate = new Rotate();
            double nextAngle = i*degreesPerSegment;// + currentAngle;
            nextAngle = nextAngle >= 360? nextAngle%360 : nextAngle;
            rotate.setAngle(nextAngle);
            rotate.setAxis(rotationAxis);
            rotate.setPivotX(projectedPoint.getX());
            rotate.setPivotY(projectedPoint.getY());
            rotate.setPivotZ(projectedPoint.getZ());
            Point3D pointOnCircle = rotate.transform(cAlphaPoint.getX(),cAlphaPoint.getY(),cAlphaPoint.getZ());


            // Approximation, more edges
            pointOnCircle = pointOnCircle.add(segmentVector.multiply(i));//.multiply(alphaSegment));
            Point3D correctionVector = nextCAlphaPoint.subtract(pointOnCircle);
            pointOnCircle = pointOnCircle.add(correctionVector.multiply(alphaOffset));
            segmentPoints.add(pointOnCircle);
            //Smoother, more realistic
            //segmentPoints.add(pointOnCircle.add(segmentVector.multiply(i)));

        }

        return segmentPoints;
    }

    private static AcidTriangleMesh generateMeshFromPoints(List<ANode> nodes,String structureType,int subsegmentCount){
        AcidTriangleMesh mesh = new AcidTriangleMesh();
        ArrayList<Float> pointList = new ArrayList<>();
        ArrayList<Integer> facesList = new ArrayList<>();
        ArrayList<Integer> alternateFacesList = new ArrayList<>();
        float[] texCoords = AcidTextureLoader.getAcidTexCoordinates();
        for(int i = 0; i < mainHelixPoints.size();++i){
            //addPointToPointList(pointList, mainHelixPoints.get(i), subHelixPositivePoints.get(i));
            addPointToPointList(pointList, mainHelixPoints.get(i), subHelixPositivePoints.get(i),subHelixNegativePoints.get(i));
        }
        int counter = 1;
        int subCounter = 0;
        for(int i = 0; i  < (pointList.size()/3)-5  ; i+=3){

            String currentAcidType = nodes.get(counter).getAcidType();

            int[] structureTexCoords = AcidTextureLoader.getAcidTextureStartId(currentAcidType);
            int[] alternateTexCoords = AcidTextureLoader.getAcidTextureStartId(structureType);

            subCounter+=3;
            if(subCounter >= 2*subsegmentCount){
                counter+=3;
                counter = counter >= nodes.size()-1?nodes.size()-1:counter;
                subCounter = 0;
            }

            //clockwise
            addPointToFaces(facesList,i,i+4,i+3,structureTexCoords);
            addPointToFaces(facesList,i,i+1,i+4,structureTexCoords);
            addPointToFaces(facesList,i,i+5,i+2,structureTexCoords);
            addPointToFaces(facesList,i,i+3,i+5,structureTexCoords);

            addPointToFaces(alternateFacesList,i,i+4,i+3,alternateTexCoords);
            addPointToFaces(alternateFacesList,i,i+1,i+4,alternateTexCoords);
            addPointToFaces(alternateFacesList,i,i+5,i+2,alternateTexCoords);
            addPointToFaces(alternateFacesList,i,i+3,i+5,alternateTexCoords);
            //counter clock wise
            addPointToFaces(facesList,i,i+3,i+4,structureTexCoords);
            addPointToFaces(facesList,i,i+4,i+1,structureTexCoords);
            addPointToFaces(facesList,i,i+2,i+5,structureTexCoords);
            addPointToFaces(facesList,i,i+5,i+3,structureTexCoords);

            addPointToFaces(alternateFacesList,i,i+3,i+4,alternateTexCoords);
            addPointToFaces(alternateFacesList,i,i+4,i+1,alternateTexCoords);
            addPointToFaces(alternateFacesList,i,i+2,i+5,alternateTexCoords);
            addPointToFaces(alternateFacesList,i,i+5,i+3,alternateTexCoords);
        }
        int[] smoothing = new int[facesList.size() / 6];
        for (int i = 0; i < smoothing.length; i+=8) {
            smoothing[i] = 1;
            smoothing[i+1] = 1;
            smoothing[i+2] = 1;
            smoothing[i+3] = 1;

            smoothing[i+4] = 2;
            smoothing[i+5] = 2;
            smoothing[i+6] = 2;
            smoothing[i+7] = 2;
        }
        mesh.getFaces().addAll(facesList.stream().mapToInt(i->i).toArray());
        mesh.getAlternateFaces().addAll(alternateFacesList);
        mesh.getPoints().addAll(ArrayHelper.toPrimitiveFloatArray(pointList));
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaceSmoothingGroups().addAll(smoothing);
        return mesh;
    }

    private static void addPointToFaces(ArrayList<Integer>facesList,int first,int second, int third, int[] texCoords){
        facesList.add(first);
        facesList.add(texCoords[0]);
        facesList.add(second);
        facesList.add(texCoords[1]);
        facesList.add(third);
        facesList.add(texCoords[2]);
    }

    private static void addPointToPointList(ArrayList<Float> pointList, Point3D mainHelix, Point3D secondHelix, Point3D negativeHelix) {
        pointList.add((float) mainHelix.getX());
        pointList.add((float) mainHelix.getY());
        pointList.add((float) mainHelix.getZ());

        pointList.add((float) secondHelix.getX());
        pointList.add((float) secondHelix.getY());
        pointList.add((float) secondHelix.getZ());

        pointList.add((float) negativeHelix.getX());
        pointList.add((float) negativeHelix.getY());
        pointList.add((float) negativeHelix.getZ());
    }

    private static void smoothPoints(){
        if(mainHelixPoints.size() <= 0 || subHelixPositivePoints.size() <=0 || subHelixNegativePoints.size() <= 0)
            return;
        List<Point3D> smoothedMainHelix = SuperMath.convolve1D(mainHelixPoints,2);
        List<Point3D> smoothedSideHelix = SuperMath.convolve1D(subHelixPositivePoints,2);
        List<Point3D> smoothedNegSideHelix = SuperMath.convolve1D(subHelixNegativePoints,2);
        mainHelixPoints.clear();
        mainHelixPoints.addAll(new ArrayList<>(smoothedMainHelix));
        subHelixPositivePoints.clear();
        subHelixPositivePoints.addAll(new ArrayList<>(smoothedSideHelix));
        subHelixNegativePoints.clear();
        subHelixNegativePoints.addAll(new ArrayList<>(smoothedNegSideHelix));
    }

    private static Point3D calculateTubeNormal(List<Point3D> oVectors, List<Point3D> cVectors){
        final List<Point3D> tmp = new ArrayList<>();
        for(int i = 0; i < oVectors.size(); ++i){
            tmp.add(oVectors.get(i).subtract(cVectors.get(i)));
        }
        //noinspection unchecked
        return SuperMath.calculateMeanForLists(tmp).normalize();
    }

}
