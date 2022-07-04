package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Context {

    // Singleton instance
    private static Context instance = null;
    // Context data
    private Sistema sistemaReacciones;
    private Experimento experimento;
    private ObservableList<Paso> pasosExperimento;
    private ObservableList<Reactivo> reactivos;
    private ObservableList<Reaccion> reacciones;
    private ObservableList<Factor> factores;
    private ObservableList<Factor> constantesReaccion;
    private ObservableList<Sistema> historial;

    public static Locale locale = new Locale("en");
    public static ResourceBundle bundle = ResourceBundle.getBundle("com/mmaguire/prototiporeacciones2/i18n/texts", locale);

    private Context(){
        this.sistemaReacciones = new Sistema();
        this.experimento = this.sistemaReacciones.getExperimento();
        this.pasosExperimento = FXCollections.observableList(this.experimento.getPasos());
        this.reactivos = FXCollections.observableList(this.sistemaReacciones.getReactivos());
        this.reacciones = FXCollections.observableList(this.sistemaReacciones.getReacciones());
        this.factores = FXCollections.observableList(this.sistemaReacciones.getFactores());
        this.constantesReaccion = FXCollections.observableList(this.sistemaReacciones.getConstantesReaccion());
        this.historial = FXCollections.observableList(new ArrayList<>());
    }

    public static Context getContext(){
        if(instance == null)
            instance = new Context();
        return instance;
    }

    public static void reset(){
        instance.resetInstance();
        instance.setContadorReacciones();
    }

    private void resetInstance(){
        this.pasosExperimento.clear();
        this.reactivos.clear();
        this.reacciones.clear();
        this.factores.clear();
        this.constantesReaccion.clear();
        this.historial.clear();
    }

    public Sistema getSistemaReacciones() {
        return sistemaReacciones;
    }

    public Experimento getExperimento() {
        return experimento;
    }

    public ObservableList<Paso> getPasosExperimento() {
        return pasosExperimento;
    }
    public ObservableList<Reactivo> getReactivos() {
        return reactivos;
    }

    public ObservableList<Reaccion> getReacciones() {
        return reacciones;
    }

    public ObservableList<Factor> getFactores() {
        return factores;
    }

    public ObservableList<Factor> getConstantesReaccion() {
        return constantesReaccion;
    }

    public void setSistemaReacciones(Sistema sistemaReacciones) {
        this.sistemaReacciones = sistemaReacciones;
    }

    public void setExperimento(Experimento experimento) {
        this.experimento = experimento;
    }

    public void setPasosExperimento(ObservableList<Paso> pasosExperimento) {
        this.pasosExperimento.setAll(pasosExperimento);
        this.sistemaReacciones.getExperimento().setPasos(this.pasosExperimento);
    }

    public void setReactivos(ObservableList<Reactivo> reactivos) {
        this.reactivos.setAll(reactivos);
        this.sistemaReacciones.setReactivos(this.reactivos);
    }

    public void setReacciones(ObservableList<Reaccion> reacciones) {
        this.reacciones.setAll(reacciones);
        this.sistemaReacciones.setReacciones(this.reacciones);
    }

    public void setFactores(ObservableList<Factor> factores) {
        this.factores.setAll(factores);
        this.sistemaReacciones.setFactores(this.factores);
    }

    public void setConstantesReaccion(ObservableList<Factor> constantesReaccion) {
        this.constantesReaccion.setAll(constantesReaccion);
        this.sistemaReacciones.setConstantesReaccion(this.constantesReaccion);
    }

    public ObservableList<Sistema> getHistorial() {
        return historial;
    }

    public void setHistorial(ObservableList<Sistema> historial) {
        this.historial = historial;
    }

    public void setContadorReacciones(){
        int lastReaccion = 0;
        for (Reaccion reaccion : reacciones) {
            if (reaccion.getNroReaccion() > 0)
                lastReaccion = reaccion.getNroReaccion();
        }
        Reaccion.setContador(lastReaccion+1);
    }
}
