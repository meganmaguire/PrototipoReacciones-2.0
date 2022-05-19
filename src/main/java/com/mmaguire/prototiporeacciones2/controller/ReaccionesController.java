package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.manager.ButtonCellReaccion;
import com.mmaguire.prototiporeacciones2.manager.ButtonCellReactivo;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Reaccion;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import com.mmaguire.prototiporeacciones2.model.TipoReaccion;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.ArrayList;

public class ReaccionesController {

    @FXML
    private ComboBox<Reactivo> comboBoxReactivos;
    @FXML
    private Spinner<Integer> cantidadReactivos;
    @FXML
    private TableView<Reactivo> tablaReactivos;
    @FXML
    private TableColumn<Reactivo, String> columnaNombreReactivos;
    @FXML
    private TableColumn<Reactivo, Integer> columnaCantidadReactivos;
    @FXML
    private TableColumn<Reactivo, Boolean> columnaEliminarReactivo;

    @FXML
    private ComboBox<TipoReaccion> comboBoxTipoReaccion;
    @FXML
    private TextField constanteAlpha;
    @FXML
    private TextField constanteBeta;

    @FXML
    private ComboBox<Reactivo> comboBoxProductos;
    @FXML
    private Spinner<Integer> cantidadProductos;
    @FXML
    private TableView<Reactivo> tablaProductos;
    @FXML
    private TableColumn<Reactivo, String> columnaNombreProductos;
    @FXML
    private TableColumn<Reactivo, Integer> columnaCantidadProductos;
    @FXML
    private TableColumn<Reactivo, Boolean> columnaEliminarProducto;
    @FXML
    private Button botonAñadirProducto;


    @FXML
    private Label labelReactivos;
    @FXML
    private Label labelTipoReaccion;
    @FXML
    private Label labelProductos;

    @FXML
    private TableView<Reaccion> tablaReacciones;
    @FXML
    private TableColumn<Reaccion, Integer> columnaNroReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaTasaReaccion;
    @FXML
    private TableColumn<Reaccion, Boolean> columnaEliminarReaccion;

    private Context contexto;

    private ObservableList<Reactivo> reactivosReaccion;
    private ObservableList<Reactivo> productosReaccion;
    private ObservableList<TipoReaccion> tipoReacciones;


    @FXML
    public void initialize() {
        // Set context
        contexto = Context.getContext();
        this.reactivosReaccion = FXCollections.observableList(new ArrayList<>());
        this.productosReaccion = FXCollections.observableList(new ArrayList<>());

        this.tipoReacciones = FXCollections.observableList(new ArrayList<>());
        this.tipoReacciones.add(TipoReaccion.reversible);
        this.tipoReacciones.add(TipoReaccion.irreversible);
        this.tipoReacciones.add(TipoReaccion.degradacion);

        // Set ComboBox
        this.comboBoxReactivos.setItems(contexto.getReactivos());
        this.comboBoxProductos.setItems(contexto.getReactivos());
        this.comboBoxTipoReaccion.setItems(this.tipoReacciones);
        this.comboBoxTipoReaccion.getSelectionModel().selectFirst();
        this.comboBoxTipoReaccion.valueProperty().addListener(new ChangeListener<TipoReaccion>() {
            @Override public void changed(ObservableValue ov, TipoReaccion seleccionAnterior, TipoReaccion seleccionActual) {
                switch (seleccionActual){
                    case reversible -> {
                        labelTipoReaccion.setText("⇌");
                        constanteBeta.setDisable(false);
                        botonAñadirProducto.setDisable(false);
                    }
                    case irreversible -> {
                        labelTipoReaccion.setText("⟶");
                        constanteBeta.setDisable(true);
                        botonAñadirProducto.setDisable(false);
                    }
                    case degradacion -> {
                        labelTipoReaccion.setText("⟶");
                        constanteBeta.setDisable(true);
                        botonAñadirProducto.setDisable(true);
                    }
                }
                constanteBeta.setDisable(seleccionActual != TipoReaccion.reversible);
                if(seleccionActual == TipoReaccion.reversible)
                    labelTipoReaccion.setText("⇌");
                else
                    labelTipoReaccion.setText("⟶");
            }
        });

        // Set tablas
        this.tablaReactivos.setItems(this.reactivosReaccion);
        this.columnaNombreReactivos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNombre()));
        this.columnaCantidadReactivos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidadInicial()));
        this.columnaEliminarReactivo.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Reactivo, Boolean>,
                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Reactivo, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        this.columnaEliminarReactivo.setCellFactory(
                new Callback<TableColumn<Reactivo, Boolean>, TableCell<Reactivo, Boolean>>() {
                    @Override
                    public TableCell<Reactivo, Boolean>call(TableColumn<Reactivo, Boolean> p) {
                        return new ButtonCellReactivo(tablaReactivos, contexto.getReactivos());
                    }
                });

        this.tablaProductos.setItems(this.productosReaccion);
        this.columnaNombreProductos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNombre()));
        this.columnaCantidadProductos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidadInicial()));
        this.columnaEliminarProducto.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Reactivo, Boolean>,
                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Reactivo, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        this.columnaEliminarProducto.setCellFactory(
                new Callback<TableColumn<Reactivo, Boolean>, TableCell<Reactivo, Boolean>>() {
                    @Override
                    public TableCell<Reactivo, Boolean>call(TableColumn<Reactivo, Boolean> p) {
                        return new ButtonCellReactivo(tablaReactivos, contexto.getReactivos());
                    }
                });

        this.tablaReacciones.setItems(contexto.getReacciones());
        this.columnaNroReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNroReaccion()));
        this.columnaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        this.columnaTasaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().calculateTasaReaccion()));
        this.columnaEliminarReaccion.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Reaccion, Boolean>,
                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Reaccion, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        this.columnaEliminarReaccion.setCellFactory(
                new Callback<TableColumn<Reaccion, Boolean>, TableCell<Reaccion, Boolean>>() {
                    @Override
                    public TableCell<Reaccion, Boolean>call(TableColumn<Reaccion, Boolean> p) {
                        return new ButtonCellReaccion(tablaReacciones, contexto.getReacciones());
                    }
                });

        // Set spinners
        this.cantidadReactivos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1));
        this.cantidadProductos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1));


    }

    @FXML
    public void añadirReactivo() {}

    @FXML
    public void añadirProducto() {}

    @FXML
    public void añadirReaccion() {}


    private void actualizarReaccion() {
        StringBuilder reactivos = new StringBuilder();
        StringBuilder productos = new StringBuilder();
        Reactivo reactivo;

        for (int i = 0; i < this.reactivosReaccion.size(); i++) {
            reactivo = this.reactivosReaccion.get(i);
            reactivos
                    .append(reactivo.getCantidadInicial())
                    .append(" ")
                    .append(reactivo.getNombre())
                    .append((i != this.reactivosReaccion.size() - 1) ? " + " : "");
        }

        for (int i = 0; i < this.productosReaccion.size(); i++) {
            reactivo = this.productosReaccion.get(i);
            productos
                    .append(reactivo.getCantidadInicial())
                    .append(" ")
                    .append(reactivo.getNombre())
                    .append((i != this.productosReaccion.size() - 1) ? " + " : "");
        }

        this.labelReactivos.setText(reactivos.toString());
        this.labelProductos.setText(productos.toString());
    }

    public boolean existeReactivoConNombre(String nombre, ObservableList<Reactivo> reactivos) {

        for(Reactivo reactivo : reactivos) {
            if(reactivo.getNombre().equals(nombre))
                return true;
        }
        return false;
    }



}
