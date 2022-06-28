package com.mmaguire.prototiporeacciones2.model;

import java.time.LocalDateTime;
import java.util.List;

public class Simulacion {
    private List<DatosComponente> datos;
    private String titulo;
    private LocalDateTime tiempo;

    public Simulacion() {
    }

    public Simulacion(List<DatosComponente> datos, String titulo, LocalDateTime tiempo) {
        this.datos = datos;
        this.titulo = titulo;
        this.tiempo = tiempo;
    }

    public List<DatosComponente> getDatos() {
        return datos;
    }

    public void setDatos(List<DatosComponente> datos) {
        this.datos = datos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getTiempo() {
        return tiempo;
    }

    public void setTiempo(LocalDateTime tiempo) {
        this.tiempo = tiempo;
    }
}
