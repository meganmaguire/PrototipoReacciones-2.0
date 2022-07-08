package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.model.Paso;
import com.mmaguire.prototiporeacciones2.model.Reaccion;
import com.mmaguire.prototiporeacciones2.model.Sistema;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import static com.mmaguire.prototiporeacciones2.manager.Context.bundle;
import static com.mmaguire.prototiporeacciones2.manager.Helper.createModalWindow;
import static com.mmaguire.prototiporeacciones2.manager.Helper.itemArray2String;

public class VerSistemaController {

    @FXML
    public AnchorPane simulation_pane;
    @FXML
    private TableView<Reaccion> tablaReacciones;
    @FXML
    private TableColumn<Reaccion, Integer> columnaNroReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaTasaReaccion;

    @FXML
    private TableView<Paso> tablaExperimento;
    @FXML
    private TableColumn<Paso, Integer> columnaTiempoPaso;
    @FXML
    private TableColumn<Paso, String> columnaModificacionesPaso;

    private Sistema sistema;

    @FXML
    public void initialize(){

        this.columnaNroReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNroReaccion()));
        this.columnaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        this.columnaTasaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(itemArray2String(cellData.getValue().calculateTasaReaccion())));

        this.columnaTiempoPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTiempo()));
        this.columnaModificacionesPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));

        this.tablaReacciones.setRowFactory(tv -> {
            TableRow<Reaccion> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Reaccion reaccion = row.getItem();
                    verReaccion(reaccion, event);
                }
            });
            return row;
        });
    }


    @FXML
    public void receiveData(Sistema sistema) {
        this.sistema = sistema;

        this.tablaReacciones.setItems(FXCollections.observableList(this.sistema.getReacciones()));
        this.tablaExperimento.setItems(FXCollections.observableList(this.sistema.getExperimento().getPasos()));

    }

    @FXML
    public void cerrar(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void verReaccion(Reaccion reaccion, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/ver-reaccion.fxml"), bundle);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            VerReaccionController controller = loader.getController();
            controller.receiveData(reaccion);

            Stage dialog = createModalWindow(scene, event);
            dialog.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
