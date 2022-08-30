package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static com.mmaguire.prototiporeacciones2.manager.Context.bundle;
import static com.mmaguire.prototiporeacciones2.manager.Helper.*;

public class EditarExperimentoController {

    @FXML
    private ComboBox<String> comboBoxReloj;
    @FXML
    private CheckBox checkBoxIntervalo;
    @FXML
    private Label relojLimiteSup;
    @FXML
    private Label relojLimiteInf;
    @FXML
    private ComboBox<String> restriccionSup;
    @FXML
    private Spinner<Integer> limiteSup;
    @FXML
    private ComboBox<String> restriccionInf;
    @FXML
    private Spinner<Integer> limiteInf;

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

    private Context contexto;
    private Paso paso;

    private ObservableList<ReactivoReaccion> reactivosPasoExperimento;
    private ObservableList<Factor> factoresPasoExperimento;

    private ObservableList<String> restriccionesIntervalo;
    private ObservableList<String> restriccionesTiempoFijo;

    @FXML
    public void initialize(){
        this.contexto = Context.getContext();

        this.reactivosPasoExperimento = FXCollections.observableList(new ArrayList<>());
        this.factoresPasoExperimento = FXCollections.observableList(new ArrayList<>());

        this.restriccionesIntervalo = FXCollections.observableList(new ArrayList<>());
        this.restriccionesIntervalo.add("<=");
        this.restriccionesIntervalo.add("<");

        this.restriccionesTiempoFijo = FXCollections.observableList(new ArrayList<>());
        this.restriccionesTiempoFijo.add("==");
        this.restriccionesTiempoFijo.add("<=");
        this.restriccionesTiempoFijo.add("<");

        this.limiteInf.setDisable(true);
        this.restriccionInf.setDisable(true);
        this.relojLimiteInf.setDisable(true);

        // Set ComboBox
        this.comboBoxReactivos.setItems(contexto.getReactivos());
        this.comboBoxFactores.setItems(contexto.getFactores());
        this.comboBoxReloj.setItems(FXCollections.observableList(contexto.getSistemaReacciones().getRelojes()));
        this.comboBoxReloj.getSelectionModel().selectFirst();

        this.restriccionSup.setItems(this.restriccionesTiempoFijo);
        this.restriccionSup.getSelectionModel().selectFirst();

        this.restriccionInf.setItems(this.restriccionesIntervalo);
        this.restriccionInf.getSelectionModel().selectFirst();

        // Set listener de CheckBox
        this.checkBoxIntervalo.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov, Boolean valAnterior, Boolean valActual) {
                limiteInf.setDisable(!valActual);
                restriccionInf.setDisable(!valActual);
                relojLimiteInf.setDisable(!valActual);
                if(valActual){
                    restriccionSup.setItems(restriccionesIntervalo);
                    restriccionSup.getSelectionModel().selectFirst();
                } else {
                    restriccionSup.setItems(restriccionesTiempoFijo);
                    restriccionSup.getSelectionModel().selectFirst();
                }
            }});

        this.comboBoxReloj.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String valAnterior, String valActual) {
                relojLimiteInf.setText(valActual);
                relojLimiteSup.setText(valActual);
            }});

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
        this.limiteInf.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000, 0));
        this.limiteInf.setEditable(true);
        this.limiteSup.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000, 0));
        this.limiteSup.setEditable(true);
    }

    @FXML
    public void receiveData(Paso paso) {
        this.paso = paso;
        this.reactivosPasoExperimento.addAll(this.paso.getReactivosActualizados());
        this.factoresPasoExperimento.addAll(this.paso.getFactoresActualizados());
        Restriccion tiempo = paso.getTiempo();
        this.limiteSup.getValueFactory().setValue(tiempo.getLimiteSup());
        this.restriccionSup.getSelectionModel().select(tiempo.getRestriccionSup());
        this.comboBoxReloj.getSelectionModel().select(tiempo.getComponente());
        if (tiempo instanceof RestriccionIntervalo){
            this.checkBoxIntervalo.setSelected(true);
            this.limiteInf.getValueFactory().setValue(((RestriccionIntervalo) tiempo).getLimiteInf());
            this.restriccionInf.getSelectionModel().select(((RestriccionIntervalo) tiempo).getRestriccionInf());


        }
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

            this.paso.getTiempo().setLimiteSup(this.limiteSup.getValue());
            this.paso.getTiempo().setRestriccionSup(this.restriccionSup.getValue());
            this.paso.getTiempo().setComponente(this.comboBoxReloj.getValue());
            if (this.paso.getTiempo() instanceof RestriccionIntervalo){
                ((RestriccionIntervalo) this.paso.getTiempo()).setLimiteInf(this.limiteInf.getValue());
                ((RestriccionIntervalo) this.paso.getTiempo()).setRestriccionInf(this.restriccionInf.getValue());
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void añadirReloj(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/añadir-reloj.fxml"), bundle);
            Parent root = loader.load();
            AddRelojController controller = loader.getController();

            Scene scene = new Scene(root);

            Stage stage = createModalWindow(scene, event);
            stage.showAndWait();

            String reloj = controller.getReloj();
            if (reloj != null)
                this.contexto.getSistemaReacciones().getRelojes().add(reloj);

            this.comboBoxReloj.setItems(FXCollections.observableList(contexto.getSistemaReacciones().getRelojes()));
        }
        catch (IOException e) {
            System.out.println("Hubo un problema al leer el archivo FXML");
            e.printStackTrace();
        }
    }

    @FXML
    public void descartarCambios(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
