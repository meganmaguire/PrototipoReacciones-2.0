package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.ButtonCellReactivo;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Factor;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class ComponentesController {

    @FXML
    private TextField nombreComponente;
    @FXML
    private Spinner<Integer> cantidadComponente;
    @FXML
    private CheckBox actualizableComponente;
    @FXML
    private CheckBox poseeConstante;
    @FXML
    private ComboBox<Factor> constanteAsociada;
    @FXML
    private Button botonAñadirConstante;

    @FXML
    private TableView<Reactivo> tablaComponentes;
    @FXML
    private TableColumn<Reactivo, String> columnaNombre;
    @FXML
    private TableColumn<Reactivo, Integer> columnaCantidad;
    @FXML
    private TableColumn<Reactivo, Boolean> columnaActualizable;
    @FXML
    private TableColumn<Reactivo, Boolean> columnaEliminar;

    private Context contexto;
    private ObservableList<Factor> constantes;

    @FXML
    public void initialize(){
        contexto = Context.getContext();
        this.tablaComponentes.setItems(contexto.getReactivos());
        this.columnaNombre.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNombre()));
        this.columnaCantidad.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidadInicial()));
        this.columnaActualizable.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isActualizable()));
        this.columnaEliminar.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Reactivo, Boolean>,
                                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Reactivo, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        this.columnaEliminar.setCellFactory(
                new Callback<TableColumn<Reactivo, Boolean>, TableCell<Reactivo, Boolean>>() {
                    @Override
                    public TableCell<Reactivo, Boolean>call(TableColumn<Reactivo, Boolean> p) {
                        return new ButtonCellReactivo(tablaComponentes, contexto.getReactivos());
                    }
                });

        this.constantes = FXCollections.observableArrayList(new ArrayList<>());
        this.constanteAsociada.setItems(this.constantes);
        this.cantidadComponente.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0,1));
        this.cantidadComponente.setEditable(true);

        this.actualizableComponente.setSelected(true);
        this.constanteAsociada.setDisable(true);
        this.botonAñadirConstante.setDisable(true);
        this.poseeConstante.selectedProperty().addListener((ov, seleccionAnterior, seleccionActual) -> {
            constanteAsociada.setDisable(!seleccionActual);
            botonAñadirConstante.setDisable(!seleccionActual);
        });
    }

    @FXML
    public void añadirComponente() {
        String nombre = this.nombreComponente.getText();
        int cantidad = this.cantidadComponente.getValue();
        boolean isActualizable = this.actualizableComponente.isSelected();
        Factor constante = null;
        if(this.poseeConstante.isSelected())
            constante = this.constanteAsociada.getValue();

        Reactivo componente = new Reactivo(nombre, cantidad, isActualizable, constante);

        if(!existeReactivoConNombre(nombre, contexto.getReactivos())) {
            contexto.getReactivos().add(componente);
        }

        clearFields();
    }

    @FXML
    public void añadirConstante(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("añadir-constante.fxml"));
            Parent root = loader.load();
            AddConstanteController controller = loader.getController();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            Node node = (Node) event.getSource();
            Stage parentStage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.initOwner(parentStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            Factor constante = controller.getConstante();
            if (constante != null)
                this.constantes.add(constante);

        }
        catch (IOException e) {
            System.out.println("Hubo un problema al leer el archivo FXML");
        }
    }

    public void clearFields(){
        this.nombreComponente.setText(null);
        this.actualizableComponente.setSelected(true);
        this.poseeConstante.setSelected(false);
    }

    public boolean existeReactivoConNombre(String nombre, ObservableList<Reactivo> reactivos) {

        for(Reactivo reactivo : reactivos) {
            if(reactivo.getNombre().equals(nombre))
                return true;
        }
        return false;
    }
}
