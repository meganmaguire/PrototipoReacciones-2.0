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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static com.mmaguire.prototiporeacciones2.manager.Context.bundle;
import static com.mmaguire.prototiporeacciones2.manager.Helper.*;

public class ExperimentosController {

    @FXML
    private ComboBox<String> comboBoxRestriccion;
    @FXML
    private ComboBox<String> comboBoxComponente;
    @FXML
    private Button botonAñadirReloj;
    @FXML
    private CheckBox checkBoxIntervalo;
    @FXML
    private Label labelLimiteSup;
    @FXML
    private Label labelLimiteInf;
    @FXML
    private ComboBox<String> restriccionSup;
    @FXML
    private Spinner<Integer> limiteSup;
    @FXML
    private ComboBox<String> restriccionInf;
    @FXML
    private Spinner<Integer> limiteInf;
    @FXML
    private Label labelSegSup;
    @FXML
    private Label labelSegInf;

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
    private TableView<Paso> tablaExperimento;
    @FXML
    private TableColumn<Paso, String> columnaTiempoPaso;
    @FXML
    private TableColumn<Paso, String> columnaModificacionesPaso;
    @FXML
    private TableColumn<Paso, Paso> columnaEliminarPaso;


    private Context contexto;

    private ObservableList<ReactivoReaccion> reactivosPasoExperimento;
    private ObservableList<Factor> factoresPasoExperimento;

    private ObservableList<String> tiposRestriccion;
    private ObservableList<String> restriccionesIntervalo;
    private ObservableList<String> restriccionesTiempoFijo;


