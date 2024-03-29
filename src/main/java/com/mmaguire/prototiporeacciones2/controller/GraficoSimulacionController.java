package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.model.Simulacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import on.S;

import java.io.File;

import static com.mmaguire.prototiporeacciones2.manager.FileManager.exportSimulationData;
import static com.mmaguire.prototiporeacciones2.manager.SimulationManager.fillSimulationChart;

public class GraficoSimulacionController {

    @FXML
    private LineChart<Number, Number> graficoSimulacion;

    private Simulacion simulacion;

    @FXML
    public void receiveData(Simulacion result) {

        this.simulacion = result;
        fillSimulationChart(this.graficoSimulacion, this.simulacion);
    }

    @FXML
    public void cerrar(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    public void exportarDatos(ActionEvent event){
        Node node = (Node) event.getSource();
        Window window = node.getScene().getWindow();
        FileChooser fileChooser =  new FileChooser();
        fileChooser.setInitialFileName("simulacion.csv");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Comma Separated Values (*.csv)", "*.csv")
        );
        File selected = fileChooser.showSaveDialog(window);
        if(selected != null) {
            boolean result = exportSimulationData(this.simulacion, selected.getAbsolutePath());

            if (result) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText("Éxito");
                alert.setContentText("Se han exportado correctamente los datos de la simulación");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al exportar datos");
                alert.setContentText("Ha ocurrido un error al intentar exportar los datos de la simulación.");
                alert.showAndWait();
            }
        }
    }
}
