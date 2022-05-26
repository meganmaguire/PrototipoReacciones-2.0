package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.model.EquationItem;
import com.mmaguire.prototiporeacciones2.model.Factor;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Helper {



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

}
