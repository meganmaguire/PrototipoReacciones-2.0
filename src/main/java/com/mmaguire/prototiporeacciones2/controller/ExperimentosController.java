package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Experimento;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static com.mmaguire.prototiporeacciones2.manager.Context.bundle;
import static com.mmaguire.prototiporeacciones2.manager.Helper.createModalWindow;
import static com.mmaguire.prototiporeacciones2.manager.Helper.styleDeleteButton;

public class ExperimentosController {

    @FXML
    private TableView<Experimento> tablaExperimentos;
    @FXML
    private TableColumn<Experimento, Integer> columnaNroExperimento;
    @FXML
    private TableColumn<Experimento, String> columnaPasosExperimento;
    @FXML
    private TableColumn<Experimento, Experimento> columnaEliminarExperimento;

    private Context contexto;

    @FXML
    public void initialize(){

        this.contexto = Context.getContext();
        this.tablaExperimentos.setItems(this.contexto.getExperimentos());
        this.columnaNroExperimento.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumero()));
        this.columnaPasosExperimento.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().pasosToString()));
        this.columnaEliminarExperimento.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnaEliminarExperimento.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Experimento experimento, boolean empty) {
                super.updateItem(experimento, empty);

                if (experimento == null) {
                    setGraphic(null);
                    return;
                }
                styleDeleteButton(deleteButton);
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> getTableView().getItems().remove(experimento)
                );
            }
        });
        this.tablaExperimentos.setRowFactory(tv -> {
            TableRow<Experimento> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Experimento experimento = row.getItem();
                    editarExperimento(experimento, event);
                }
            });
            return row;
        });

    }
    @FXML
    public void añadirExperimento(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/experimento.fxml"), bundle);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage dialog = createModalWindow(scene, event);
            dialog.showAndWait();
            this.tablaExperimentos.refresh();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void editarExperimento(Experimento experimento, Event event){
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/experimento.fxml"), bundle);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            ExperimentoController controller = loader.getController();
            controller.recieveData(experimento);

            Stage dialog = createModalWindow(scene, event);
            dialog.showAndWait();
            this.tablaExperimentos.refresh();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
