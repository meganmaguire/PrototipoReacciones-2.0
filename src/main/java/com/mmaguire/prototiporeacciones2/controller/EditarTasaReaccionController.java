package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.manager.Context;
import com.mmaguire.prototiporeacciones2.model.Reaccion;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class EditarTasaReaccionController {


    @FXML
    private ComboBox<Reactivo> comboBoxComponentes;
    @FXML
    private TextField textFieldTasaReaccion;
    @FXML
    private Canvas canvasLatex;
    private FXGraphics2D g2;
    private TeXIcon icon;

    private Reaccion reaccion;
    private ArrayList<String> tasaReaccion;
    private String tasaReaccionLatex;
    private String lastAdded;
    private ObservableSet<Reactivo> componentes;
    private Stack<String> parentesisCheck;

    @FXML
    public void initialize(){
        this.tasaReaccion = new ArrayList<>();
        this.lastAdded = "";
        this.tasaReaccionLatex = "";
        this.componentes = FXCollections.observableSet(new HashSet<>());
        this.parentesisCheck = new Stack<>();
        this.textFieldTasaReaccion.setEditable(false);
        this.g2 = new FXGraphics2D(canvasLatex.getGraphicsContext2D());
        renderFormula(tasaReaccionLatex);
    }

    private void draw() {
        double width = this.canvasLatex.getWidth();
        double height = this.canvasLatex.getHeight();
        this.g2.setColor(Color.WHITE);
        this.g2.fillRect(0,0, ((int) width), ((int) height));
        BufferedImage image = new BufferedImage(icon.getIconWidth(),
                icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = image.createGraphics();
        gg.setColor(Color.WHITE);
        gg.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(Color.BLACK);
        icon.paintIcon(jl, gg, 0, 0);
        this.g2.drawImage(image,((int) (width/2)-(icon.getIconWidth()/2)), ((int) (height/2)-(icon.getIconHeight()/2)), null);
    }

    private void renderFormula(String latex){
        TeXFormula formula = new TeXFormula(latex);
        this.icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY,25);
        this.draw();
    }
    private void updateTasa(){
        this.textFieldTasaReaccion.setText(stringArray2String(this.tasaReaccion));
        this.tasaReaccionLatex = this.tasaReaccion2LaTeX(this.tasaReaccion);
        renderFormula(tasaReaccionLatex);
    }
    @FXML
    public void recieveData(Reaccion reaccion) {
        this.reaccion = reaccion;
        renderFormula(tasaReaccionLatex);
        this.componentes.addAll(this.reaccion.getReactantes());
        this.componentes.addAll(this.reaccion.getProductos());
        this.comboBoxComponentes.setItems(FXCollections.observableList(this.componentes.stream().toList()));
    }

    @FXML
    public void añadirMas(ActionEvent event) {
        if (lastAdded.equalsIgnoreCase("componente") || lastAdded.equalsIgnoreCase("parentesisCierra")) {
            this.tasaReaccion.add("+");
            updateTasa();
        }
        this.lastAdded = "operador";
    }
    @FXML
    public void añadirMenos(ActionEvent event) {
        if (lastAdded.equalsIgnoreCase("componente") || lastAdded.equalsIgnoreCase("parentesisCierra")) {
            this.tasaReaccion.add("-");
            updateTasa();
        }
        this.lastAdded = "operador";
    }
    @FXML
    public void añadirPor(ActionEvent event) {
        if (lastAdded.equalsIgnoreCase("componente") || lastAdded.equalsIgnoreCase("parentesisCierra")) {
            this.tasaReaccion.add("*");
            updateTasa();
        }
        this.lastAdded = "operador";
    }
    @FXML
    public void añadirDivision(ActionEvent event) {
        if (lastAdded.equalsIgnoreCase("componente") || lastAdded.equalsIgnoreCase("parentesisCierra")) {
            this.tasaReaccion.add("/");
            updateTasa();
        }
        this.lastAdded = "operador";
    }
    @FXML
    public void añadirParentesisAbre(ActionEvent event) {
        this.parentesisCheck.push("parentesis" + this.parentesisCheck.size()+1);
        this.tasaReaccion.add("(");
        updateTasa();
        this.lastAdded = "parentesisAbre";
    }
    @FXML
    public void añadirParentesisCierra(ActionEvent event) {
        if(!this.parentesisCheck.isEmpty()) {
            this.parentesisCheck.pop();
            this.tasaReaccion.add(")");
            updateTasa();
            this.lastAdded = "parentesisCierra";
        }
    }
    @FXML
    public void añadirPotencia(ActionEvent event) {
        if (lastAdded.equalsIgnoreCase("componente") || lastAdded.equalsIgnoreCase("parentesisCierra")) {
            this.tasaReaccion.add("^");
            updateTasa();
        }
        this.lastAdded = "operador";
    }
    @FXML
    public void añadirComponente(ActionEvent event) {
        if (this.comboBoxComponentes.getValue() != null) {
            if (!lastAdded.equalsIgnoreCase("componente") && !lastAdded.equalsIgnoreCase("parentesisCierra")) {
                this.tasaReaccion.add(this.comboBoxComponentes.getValue().getNombre());
                updateTasa();
                this.lastAdded = "componente";
            }
        }
    }

    @FXML
    public void borrar(ActionEvent event) {
        if (!this.tasaReaccion.isEmpty()) {
            if (lastAdded.equalsIgnoreCase("parentesisAbre")) {
                this.parentesisCheck.pop();
                if(lastAdded.equalsIgnoreCase("parentesisCierra"))
                    this.parentesisCheck.push("parentesis" + this.parentesisCheck.size()+1);
            }
            this.tasaReaccion.remove(this.tasaReaccion.size() - 1);
            if (!this.tasaReaccion.isEmpty())
                this.lastAdded = this.tasaReaccion.get(this.tasaReaccion.size() - 1);
            else
                this.lastAdded = "";
            updateTasa();
        }
    }

    @FXML
    public void guardarCambios(ActionEvent event) {
    }

    public void descartarCambios(ActionEvent event) {
    }

    private String tasaReaccion2LaTeX(ArrayList<String> tasaReaccion){
        String result = "";
        result = stringArray2String(tasaReaccion);
        return result;
    }

    private String stringArray2String(ArrayList<String> array){
        StringBuilder result = new StringBuilder();
        for(String element : array){
            result.append(element);
        }
        return result.toString();
    }

}
