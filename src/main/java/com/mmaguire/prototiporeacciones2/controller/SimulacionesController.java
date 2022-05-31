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
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.mmaguire.prototiporeacciones2.manager.Helper.itemArray2String;
import static com.mmaguire.prototiporeacciones2.manager.ModelManager.options;
import static com.mmaguire.prototiporeacciones2.manager.ModelManager.qf;
import static com.mmaguire.prototiporeacciones2.manager.ReaccionManager.createModel;
import static com.mmaguire.prototiporeacciones2.manager.ReaccionManager.generateSimulationQuery;

public class SimulacionesController {

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

    }

    @FXML
    public void simularSistema(ActionEvent event){
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
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("views/grafico-simulacion.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);

                GraficoSimulacionController controller = loader.getController();
                controller.receiveData(simulacion);

                Stage stage = new Stage();
                Node node = (Node) event.getSource();
                Stage parentStage = (Stage) node.getScene().getWindow();
                stage.setScene(scene);
                stage.initOwner(parentStage);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

            } catch (IOException e) {
                System.out.println("No se pudo escribir el archivo");
                e.printStackTrace();
            } catch (EngineException e) {
                System.out.println("No se conectar con el engine");
                e.printStackTrace();
            }
        }
    }

}
