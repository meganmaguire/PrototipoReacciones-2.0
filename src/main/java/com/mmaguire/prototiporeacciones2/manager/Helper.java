package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.model.EquationItem;
import com.mmaguire.prototiporeacciones2.model.Factor;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import com.mmaguire.prototiporeacciones2.model.ReactivoReaccion;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Helper {


    public static void actualizarReaccion(ObservableList<ReactivoReaccion> reactivosReaccion, ObservableList<ReactivoReaccion> productosReaccion, Label labelReactivos, Label labelProductos) {
        labelReactivos.setText(generateLabel(reactivosReaccion));
        labelProductos.setText(generateLabel(productosReaccion));
    }

    private static String generateLabel(ObservableList<ReactivoReaccion> reactivos){
        StringBuilder label = new StringBuilder();
        ReactivoReaccion reactivo;
        for (int i = 0; i < reactivos.size(); i++) {
            reactivo = reactivos.get(i);
            label
                    .append(reactivo.getCantidad() != 1 ? reactivo.getCantidad() : "")
                    .append(" ")
                    .append(reactivo.getReactivoAsociado().getNombre())
                    .append((i != reactivos.size() - 1) ? " + " : "");
        }
        return label.toString();
    }

    public static boolean existeReactivoReaccionConNombre(String nombre, ObservableList<ReactivoReaccion> reactivos) {

        for(ReactivoReaccion reactivo : reactivos) {
            if(reactivo.getReactivoAsociado().getNombre().equals(nombre))
                return true;
        }
        return false;
    }

    public static boolean existeReactivoConNombre(String nombre, ObservableList<Reactivo> reactivos) {

        for(Reactivo reactivo : reactivos) {
            if(reactivo.getNombre().equals(nombre))
                return true;
        }
        return false;
    }
    public static boolean existeFactorConNombre(String nombre, ObservableList<Factor> factores) {

        for(Factor factor : factores) {
            if(factor.getNombre().equals(nombre))
                return true;
        }
        return false;
    }

    public static void styleButton(Button cellButton) {
        Image image = new Image(MainApp.class.getResourceAsStream("icons/baseline_delete_black_24dp.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        cellButton.setGraphic(imageView);
        cellButton.getStyleClass().add("delete-button");
    }


    public static String tasaReaccion2LaTeX(ArrayList<EquationItem> tasaReaccion){
        String result = "";
//        for (EquationItem item :tasaReaccion){
//            switch (item.getType()){
//                case parentesisAbre -> result += "{" + item.getItem();
//            }
//        }
        result = itemArray2String(tasaReaccion);
        return result;
    }

    public static String itemArray2String(ArrayList<EquationItem> array){
        StringBuilder result = new StringBuilder();
        for(EquationItem element : array){
            result.append(element.getItem());
        }
        return result.toString();
    }

    public static ArrayList<ArrayList<EquationItem>> separateTasaReaccion(ArrayList<EquationItem> tasaReaccion){
        ArrayList<ArrayList<EquationItem>> result = new ArrayList<>();
        ArrayList<EquationItem> tasaIda = new ArrayList<>();
        ArrayList<EquationItem> tasaVuelta = new ArrayList<>();
        boolean mitadReaccion = false;
        for (EquationItem item : tasaReaccion) {
            if (mitadReaccion)
                tasaVuelta.add(item);
            if (!item.getItem().equals("-") && !mitadReaccion) {
                tasaIda.add(item);
            } else
                mitadReaccion = true;
        }
        result.add(tasaIda);
        result.add(tasaVuelta);
        return result;
    }

    public static ArrayList<EquationItem> cloneTasaReaccion(ArrayList<EquationItem> tasaReaccion) {
        ArrayList<EquationItem> result = new ArrayList<>();
        tasaReaccion.forEach(item -> result.add(item.clone()));
        return result;
    }
}
