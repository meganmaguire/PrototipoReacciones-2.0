package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Context {

    // Singleton instance
    private static Context instance = null;
    // Context data
    private Sistema sistemaReacciones;
    private ObservableList<Reactivo> reactivos;
    private ObservableList<TipoReaccion> tipoReacciones;
    private ObservableList<Reaccion> reacciones;
    private ObservableList<Factor> factores;
    private ObservableList<Factor> constantesReaccion;

    private ObservableList<Reactivo> reactivosPasoExperimento;
    private ObservableList<Factor> factoresPasoExperimento;
    private ObservableList<Paso> pasosExperimento;

    private Paso ultimoPaso;
    private Experimento experimento;

    private Context(){
        this.sistemaReacciones = new Sistema();
        this.experimento = new Experimento();
        this.reactivos = FXCollections.observableList(new ArrayList<>());
        this.reacciones = FXCollections.observableList(new ArrayList<>());
        this.factores = FXCollections.observableList(new ArrayList<>());
        this.constantesReaccion = FXCollections.observableList(new ArrayList<>());
        this.reactivosPasoExperimento = FXCollections.observableList(new ArrayList<>());
        this.factoresPasoExperimento = FXCollections.observableList(new ArrayList<>());
        this.pasosExperimento = FXCollections.observableList(new ArrayList<>());
        this.ultimoPaso = new Paso(null, null, 0);
    }

    public static Context getContext(){
        if(instance == null)
            instance = new Context();
        return instance;
    }

    public Sistema getSistemaReacciones() {
        return sistemaReacciones;
    }

    public ObservableList<Reactivo> getReactivos() {
        return reactivos;
    }

    public ObservableList<TipoReaccion> getTipoReacciones() {
        return tipoReacciones;
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

    public ObservableList<Reactivo> getReactivosPasoExperimento() {
        return reactivosPasoExperimento;
    }

    public ObservableList<Factor> getFactoresPasoExperimento() {
        return factoresPasoExperimento;
    }

    public ObservableList<Paso> getPasosExperimento() {
        return pasosExperimento;
    }

    public Paso getUltimoPaso() {
        return ultimoPaso;
    }

    public Experimento getExperimento() {
        return experimento;
    }


    public void setSistemaReacciones(Sistema sistemaReacciones) {
        this.sistemaReacciones = sistemaReacciones;
    }

    public void setReactivos(ObservableList<Reactivo> reactivos) {
        this.reactivos = reactivos;
    }

    public void setTipoReacciones(ObservableList<TipoReaccion> tipoReacciones) {
        this.tipoReacciones = tipoReacciones;
    }

    public void setReacciones(ObservableList<Reaccion> reacciones) {
        this.reacciones = reacciones;
    }

    public void setFactores(ObservableList<Factor> factores) {
        this.factores = factores;
    }

    public void setConstantesReaccion(ObservableList<Factor> constantesReaccion) {
        this.constantesReaccion = constantesReaccion;
    }

    public void setReactivosPasoExperimento(ObservableList<Reactivo> reactivosPasoExperimento) {
        this.reactivosPasoExperimento = reactivosPasoExperimento;
    }

    public void setFactoresPasoExperimento(ObservableList<Factor> factoresPasoExperimento) {
        this.factoresPasoExperimento = factoresPasoExperimento;
    }

    public void setPasosExperimento(ObservableList<Paso> pasosExperimento) {
        this.pasosExperimento = pasosExperimento;
    }

    public void setUltimoPaso(Paso ultimoPaso) {
        this.ultimoPaso = ultimoPaso;
    }

    public void setExperimento(Experimento experimento) {
        this.experimento = experimento;
    }
}
