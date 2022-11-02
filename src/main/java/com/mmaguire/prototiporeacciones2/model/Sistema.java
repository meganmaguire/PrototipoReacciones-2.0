package com.mmaguire.prototiporeacciones2.model;

import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private List<Reactivo> reactivos;
    private List<Reaccion> reacciones;
    private List<Factor> factores;
    private List<Factor> constantesReaccion;
    private List<Experimento> experimentos;
    private int cantidadBombas;
    private List<String> relojes;
    private Simulacion simulacion;

    public Sistema() {
        this.reactivos = new ArrayList<>();
        this.reacciones = new ArrayList<>();
        this.factores = new ArrayList<>();
        this.constantesReaccion = new ArrayList<>();
        this.experimentos = new ArrayList<>();
        this.cantidadBombas = 210;
        this.relojes = new ArrayList<>();
        this.relojes.add("x");
    }

    public Sistema(List<Reactivo> reactivos, List<Reaccion> reacciones, List<Factor> factores, List<Factor> constantesReaccion, List<Experimento> experimentos, int cantidadBombas, List<String> relojes) {
        this.reactivos = reactivos;
        this.reacciones = reacciones;
        this.factores = factores;
        this.constantesReaccion = constantesReaccion;
        this.experimentos = experimentos;
        this.cantidadBombas = cantidadBombas;
        this.relojes = relojes;
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

    public List<Experimento> getExperimentos() {
        return experimentos;
    }

    public void setExperimentos(List<Experimento> experimentos) {
        this.experimentos = experimentos;
    }

    public int getCantidadBombas() {
        return cantidadBombas;
    }

    public void setCantidadBombas(int cantidadBombas) {
        this.cantidadBombas = cantidadBombas;
    }

    public List<String> getRelojes() {
        return relojes;
    }

    public void setRelojes(List<String> relojes) {
        this.relojes = relojes;
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
            this.experimentos,
            this.cantidadBombas,
            this.relojes
        );
    }
}
