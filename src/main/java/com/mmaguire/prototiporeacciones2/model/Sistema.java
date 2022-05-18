package com.mmaguire.prototiporeacciones2.model;

import java.util.List;

public class Sistema {
    private List<Reactivo> reactivos;
    private List<Reaccion> reacciones;
    private Experimento experimento;

    public Sistema() {
    }

    public List<Reactivo> getReactivos() {
        return reactivos;
    }

    public void setReactivos(List<Reactivo> reactivos) {
        this.reactivos = reactivos;
    }

    public List<Reaccion> getReacciones() {
        return reacciones;
    }

    public void setReacciones(List<Reaccion> reacciones) {
        this.reacciones = reacciones;
    }

    public Experimento getExperimento() {
        return experimento;
    }

    public void setExperimento(Experimento experimento) {
        this.experimento = experimento;
    }

}
