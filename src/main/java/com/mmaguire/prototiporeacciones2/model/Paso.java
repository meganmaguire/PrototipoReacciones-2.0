package com.mmaguire.prototiporeacciones2.model;

import java.util.List;

public class Paso {

    private List<Reactivo> reactivosActualizados;
    private List<Factor> factoresActualizados;
    private int tiempo;

    public Paso() {
    }

    public Paso(List<Reactivo> reactivosActualizados, List<Factor> factoresActualizados, int tiempo) {
        this.reactivosActualizados = reactivosActualizados;
        this.factoresActualizados = factoresActualizados;
        this.tiempo = tiempo;
    }

    public List<Reactivo> getReactivosActualizados() {
        return reactivosActualizados;
    }

    public void setReactivosActualizados(List<Reactivo> reactivosActualizados) {
        this.reactivosActualizados = reactivosActualizados;
    }

    public List<Factor> getFactoresActualizados() {
        return factoresActualizados;
    }

    public void setFactoresActualizados(List<Factor> factoresActualizados) {
        this.factoresActualizados = factoresActualizados;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.reactivosActualizados.size(); i++){
            result.append(this.reactivosActualizados.get(i).getNombre())
                    .append(" = ")
                    .append(this.reactivosActualizados.get(i).getCantidadInicial())
                    .append(i != this.reactivosActualizados.size() - 1 ? ", " : "");
        }
        if(reactivosActualizados.size() > 0 && factoresActualizados.size() > 0) result.append(", ");
        for (int i = 0; i < this.factoresActualizados.size(); i++){
            result.append(this.factoresActualizados.get(i).getNombre())
                    .append(" = ")
                    .append(this.factoresActualizados.get(i).getValor())
                    .append(i != this.factoresActualizados.size() - 1 ? ", " : "");;
        }
        return result.toString();
    }

    @Override
    public Paso clone() {
        return new Paso(
                this.reactivosActualizados,
                this.factoresActualizados,
                this.tiempo
        );
    }
}
