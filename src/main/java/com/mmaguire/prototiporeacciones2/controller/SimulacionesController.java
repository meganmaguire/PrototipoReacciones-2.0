package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.manager.ModelManager;
import com.mmaguire.prototiporeacciones2.manager.Parser;
import com.mmaguire.prototiporeacciones2.manager.SimulationManager;
import com.mmaguire.prototiporeacciones2.model.*;
import com.uppaal.engine.Engine;
import com.uppaal.engine.EngineException;
import com.uppaal.model.core2.Document;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mmaguire.prototiporeacciones2.manager.Context.bundle;
import static com.mmaguire.prototiporeacciones2.manager.FileManager.saveQueryToFile;
import static com.mmaguire.prototiporeacciones2.manager.Helper.*;
import static com.mmaguire.prototiporeacciones2.manager.Helper.showData;
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
    private TableColumn<Paso, String> columnaTiempoPaso;
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
        this.columnaTiempoPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTiempo().toString()));
        this.columnaModificacionesPaso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));

        this.tiempoSimulacion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 60, 1));
        this.tiempoSimulacion.setEditable(true);
        this.cantidadBombas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 210, 1));
        this.cantidadBombas.setEditable(true);
    }

    @FXML
    public void simularSistema(ActionEvent event){
        this.contexto.getSistemaReacciones().setCantidadBombas(this.cantidadBombas.getValue());
        Stage stage = new Stage();
        //simulateSystem(event, stage);
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
                String tempDir = getTempDirectory(System.getProperty("os.name"));
                ArrayList<String> out = new ArrayList<>();

                doc.save(tempDir + "untitled.xml");
                // connect to the engine server:
                Engine engine = ModelManager.connectToEngine();
                // compile the document into system representation:
                UppaalSystem sys = ModelManager.compile(engine, doc);

                saveQueryToFile(generateSimulationQuery(
                        tiempoSimulacion.getValue(),
                        this.contexto.getSistemaReacciones()),
                        tempDir + "query.q" );
                int exitCode = SimulationManager.executeSimulation(tempDir, out);

                if(exitCode == 0) {
                    try {
                        out = Parser.removeHeader(out);
                        List<DatosComponente> datos = Parser.parse(out);
                        Simulacion simulacion = new Simulacion(datos, "simulacion", LocalDateTime.now());
                        Sistema nuevoSistema = this.contexto.getSistemaReacciones().clone();
                        nuevoSistema.setSimulacion(simulacion);

                        this.contexto.getHistorial().add(nuevoSistema);
                        // Generar pantalla de simulación y enviar datos
                        Platform.runLater(() -> {
                            cargandoStage.close();
                            showData(event, simulacion);
                        });
                    }
                    catch (IndexOutOfBoundsException e){
                        Platform.runLater(()-> {
                            cargandoStage.close();
                            Alert alert;
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error al simular el modelo");
                            alert.setContentText("Ha ocurrido un error al ejecutar el comando de simulación. Revise el modelo en UPPAAL para mayor detalle.");
                            alert.showAndWait();
                        });}
                }
                else{
                    Platform.runLater(()-> {
                        cargandoStage.close();
                        Alert alert;
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error al simular el modelo");
                        alert.setContentText("Ha ocurrido un error al ejecutar el comando de simulación. Código de error: " + exitCode);
                        alert.showAndWait();
                    });
                }

            } catch (IOException e) {
                System.out.println("No se pudo escribir el archivo");
                e.printStackTrace();
            } catch (EngineException e) {
                cargandoStage.close();
                System.out.println("No se conectar con el engine");
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedOperationException e){
                System.out.println("Sistema operativo no reconcido. No se puede simular el sistema");
            }
        }
    }

    private void showData(ActionEvent event, Simulacion simulacion){
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/grafico-simulacion.fxml"), bundle);
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
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/cargando.fxml"), bundle);
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
