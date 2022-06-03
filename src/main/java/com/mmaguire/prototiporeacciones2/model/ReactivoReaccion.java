package com.mmaguire.prototiporeacciones2.model;

public class ReactivoReaccion {

    private Reactivo reactivoAsociado;
    private int cantidad;

    public ReactivoReaccion() {
    }

    public ReactivoReaccion(Reactivo reactivoAsociado, int cantidad) {
        this.reactivoAsociado = reactivoAsociado;
        this.cantidad = cantidad;
    }

    public Reactivo getReactivoAsociado() {
        return reactivoAsociado;
    }

    public void setReactivoAsociado(Reactivo reactivoAsociado) {
        this.reactivoAsociado = reactivoAsociado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return this.getReactivoAsociado().getNombre();
    }
}
