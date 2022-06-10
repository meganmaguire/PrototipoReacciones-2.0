package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.manager.ModelManager;
import com.mmaguire.prototiporeacciones2.model.Paso;
import com.mmaguire.prototiporeacciones2.model.Reaccion;
import com.uppaal.engine.Engine;
import com.uppaal.engine.EngineException;
import com.uppaal.engine.QueryResult;
import com.uppaal.model.core2.Document;
import com.uppaal.model.core2.Query;
import com.uppaal.model.system.UppaalSystem;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.mmaguire.prototiporeacciones2.manager.Helper.createModalWindow;
import static com.mmaguire.prototiporeacciones2.manager.Helper.itemArray2String;
import static com.mmaguire.prototiporeacciones2.manager.ModelManager.options;
import static com.mmaguire.prototiporeacciones2.manager.ModelManager.qf;
import static com.mmaguire.prototiporeacciones2.manager.ReaccionManager.createModel;
import static com.mmaguire.prototiporeacciones2.manager.ReaccionManager.generateSimulationQuery;

public class SimulacionesController {

    @FXML
    public Button button_simulate;
    @FXML
    public AnchorPane simulation_pane;
    @FXML
    private TableView<Reaccion> tablaReacciones;
    @FXML
    private TableColumn<Reaccion, Integer> columnaNroReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaReaccion;
    @FXML
    private TableColumn<Reaccion, String> columnaTasaReaccion;

    @FXML
    private TableView<Paso> tablaExperimento;
    @FXML
    private TableColumn<Paso, Integer> columnaTiempoPaso;
    @FXML
    private TableColumn<Paso, String> columnaModificacionesPaso;
    @FXML
    private Spinner<Integer> cantidadBombas;
    @FXML
    private Spinner<Integer> tiempoSimulacion;

    private Context contexto;

    @FXML
    public void initialize(){
        contexto = Context.getContext();

        this.tablaReacciones.setItems(this.contexto.getReacciones());
        this.columnaNroReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNroReaccion()));
        this.columnaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        this.columnaTasaReaccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(itemArray2String(cellData.getValue().calculateTasaReaccion())));

        this.tablaExperimento.setItems(this.contexto.getPasosExperimento());
        this.columnaTiempoPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTiempo()));
        this.columnaModificacionesPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));

        this.tiempoSimulacion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 60, 1));
        this.tiempoSimulacion.setEditable(true);
        this.cantidadBombas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 210, 1));
        this.cantidadBombas.setEditable(true);
    }

    @FXML
    public void simularSistema(ActionEvent event){
        this.contexto.getSistemaReacciones().setCantidadBombas(this.cantidadBombas.getValue());
        Stage stage = new Stage();
        // Tarea para el thread de simulación
        Task<Void> simulationTask = new Task<>() {
            @Override
            public Void call() {
                simulateSystem(event, stage);
                return null;
            }
        };

        // Muestra feedback de generación de simulación
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(simulationTask.progressProperty());
        generateCargandoStage(stage, progressBar, event);
        // Ejecuta el thread
        Thread getItemsThread = new Thread(simulationTask);
        getItemsThread.setDaemon(true);
        getItemsThread.start();

    }

    private void simulateSystem(ActionEvent event, Stage cargandoStage) {
        if (this.contexto.getReacciones().size() > 0) {

            Document doc = createModel(this.contexto.getSistemaReacciones());
            try {
                doc.save("untitled.xml");
                // connect to the engine server:
                Engine engine = ModelManager.connectToEngine();

                // compile the document into system representation:
                UppaalSystem sys = ModelManager.compile(engine, doc);

                Query query = new Query(generateSimulationQuery(tiempoSimulacion.getValue(), this.contexto.getSistemaReacciones()), "");
                QueryResult simulacion = engine.query(sys, options, query, qf);

                // Generar pantalla de simulación y enviar datos
                Platform.runLater(()-> {
                    cargandoStage.close();
                    showData(event, simulacion);
                });

            } catch (IOException e) {
                System.out.println("No se pudo escribir el archivo");
                e.printStackTrace();
            } catch (EngineException e) {
                System.out.println("No se conectar con el engine");
                e.printStackTrace();
            }
        }
    }

    private void showData(ActionEvent event, QueryResult simulacion){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("views/grafico-simulacion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            GraficoSimulacionController controller = loader.getController();
            controller.receiveData(simulacion);

            Stage stage = createModalWindow(scene, event);
            stage.showAndWait();
        }
        catch (IOException e) {
            System.out.println("Error al cargar archivo XML");
        }
    }

    public void generateCargandoStage(Stage stage, ProgressBar progressBar, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("views/cargando.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);


            Node node = (Node) event.getSource();
            Stage parentStage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);

            VBox contenedor = ((VBox) root.getChildren().get(0));
            contenedor.getChildren().add(progressBar);

            stage.initOwner(parentStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al cargar archivo XML");
        }
    }
}
