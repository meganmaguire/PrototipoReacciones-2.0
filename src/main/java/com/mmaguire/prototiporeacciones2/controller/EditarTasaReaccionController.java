package com.mmaguire.prototiporeacciones2.controller;

import com.mmaguire.prototiporeacciones2.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import static com.mmaguire.prototiporeacciones2.manager.Helper.itemArray2String;
import static com.mmaguire.prototiporeacciones2.manager.Helper.tasaReaccion2LaTeX;

public class EditarTasaReaccionController {


    @FXML
    private ComboBox<Reactivo> comboBoxComponentes;
    @FXML
    private ComboBox<Factor> comboBoxConstantes;
    @FXML
    private TextField textFieldTasaReaccion;
    @FXML
    private Canvas canvasLatex;
    private FXGraphics2D g2;
    private TeXIcon icon;

    private Reaccion reaccion;
    private ArrayList<EquationItem> tasaReaccion;
    private String tasaReaccionLatex;
    private EquationItemType lastAdded;
    private ObservableSet<Reactivo> componentes;
    private ObservableList<Factor> constantes;
    private Stack<String> parentesisCheck;

    @FXML
    public void initialize(){
        this.tasaReaccion = new ArrayList<>();
        this.lastAdded = EquationItemType.empty;
        this.tasaReaccionLatex = "";
        this.componentes = FXCollections.observableSet(new HashSet<>());
        this.constantes = FXCollections.observableList(new ArrayList<>());
        this.parentesisCheck = new Stack<>();
        this.textFieldTasaReaccion.setEditable(false);
        //this.textFieldTasaReaccion.setOnKeyPressed(keyEvent -> keyPressed(keyEvent.getCode(), keyEvent));
        this.g2 = new FXGraphics2D(canvasLatex.getGraphicsContext2D());
        renderFormula(tasaReaccionLatex);
    }

    @FXML
    public void recieveData(Reaccion reaccion) {
        this.reaccion = reaccion;
        renderFormula(tasaReaccionLatex);
        this.componentes.addAll(this.reaccion.getReactantes());
        this.componentes.addAll(this.reaccion.getProductos());
        // Añadir constantes a la lista
        this.reaccion.getReactantes().forEach(reactivo -> {
            if(reactivo.getConstanteAsociada() != null)
                this.constantes.add(reactivo.getConstanteAsociada());
        });
        this.reaccion.getProductos().forEach(reactivo -> {
            if(reactivo.getConstanteAsociada() != null)
                this.constantes.add(reactivo.getConstanteAsociada());
        });this.constantes.add(this.reaccion.getAlpha());
        if (reaccion instanceof ReaccionReversible)
            this.constantes.add(((ReaccionReversible) this.reaccion).getBeta());
        this.comboBoxComponentes.setItems(FXCollections.observableList(this.componentes.stream().toList()));
        this.comboBoxConstantes.setItems(this.constantes);
        this.tasaReaccion = this.reaccion.getTasaReaccion();
        updateTasa();
    }

//    public void keyPressed(KeyCode keyCode, Event event) {
//        if(keyCode.isDigitKey()) {
//            añadirDigito(keyCode.toString());
//        }
//        switch (keyCode){
//            case ADD -> añadirOperador("+");
//            case MINUS -> añadirOperador("-");
//            case MULTIPLY -> añadirOperador("*");
//            case DIVIDE -> añadirOperador("/");
//            case CIRCUMFLEX -> añadirOperador("^");
//            case LEFT_PARENTHESIS -> añadirParentesisAbre();
//            case RIGHT_PARENTHESIS -> añadirParentesisCierra();
//            default -> event.consume();
//        }
//
//    }

