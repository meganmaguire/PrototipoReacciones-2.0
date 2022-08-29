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
        @JsonSubTypes.Type(value = RestriccionTiempo.class, name = "RestriccionTiempo"),
        @JsonSubTypes.Type(value = RestriccionIntervalo.class, name = "RestriccionIntervalo")
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestriccionTiempo {

    private int limiteSup;
    private String restriccionSup;
    private String reloj;

    public RestriccionTiempo() {
    }

    public RestriccionTiempo(int limiteSup, String restriccionSup, String reloj) {
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

    @JsonProperty("@ttype")
    public String getChildType(){
        return "RestriccionTiempo";
    }
}
