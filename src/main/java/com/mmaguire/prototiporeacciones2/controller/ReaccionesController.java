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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static com.mmaguire.prototiporeacciones2.manager.Context.bundle;
import static com.mmaguire.prototiporeacciones2.manager.Helper.*;

public class ReaccionesController {

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
    private TableView<Reaccion> tablaReacciones;
    @FXML
    private TableColumn<Reaccion, Integer> columnaNroReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaTasaReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaAlpha;
    @FXML
    private TableColumn<Reaccion, String> columnaBeta;
    @FXML
    private TableColumn<Reaccion, Reaccion> columnaEliminarReaccion;

    private Context contexto;

    private ObservableList<ReactivoReaccion> reactivosReaccion;
    private ObservableList<ReactivoReaccion> productosReaccion;
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

        // Tabla Reacciones
        this.tablaReacciones.setItems(contexto.getReacciones());
        this.columnaNroReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNroReaccion()));
        this.columnaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        this.columnaTasaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(itemArray2String(cellData.getValue().getTasaReaccion())));
        this.columnaAlpha.setCellValueFactory(cellData -> new SimpleObjectProperty<>(String.valueOf(cellData.getValue().getAlpha().getValor())));
        this.columnaBeta.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                cellData.getValue() instanceof ReaccionReversible ? (String.valueOf(( (ReaccionReversible) cellData.getValue()).getBeta().getValor())) : ""));
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
                styleDeleteButton(deleteButton);
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
        this.cantidadReactivos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1, 1));
        this.cantidadReactivos.setEditable(true);
        this.cantidadProductos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1, 1));
        this.cantidadProductos.setEditable(true);

    }

    @FXML
    public void añadirReactivo() {
        if(this.comboBoxReactivos.getValue() != null) {
            ReactivoReaccion reactivo = new ReactivoReaccion(
                    this.comboBoxReactivos.getValue(),
                    this.cantidadReactivos.getValue()
            );

            if (!existeReactivoReaccionConNombre(reactivo.getReactivoAsociado().getNombre(), this.reactivosReaccion))
                this.reactivosReaccion.add(reactivo);
            actualizarReaccion(reactivosReaccion, productosReaccion, labelReactivos, labelProductos);
        }
    }

    @FXML
    public void añadirProducto() {
            if(this.comboBoxProductos.getValue() != null) {
            ReactivoReaccion reactivo = new ReactivoReaccion(
                    this.comboBoxProductos.getValue(),
                    this.cantidadProductos.getValue()
            );
            this.productosReaccion.add(reactivo);
            actualizarReaccion(reactivosReaccion, productosReaccion, labelReactivos, labelProductos);
        }
    }

    @FXML
    public void añadirReaccion() {
        Reaccion reaccion = null;
        TipoReaccion tipo = this.comboBoxTipoReaccion.getValue();
        if (!this.reactivosReaccion.isEmpty() && !this.constanteAlpha.getText().isEmpty()) {
            switch (tipo) {
                case reversible -> {
                    if(!this.productosReaccion.isEmpty() && !this.constanteBeta.getText().isEmpty()) {
                        reaccion = new ReaccionReversible(
                                "r" + Reaccion.getContador(),
                                new ArrayList<>(this.reactivosReaccion),
                                new ArrayList<>(this.productosReaccion),
                                tipo,
                                new Factor(
                                        "alpha" + (Reaccion.getContador()),
                                        Double.parseDouble(this.constanteAlpha.getText().replace(',', '.'))),
                                new Factor(
                                        "f" + (Reaccion.getContador()),
                                        1.0),
                                new Factor(
                                        "beta" + (Reaccion.getContador()),
                                        Double.parseDouble(this.constanteBeta.getText().replace(',', '.'))),
                                new Factor("f_" + (Reaccion.getContador()),
                                        1.0)
                        );
                        contexto.getFactores().add(((ReaccionReversible) reaccion).getFactorVuelta());
                    }
                }
                case irreversible -> {
                    if(!this.productosReaccion.isEmpty()){
                        reaccion = new Reaccion(
                                "r" + Reaccion.getContador(),
                                new ArrayList<>(this.reactivosReaccion),
                                new ArrayList<>(this.productosReaccion),
                                tipo,
                                new Factor(
                                        "alpha" + (Reaccion.getContador()),
                                        Double.parseDouble(this.constanteAlpha.getText().replace(',', '.'))),
                                new Factor(
                                        "f" + (Reaccion.getContador()),
                                        1.0)
                        );
                    }
                }
                case degradacion -> {
                    if(this.productosReaccion.isEmpty()){
                        reaccion = new Reaccion(
                                "r" + Reaccion.getContador(),
                                new ArrayList<>(this.reactivosReaccion),
                                new ArrayList<>(this.productosReaccion),
                                tipo,
                                new Factor(
                                        "alpha" + (Reaccion.getContador()),
                                        Double.parseDouble(this.constanteAlpha.getText().replace(',', '.'))),
                                new Factor(
                                        "f" + (Reaccion.getContador()),
                                        1.0)
                        );
                    }
                }
            }
            if(reaccion != null) {
                reaccion.setTasaReaccion(reaccion.calculateTasaReaccion());
                contexto.getReacciones().add(reaccion);
                contexto.getFactores().add(reaccion.getFactor());
                Reaccion.forwardContador();
                clearFieldsReacciones();
            }
        }
    }

    public void editarReaccion(Reaccion reaccion, Event event){
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/editar-reaccion.fxml"), bundle);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            EditarReaccionController controller = loader.getController();
            controller.receiveData(reaccion);

            Stage dialog = createModalWindow(scene, event);
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

        this.constanteAlpha.clear();
        this.constanteBeta.clear();
        actualizarReaccion(reactivosReaccion, productosReaccion, labelReactivos, labelProductos);
    }




}
