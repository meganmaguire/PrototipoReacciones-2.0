package com.mmaguire.prototiporeacciones2.model;

public class Reactivo {

    private String nombre;
    private int cantidadInicial;
    private boolean actualizable;
    private Factor constanteAsociada;

    public Reactivo() {}

    public Reactivo(String nombre, int cantidadInicial, boolean actualizable, Factor constanteAsociada) {
        this.nombre = nombre;
        this.cantidadInicial = cantidadInicial;
        this.actualizable = actualizable;
        this.constanteAsociada = constanteAsociada;
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

    public boolean isActualizable() {
        return actualizable;
    }

    public void setActualizable(boolean actualizable) {
        this.actualizable = actualizable;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public Reactivo clone() {
        return new Reactivo(
                this.nombre,
                this.cantidadInicial,
                this.actualizable,
                this.constanteAsociada
        );
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
