package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.model.DatosComponente;
import com.mmaguire.prototiporeacciones2.model.Punto;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static ArrayList<String> removeHeader(ArrayList<String> out){
        ArrayList<String> result = new ArrayList<>(out
                .stream().dropWhile(s -> !s.contains("Formula is satisfied"))
                .toList());
        // Remueve línea de "Formula is satisfied"
        result.remove(0);
        return result;
    }

    public static List<DatosComponente> parse(ArrayList<String> out) {
        List<DatosComponente> result = new ArrayList<>();
        List<Punto> listPuntos;
        // Avanza de 2 en dos porque cada componente utiliza dos líneas; la primera para el nombre y
        // la segunda para el listado de puntos cartesianos
        for (int i = 0; i < out.size()-1; i+=2) {
            listPuntos = new ArrayList<>();
            String nombre = out.get(i);
            nombre = nombre.substring(0,nombre.length()-1);

            String[] puntos = out.get(i+1).split(" ");
            // Comienza desde el segundo string porque el primero posee el número de corrida (por defecto [0])
            for (int j = 1; j < puntos.length; j++) {
                String[] puntosSplit = puntos[j].substring(1,puntos[j].length()-1).split(",");
                Punto punto = new Punto(
                        Double.parseDouble(puntosSplit[0]),
                        Double.parseDouble(puntosSplit[1]));
                listPuntos.add(punto);
            }
            result.add(new DatosComponente(listPuntos, nombre));
        }
        return result;
    }
}
