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
package views.charts;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import models.APeptide;
import utils.AcidTranslator;

@SuppressWarnings("unchecked")
public class AcidChartFactory {

    public static BarChart<String,Number> generateBarChart(APeptide peptide){
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setTickUnit(1);
        final BarChart<String,Number> bc =
                new BarChart<>(xAxis,yAxis);
        bc.setTitle("Amino Acid Summary");
        xAxis.setLabel("Acid");
        yAxis.setLabel("Amount");

        XYChart.Series series = new XYChart.Series();
        series.setName("Acid Type");
        for(String acidType : AcidTranslator.getAcidTypeAsList()){
            series.getData().add(generateData(acidType,peptide));
        }
        bc.getData().add(series);
        return bc;
    }

    private static XYChart.Data generateData(String acid, APeptide p){
        int coudnt = p.getAcidCount(acid);
        return new XYChart.Data(acid, coudnt);
    }
}
