package com.mmaguire.prototiporeacciones2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("RestriccionIntervalo")
public class RestriccionIntervalo extends Restriccion {

    private int limiteInf;
    private String restriccionInf;

    public RestriccionIntervalo() {
    }

    public RestriccionIntervalo(int limiteSup, String restriccionSup, String componente, String tipo, int limiteInf, String restriccionInf) {
        super(limiteSup, restriccionSup, componente, tipo);
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
        return this.limiteInf + " " + this.restriccionInf + " " + this.getComponente() + " && " + super.toString();
    }

    @JsonProperty("@ttype")
    @Override
    public String getChildType(){
        return "RestriccionIntervalo";
    }
}
