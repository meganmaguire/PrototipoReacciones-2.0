package com.mmaguire.prototiporeacciones2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("ReaccionReversible")
public class ReaccionReversible extends Reaccion{

    private Factor beta;
    private Factor factorVuelta;

    public ReaccionReversible() {
    }

    public ReaccionReversible(String nroReaccion, List<ReactivoReaccion> reactantes, List<ReactivoReaccion> productos, TipoReaccion tipo, Factor alpha, Factor factor, Factor beta) {
        super(nroReaccion, reactantes, productos, tipo, alpha, factor);
        this.beta = beta;
    }

    public ReaccionReversible(String nombreReaccion, List<ReactivoReaccion> reactantes, List<ReactivoReaccion> productos, TipoReaccion tipo, Factor alpha, Factor factor, Factor beta, Factor factorVuelta) {
        super(nombreReaccion, reactantes, productos, tipo, alpha, factor);
        this.beta = beta;
        this.factorVuelta = factorVuelta;
    }

    public Factor getBeta() {
        return beta;
    }

    public void setBeta(Factor beta) {
        this.beta = beta;
    }

    public Factor getFactorVuelta() {
        return factorVuelta;
    }

    public void setFactorVuelta(Factor factorVuelta) {
        this.factorVuelta = factorVuelta;
    }

    @JsonIgnore
    @Override
    public ArrayList<EquationItem> calculateTasaReaccion(){
        ArrayList<EquationItem> result = super.calculateTasaReaccion();

        result.add(new EquationItem("-", EquationItemType.operador));
        result.add(new EquationItem(this.getFactorVuelta().getNombre(), EquationItemType.componente));
        result.add(new EquationItem("*", EquationItemType.operador));

        result.add(new EquationItem(this.getBeta().getNombre(), EquationItemType.componente));

        for (ReactivoReaccion reactivo : this.getProductos()){
            result.add(new EquationItem("*", EquationItemType.operador));
            // Posee un componente tipo Ión con constante asociada para tasa de reacción
            if (reactivo.getReactivoAsociado().getConstanteAsociada() != null) {
                addComponenteConConstante(result, reactivo);
            }
            else {
                result.add(new EquationItem(reactivo.getReactivoAsociado().getNombre(), EquationItemType.componente));
            }
        }
        return result;
    }

    @JsonProperty("@ttype")
    @Override
    public String getChildType(){
        return "ReaccionReversible";
    }
}
