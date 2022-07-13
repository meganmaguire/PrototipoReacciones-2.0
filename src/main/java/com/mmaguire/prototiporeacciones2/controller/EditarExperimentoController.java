package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Factor;
import com.mmaguire.prototiporeacciones2.model.Paso;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import com.mmaguire.prototiporeacciones2.model.ReactivoReaccion;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.mmaguire.prototiporeacciones2.manager.Helper.*;

public class EditarExperimentoController {

    @FXML
    private ComboBox<Reactivo> comboBoxReactivos;
    @FXML
    private Spinner<Integer> cantidadReactivos;
    @FXML
    private TableView<ReactivoReaccion> tablaReactivos;
    @FXML
    private TableColumn<ReactivoReaccion, String> columnaNombreReactivos;
    @FXML
    private TableColumn<ReactivoReaccion, Integer> columnaCantidadReactivos;
    @FXML
    private TableColumn<ReactivoReaccion, ReactivoReaccion> columnaEliminarReactivo;

    @FXML
    private ComboBox<Factor> comboBoxFactores;
    @FXML
    private Spinner<Double> valorFactores;
    @FXML
    private TableView<Factor> tablaFactores;
    @FXML
    private TableColumn<Factor, String> columnaNombreFactor;
    @FXML
    private TableColumn<Factor, Double> columnaValorFactor;
    @FXML
    private TableColumn<Factor, Factor> columnaEliminarFactor;

    @FXML
    private Spinner<Integer> tiempoPaso;

    private Context contexto;
    private Paso paso;

    private ObservableList<ReactivoReaccion> reactivosPasoExperimento;
    private ObservableList<Factor> factoresPasoExperimento;


    @FXML
    public void initialize(){
        this.contexto = Context.getContext();

        this.reactivosPasoExperimento = FXCollections.observableList(new ArrayList<>());
        this.factoresPasoExperimento = FXCollections.observableList(new ArrayList<>());

        // Set ComboBox
        this.comboBoxReactivos.setItems(contexto.getReactivos());
        this.comboBoxFactores.setItems(contexto.getFactores());

        // Set tablas
        // Tabla Reactivos
        this.tablaReactivos.setItems(this.reactivosPasoExperimento);
        this.columnaNombreReactivos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReactivoAsociado().getNombre()));
        this.columnaCantidadReactivos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidad()));
        this.columnaEliminarReactivo.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminarReactivo.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(ReactivoReaccion reactivo, boolean empty) {
                super.updateItem(reactivo, empty);

                if (reactivo == null) {
                    setGraphic(null);
                    return;
                }
                styleDeleteButton(deleteButton);
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> getTableView().getItems().remove(reactivo)
                );
            }
        });
        // Tabla Factores
        this.tablaFactores.setItems(this.factoresPasoExperimento);
        this.columnaNombreFactor.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNombre()));
        this.columnaValorFactor.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValor()));
        this.columnaEliminarFactor.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminarFactor.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Factor factor, boolean empty) {
                super.updateItem(factor, empty);

                if (factor == null) {
                    setGraphic(null);
                    return;
                }
                styleDeleteButton(deleteButton);
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> getTableView().getItems().remove(factor)
                );
            }
        });


        // Set spinners
        this.cantidadReactivos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1));
        this.cantidadReactivos.setEditable(true);
        this.valorFactores.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,10000,0,0.01));
        this.valorFactores.setEditable(true);
        this.tiempoPaso.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000, 0));
        this.tiempoPaso.setEditable(true);
    }

    @FXML
    public void receiveData(Paso paso) {
        this.paso = paso;
        this.reactivosPasoExperimento.addAll(this.paso.getReactivosActualizados());
        this.factoresPasoExperimento.addAll(this.paso.getFactoresActualizados());
        this.tiempoPaso.getValueFactory().setValue(this.paso.getTiempo());
    }

    @FXML
    public void añadirReactivo() {
        if(this.comboBoxReactivos.getValue() != null) {
            ReactivoReaccion reactivo = new ReactivoReaccion(
                    this.comboBoxReactivos.getValue(),
                    this.cantidadReactivos.getValue()
            );
            if (!existeReactivoReaccionConNombre(reactivo.getReactivoAsociado().getNombre(), this.reactivosPasoExperimento))
                this.reactivosPasoExperimento.add(reactivo);
        }
    }
    @FXML
    public void añadirFactor(){
        if(this.comboBoxFactores.getValue() != null) {
            Factor factor = this.comboBoxFactores.getValue().clone();
            factor.setValor(this.valorFactores.getValue());
            if (!existeFactorConNombre(factor.getNombre(), factoresPasoExperimento))
                this.factoresPasoExperimento.add(factor);
        }
    }


    @FXML
    public void guardarCambios(ActionEvent event){
        if(!this.factoresPasoExperimento.isEmpty() || !this.reactivosPasoExperimento.isEmpty()) {
            this.paso.setFactoresActualizados(this.factoresPasoExperimento);
            this.paso.setReactivosActualizados(this.reactivosPasoExperimento);
            this.paso.setTiempo(this.tiempoPaso.getValue());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void descartarCambios(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
