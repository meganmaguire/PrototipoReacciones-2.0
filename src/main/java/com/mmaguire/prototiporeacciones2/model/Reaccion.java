package com.mmaguire.prototiporeacciones2.model;

import com.fasterxml.jackson.annotation.*;
import javafx.event.Event;

import java.util.ArrayList;
import java.util.List;

import static com.mmaguire.prototiporeacciones2.manager.Helper.generateLabel;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "@ttype")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Reaccion.class, name = "Reaccion"),
        @JsonSubTypes.Type(value = ReaccionReversible.class, name = "ReaccionReversible")
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reaccion {

    private static int contador = 1;

    private String nombreReaccion;
    private int nroReaccion;
    private List<ReactivoReaccion> reactantes;
    private List<ReactivoReaccion> productos;
    private TipoReaccion tipo;
    private Factor alpha;
    private Factor factor;
    private ArrayList<EquationItem> tasaReaccion;

    public Reaccion() {}


    public Reaccion(String nombreReaccion, List<ReactivoReaccion> reactantes, List<ReactivoReaccion> productos, TipoReaccion tipo, Factor alpha, Factor factor) {
        this.nombreReaccion = nombreReaccion;
        this.nroReaccion = contador;
        this.reactantes = reactantes;
        this.productos = productos;
        this.tipo = tipo;
        this.alpha = alpha;
        this.factor = factor;
    }

    public Reaccion(String nombreReaccion, List<ReactivoReaccion> reactantes, List<ReactivoReaccion> productos, TipoReaccion tipo, Factor alpha, Factor factor, ArrayList<EquationItem> tasaReaccion) {
        this.nombreReaccion = nombreReaccion;
        this.nroReaccion = contador;
        this.reactantes = reactantes;
        this.productos = productos;
        this.tipo = tipo;
        this.alpha = alpha;
        this.factor = factor;
        this.tasaReaccion = tasaReaccion;
    }

    public String getNombreReaccion() {
        return nombreReaccion;
    }

    public void setNombreReaccion(String nombreReaccion) {
        this.nombreReaccion = nombreReaccion;
    }

    public int getNroReaccion() {
        return nroReaccion;
    }

    public void setNroReaccion(int nroReaccion) {
        this.nroReaccion = nroReaccion;
    }

    public List<ReactivoReaccion> getReactantes() {
        return reactantes;
    }

    public void setReactantes(List<ReactivoReaccion> reactantes) {
        this.reactantes = reactantes;
    }

    public List<ReactivoReaccion> getProductos() {
        return productos;
    }

    public void setProductos(List<ReactivoReaccion> productos) {
        this.productos = productos;
    }

    public TipoReaccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoReaccion tipo) {
        this.tipo = tipo;
    }

    public Factor getAlpha() {
        return alpha;
    }

    public void setAlpha(Factor alpha) {
        this.alpha = alpha;
    }

    public Factor getFactor() {
        return factor;
    }

    public void setFactor(Factor factor) {
        this.factor = factor;
    }

    public ArrayList<EquationItem> getTasaReaccion(){
        return tasaReaccion;
    }

    public void setTasaReaccion(ArrayList<EquationItem> tasaReaccion){
        this.tasaReaccion = tasaReaccion;
    }

    @JsonIgnore
    public ArrayList<EquationItem> calculateTasaReaccion(){
        ArrayList<EquationItem> result = new ArrayList<>();
        result.add(new EquationItem(this.factor.getNombre(), EquationItemType.componente));
        result.add(new EquationItem("*", EquationItemType.operador));
        result.add(new EquationItem(this.alpha.getNombre(), EquationItemType.componente));
        for (ReactivoReaccion reactivo : this.reactantes){
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

    public void addComponenteConConstante(ArrayList<EquationItem> result, ReactivoReaccion reactivo) {
        result.add(new EquationItem("(", EquationItemType.parentesisAbre));
        // Añade el componente multiplicado n veces según la cantidad en la reacción
        for (int i = 0; i < reactivo.getCantidad(); i++){
            result.add(new EquationItem(reactivo.getReactivoAsociado().getNombre(), EquationItemType.componente));
            // Producto salvo en el último componente
            if( i != reactivo.getCantidad()-1)
                result.add(new EquationItem("*", EquationItemType.operador));
        }
        result.add(new EquationItem("/", EquationItemType.operador));
        result.add(new EquationItem("(", EquationItemType.parentesisAbre));
        añadirDenominadorConstantes(result, reactivo);
        result.add(new EquationItem("*", EquationItemType.operador));
        añadirDenominadorConstantes(result, reactivo);
        result.add(new EquationItem(")", EquationItemType.parentesisCierra));
        result.add(new EquationItem(")", EquationItemType.parentesisCierra));
    }

    public void añadirDenominadorConstantes(ArrayList<EquationItem> result, ReactivoReaccion reactivo) {
        result.add(new EquationItem("(", EquationItemType.parentesisAbre));
        result.add(new EquationItem(reactivo.getReactivoAsociado().getNombre(), EquationItemType.componente));
        result.add(new EquationItem("+", EquationItemType.operador));
        result.add(new EquationItem(reactivo.getReactivoAsociado().getConstanteAsociada().getNombre(), EquationItemType.componente));
        result.add(new EquationItem(")", EquationItemType.parentesisCierra));
    }

    @Override
    public String toString(){
        String reactivos = generateLabel(getReactantes());
        String productos = generateLabel(getProductos());
        String tipoReaccion = "";
        if (this.tipo == TipoReaccion.reversible)
            tipoReaccion = " ⇌ ";
        else
            tipoReaccion = " → ";

        return reactivos + tipoReaccion + productos;
    }

    @JsonIgnore
    public boolean isReversible(){
        return this.tipo == TipoReaccion.reversible;
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

    @JsonProperty("@ttype")
    public String getChildType(){
        return "Reaccion";
    }
}
