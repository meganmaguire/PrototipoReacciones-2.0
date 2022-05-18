package com.mmaguire.prototiporeacciones2.model;

public class Factor {

    private String nombre;
    private double valor;

    public Factor() {
        this.valor = 1;
    }

    public Factor(String nombre, double valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public Factor clone(){
        return new Factor(
                this.nombre,
                this.valor
        );
    }

    @Override
    public String toString(){
        return nombre;
    }
}
