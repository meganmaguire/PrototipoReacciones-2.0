package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.model.Factor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddConstanteController {

    @FXML
    private TextField nombreConstante;
    @FXML
    private Spinner<Double> valorConstante;

    private Factor constante;

    @FXML
    public void initialize() {
        this.valorConstante.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0,0.01));

    }

    @FXML
    public void añadirConstante(ActionEvent event) {
        String nombre = this.nombreConstante.getText();
        double valor = this.valorConstante.getValue();

        constante = new Factor(nombre,valor);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public Factor getConstante(){
        return constante;
    }
}