    @FXML
    public void initialize(){
        this.contexto = Context.getContext();

        this.reactivosPasoExperimento = FXCollections.observableList(new ArrayList<>());
        this.factoresPasoExperimento = FXCollections.observableList(new ArrayList<>());

        this.tiposRestriccion = FXCollections.observableList(new ArrayList<>());
        this.tiposRestriccion.add("Tiempo");
        this.tiposRestriccion.add("Componente");

        this.restriccionesIntervalo = FXCollections.observableList(new ArrayList<>());
        this.restriccionesIntervalo.add("<=");
        this.restriccionesIntervalo.add("<");

        this.restriccionesTiempoFijo = FXCollections.observableList(new ArrayList<>());
        this.restriccionesTiempoFijo.add("==");
        this.restriccionesTiempoFijo.add("<=");
        this.restriccionesTiempoFijo.add("<");

        this.limiteInf.setDisable(true);
        this.restriccionInf.setDisable(true);
        this.labelLimiteInf.setDisable(true);

        // Set ComboBox
        this.comboBoxReactivos.setItems(contexto.getReactivos());
        this.comboBoxFactores.setItems(contexto.getFactores());
        this.comboBoxComponente.setItems(FXCollections.observableList(contexto.getSistemaReacciones().getRelojes()));
        this.comboBoxComponente.getSelectionModel().selectFirst();

        this.comboBoxRestriccion.setItems(this.tiposRestriccion);
        this.comboBoxRestriccion.getSelectionModel().selectFirst();

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
                labelLimiteInf.setDisable(!valActual);
                if(valActual){
                    restriccionSup.setItems(restriccionesIntervalo);
                    restriccionSup.getSelectionModel().selectFirst();
                } else {
                    restriccionSup.setItems(restriccionesTiempoFijo);
                    restriccionSup.getSelectionModel().selectFirst();
                }
            }});

        this.comboBoxComponente.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String valAnterior, String valActual) {
                labelLimiteInf.setText(valActual);
                labelLimiteSup.setText(valActual);
            }});

        this.comboBoxRestriccion.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String valAnterior, String valActual) {
                if (valActual.equals("Componente")) {
                    botonAñadirReloj.setDisable(true);
                    botonAñadirReloj.setVisible(false);
                    comboBoxComponente.setItems(FXCollections.observableList(
                            contexto.getReactivos().stream().map(Reactivo::getNombre).toList()));
                    comboBoxComponente.getSelectionModel().selectFirst();
                    labelSegSup.setVisible(false);
                    labelSegInf.setVisible(false);
                }
                else {
                    botonAñadirReloj.setDisable(false);
                    botonAñadirReloj.setVisible(true);
                    comboBoxComponente.setItems(FXCollections.observableList(contexto.getSistemaReacciones().getRelojes()));
                    comboBoxComponente.getSelectionModel().selectFirst();
                    labelSegSup.setVisible(true);
                    labelSegInf.setVisible(true);
                }
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
        // Tabla Experimento
        this.tablaExperimento.setItems(this.contexto.getPasosExperimento());
        this.columnaTiempoPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTiempo().toString()));
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
                styleDeleteButton(deleteButton);
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> getTableView().getItems().remove(paso)
                );
            }
        });
        this.tablaExperimento.setRowFactory(tv -> {
            TableRow<Paso> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Paso paso = row.getItem();
                    editarExperimento(paso, event);
                }
            });
            return row;
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
    public void añadirReactivo() {
        if (this.comboBoxReactivos.getValue() != null) {
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
        if (this.comboBoxFactores.getValue() != null) {
            Factor factor = this.comboBoxFactores.getValue().clone();
            factor.setValor(this.valorFactores.getValue());
            if (!existeFactorConNombre(factor.getNombre(), factoresPasoExperimento))
                this.factoresPasoExperimento.add(factor);
        }
    }

    @FXML
    public void añadirPasoExperimento() {
        Restriccion tiempo;
        if(!this.reactivosPasoExperimento.isEmpty() || !this.factoresPasoExperimento.isEmpty()) {
            if (this.checkBoxIntervalo.isSelected()) {
                if (this.limiteSup.getValue() > this.limiteInf.getValue()) {
                    tiempo = new RestriccionIntervalo(
                            this.limiteSup.getValue(),
                            this.restriccionSup.getValue(),
                            this.comboBoxComponente.getValue(),
                            this.comboBoxRestriccion.getValue(),
                            this.limiteInf.getValue(),
                            this.restriccionInf.getValue());
                } else return;
            } else {
                tiempo = new Restriccion(
                        this.limiteSup.getValue(),
                        this.restriccionSup.getValue(),
                        this.comboBoxComponente.getValue(),
                        this.comboBoxRestriccion.getValue());
            }

            Paso paso = new Paso(
                        new ArrayList<>(this.reactivosPasoExperimento),
                        new ArrayList<>(this.factoresPasoExperimento),
                        tiempo);
            this.contexto.getPasosExperimento().add(paso);
            this.reactivosPasoExperimento.clear();
            this.factoresPasoExperimento.clear();
            this.limiteInf.getValueFactory().setValue(0);
            this.limiteSup.getValueFactory().setValue(0);
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

            this.comboBoxComponente.setItems(FXCollections.observableList(contexto.getSistemaReacciones().getRelojes()));
        }
        catch (IOException e) {
            System.out.println("Hubo un problema al leer el archivo FXML");
            e.printStackTrace();
        }
    }

    public void editarExperimento(Paso paso, Event event){
        try {
            // Generar pantalla de simulación y enviar datos
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/editar-experimento.fxml"), bundle);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            EditarExperimentoController controller = loader.getController();
            controller.receiveData(paso);

            Stage dialog = createModalWindow(scene, event);
            dialog.showAndWait();
            this.tablaExperimento.refresh();
            // this.contexto.getPasosExperimento().sort(Comparator.comparingInt(Paso::getTiempo));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

//    private boolean existePasoConTiempo(int tiempo, ObservableList<Paso> pasosExperimento){
//        for(Paso paso: pasosExperimento){
//            if(tiempo == paso.getTiempo())
//                return true;
//        }
//        return false;
//    }

}
