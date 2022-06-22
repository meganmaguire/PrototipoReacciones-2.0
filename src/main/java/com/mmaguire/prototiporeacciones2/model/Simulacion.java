package com.mmaguire.prototiporeacciones2.model;

import com.uppaal.engine.QueryResult;
import com.uppaal.model.core2.DataSet2D;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Simulacion {
    private QueryResult datos;
    private LocalDateTime tiempo;

    public Simulacion() {
    }

    public Simulacion(QueryResult datos, LocalDateTime tiempo) {
        this.datos = datos;
        this.tiempo = tiempo;
    }

    public QueryResult getDatos() {
        return datos;
    }

    public void setDatos(QueryResult datos) {
        this.datos = datos;
    }

    public LocalDateTime getTiempo() {
        return tiempo;
    }

    public void setTiempo(LocalDateTime tiempo) {
        this.tiempo = tiempo;
    }
}
