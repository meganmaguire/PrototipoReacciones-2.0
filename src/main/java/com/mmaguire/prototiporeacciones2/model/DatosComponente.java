package com.mmaguire.prototiporeacciones2.model;

import java.util.List;

public class DatosComponente {
    private List<Punto> datos;
    private String titulo;

    public DatosComponente() {
    }

    public DatosComponente(List<Punto> datos, String titulo) {
        this.datos = datos;
        this.titulo = titulo;
    }

    public List<Punto> getDatos() {
        return datos;
    }

    public void setDatos(List<Punto> datos) {
        this.datos = datos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
