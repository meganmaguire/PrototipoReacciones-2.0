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
    private TableView<ReactivoReaccion> tablaReactivos;
    @FXML
    private TableColumn<ReactivoReaccion, String> columnaNombreReactivos;
    @FXML
    private TableColumn<ReactivoReaccion, Integer> columnaCantidadReactivos;
    @FXML
    private TableColumn<ReactivoReaccion, ReactivoReaccion> columnaEliminarReactivo;

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
    private TableView<ReactivoReaccion> tablaProductos;
    @FXML
    private TableColumn<ReactivoReaccion, String> columnaNombreProductos;
    @FXML
    private TableColumn<ReactivoReaccion, Integer> columnaCantidadProductos;
    @FXML
    private TableColumn<ReactivoReaccion, ReactivoReaccion> columnaEliminarProducto;
    @FXML
    private Button botonAñadirProducto;


    @FXML
    private Label labelReactivos;
    @FXML
    private Label labelTipoReaccion;
    @FXML
    private Label labelProductos;

    @FXML
    private TextField textFieldTasaReaccion;

    private Context contexto;
    private Reaccion reaccion;
    private ObservableList<ReactivoReaccion> reactivosReaccion;
    private ObservableList<ReactivoReaccion> productosReaccion;

    private ArrayList<EquationItem> tasaReaccion;

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
                        event -> {
                            getTableView().getItems().remove(reactivo);
                            actualizarReaccion(reactivosReaccion, productosReaccion, labelReactivos, labelProductos);
                        }
                );
            }
        });

        // Tabla Productos
        this.tablaProductos.setItems(this.productosReaccion);
        this.columnaNombreProductos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReactivoAsociado().getNombre()));
        this.columnaCantidadProductos.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidad()));
        this.columnaEliminarProducto.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminarProducto.setCellFactory(param -> new TableCell<>() {
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
                        event -> {
                            getTableView().getItems().remove(reactivo);
                            actualizarReaccion(reactivosReaccion, productosReaccion, labelReactivos, labelProductos);
                        }
                );
            }
        });

        // Set spinners
        this.cantidadReactivos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1, 1));
        this.cantidadReactivos.setEditable(true);
        this.cantidadProductos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1, 1));
        this.cantidadProductos.setEditable(true);

        this.textFieldTasaReaccion.setEditable(false);
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
        this.tasaReaccion = this.reaccion.getTasaReaccion();
        this.textFieldTasaReaccion.setText(itemArray2String(this.tasaReaccion));
        actualizarReaccion(reactivosReaccion, productosReaccion, labelReactivos, labelProductos);
    }

    @FXML
    public void añadirReactivo() {
        ReactivoReaccion reactivo = new ReactivoReaccion(
                this.comboBoxReactivos.getValue(),
                this.cantidadReactivos.getValue()
        );

        if(!existeReactivoReaccionConNombre(reactivo.getReactivoAsociado().getNombre(), this.reactivosReaccion))
            this.reactivosReaccion.add(reactivo);
        actualizarReaccion(reactivosReaccion, productosReaccion, labelReactivos, labelProductos);
    }

    @FXML
    public void añadirProducto() {
        ReactivoReaccion reactivo = new ReactivoReaccion(
                this.comboBoxProductos.getValue(),
                this.cantidadProductos.getValue()
        );
        this.productosReaccion.add(reactivo);
        actualizarReaccion(reactivosReaccion, productosReaccion, labelReactivos, labelProductos);
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

            Stage dialog = createModalWindow(scene, event);
            dialog.showAndWait();
            if(controller.getTasaReaccion() != null) {
                this.tasaReaccion = controller.getTasaReaccion();
                this.textFieldTasaReaccion.setText(itemArray2String(this.tasaReaccion));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void guardarCambios(ActionEvent event) {
        this.reaccion.setReactantes(this.reactivosReaccion);
        this.reaccion.setProductos(this.productosReaccion);
        this.reaccion.setTasaReaccion(this.tasaReaccion);
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

}
