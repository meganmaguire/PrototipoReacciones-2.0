package com.mmaguire.prototiporeacciones2.controller;

import com.uppaal.engine.QueryResult;
import com.uppaal.model.core2.DataSet2D;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.stage.Stage;

import static com.mmaguire.prototiporeacciones2.manager.SimulationManager.fillSimulationChart;

public class GraficoSimulacionController {

    @FXML
    private LineChart<Number, Number> graficoSimulacion;

    private QueryResult simulacion;
    private DataSet2D data;

    @FXML
    public void receiveData(QueryResult result) {

        this.simulacion = result;

        String title = this.simulacion.getData().getDataTitles().stream().findFirst().get();
        this.data = this.simulacion.getData().getData(title);
        fillSimulationChart(this.graficoSimulacion, this.data);
    }

    @FXML
    public void cerrar(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
