package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.model.Reaccion;
import com.mmaguire.prototiporeacciones2.model.ReaccionReversible;
import com.mmaguire.prototiporeacciones2.model.ReactivoReaccion;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import static com.mmaguire.prototiporeacciones2.manager.Helper.*;

public class VerReaccionController {

    @FXML
    private TableView<ReactivoReaccion> tablaReactivos;
    @FXML
    private TableColumn<ReactivoReaccion, String> columnaNombreReactivos;
    @FXML
    private TableColumn<ReactivoReaccion, Integer> columnaCantidadReactivos;

    @FXML
    private Label tipoReaccion;
    @FXML
    private TextField constanteAlpha;
    @FXML
    private TextField constanteBeta;

    @FXML
    private TableView<ReactivoReaccion> tablaProductos;
    @FXML
    private TableColumn<ReactivoReaccion, String> columnaNombreProductos;
    @FXML
    private TableColumn<ReactivoReaccion, Integer> columnaCantidadProductos;



    @FXML
    private Label labelReactivos;
    @FXML
    private Label labelTipoReaccion;
    @FXML
    private Label labelProductos;

    @FXML
    private TextField textFieldTasaReaccion;


    private Reaccion reaccion;

    @FXML
    public void initialize(){

        // Set tablas
        // Tabla Reactivos
        this.columnaNombreReactivos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReactivoAsociado().getNombre()));
        this.columnaCantidadReactivos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidad()));

        // Tabla Productos
        this.columnaNombreProductos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReactivoAsociado().getNombre()));
        this.columnaCantidadProductos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidad()));

        this.constanteAlpha.setEditable(false);
        this.constanteBeta.setEditable(false);
    }

    @FXML
    public void receiveData(Reaccion reaccion) {

        this.reaccion = reaccion;
        this.tablaReactivos.setItems(FXCollections.observableList(this.reaccion.getReactantes()));
        this.tablaProductos.setItems(FXCollections.observableList(this.reaccion.getProductos()));
        this.constanteAlpha.setText(String.valueOf(this.reaccion.getAlpha().getValor()));
        if(reaccion instanceof ReaccionReversible)
            this.constanteBeta.setText(String.valueOf(((ReaccionReversible) this.reaccion).getBeta().getValor()));

        this.textFieldTasaReaccion.setText(itemArray2String(this.reaccion.getTasaReaccion()));
        actualizarReaccion(
                FXCollections.observableList(this.reaccion.getReactantes()),
                FXCollections.observableList(this.reaccion.getProductos()),
                labelReactivos,
                labelProductos);
    }

    @FXML
    public void cerrar(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
