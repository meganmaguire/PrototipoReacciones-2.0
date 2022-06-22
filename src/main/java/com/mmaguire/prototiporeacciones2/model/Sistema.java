package com.mmaguire.prototiporeacciones2.model;

import com.uppaal.engine.QueryResult;
import com.uppaal.model.core2.DataSet2D;

import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private List<Reactivo> reactivos;
    private List<Reaccion> reacciones;
    private List<Factor> factores;
    private List<Factor> constantesReaccion;
    private Experimento experimento;
    private int cantidadBombas;
    private Simulacion simulacion;

    public Sistema() {
        this.reactivos = new ArrayList<>();
        this.reacciones = new ArrayList<>();
        this.factores = new ArrayList<>();
        this.constantesReaccion = new ArrayList<>();
        this.experimento = new Experimento();
        this.cantidadBombas = 210;
    }

    public Sistema(List<Reactivo> reactivos, List<Reaccion> reacciones, List<Factor> factores, List<Factor> constantesReaccion, Experimento experimento, int cantidadBombas) {
        this.reactivos = reactivos;
        this.reacciones = reacciones;
        this.factores = factores;
        this.constantesReaccion = constantesReaccion;
        this.experimento = experimento;
        this.cantidadBombas = cantidadBombas;
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

    public int getCantidadBombas() {
        return cantidadBombas;
    }

    public void setCantidadBombas(int cantidadBombas) {
        this.cantidadBombas = cantidadBombas;
    }

    public Simulacion getSimulacion() {
        return simulacion;
    }

    public void setSimulacion(Simulacion simulacion) {
        this.simulacion = simulacion;
    }

    @Override
    public Sistema clone(){
        return new Sistema(
            this.reactivos,
            this.reacciones,
            this.factores,
            this.constantesReaccion,
            this.experimento,
            this.cantidadBombas
        );
    }
}
