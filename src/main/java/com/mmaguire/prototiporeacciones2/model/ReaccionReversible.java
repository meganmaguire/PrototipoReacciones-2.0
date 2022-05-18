package com.mmaguire.prototiporeacciones2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("ReaccionReversible")
public class ReaccionReversible extends Reaccion{

    private Factor beta;

    public ReaccionReversible() {
    }

    public ReaccionReversible(String nroReaccion, List<Reactivo> reactantes, List<Reactivo> productos, TipoReaccion tipo, Factor alpha, Factor factor, Factor beta) {
        super(nroReaccion, reactantes, productos, tipo, alpha, factor);
        this.beta = beta;
    }

    public Factor getBeta() {
        return beta;
    }

    public void setBeta(Factor beta) {
        this.beta = beta;
    }

    @JsonIgnore
    @Override
    public String calculateTasaReaccion(){
        StringBuilder result = new StringBuilder("r = ");
        result.append(this.getFactor().getNombre()).append("*");
        result.append(this.getAlpha().getNombre()).append("*");
        for (Reactivo reactivo : this.getReactantes()){
            result.append(" [")
                    .append(reactivo.getNombre())
                    .append("]^")
                    .append(reactivo.getCantidadInicial())
                    .append(" ");
        }
        result.append(" - ");
        result.append("f_").append(this.getNroReaccion()).append("*");
        result.append(this.getBeta().getNombre()).append("*");
        for (Reactivo reactivo : this.getProductos()){
            result.append(" [")
                    .append(reactivo.getNombre())
                    .append("]^")
                    .append(reactivo.getCantidadInicial())
                    .append(" ");
        }
        return result.toString();
    }

    @JsonProperty("@ttype")
    @Override
    public String getChildType(){
        return "ReaccionReversible";
    }
}
