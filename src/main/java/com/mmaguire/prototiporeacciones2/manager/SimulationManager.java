package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.model.DatosComponente;
import com.mmaguire.prototiporeacciones2.model.Punto;
import com.mmaguire.prototiporeacciones2.model.Simulacion;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class SimulationManager {

    public static void fillSimulationChart(LineChart<Number,Number> lineChart, Simulacion simulacion) {
        lineChart.setCreateSymbols(false);
        for (DatosComponente datosComponente : simulacion.getDatos()) {
            XYChart.Series<Number,Number> serie = new XYChart.Series<>();
            serie.setName(datosComponente.getTitulo());
            for (Punto punto : datosComponente.getDatos()) {
                serie.getData().add(new XYChart.Data<>(punto.getX(), punto.getY()));
            }
            lineChart.getData().add(serie);
        }
    }


}
