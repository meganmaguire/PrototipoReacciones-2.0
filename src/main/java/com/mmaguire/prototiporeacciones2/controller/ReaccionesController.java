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
import java.util.ArrayList;

import static com.mmaguire.prototiporeacciones2.manager.Helper.existeReactivoConNombre;
import static com.mmaguire.prototiporeacciones2.manager.Helper.styleButton;

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
    private TableColumn<Reactivo, Reactivo> columnaEliminarReactivo;

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
    private TableView<Reaccion> tablaReacciones;
    @FXML
    private TableColumn<Reaccion, Integer> columnaNroReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaTasaReaccion;
    @FXML
    private TableColumn<Reaccion, Reaccion> columnaEliminarReaccion;

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
                        labelTipoReaccion.setText("→");
                        constanteBeta.setDisable(true);
                        botonAñadirProducto.setDisable(false);
                    }
                    case degradacion -> {
                        labelTipoReaccion.setText("→");
                        constanteBeta.setDisable(true);
                        botonAñadirProducto.setDisable(true);
                    }
                }
                constanteBeta.setDisable(seleccionActual != TipoReaccion.reversible);
                if(seleccionActual == TipoReaccion.reversible)
                    labelTipoReaccion.setText("⇌");
                else
                    labelTipoReaccion.setText("→");
            }
        });

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

        // Tabla Reacciones
        this.tablaReacciones.setItems(contexto.getReacciones());
        this.columnaNroReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNroReaccion()));
        this.columnaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        this.columnaTasaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().calculateTasaReaccion()));
        this.columnaEliminarReaccion.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminarReaccion.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Reaccion reaccion, boolean empty) {
                super.updateItem(reaccion, empty);

                if (reaccion == null) {
                    setGraphic(null);
                    return;
                }
                styleButton(deleteButton);
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> getTableView().getItems().remove(reaccion)
                );
            }
        });
        this.tablaReacciones.setRowFactory(tv -> {
            TableRow<Reaccion> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Reaccion reaccion = row.getItem();
                    editarReaccion(reaccion, event);
                }
            });
            return row;
        });

        // Set spinners
        this.cantidadReactivos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1));
        this.cantidadReactivos.setEditable(true);
        this.cantidadProductos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1));
        this.cantidadProductos.setEditable(true);

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
    public void añadirReaccion() {
        Reaccion reaccion;
        TipoReaccion tipo = this.comboBoxTipoReaccion.getValue();
        if (!this.reactivosReaccion.isEmpty() && this.constanteAlpha != null) {
            if (tipo != TipoReaccion.reversible) {
                reaccion = new Reaccion(
                        "r" + Reaccion.getContador(),
                        new ArrayList<>(this.reactivosReaccion),
                        new ArrayList<>(this.productosReaccion),
                        tipo,
                        new Factor(
                                "alpha" + (Reaccion.getContador()),
                                Double.parseDouble(this.constanteAlpha.getText())),
                        new Factor(
                                "f" + (Reaccion.getContador()),
                                1.0)
                );
            } else {
                reaccion = new ReaccionReversible(
                        "r" + Reaccion.getContador(),
                        new ArrayList<>(this.reactivosReaccion),
                        new ArrayList<>(this.productosReaccion),
                        tipo,
                        new Factor(
                                "alpha" + (Reaccion.getContador()),
                                Double.parseDouble(this.constanteAlpha.getText())),
                        new Factor(
                                "f" + (Reaccion.getContador()),
                                1.0),
                        new Factor(
                                "beta" + (Reaccion.getContador()),
                                Double.parseDouble(this.constanteBeta.getText()))
                );
                contexto.getFactores().add(new Factor("f_" + Reaccion.getContador(), 1.0));
            }

            contexto.getReacciones().add(reaccion);
            contexto.getFactores().add(reaccion.getFactor());
            Reaccion.forwardContador();
            clearFieldsReacciones();
        }
    }

    public void editarReaccion(Reaccion reaccion, Event event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("editar-reaccion.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            EditarReaccionController controller = loader.getController();
            controller.receiveData(reaccion);

            Stage dialog = new Stage();
            Node node = (Node) event.getSource();
            Stage parentStage = (Stage) node.getScene().getWindow();
            dialog.setScene(scene);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
            this.tablaReacciones.refresh();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void clearFieldsReacciones(){
        this.reactivosReaccion.clear();
        this.productosReaccion.clear();

        this.constanteAlpha.setText(null);
        this.constanteBeta.setText(null);
        actualizarReaccion();
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
