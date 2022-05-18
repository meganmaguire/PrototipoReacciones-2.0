package com.mmaguire.prototiporeacciones2.model;

public class Reactivo {

    private String nombre;
    private int cantidadInicial;

    public Reactivo() {}

    public Reactivo(String nombre, int cantidadInicial) {
        this.nombre = nombre;
        this.cantidadInicial = cantidadInicial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadInicial() {
        return cantidadInicial;
    }

    public void setCantidadInicial(int cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public Reactivo clone() {
        return new Reactivo(this.nombre, this.cantidadInicial);
    }

    @Override
    public boolean equals(Object object){
        if(object == this)
            return true;
        if(!(object instanceof Reactivo))
            return false;
        Reactivo reactivo = (Reactivo) object;
        return this.getNombre().equals(reactivo.getNombre());
    }
}
