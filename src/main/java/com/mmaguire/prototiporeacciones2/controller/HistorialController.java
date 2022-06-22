package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Sistema;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static com.mmaguire.prototiporeacciones2.manager.Helper.*;

public class HistorialController {

    @FXML
    public TableView<Sistema> tablaHistorial;
    @FXML
    public TableColumn<Sistema, Integer> columnaNro;
    @FXML
    public TableColumn<Sistema, String> columnaTiempo;
    @FXML
    public TableColumn<Sistema, String> columnaComponentes;
    @FXML
    public TableColumn<Sistema, Integer> columnaCantReacciones;
    @FXML
    public TableColumn<Sistema, Integer> columnaPasos;
    @FXML
    public TableColumn<Sistema, Sistema> columnaGrafico;

    private Context contexto;

    @FXML
    public void initialize() {
        contexto = Context.getContext();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        this.tablaHistorial.setItems(contexto.getHistorial());
        this.columnaNro.setCellValueFactory(cellData -> new SimpleObjectProperty<>(this.contexto.getHistorial().indexOf(cellData.getValue())+1));
        this.columnaTiempo.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSimulacion().getTiempo().format(formatter)));
        this.columnaComponentes.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReactivos().toString()));
        this.columnaCantReacciones.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReacciones().size()));
        this.columnaPasos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getExperimento().getPasos().size()));

        this.columnaGrafico.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaGrafico.setCellFactory(param -> new TableCell<>() {
            private final Button graphicButton = new Button();

            @Override
            protected void updateItem(Sistema sistema, boolean empty) {
                super.updateItem(sistema, empty);

                if (sistema == null) {
                    setGraphic(null);
                    return;
                }
                styleGraphicButton(graphicButton);
                setGraphic(graphicButton);
                graphicButton.setOnAction(
                        event -> {
                            // Mostrar gráfico
                            showData(event, sistema.getSimulacion().getDatos());
                        }
                );
            }
        });
        this.tablaHistorial.setRowFactory(tv -> {
            TableRow<Sistema> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Sistema sistema = row.getItem();
                    verSistema(sistema, event);
                }
            });
            return row;
        });
    }

    public void verSistema(Sistema sistema, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("views/ver-sistema.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            VerSistemaController controller = loader.getController();
            controller.receiveData(sistema);

            Stage dialog = createModalWindow(scene, event);
            dialog.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
