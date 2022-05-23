package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.manager.Routes;
import com.mmaguire.prototiporeacciones2.model.Sistema;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

import static com.mmaguire.prototiporeacciones2.manager.FileManager.loadSystemFromFile;
import static com.mmaguire.prototiporeacciones2.manager.FileManager.saveSystemToFile;

public class PrincipalController {

    private Context contexto;
    private AnchorPane componentesPane;
    private AnchorPane reaccionesPane;
    private AnchorPane experimentosPane;
    private AnchorPane simulacionesPane;
    private AnchorPane historialPane;

    @FXML
    public void initialize() {
        contexto = Context.getContext();
        FXMLLoader fxmlLoader;
        try {
            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.componentes));
            componentesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.reacciones));
            reaccionesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.experimentos));
            experimentosPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.simulaciones));
            simulacionesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.historial));
            historialPane = fxmlLoader.load();

        }
        catch (IOException e){
            System.out.println("Hubo un problema al intentar cargar el archivo FXML correspondiente.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onMouseClickedComponentes(MouseEvent event){
        selectPane(componentesPane, event);
    }

    @FXML
    public void onMouseClickedReacciones(MouseEvent event){
        selectPane(reaccionesPane, event);
    }

    @FXML
    public void onMouseClickedExperimentos(MouseEvent event){
        selectPane(experimentosPane, event);
    }

    @FXML
    public void onMouseClickedSimulaciones(MouseEvent event){
        selectPane(simulacionesPane, event);
    }

    @FXML
    public void onMouseClickedHistorial(MouseEvent event){
        selectPane(historialPane, event);
    }

    // Menu
    @FXML
    public void newFile(){}

    @FXML
    public void openFile(ActionEvent event) {
        MenuItem menuItem = ((MenuItem) event.getTarget());
        ContextMenu cm = menuItem.getParentPopup();
        Window window = cm.getScene().getWindow();
        FileChooser.ExtensionFilter filterRS = new FileChooser.ExtensionFilter("Sistema de Reacción (*.rs)", "*.rs");
        FileChooser.ExtensionFilter filterAll = new FileChooser.ExtensionFilter("Todos los archivos", "*.*");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filterRS);
        fileChooser.getExtensionFilters().add(filterAll);
        File selected = fileChooser.showOpenDialog(window);

        if (selected != null) {
            Sistema sistemaReacciones = loadSystemFromFile(selected.getAbsolutePath());

            if (sistemaReacciones != null) {
                this.contexto.setSistemaReacciones(sistemaReacciones);
                this.contexto.setReactivos(FXCollections.observableList(sistemaReacciones.getReactivos()));
                this.contexto.setReacciones(FXCollections.observableList(sistemaReacciones.getReacciones()));
                this.contexto.setFactores(FXCollections.observableList(sistemaReacciones.getFactores()));
                this.contexto.setConstantesReaccion(FXCollections.observableList(sistemaReacciones.getConstantesReaccion()));
                this.contexto.setExperimento(sistemaReacciones.getExperimento());

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al cargar datos");
                alert.setContentText("Ha ocurrido un error al intentar cargar los datos del sistema de reacciones.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void closeSystem(){}

    @FXML
    public void saveFile(){}

    @FXML
    public void saveFileAs(ActionEvent event){
        MenuItem menuItem = ((MenuItem) event.getTarget());
        ContextMenu cm = menuItem.getParentPopup();
        Window window = cm.getScene().getWindow();
        FileChooser fileChooser =  new FileChooser();
        fileChooser.setInitialFileName("sistema.rs");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sistema de Reacción (*.rs)", "*.rs")
        );
        File selected = fileChooser.showSaveDialog(window);

        if(selected != null) {
            boolean result = saveSystemToFile(this.contexto.getSistemaReacciones(), selected.getAbsolutePath());

            if(result) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText("Éxito");
                alert.setContentText("Se han guardado correctamente los datos del sistema de reacciones.");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al guardar datos");
                alert.setContentText("Ha ocurrido un error al intentar guardar los datos del sistema de reacciones.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void exportXML(){}

    @FXML
    public void saveHistory(){}

    @FXML
    public void loadHistory(){}

    @FXML
    public void quitApp(){
        Platform.exit();
    }


    private void selectPane(AnchorPane pane, MouseEvent event){
        Node node = (Node) event.getSource();
        SplitPane splitPane = (SplitPane) node.getParent().getParent().getParent().getParent().getParent();

        splitPane.getItems().remove(1);
        splitPane.getItems().add(pane);
    }
}
