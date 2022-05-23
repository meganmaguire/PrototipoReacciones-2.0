package com.mmaguire.prototiporeacciones2.model;

import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private List<Reactivo> reactivos;
    private List<Reaccion> reacciones;
    private List<Factor> factores;
    private List<Factor> constantesReaccion;
    private Experimento experimento;

    public Sistema() {
        this.reactivos = new ArrayList<>();
        this.reacciones = new ArrayList<>();
        this.factores = new ArrayList<>();
        this.constantesReaccion = new ArrayList<>();
        this.experimento = new Experimento();
    }

    public List<Reactivo> getReactivos() {
        return reactivos;
    }

    public void setReactivos(List<Reactivo> reactivos) {
        this.reactivos = reactivos;
    }

    public List<Reaccion> getReacciones() {
        return reacciones;
    }

    public void setReacciones(List<Reaccion> reacciones) {
        this.reacciones = reacciones;
    }

    public List<Factor> getFactores() {
        return factores;
    }

    public void setFactores(List<Factor> factores) {
        this.factores = factores;
    }

    public List<Factor> getConstantesReaccion() {
        return constantesReaccion;
    }

    public void setConstantesReaccion(List<Factor> constantesReaccion) {
        this.constantesReaccion = constantesReaccion;
    }

    public Experimento getExperimento() {
        return experimento;
    }

    public void setExperimento(Experimento experimento) {
        this.experimento = experimento;
    }

}