    @FXML
    public void añadirMas() {
        añadirOperador("+");
    }
    @FXML
    public void añadirMenos() {
        añadirOperador("-");
    }
    @FXML
    public void añadirPor() {
        añadirOperador("*");
    }
    @FXML
    public void añadirDivision() {
        añadirOperador("/");
    }
    @FXML
    public void añadirParentesisAbre() {
        this.parentesisCheck.push("parentesis" + this.parentesisCheck.size()+1);
        this.tasaReaccion.add(new EquationItem("(", EquationItemType.parentesisAbre));
        updateTasa();
        this.lastAdded = EquationItemType.parentesisAbre;
    }
    @FXML
    public void añadirParentesisCierra() {
        if(!this.parentesisCheck.isEmpty()) {
            this.parentesisCheck.pop();
            this.tasaReaccion.add(new EquationItem(")", EquationItemType.parentesisCierra));
            updateTasa();
            this.lastAdded = EquationItemType.parentesisCierra;
        }
    }
    @FXML
    public void añadirPotencia() {
        añadirOperador("^");
    }
    @FXML
    public void añadirComponente() {
        if (this.comboBoxComponentes.getValue() != null) {
            if (lastAdded != EquationItemType.componente && lastAdded != EquationItemType.parentesisCierra && lastAdded != EquationItemType.digito) {
                this.tasaReaccion.add(new EquationItem(
                        this.comboBoxComponentes.getValue().getNombre(),
                        EquationItemType.componente)
                );
                updateTasa();
                this.lastAdded = EquationItemType.componente;
            }
        }
    }

    @FXML
    public void añadirConstante(){
        if (this.comboBoxConstantes.getValue() != null) {
            if (lastAdded != EquationItemType.componente &&
                    lastAdded != EquationItemType.parentesisCierra &&
                    lastAdded != EquationItemType.digito) {
                this.tasaReaccion.add(new EquationItem(
                        this.comboBoxConstantes.getValue().getNombre(),
                        EquationItemType.componente)
                );
                updateTasa();
                this.lastAdded = EquationItemType.componente;
            }
        }
    }

    @FXML
    public void añadirUno(){
        añadirDigito("1");
    }
    @FXML
    public void añadirDos(){
        añadirDigito("2");
    }
    @FXML
    public void añadirTres(){
        añadirDigito("3");
    }
    @FXML
    public void añadirCuatro(){
        añadirDigito("4");
    }
    @FXML
    public void añadirCinco(){
        añadirDigito("5");
    }
    @FXML
    public void añadirSeis(){
        añadirDigito("6");
    }
    @FXML
    public void añadirSiete(){
        añadirDigito("7");
    }
    @FXML
    public void añadirOcho(){
        añadirDigito("8");
    }
    @FXML
    public void añadirNueve(){
        añadirDigito("9");
    }
    @FXML
    public void añadirCero(){
        añadirDigito("0");
    }

    private void añadirDigito(String digito){
        if (lastAdded != EquationItemType.componente && lastAdded != EquationItemType.parentesisCierra) {
            this.tasaReaccion.add(new EquationItem(digito, EquationItemType.digito));
            updateTasa();
        }
        this.lastAdded = EquationItemType.digito;
    }
    private void añadirOperador(String operador){
        if (lastAdded == EquationItemType.componente || lastAdded == EquationItemType.parentesisCierra || lastAdded == EquationItemType.digito) {
            this.tasaReaccion.add(new EquationItem(operador, EquationItemType.operador));
            updateTasa();
        }
        this.lastAdded = EquationItemType.operador;
    }
    @FXML
    public void borrar() {
        if (!this.tasaReaccion.isEmpty()) {
            switch (lastAdded) {
                case parentesisAbre -> this.parentesisCheck.pop();
                case parentesisCierra -> this.parentesisCheck.push("parentesis" + this.parentesisCheck.size() + 1);
            }
            this.tasaReaccion.remove(this.tasaReaccion.size() - 1);
            if (!this.tasaReaccion.isEmpty())
                this.lastAdded = this.tasaReaccion.get(this.tasaReaccion.size() - 1).getType();
            else
                this.lastAdded = EquationItemType.empty;
            updateTasa();
        }
    }

    @FXML
    public void guardarCambios(ActionEvent event) {
    }
    @FXML
    public void descartarCambios(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
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
        this.textFieldTasaReaccion.setText(itemArray2String(this.tasaReaccion));
        this.tasaReaccionLatex = tasaReaccion2LaTeX(this.tasaReaccion);
        renderFormula(tasaReaccionLatex);
    }
}
