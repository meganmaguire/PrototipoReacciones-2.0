package com.mmaguire.prototiporeacciones2.model;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

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
    private List<Reactivo> reactantes;
    private List<Reactivo> productos;
    private TipoReaccion tipo;
    private Factor alpha;
    private Factor factor;
    private String tasaReaccion;

    public Reaccion() {}


    public Reaccion(String nombreReaccion, List<Reactivo> reactantes, List<Reactivo> productos, TipoReaccion tipo, Factor alpha, Factor factor) {
        this.nombreReaccion = nombreReaccion;
        this.nroReaccion = contador;
        this.reactantes = reactantes;
        this.productos = productos;
        this.tipo = tipo;
        this.alpha = alpha;
        this.factor = factor;
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

    public List<Reactivo> getReactantes() {
        return reactantes;
    }

    public void setReactantes(List<Reactivo> reactantes) {
        this.reactantes = reactantes;
    }

    public List<Reactivo> getProductos() {
        return productos;
    }

    public void setProductos(List<Reactivo> productos) {
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

    public String getTasaReaccion(){
        return tasaReaccion;
    }

    public void setTasaReaccion(String tasaReaccion){
        this.tasaReaccion = tasaReaccion;
    }

    @JsonIgnore
    public String calculateTasaReaccion(){
        StringBuilder result = new StringBuilder("r = ");
        result.append(this.factor.getNombre()).append("*");
        result.append(this.alpha.getNombre()).append("*");
        for (Reactivo reactivo : this.reactantes){
            result.append(" [")
                    .append(reactivo.getNombre())
                    .append("]^")
                    .append(reactivo.getCantidadInicial())
                    .append(" ");
        }
        return result.toString();
    }

    @Override
    public String toString(){
        String reactivos = "";
        for (int i=0; i < this.reactantes.size(); i++){
            if(i != 0)
                reactivos += " + ";
            reactivos += this.reactantes.get(i).getCantidadInicial() + this.reactantes.get(i).getNombre();
        }
        String productos = "";
        for (int i=0; i < this.productos.size(); i++){
            if(i != 0)
                productos += " + ";
            productos += this.productos.get(i).getCantidadInicial() + this.productos.get(i).getNombre();
        }

        String tipoReaccion = "";
        if (this.tipo == TipoReaccion.reversible)
            tipoReaccion = " <--> ";
        else
            tipoReaccion = " --> ";

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
