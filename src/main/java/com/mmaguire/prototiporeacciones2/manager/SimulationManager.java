package com.mmaguire.prototiporeacciones2.manager;

import com.uppaal.model.core2.Data2D;
import com.uppaal.model.core2.DataSet2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.awt.geom.Point2D;

public class SimulationManager {

    public static void fillSimulationChart(LineChart<Number,Number> lineChart, DataSet2D data) {
        lineChart.setCreateSymbols(false);
        for (Data2D variableData : data) {
            XYChart.Series<Number,Number> serie = new XYChart.Series<>();
            serie.setName(variableData.getTitle());
            for (Point2D.Double punto : variableData) {
                serie.getData().add(new XYChart.Data<>(punto.x, punto.y));
            }
            lineChart.getData().add(serie);
        }
    }


}
