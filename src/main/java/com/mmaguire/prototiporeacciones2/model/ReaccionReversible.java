package com.mmaguire.prototiporeacciones2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
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
    public ArrayList<EquationItem> calculateTasaReaccion(){
        ArrayList<EquationItem> result = new ArrayList<>();
        result.add(new EquationItem(this.getFactor().getNombre(), EquationItemType.componente));
        result.add(new EquationItem("*", EquationItemType.operador));
        result.add(new EquationItem(this.getAlpha().getNombre(), EquationItemType.componente));
        for (Reactivo reactivo : this.getReactantes()){
            result.add(new EquationItem("*", EquationItemType.operador));
            result.add(new EquationItem(reactivo.getNombre(), EquationItemType.componente));
        }
        result.add(new EquationItem("-", EquationItemType.operador));
        result.add(new EquationItem("f_" + this.getNroReaccion(), EquationItemType.componente));
        result.add(new EquationItem("*", EquationItemType.operador));

        result.add(new EquationItem(this.getBeta().getNombre(), EquationItemType.componente));

        for (Reactivo reactivo : this.getProductos()){
            result.add(new EquationItem("*", EquationItemType.operador));
            result.add(new EquationItem(reactivo.getNombre(), EquationItemType.componente));
        }
        return result;
    }

    @JsonProperty("@ttype")
    @Override
    public String getChildType(){
        return "ReaccionReversible";
    }
}
