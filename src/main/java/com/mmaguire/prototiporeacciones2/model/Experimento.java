package com.mmaguire.prototiporeacciones2.model;

import java.util.ArrayList;
import java.util.List;

public class Experimento {

    private static int contador = 1;

    private String nombre;
    private List<Paso> pasos;
    private int numero;
    private boolean activo;

    public Experimento() {
        this.nombre = "exp_" + contador;
        this.pasos = new ArrayList<>();
        this.numero = contador;
        this.activo = false;
        forwardContador();
    }

    public Experimento(String nombre, List<Paso> pasos, int numero, boolean activo) {
        this.nombre = nombre;
        this.pasos = pasos;
        this.numero = numero;
        this.activo = activo;
        forwardContador();
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public String pasosToString() {
        StringBuilder result = new StringBuilder();
        for(Paso paso : pasos) {
            result.append(paso.getTiempo().toString()).append(" || ").append(paso).append("\n");
        }
        return result.toString();
    }

    public static int getContador(){
        return contador;
    }
    public static void setContador(int cont){
        contador = cont;
    }
    public static void forwardContador() {
        contador++;
    }
}
