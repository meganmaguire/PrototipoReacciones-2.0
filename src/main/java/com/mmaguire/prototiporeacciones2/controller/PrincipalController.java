package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.manager.ModelManager;
import com.mmaguire.prototiporeacciones2.manager.Routes;
import com.mmaguire.prototiporeacciones2.model.Sistema;
import com.uppaal.engine.Engine;
import com.uppaal.engine.EngineException;
import com.uppaal.model.core2.Document;
import com.uppaal.model.system.UppaalSystem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.mmaguire.prototiporeacciones2.manager.Context.bundle;
import static com.mmaguire.prototiporeacciones2.manager.FileManager.*;
import static com.mmaguire.prototiporeacciones2.manager.ReaccionManager.createModel;

public class PrincipalController {

    @FXML
    private Label menuComponentes;
    @FXML
    private Label menuReacciones;
    @FXML
    private Label menuExperimentos;
    @FXML
    private Label menuSimulaciones;
    @FXML
    private Label menuHistorial;


    private Context contexto;
    private String filePath;
    private Pane componentesPane;
    private Pane reaccionesPane;
    private Pane experimentosPane;
    private Pane simulacionesPane;
    private Pane historialPane;

    @FXML
    public void initialize() {
        contexto = Context.getContext();
        FXMLLoader fxmlLoader;
        try {
            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.componentes), bundle);
            componentesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.reacciones), bundle);
            reaccionesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.experimentos), bundle);
            experimentosPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.simulaciones), bundle);
            simulacionesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(MainApp.class.getResource(Routes.historial), bundle);
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
        menuComponentes.getStyleClass().add("menu-label-selected");
        menuReacciones.getStyleClass().remove("menu-label-selected");
        menuExperimentos.getStyleClass().remove("menu-label-selected");
        menuSimulaciones.getStyleClass().remove("menu-label-selected");
        menuHistorial.getStyleClass().remove("menu-label-selected");
    }

    @FXML
    public void onMouseClickedReacciones(MouseEvent event){
        selectPane(reaccionesPane, event);
        menuReacciones.getStyleClass().add("menu-label-selected");
        menuComponentes.getStyleClass().remove("menu-label-selected");
        menuExperimentos.getStyleClass().remove("menu-label-selected");
        menuSimulaciones.getStyleClass().remove("menu-label-selected");
        menuHistorial.getStyleClass().remove("menu-label-selected");

    }

    @FXML
    public void onMouseClickedExperimentos(MouseEvent event){
        selectPane(experimentosPane, event);
        menuExperimentos.getStyleClass().add("menu-label-selected");
        menuReacciones.getStyleClass().remove("menu-label-selected");
        menuComponentes.getStyleClass().remove("menu-label-selected");
        menuSimulaciones.getStyleClass().remove("menu-label-selected");
        menuHistorial.getStyleClass().remove("menu-label-selected");
    }

    @FXML
    public void onMouseClickedSimulaciones(MouseEvent event){
        selectPane(simulacionesPane, event);
        menuSimulaciones.getStyleClass().add("menu-label-selected");
        menuReacciones.getStyleClass().remove("menu-label-selected");
        menuExperimentos.getStyleClass().remove("menu-label-selected");
        menuComponentes.getStyleClass().remove("menu-label-selected");
        menuHistorial.getStyleClass().remove("menu-label-selected");
    }

    @FXML
    public void onMouseClickedHistorial(MouseEvent event){
        selectPane(historialPane, event);
        menuHistorial.getStyleClass().add("menu-label-selected");
        menuReacciones.getStyleClass().remove("menu-label-selected");
        menuExperimentos.getStyleClass().remove("menu-label-selected");
        menuSimulaciones.getStyleClass().remove("menu-label-selected");
        menuComponentes.getStyleClass().remove("menu-label-selected");
    }

    // Menu
    @FXML
    public void newFile(){
        // Si se han realizado cambios
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("alert.warning"));
        alert.setHeaderText(bundle.getString("alert.close"));
        alert.setContentText(bundle.getString("alert.open.detail"));
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK))
            Context.reset();
    }

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
            this.filePath =  selected.getAbsolutePath();
            Sistema sistemaReacciones = loadSystemFromFile(this.filePath);
            if (sistemaReacciones != null) {
                Context.reset();
                this.contexto.setSistemaReacciones(sistemaReacciones);
                this.contexto.setReactivos(FXCollections.observableList(this.contexto.getSistemaReacciones().getReactivos()));
                this.contexto.setReacciones(FXCollections.observableList(this.contexto.getSistemaReacciones().getReacciones()));
                this.contexto.setFactores(FXCollections.observableList(this.contexto.getSistemaReacciones().getFactores()));
                this.contexto.setConstantesReaccion(FXCollections.observableList(this.contexto.getSistemaReacciones().getConstantesReaccion()));
                this.contexto.setExperimentos(FXCollections.observableList(this.contexto.getSistemaReacciones().getExperimentos()));
                this.contexto.setContadorReacciones();
                this.contexto.setContadorExperimentos();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("alert.error"));
                alert.setHeaderText(bundle.getString("alert.open"));
                alert.setContentText(bundle.getString("alert.open.detail"));
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void closeSystem(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("alert.aviso"));
        alert.setHeaderText(bundle.getString("alert.close"));
        alert.setContentText(bundle.getString("alert.close.detail"));
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK))
            Context.reset();
    }

    @FXML
    public void saveFile(ActionEvent event){
        if(this.filePath != null){
            boolean result = saveSystemToFile(this.contexto.getSistemaReacciones(), this.filePath);
            showDialog(result);
        }
        else {
            saveFileAs(event);
        }
    }

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
            this.filePath = selected.getAbsolutePath();
            boolean result = saveSystemToFile(this.contexto.getSistemaReacciones(), this.filePath);
            showDialog(result);
        }
    }

    @FXML
    public void exportXML(ActionEvent event){
        MenuItem menuItem = ((MenuItem) event.getTarget());
        ContextMenu cm = menuItem.getParentPopup();
        Window window = cm.getScene().getWindow();
        FileChooser fileChooser =  new FileChooser();
        fileChooser.setInitialFileName("model.xml");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("UPPAAL XML (*.xml)", "*.xml")
        );
        File selected = fileChooser.showSaveDialog(window);

        if(selected != null) {
            if (this.contexto.getSistemaReacciones().getReacciones().size() > 0) {
                Document doc = createModel(this.contexto.getSistemaReacciones());
                try {
                    doc.save(selected.getAbsolutePath());
                    // connect to the engine server:
                    Engine engine = ModelManager.connectToEngine();

                    // compile the document into system representation:
                    UppaalSystem sys = ModelManager.compile(engine, doc);

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(bundle.getString("alert.exito"));
                    alert.setHeaderText(bundle.getString("alert.exito"));
                    alert.setContentText(bundle.getString("alert.generation"));

                    alert.showAndWait();

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

    @FXML
    public void saveHistory(ActionEvent event){
        MenuItem menuItem = ((MenuItem) event.getTarget());
        ContextMenu cm = menuItem.getParentPopup();
        Window window = cm.getScene().getWindow();
        FileChooser fileChooser =  new FileChooser();
        fileChooser.setInitialFileName("historial.rsh");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Historial de Sistema de Reacción (*.rsh)", "*.rsh")
        );
        File selected = fileChooser.showSaveDialog(window);
        if(selected != null) {
            this.filePath = selected.getAbsolutePath();
            boolean result = saveHistoryToFile(this.contexto.getHistorial(), this.filePath);
            showDialog(result);
        }
    }

    @FXML
    public void loadHistory(ActionEvent event) {
        MenuItem menuItem = ((MenuItem) event.getTarget());
        ContextMenu cm = menuItem.getParentPopup();
        Window window = cm.getScene().getWindow();
        FileChooser.ExtensionFilter filterRSH = new FileChooser.ExtensionFilter("Historial de Sistema de Reacción (*.rsh)", "*.rsh");
        FileChooser.ExtensionFilter filterAll = new FileChooser.ExtensionFilter("Todos los archivos", "*.*");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filterRSH);
        fileChooser.getExtensionFilters().add(filterAll);
        File selected = fileChooser.showOpenDialog(window);

        if (selected != null) {
            this.filePath = selected.getAbsolutePath();
            List<Sistema> historial = loadHistoryFromFile(this.filePath);

            if(historial != null) {
                this.contexto.getHistorial().clear();
                this.contexto.getHistorial().addAll(historial);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("alert.error"));
                alert.setHeaderText(bundle.getString("alert.open"));
                alert.setContentText(bundle.getString("alert.open.detail"));
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void quitApp(){
        Platform.exit();
    }

    @FXML
    public void openManual() {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(bundle.getString("user_guide"));
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("alert.error"));
                alert.setHeaderText(bundle.getString("alert.manual"));
                alert.setContentText(bundle.getString("alert.manual.detail"));
                alert.showAndWait();
            }
        }
    }

    private void showDialog(boolean result) {
        Alert alert;
        if (result) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("alert.exito"));
            alert.setHeaderText(bundle.getString("alert.exito"));
            alert.setContentText(bundle.getString("alert.save"));
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("alert.error"));
            alert.setHeaderText(bundle.getString("alert.save.error"));
            alert.setContentText(bundle.getString("alert.save.error.detail"));
        }
        alert.showAndWait();
    }

    private void selectPane(Pane pane, MouseEvent event){
        Node node = (Node) event.getSource();
        SplitPane splitPane = (SplitPane) node.getParent().getParent().getParent().getParent();

        splitPane.getItems().remove(1);
        splitPane.getItems().add(pane);
    }

    public Pane getComponentesPane() {
        return componentesPane;
    }

    public Label getMenuComponentes() {
        return menuComponentes;
    }
}
