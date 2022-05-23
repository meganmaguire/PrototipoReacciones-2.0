package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Factor;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EditarComponenteController {

    @FXML
    private TextField nombreComponente;
    @FXML
    private Spinner<Integer> cantidadComponente;
    @FXML
    private CheckBox actualizableComponente;
    @FXML
    private CheckBox poseeConstante;
    @FXML
    private ComboBox<Factor> constanteAsociada;
    @FXML
    private Button botonAñadirConstante;

    private Context contexto;
    private Reactivo componente;

    @FXML
    public void initialize(){
        contexto = Context.getContext();

        this.constanteAsociada.setItems(this.contexto.getConstantesReaccion());
        this.cantidadComponente.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0,1));
        this.cantidadComponente.setEditable(true);

        this.actualizableComponente.setSelected(true);
        this.constanteAsociada.setDisable(true);
        this.botonAñadirConstante.setDisable(true);
        this.poseeConstante.selectedProperty().addListener((ov, seleccionAnterior, seleccionActual) -> {
            constanteAsociada.setDisable(!seleccionActual);
            botonAñadirConstante.setDisable(!seleccionActual);
        });
    }

    @FXML
    public void receiveData(Reactivo componente) {
        this.componente = componente;
        this.nombreComponente.setText(this.componente.getNombre());
        this.cantidadComponente.getValueFactory().setValue(this.componente.getCantidadInicial());
        this.actualizableComponente.setSelected(this.componente.isActualizable());
        if (this.componente.getConstanteAsociada() != null){
            this.poseeConstante.setSelected(true);
            this.constanteAsociada.getSelectionModel().select(this.componente.getConstanteAsociada());
        }
    }

    @FXML
    public void guardarCambios(ActionEvent event){
        this.componente.setNombre(this.nombreComponente.getText());
        this.componente.setCantidadInicial(this.cantidadComponente.getValue());
        this.componente.setActualizable(this.actualizableComponente.isSelected());
        if(this.poseeConstante.isSelected())
            this.componente.setConstanteAsociada(this.constanteAsociada.getValue());
        else
            this.componente.setConstanteAsociada(null);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void descartarCambios(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void añadirConstante(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("añadir-constante.fxml"));
            Parent root = loader.load();
            AddConstanteController controller = loader.getController();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            Node node = (Node) event.getSource();
            Stage parentStage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.initOwner(parentStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            Factor constante = controller.getConstante();
            if (constante != null)
                this.contexto.getConstantesReaccion().add(constante);

        }
        catch (IOException e) {
            System.out.println("Hubo un problema al leer el archivo FXML");
        }
    }

}
