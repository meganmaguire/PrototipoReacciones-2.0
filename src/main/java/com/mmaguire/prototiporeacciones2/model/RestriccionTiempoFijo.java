package com.mmaguire.prototiporeacciones2.model;

public class RestriccionTiempoFijo {

    private int limiteSup;
    private String restriccionSup;
    private String reloj;

    public RestriccionTiempoFijo() {
    }

    public RestriccionTiempoFijo(int limiteSup, String restriccionSup, String reloj) {
        this.limiteSup = limiteSup;
        this.restriccionSup = restriccionSup;
        this.reloj = reloj;
    }

    public int getLimiteSup() {
        return limiteSup;
    }

    public void setLimiteSup(int limiteSup) {
        this.limiteSup = limiteSup;
    }

    public String getRestriccionSup() {
        return restriccionSup;
    }

    public void setRestriccionSup(String restriccionSup) {
        this.restriccionSup = restriccionSup;
    }

    public String getReloj() {
        return reloj;
    }

    public void setReloj(String reloj) {
        this.reloj = reloj;
    }

    @Override
    public String toString(){
        return this.reloj + " " + this.restriccionSup + " " + this.limiteSup;
    }
}
