package com.mmaguire.prototiporeacciones2.model;

import java.util.ArrayList;
import java.util.List;

public class Experimento {

    private String nombre;
    private List<Paso> pasos;

    public Experimento() {
        this.nombre = "exp1";
        this.pasos = new ArrayList<>();
    }

    public Experimento(String nombre, List<Paso> pasos) {
        this.nombre = nombre;
        this.pasos = pasos;
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

}
