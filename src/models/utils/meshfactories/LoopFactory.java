package models.utils.meshfactories;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import views.graphviews.viewobjects.ALine3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Friederike Hanssen  Denis Heid
 */

public class LoopFactory {
    private static final List<ALine3D> myLines = new ArrayList<>();

    public static List<ALine3D> generateHelixMesh(List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints,
                                                   List<Point3D> nPoints, int windowSize, int subSegmentCount, int smoothFactor){
        myLines.clear();
        List<Point3D> temp = new ArrayList<>();
        if(cAlphaPoints.size() != cPoints.size() || cAlphaPoints.size() !=oPoints.size() || cAlphaPoints.size() != nPoints.size()){
            System.err.println("Input is GARBAGE");
            return null;
        }
        final int numHelixSegments = cAlphaPoints.size();
        double deltaAnglePerSegment;

        for(int helixSegment = 0 ; helixSegment  < numHelixSegments-1 ; ++helixSegment){
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
            Point3D rPrime = cAlphaPoints.get(helixSegment+1).subtract(nextProjectedPoint).normalize();
            deltaAnglePerSegment = Math.acos(r.dotProduct(rPrime)) * 180.0/Math.PI;

            temp.addAll(calculateSegmentPoints(deltaAnglePerSegment, subSegmentCount,
                    currentProjectedPoint,nextProjectedPoint,cAlphaPoints.get(helixSegment), cAlphaPoints.get(helixSegment+1),tubeVectorNormal));

        }
        for(int i = 0; i <smoothFactor; i++){
            List<Point3D> temptemp = smoothPoints(temp);
            if(temptemp != null) {
                temp = temptemp;
            }
        }
        for (int i = 1; i < temp.size(); i++)
            myLines.add(new ALine3D(new SimpleDoubleProperty(temp.get(i - 1).getX()), new SimpleDoubleProperty(temp.get(i - 1).getY()), new SimpleDoubleProperty(temp.get(i - 1).getZ()),
                    new SimpleDoubleProperty(temp.get(i).getX()), new SimpleDoubleProperty(temp.get(i).getY()), new SimpleDoubleProperty(temp.get(i).getZ())));

        return myLines;
    }

    private static Point3D computeProjectedPoint(List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints,
                                                 List<Point3D> nPoints,Point3D normal,int startIndex, int endIndex, int helixSegment){
        final Point3D meanInTubePoint = calculateMeanForLists(cAlphaPoints.subList(startIndex,endIndex),
                cPoints.subList(startIndex,endIndex),
                oPoints.subList(startIndex,endIndex),
                nPoints.subList(startIndex,endIndex));
        Point3D aVector = cAlphaPoints.get(helixSegment).subtract(meanInTubePoint);

        final double aOneMagnitude = aVector.dotProduct(normal);
        return meanInTubePoint.add(normal.multiply(aOneMagnitude));
    }

    private static List<Point3D> calculateSegmentPoints(double deltaAnglePerSegment, double subSegmentCount,
                                                        Point3D projectedPoint,Point3D nextProjectedPoint, Point3D cAlphaPoint,
                                                        Point3D nextCAlphaPoint, Point3D rotationAxis){
        final double degreesPerSegment = deltaAnglePerSegment / subSegmentCount;
        final List<Point3D> segmentPoints = new ArrayList<>();
        final Point3D segmentVector = rotationAxis.multiply(projectedPoint.subtract(nextProjectedPoint).magnitude()/(subSegmentCount-1));

        double alphaOffset = 0;
        double alphaSegment = 1;
        final double alphaStep = 1.0/(subSegmentCount);

        for(int i = 0; i < subSegmentCount-1; ++i,alphaOffset+=alphaStep, alphaSegment-=alphaStep){
            final Rotate rotate = new Rotate();
            double nextAngle = i*degreesPerSegment;
            nextAngle = nextAngle >= 360? nextAngle%360 : nextAngle;
            rotate.setAngle(nextAngle);
            rotate.setAxis(rotationAxis);
            rotate.setPivotX(projectedPoint.getX());
            rotate.setPivotY(projectedPoint.getY());
            rotate.setPivotZ(projectedPoint.getZ());
            Point3D pointOnCircle = rotate.transform(cAlphaPoint.getX(),cAlphaPoint.getY(),cAlphaPoint.getZ());
            Point3D offsetVector = nextCAlphaPoint.subtract(pointOnCircle);

            pointOnCircle = pointOnCircle.add(offsetVector.multiply(alphaOffset));
            pointOnCircle = pointOnCircle.add(segmentVector.multiply(i).multiply(alphaSegment));
            segmentPoints.add(pointOnCircle);
        }

        return segmentPoints;
    }

    private static Point3D calculateTubeNormal(List<Point3D> oVectors, List<Point3D> cVectors){
        final List<Point3D> tmp = new ArrayList<>();
        for(int i = 0; i < oVectors.size(); ++i){
            tmp.add(oVectors.get(i).subtract(cVectors.get(i)));
        }
        return calculateMeanForLists(tmp).normalize();
    }

    @SafeVarargs
    private static Point3D calculateMeanForLists(List<Point3D>... lists){
        Point3D mean = new Point3D(0.0,0.0,0.0);
        for(List<Point3D> l : lists){
            for(Point3D p : l){
                mean = mean.add(p);
            }
        }
        return mean.
                multiply( 1.0 / (Arrays.
                        stream(lists).
                        mapToInt(List::size).
                        sum()));
    }

    private static List<Point3D> smoothPoints(List<Point3D> temp){
        if(temp.size() <= 0)
            return null;
        return convolve1D(temp,2);
    }

    private static List<Point3D> convolve1D(List<Point3D> list , @SuppressWarnings("SameParameterValue") int windowSize){
        double sum = 2*windowSize;
        List<Point3D> smoothedPoints = new ArrayList<>();
        for(int i = 0; i < list.size(); ++i){
            Point3D smoothedPoint = new Point3D(0.0,0.0,0.0);
            if(i-windowSize > 0 && i + windowSize < list.size())
                for(int j = -windowSize; j< windowSize; j++){
                    smoothedPoint = smoothedPoint.add(list.get(j + i));
                }
            else
                smoothedPoint = list.get(i).multiply(sum);
            smoothedPoints.add(smoothedPoint.multiply(1.0/sum));
        }
        return smoothedPoints;
    }

}
