package com.mmaguire.prototiporeacciones2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "@ttype")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Restriccion.class, name = "RestriccionTiempo"),
        @JsonSubTypes.Type(value = RestriccionIntervalo.class, name = "RestriccionIntervalo")
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Restriccion {

    private int limiteSup;
    private String restriccionSup;
    private String componente;

    public Restriccion() {
    }

    public Restriccion(int limiteSup, String restriccionSup, String componente) {
        this.limiteSup = limiteSup;
        this.restriccionSup = restriccionSup;
        this.componente = componente;
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

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    @Override
    public String toString(){
        return this.componente + " " + this.restriccionSup + " " + this.limiteSup;
    }

    @JsonProperty("@ttype")
    public String getChildType(){
        return "RestriccionTiempo";
    }
}
