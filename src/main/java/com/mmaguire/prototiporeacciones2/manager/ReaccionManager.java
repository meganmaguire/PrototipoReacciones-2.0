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
        // Cantidad de bombas de sodio-potasio
        setBombas(doc, sistema.getCantidadBombas());
        // Reactivos con valores iniciales
        setReactivos(doc, sistema.getReactivos());
        // Constantes asociadas a reactivos
        setConstantes(doc, sistema.getConstantesReaccion());
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
            setClocks(doc, sistema.getRelojes());
            doc.insert(createModelExperimento(doc, sistema.getExperimento()), null);
        }
        return doc;
    }

    /**
     * Añade una especificación de reloj a las declaraciones del NSTA de UPPAAL según el nombre
     * indicado en el experimento modelado pasado por parámetro.
     *
     * @param doc documento de UPPAAL al cual añadir el reloj
     * @param clocks listado de relojes declarados para el modelo
     */
    private static void setClocks(Document doc, List<String> clocks) {
        StringBuilder append = new StringBuilder();
        for(String clock : clocks){
            append.append("clock ").append(clock).append(";\n");
        }
        append.append("\n");
        doc.setProperty("declaration", doc.getProperty("declaration").getValue().toString() + append);

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
        doc.setProperty("declaration", doc.getProperty("declaration").getValue().toString() + variables.append("\n"));
    }

    /**
     * Añade las constantes de reactivos para tasas de reacción del sistema al NSTA de UPPAAL.
     *
     * @param doc documento de UPPAAL al cual añadir los reactivos iniciales
     * @param constantes listado de constantes asociadas a componentes del sistema para tasas de reacción
     */
    private static void setConstantes(Document doc, List<Factor> constantes) {
        StringBuilder variables = new StringBuilder();
        for (Factor constante : constantes) {
            variables.append("double ").append(constante.getNombre()).append(" = ").append(constante.getValor()).append(";\n");
        }
        doc.setProperty("declaration", doc.getProperty("declaration").getValue().toString() + variables.append("\n"));
    }

    /**
     * Añade la cantidad de bombas de Sodio-Potasio con las que va a trabajar la simulación
     * al sistema al NSTA de UPPAAL.
     *
     * @param doc documento de UPPAAL al cual añadir la constante de cantidad de bombas
     * @param cantidadBombas cantidad de bombas de Sodio-Potasio que se desea modelar en la simulación
     */
    private static void setBombas(Document doc, int cantidadBombas) {
        String variableBombas = "const int N = " + cantidadBombas + ";\n\n";
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
        boolean primerElemento = true;
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
            if(reactivo.getReactivoAsociado().isActualizable()) {
                update.append(!primerElemento ? ",\n" : "")
                        .append(reactivo.getReactivoAsociado().getNombre());
                if (reactivo.getCantidad()>1)
                    update.append(" -= ").append(reactivo.getCantidad());
                else
                    update.append("-- ");
                primerElemento = false;
            }
        }
        for (ReactivoReaccion producto : reaccion.getProductos()) {
            if(producto.getReactivoAsociado().isActualizable()) {
                update.append(",\n").append(producto.getReactivoAsociado().getNombre());
                if (producto.getCantidad() > 1)
                    update.append(" += ").append(producto.getCantidad());
                else
                    update.append("++ ");
            }
            if(producto.getReactivoAsociado().isSubestado())
                guard.append(" && ").append(producto.getReactivoAsociado().getNombre()).append(" < N");

        }

        Location location = ModelManager.addLocation(template, reaccion.getNombreReaccion(),expRate.toString(), 0, 0);
        location.setProperty("init", true);
        setLabel(location, LocationKind.comments, reaccion.toString(), -100, -125 );
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
        StringBuilder systemProperties = new StringBuilder("system \n");
        Reaccion reaccion;
        if(experimento.getPasos().size() > 0)
            systemProperties.append(experimento.getNombre()).append(", ");
        for (int i = 0; i < reacciones.size() ; i++) {
            reaccion = reacciones.get(i);
            if(reaccion instanceof ReaccionReversible)
                systemProperties.append("r_").append(reaccion.getNroReaccion()).append(", ");
            systemProperties.append(reaccion.getNombreReaccion()).append(i != reacciones.size() - 1 ? ", \n" : ";");
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
        List<Reactivo> reactivos = sistema.getReactivos();
        for(int i = 0; i<reactivos.size(); i++){
            query.append(reactivos.get(i).getNombre()).append(i != reactivos.size() - 1 ? ", " : "");
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
            if( i < pasos.size()) {
                Paso paso = pasos.get(i);
                String restriccion;
                if(paso.getTiempo().getRestriccionSup().equals("==")){
                    restriccion = "<=";
                } else
                    restriccion = paso.getTiempo().getRestriccionSup();
                setLabel(locationActual, LocationKind.invariant,
                        paso.getTiempo().getComponente() + " " + restriccion + " " + paso.getTiempo().getLimiteSup(),
                        x - 7, y + 20);
            }
            if(i == 0) locationActual.setProperty("init", true);
            // Añade transición
            else {
                Paso pasoAnterior = pasos.get(i-1);
                List<ReactivoReaccion> reactivosActualizados = pasoAnterior.getReactivosActualizados();
                for (int j = 0; j < reactivosActualizados.size() ; j++) {
                    update.append(reactivosActualizados.get(j).getReactivoAsociado().getNombre()).append(" = ").append(reactivosActualizados.get(j).getCantidad()).append(j != reactivosActualizados.size() - 1 ? ", \n" : "");
                }
                List<Factor> factoresActualizados = pasoAnterior.getFactoresActualizados();
                if(reactivosActualizados.size()>0 && factoresActualizados.size()>0) update.append(", \n");
                for (int j = 0; j < factoresActualizados.size() ; j++) {
                    update.append(factoresActualizados.get(j).getNombre()).append(" = ").append(factoresActualizados.get(j).getValor()).append(j != factoresActualizados.size() - 1 ? ", \n" : "\n");
                }

                addEdge(template, locationAnterior, locationActual, pasoAnterior.getTiempo().toString(), null, update.toString());
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
