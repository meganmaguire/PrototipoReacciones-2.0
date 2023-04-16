package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.model.Factor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarConstanteController {


    @FXML
    private TextField nombreConstante;
    @FXML
    private Spinner<Double> valorConstante;

    private Factor constante;

    @FXML
    public void initialize() {
        this.valorConstante.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000000, 1000000, 0,0.01));
        this.valorConstante.setEditable(true);
    }
    @FXML
    public void receiveData(Factor factor) {
        this.constante = factor;
        this.nombreConstante.setText(this.constante.getNombre());
        this.valorConstante.getValueFactory().setValue(this.constante.getValor());
    }
    @FXML
    public void guardarCambios(ActionEvent event) {
        if(this.nombreConstante.getText().isEmpty()){
            this.nombreConstante.getStyleClass().add("text-field-error");
        }
        if(!hayError()) {
            constante.setNombre(this.nombreConstante.getText());
            constante.setValor(this.valorConstante.getValue());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void descartarCambios(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    private boolean hayError(){
        return this.nombreConstante.getText().isBlank();
    }
}
