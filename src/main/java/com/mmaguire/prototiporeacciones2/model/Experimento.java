package com.mmaguire.prototiporeacciones2.model;

import java.util.List;

public class Experimento {

    private String nombre;
    private List<Paso> pasos;
    private String clock;

    public Experimento() {
        this.nombre = "exp1";
        this.clock = "x";
    }

    public Experimento(String nombre, List<Paso> pasos, String clock) {
        this.nombre = nombre;
        this.pasos = pasos;
        this.clock = clock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Paso> getPasos() {
        return pasos;
    }

    public void setPasos(List<Paso> pasos) {
        this.pasos = pasos;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }
}
