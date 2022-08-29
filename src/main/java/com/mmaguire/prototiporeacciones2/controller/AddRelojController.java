package com.mmaguire.prototiporeacciones2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddRelojController {


    @FXML
    private TextField nombreReloj;

    private String reloj;


    @FXML
    public void añadirReloj(ActionEvent event) {

        if(!this.nombreReloj.getText().isEmpty()) {

            reloj = this.nombreReloj.getText();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public String getReloj() {
        return reloj;
    }
}
