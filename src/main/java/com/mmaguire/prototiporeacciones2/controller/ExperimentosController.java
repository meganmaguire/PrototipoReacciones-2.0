package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Experimento;
import com.mmaguire.prototiporeacciones2.model.Factor;
import com.mmaguire.prototiporeacciones2.model.Paso;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Comparator;

public class ExperimentosController {

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
    private TableColumn<Reactivo, Reactivo> columnaEliminarReactivo;

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

    @FXML
    private TableView<Paso> tablaExperimento;
    @FXML
    private TableColumn<Paso, Integer> columnaTiempoPaso;
    @FXML
    private TableColumn<Paso, String> columnaModificacionesPaso;
    @FXML
    private TableColumn<Paso, Paso> columnaEliminarPaso;


    private Context contexto;
    private Experimento experimento;
    private Paso ultimoPaso;

    private ObservableList<Reactivo> reactivosPasoExperimento;
    private ObservableList<Factor> factoresPasoExperimento;
    private ObservableList<Paso> pasosExperimento;

    @FXML
    public void initialize(){
        this.contexto = Context.getContext();
        this.experimento = new Experimento();
        this.ultimoPaso = new Paso(null, null, 0);

        this.reactivosPasoExperimento = FXCollections.observableList(new ArrayList<>());
        this.factoresPasoExperimento = FXCollections.observableList(new ArrayList<>());
        this.pasosExperimento = FXCollections.observableList(new ArrayList<>());

        // Set ComboBox
        this.comboBoxReactivos.setItems(contexto.getReactivos());
        this.comboBoxFactores.setItems(contexto.getFactores());

        // Set tablas
        // Tabla Reactivos
        this.tablaReactivos.setItems(this.reactivosPasoExperimento);
        this.columnaNombreReactivos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNombre()));
        this.columnaCantidadReactivos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidadInicial()));
        this.columnaEliminarReactivo.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminarReactivo.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Reactivo reactivo, boolean empty) {
                super.updateItem(reactivo, empty);

                if (reactivo == null) {
                    setGraphic(null);
                    return;
                }
                styleButton(deleteButton);
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
                styleButton(deleteButton);
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> getTableView().getItems().remove(factor)
                );
            }
        });
        // Tabla Experimento
        this.tablaExperimento.setItems(this.pasosExperimento);
        this.columnaTiempoPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTiempo()));
        this.columnaModificacionesPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        this.columnaEliminarPaso.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminarPaso.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Paso paso, boolean empty) {
                super.updateItem(paso, empty);

                if (paso == null) {
                    setGraphic(null);
                    return;
                }
                styleButton(deleteButton);
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> getTableView().getItems().remove(paso)
                );
            }
        });

        // Set spinners
        this.cantidadReactivos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1));
        this.cantidadReactivos.setEditable(true);
        this.valorFactores.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,10000,0,0.001));
        this.valorFactores.setEditable(true);
        this.tiempoPaso.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000, 0));
        this.tiempoPaso.setEditable(true);
    }


    @FXML
    public void añadirReactivo() {
        Reactivo reactivo = this.comboBoxReactivos.getValue().clone();
        reactivo.setCantidadInicial(this.cantidadReactivos.getValue());
        if(!existeReactivoConNombre(reactivo.getNombre(), this.reactivosPasoExperimento))
            this.reactivosPasoExperimento.add(reactivo);
    }
    @FXML
    public void añadirFactor(){
        Factor factor = this.comboBoxFactores.getValue().clone();
        factor.setValor(this.valorFactores.getValue());
        if (!existeFactorConNombre(factor.getNombre(), factoresPasoExperimento))
            this.factoresPasoExperimento.add(factor);
    }

    @FXML
    public void añadirPasoExperimento(){
        int tiempo = this.tiempoPaso.getValue();
        if(!existePasoConTiempo(tiempo, this.pasosExperimento)) {
            Paso paso = new Paso(
                    new ArrayList<>(this.reactivosPasoExperimento),
                    new ArrayList<>(this.factoresPasoExperimento),
                    tiempo);
            this.pasosExperimento.add(paso);
            this.pasosExperimento.sort(Comparator.comparingInt(Paso::getTiempo));
            this.reactivosPasoExperimento.clear();
            this.factoresPasoExperimento.clear();
        }
    }

    private boolean existePasoConTiempo(int tiempo, ObservableList<Paso> pasosExperimento){
        for(Paso paso: pasosExperimento){
            if(tiempo == paso.getTiempo())
                return true;
        }
        return false;
    }

    private boolean existeReactivoConNombre(String nombre, ObservableList<Reactivo> reactivos) {

        for(Reactivo reactivo : reactivos) {
            if(reactivo.getNombre().equals(nombre))
                return true;
        }
        return false;
    }

    private boolean existeFactorConNombre(String nombre, ObservableList<Factor> factores) {

        for(Factor factor : factores) {
            if(factor.getNombre().equals(nombre))
                return true;
        }
        return false;
    }

    private void styleButton(Button cellButton) {
        Image image = new Image(MainApp.class.getResourceAsStream("icons/baseline_delete_black_24dp.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        cellButton.setGraphic(imageView);
        cellButton.getStyleClass().add("delete-button");
    }
}
