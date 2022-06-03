package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Factor;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.mmaguire.prototiporeacciones2.manager.Helper.existeReactivoConNombre;
import static com.mmaguire.prototiporeacciones2.manager.Helper.styleButton;

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
    private TableColumn<Reactivo, Reactivo> columnaEliminar;

    private Context contexto;

    @FXML
    public void initialize(){
        contexto = Context.getContext();
        this.tablaComponentes.setItems(contexto.getReactivos());
        this.columnaNombre.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNombre()));
        this.columnaCantidad.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidadInicial()));
        this.columnaActualizable.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isActualizable()));
        this.columnaEliminar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminar.setCellFactory(param -> new TableCell<>() {
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
        this.tablaComponentes.setRowFactory(tv -> {
            TableRow<Reactivo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Reactivo reactivo = row.getItem();
                    editarReactivo(reactivo, event);
                }
            });
            return row;
        });

        this.constanteAsociada.setItems(this.contexto.getConstantesReaccion());
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
            loader.setLocation(MainApp.class.getResource("views/añadir-constante.fxml"));
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
                this.contexto.getConstantesReaccion().add(constante);
        }
        catch (IOException e) {
            System.out.println("Hubo un problema al leer el archivo FXML");
        }
    }

    public void editarReactivo(Reactivo reactivo, Event event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("views/editar-componente.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            EditarComponenteController controller = loader.getController();
            controller.receiveData(reactivo);

            Stage dialog = new Stage();
            Node node = (Node) event.getSource();
            Stage parentStage = (Stage) node.getScene().getWindow();
            dialog.setScene(scene);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
            // Reset para notificar al ObservableList
            int index = this.contexto.getReactivos().indexOf(reactivo);
            this.contexto.getReactivos().set(index, reactivo);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void clearFields(){
        this.nombreComponente.setText(null);
        this.actualizableComponente.setSelected(true);
        this.poseeConstante.setSelected(false);
    }

}
