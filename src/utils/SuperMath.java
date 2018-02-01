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
package utils;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import views.graphviews.AGraphView3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperMath {

    public static void rotateGraphElement(AGraphView3D toRotate, double angleInDegrees, Point3D rotationAxis){
        Point3D pivot = toRotate.getCenterOfGeometry();
        Rotate rotate = new Rotate();
        rotate.setAngle(angleInDegrees);
        rotate.setAxis(rotationAxis);
        rotate.setPivotX(pivot.getX());
        rotate.setPivotY(pivot.getY());
        rotate.setPivotZ(pivot.getZ());
        toRotate.getTransformProperty().setValue(
                rotate.createConcatenation(toRotate.getTransformProperty().getValue())
        );
    }

    public static Double focalDistance(Double fov, Double graphHeight, Double viewPortHeight) {
        Double viewPortFocalDistance = (viewPortHeight/2.0)*Math.atan(fov/2.0);
        return graphHeight*viewPortFocalDistance/viewPortHeight + graphHeight;
    }

    @SafeVarargs
    public static Point3D calculateMeanForLists(List<Point3D>... lists){
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

    /*
    1D convolution with equal weight distribution
     */
    public static List<Point3D> convolve1D(List<Point3D> list , @SuppressWarnings("SameParameterValue") int windowSize){
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
