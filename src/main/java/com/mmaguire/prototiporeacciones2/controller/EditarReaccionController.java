package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
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

import java.io.IOException;
import java.util.ArrayList;

import static com.mmaguire.prototiporeacciones2.manager.Helper.*;

public class EditarReaccionController {

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
    private Label tipoReaccion;
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
    private TableColumn<Reactivo, Reactivo> columnaEliminarProducto;
    @FXML
    private Button botonAñadirProducto;


    @FXML
    private Label labelReactivos;
    @FXML
    private Label labelTipoReaccion;
    @FXML
    private Label labelProductos;

    @FXML
    private TextField tasaReaccion;

    private Context contexto;
    private Reaccion reaccion;
    private ObservableList<Reactivo> reactivosReaccion;
    private ObservableList<Reactivo> productosReaccion;

    @FXML
    public void initialize() {
        // Set context
        contexto = Context.getContext();
        this.reactivosReaccion = FXCollections.observableList(new ArrayList<>());
        this.productosReaccion = FXCollections.observableList(new ArrayList<>());


        // Set ComboBox
        this.comboBoxReactivos.setItems(contexto.getReactivos());
        this.comboBoxProductos.setItems(contexto.getReactivos());

        // Set tablas
        // Tabla Reactivos
        this.tablaReactivos.setItems(this.reactivosReaccion);
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
                        event -> {
                            getTableView().getItems().remove(reactivo);
                            actualizarReaccion();
                        }
                );
            }
        });

        // Tabla Productos
        this.tablaProductos.setItems(this.productosReaccion);
        this.columnaNombreProductos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNombre()));
        this.columnaCantidadProductos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidadInicial()));
        this.columnaEliminarProducto.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminarProducto.setCellFactory(param -> new TableCell<>() {
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
                        event -> {
                            getTableView().getItems().remove(reactivo);
                            actualizarReaccion();
                        }
                );
            }
        });

        // Set spinners
        this.cantidadReactivos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1));
        this.cantidadReactivos.setEditable(true);
        this.cantidadProductos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1));
        this.cantidadProductos.setEditable(true);

        this.tasaReaccion.setEditable(false);
    }

    @FXML
    public void receiveData(Reaccion reaccion) {

        this.reaccion = reaccion;
        this.reactivosReaccion.addAll(this.reaccion.getReactantes());
        this.productosReaccion.addAll(this.reaccion.getProductos());
        this.constanteAlpha.setText(String.valueOf(this.reaccion.getAlpha().getValor()));
        if(reaccion instanceof ReaccionReversible)
            this.constanteBeta.setText(String.valueOf(((ReaccionReversible) this.reaccion).getBeta().getValor()));
        this.tipoReaccion.setText(this.reaccion.getTipo().toString());
        switch (this.reaccion.getTipo()) {
            case reversible -> this.labelTipoReaccion.setText("⇌");
            case irreversible, degradacion -> this.labelTipoReaccion.setText("→");
        }
        this.tasaReaccion.setText(itemArray2String(this.reaccion.getTasaReaccion()));
        actualizarReaccion();
    }

    @FXML
    public void añadirReactivo() {
        Reactivo reactivo = this.comboBoxReactivos.getValue().clone();
        reactivo.setCantidadInicial(this.cantidadReactivos.getValue());
        if(!existeReactivoConNombre(reactivo.getNombre(), this.reactivosReaccion))
            this.reactivosReaccion.add(reactivo);
        actualizarReaccion();
    }

    @FXML
    public void añadirProducto() {
        Reactivo reactivo = this.comboBoxProductos.getValue().clone();
        reactivo.setCantidadInicial(this.cantidadProductos.getValue());
        this.productosReaccion.add(reactivo);
        actualizarReaccion();
    }

    @FXML
    public void editarTasaReaccion(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("views/editar-tasa-reaccion.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            EditarTasaReaccionController controller = loader.getController();
            controller.recieveData(reaccion);

            Stage dialog = new Stage();
            Node node = (Node) event.getSource();
            Stage parentStage = (Stage) node.getScene().getWindow();
            dialog.setScene(scene);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void guardarCambios(ActionEvent event) {
        this.reaccion.setReactantes(this.reactivosReaccion);
        this.reaccion.setProductos(this.productosReaccion);
        this.reaccion.setAlpha(new Factor(
                this.reaccion.getAlpha().getNombre(),
                Double.parseDouble(this.constanteAlpha.getText()))
        );
        if (this.reaccion instanceof ReaccionReversible)
            ((ReaccionReversible) this.reaccion).setBeta(new Factor(
                    ((ReaccionReversible) this.reaccion).getBeta().getNombre(),
                    Double.parseDouble(this.constanteBeta.getText()))
            );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void descartarCambios(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }



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


}
