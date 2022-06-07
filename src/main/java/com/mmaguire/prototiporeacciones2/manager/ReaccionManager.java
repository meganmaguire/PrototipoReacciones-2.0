package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.model.*;
import com.uppaal.model.core2.Document;
import com.uppaal.model.core2.Location;
import com.uppaal.model.core2.PrototypeDocument;
import com.uppaal.model.core2.Template;

import java.util.ArrayList;
import java.util.List;

import static com.mmaguire.prototiporeacciones2.manager.Helper.itemArray2String;
import static com.mmaguire.prototiporeacciones2.manager.Helper.separateTasaReaccion;
import static com.mmaguire.prototiporeacciones2.manager.ModelManager.addEdge;
import static com.mmaguire.prototiporeacciones2.manager.ModelManager.setLabel;

public class ReaccionManager {


    /**
     * Convierte un sistema de reacciones en un NSTA de UPPAAL según el método de modelado descripto
     * en la tesis de doctorado "Modelado y Análisis Estocástico de Sistemas Biológicos" (Vilallonga, 2020).
     *
     * @param sistema sistema de reacciones modelado
     * @return documento de UPPAAL con el NSTA listo para compilar en el engine
     */
    public static Document createModel(Sistema sistema){
        Document doc = new Document(new PrototypeDocument());
        // Reactivos con valores iniciales
        setReactivos(doc, sistema.getReactivos());
        // Cantidad de bombas de sodio-potasio
        setBombas(doc, sistema.getCantidadBombas());
        // Template por cada reacción
        for (Reaccion reaccion : sistema.getReacciones()) {
            if (reaccion instanceof ReaccionReversible){
                // Separar reacción reversible en dos reacciones
                List<Reaccion> reacciones = separateReaccion((ReaccionReversible) reaccion);
                for(Reaccion reac : reacciones){
                    doc.insert(createModelReaccion(doc, reac), null);
                }
            }
            else
                doc.insert(createModelReaccion(doc, reaccion), null);
        }
        // Nombre de templates para generar en el sistema
        setSystems(doc, sistema.getReacciones(), sistema.getExperimento());
        // Genera template de experimento y clock si se especifica
        if(sistema.getExperimento().getPasos().size() > 0) {
            setClock(doc, sistema.getExperimento());
            doc.insert(createModelExperimento(doc, sistema.getExperimento()), null);
        }
        return doc;
    }

    /**
     * Añade una especificación de reloj a las declaraciones del NSTA de UPPAAL según el nombre
     * indicado en el experimento modelado pasado por parámetro.
     *
     * @param doc documento de UPPAAL al cual añadir el reloj
     * @param experimento experimento modelado
     */
    private static void setClock(Document doc, Experimento experimento) {
        String clock = "clock " + experimento.getClock() + ";\n";
        doc.setProperty("declaration", doc.getProperty("declaration").getValue().toString() + clock);

    }

    /**
     * Añade las especificaciones de los reactivos iniciales del sistema al NSTA de UPPAAL.
     *
     * @param doc documento de UPPAAL al cual añadir los reactivos iniciales
     * @param reactivos listado de reactivos con sus valores iniciales
     */
    private static void setReactivos(Document doc, List<Reactivo> reactivos) {
        StringBuilder variables = new StringBuilder();
        for (Reactivo reactivo : reactivos) {
            variables.append("int ").append(reactivo.getNombre()).append(" = ").append(reactivo.getCantidadInicial()).append(";\n");
        }
        doc.setProperty("declaration", doc.getProperty("declaration").getValue().toString() + variables);
    }

    /**
     * Añade la cantidad de bombas de Sodio-Potasio con las que va a trabajar la simulación
     * al sistema al NSTA de UPPAAL.
     *
     * @param doc documento de UPPAAL al cual añadir la constante de cantidad de bombas
     * @param cantidadBombas cantidad de bombas de Sodio-Potasio que se desea modelar en la simulación
     */
    private static void setBombas(Document doc, int cantidadBombas) {
        String variableBombas = "const int N = " + cantidadBombas + ";\n";
        doc.setProperty("declaration", doc.getProperty("declaration").getValue().toString() + variableBombas);
    }


