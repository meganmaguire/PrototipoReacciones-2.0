package com.example.prototiporeacciones2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PrincipalController {


    private AnchorPane componentesPane;
    private AnchorPane reaccionesPane;
    private AnchorPane experimentosPane;
    private AnchorPane simulacionesPane;
    private AnchorPane historialPane;


    @FXML
    public void initialize() {
        FXMLLoader fxmlLoader;
        try {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(Routes.componentes));
            componentesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(Routes.reacciones));
            reaccionesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(Routes.experimentos));
            experimentosPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(Routes.simulaciones));
            simulacionesPane = fxmlLoader.load();

            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(Routes.historial));
            historialPane = fxmlLoader.load();

        }
        catch (IOException e){
            System.out.println("Hubo un problema al intentar cargar el archivo FXML correspondiente.");
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


    private void selectPane(AnchorPane pane, MouseEvent event){
        Node node = (Node) event.getSource();
        SplitPane splitPane = (SplitPane) node.getParent().getParent().getParent().getParent().getParent();

        splitPane.getItems().remove(1);
        splitPane.getItems().add(pane);
    }
}
