package com.mmaguire.prototiporeacciones2.model;

public class RestriccionIntervalo extends RestriccionTiempoFijo {

    private int limiteInf;
    private String restriccionInf;

    public RestriccionIntervalo() {
    }

    public RestriccionIntervalo(int limiteSup, String restriccionSup, String reloj, int limiteInf, String restriccionInf) {
        super(limiteSup, restriccionSup, reloj);
        this.limiteInf = limiteInf;
        this.restriccionInf = restriccionInf;
    }

    public int getLimiteInf() {
        return limiteInf;
    }

    public void setLimiteInf(int limiteInf) {
        this.limiteInf = limiteInf;
    }

    public String getRestriccionInf() {
        return restriccionInf;
    }

    public void setRestriccionInf(String restriccionInf) {
        this.restriccionInf = restriccionInf;
    }
    @Override
    public String toString() {
        return this.limiteInf + " " + this.restriccionInf + " " + super.toString();
    }
}