    /**
     * Crea un Template de UPPAAL a partir de una reacción perteneciente al sistema, y lo añade
     * al documento.
     *
     * @param doc documento de UPPAAL al cual añadir el nuevo template de reacción
     * @param reaccion reacción del sistema
     * @return template generado a partir de la reacción
     */
    private static Template createModelReaccion(Document doc, Reaccion reaccion) {
        Template template = doc.createTemplate();
        template.setProperty("name", reaccion.getNombreReaccion());
        List<ReactivoReaccion> reactivos = reaccion.getReactantes();
        List<ReactivoReaccion> productos = reaccion.getProductos();

        doc.setProperty("declaration", doc.getProperty("declaration").getValue().toString()
                + "const double " + reaccion.getAlpha().getNombre() + " = " + reaccion.getAlpha().getValor() + ";\n");
        doc.setProperty("declaration", doc.getProperty("declaration").getValue().toString()
                + "double " + reaccion.getFactor().getNombre() + " = " + reaccion.getFactor().getValor() + ";\n");

        String expRate = itemArray2String(reaccion.getTasaReaccion());
        StringBuilder guard = new StringBuilder();
        StringBuilder update = new StringBuilder();
        for (int i = 0 ; i < reactivos.size() ; i++) {
            ReactivoReaccion reactivo = reactivos.get(i);
            guard.append(reactivo.getReactivoAsociado().getNombre()).append(" > 0").append(i != reactivos.size() - 1 ? " && " : "");
            if(reactivo.getReactivoAsociado().isActualizable())
                update.append(i != 0 ? ",\n" : "")
                        .append(reactivo.getReactivoAsociado().getNombre()).append(" -= ").append(reactivo.getCantidad());
        }
        for (ReactivoReaccion producto : reaccion.getProductos()) {
            if(producto.getReactivoAsociado().isActualizable())
                update.append(",\n").append(producto.getReactivoAsociado().getNombre()).append(" += ").append(producto.getCantidad());
            if(producto.getReactivoAsociado().isSubestado())
                guard.append(" && ").append(producto.getReactivoAsociado().getNombre()).append(" < N");

        }

        Location location = ModelManager.addLocation(template, reaccion.getNombreReaccion(),expRate.toString(), 0, 0);
        location.setProperty("init", true);

        addEdge(template, location, location, guard.toString(), null, update.toString());

        return template;
    }

    /**
     * Añade a las especificaciones de sistema de UPPAAL los nombres de los templates a utilizarse
     * para compilar el sistema. Se indican templates de reacciones y template de experimento
     * (si corresponde).
     *
     * @param doc documento de UPPAAL al cual añadir la información de templates
     * @param reacciones lista de reacciones
     * @param experimento experimento modelado
     */
    private static void setSystems(Document doc, List<Reaccion> reacciones, Experimento experimento) {
        StringBuilder systemProperties = new StringBuilder("system ");
        Reaccion reaccion;
        if(experimento.getPasos().size() > 0)
            systemProperties.append(experimento.getNombre()).append(", ");
        for (int i = 0; i < reacciones.size() ; i++) {
            reaccion = reacciones.get(i);
            if(reaccion instanceof ReaccionReversible)
                systemProperties.append("r_").append(reaccion.getNroReaccion()).append(", ");
            systemProperties.append(reaccion.getNombreReaccion()).append(i != reacciones.size() - 1 ? ", " : ";");
        }
        doc.setProperty("system", systemProperties.toString());
    }

    /**
     *
     * @param timeUnits unidades de tiempo que dura la simulación
     * @param sistema sistema a simular
     * @return query de simulación para enviar al engine
     */
    public static String generateSimulationQuery(int timeUnits, Sistema sistema){
        StringBuilder query = new StringBuilder("simulate [<=" + timeUnits + "] {");
        List<ReactivoReaccion> reactivos = new ArrayList<>();
        // Toma los reactivos que se usaron en reacciones para armar la query
        for(Reaccion reaccion : sistema.getReacciones()) {
            reactivos.addAll(reaccion.getReactantes().stream().filter(r -> !reactivos.contains(r)).toList());
            reactivos.addAll(reaccion.getProductos().stream().filter(r -> !reactivos.contains(r)).toList());
        }
        for(int i = 0; i<reactivos.size(); i++){
            query.append(reactivos.get(i).getReactivoAsociado().getNombre()).append(i != reactivos.size() - 1 ? ", " : "");
        }
        query.append("}");
        return query.toString();
    }


    /**
     * Crea un Template de UPPAAL a partir de un experimento perteneciente al sistema, y lo añade
     * al documento.
     * @param doc documento de UPPAAL al cual añadir el nuevo template de experimento
     * @param experimento experimento modelado
     * @return template generado a partir del experimento
     */
    // TODO revisar lógica
    private static Template createModelExperimento(Document doc, Experimento experimento) {
        Template template = doc.createTemplate();
        template.setProperty("name", experimento.getNombre());
        List<Paso> pasos = experimento.getPasos();
        Location locationActual = null;
        Location locationAnterior = null;
        StringBuilder update = new StringBuilder();
        int x = 0;
        int y = 0;
        for (int i = 0; i < pasos.size()+1; i++) {
            // Añade locación
            locationActual = ModelManager.addLocation(template, "l" + (i+1),null , x, y);
            if( i < pasos.size())
                setLabel(locationActual, LocationKind.invariant, experimento.getClock() + "<=" + pasos.get(i).getTiempo(), x-7, y+20);
            if(i == 0) locationActual.setProperty("init", true);
            // Añade transición
            else {
                List<Reactivo> reactivosActualizados = pasos.get(i-1).getReactivosActualizados();
                for (int j = 0; j < reactivosActualizados.size() ; j++) {
                    update.append(reactivosActualizados.get(j).getNombre()).append(" = ").append(reactivosActualizados.get(j).getCantidadInicial()).append(j != reactivosActualizados.size() - 1 ? ", \n" : "");
                }
                List<Factor> factoresActualizados = pasos.get(i-1).getFactoresActualizados();
                if(reactivosActualizados.size()>0 && factoresActualizados.size()>0) update.append(", \n");
                for (int j = 0; j < factoresActualizados.size() ; j++) {
                    update.append(factoresActualizados.get(j).getNombre()).append(" = ").append(factoresActualizados.get(j).getValor()).append(j != factoresActualizados.size() - 1 ? ", \n" : "\n");
                }
                addEdge(template, locationAnterior, locationActual, experimento.getClock() + "==" + pasos.get(i-1).getTiempo(), null, update.toString());
            }
            locationAnterior = locationActual;
            update = new StringBuilder();
            if (i%2 == 0)
                x += 150;
            else y += 150;
        }

        return template;
    }

    private static List<Reaccion> separateReaccion(ReaccionReversible reaccionReversible) {
        List<Reaccion> result = new ArrayList<>();
        Reaccion reaccion;
        ArrayList<ArrayList<EquationItem>> tasasReaccion = separateTasaReaccion(reaccionReversible.getTasaReaccion());
        reaccion = new Reaccion(
                "r" + reaccionReversible.getNroReaccion(),
                reaccionReversible.getReactantes(),
                reaccionReversible.getProductos(),
                TipoReaccion.irreversible,
                reaccionReversible.getAlpha(),
                reaccionReversible.getFactor(),
                tasasReaccion.get(0)
        );
        result.add(reaccion);

        reaccion = new Reaccion(
                "r_" + reaccionReversible.getNroReaccion(),
                reaccionReversible.getProductos(),
                reaccionReversible.getReactantes(),
                TipoReaccion.irreversible,
                reaccionReversible.getBeta(),
                reaccionReversible.getFactorVuelta(),
                tasasReaccion.get(1)
        );
        result.add(reaccion);

        return result;
    }
}
