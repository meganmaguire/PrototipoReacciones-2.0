package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.controller.GraficoSimulacionController;
import com.mmaguire.prototiporeacciones2.model.*;
import com.uppaal.engine.QueryResult;
import com.uppaal.model.core2.Data2D;
import com.uppaal.model.core2.DataSet2D;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mmaguire.prototiporeacciones2.manager.Context.bundle;

public class Helper {


    public static String[] generateCommand(String currentDir, String os) throws UnsupportedOperationException {
        String command = null;
        switch (os){
            case "Windows" -> {
                return new String[]{"cmd.exe", "/C", "start", command};
            }
            case "Linux" -> {
                command = currentDir +"/uppaal_servers/linux/verifyta " + currentDir + "/untitled.xml " + currentDir + "/query.q";
                return new String[]{"bash", "-l", "-c", command};
            }
            case "Mac OS X" -> {
                command = currentDir +"/uppaal_servers/mac/verifyta " + currentDir + "/untitled.xml " + currentDir + "/query.q";
                return new String[]{"bash", "-l", "-c", command};
            }
            default -> {
                throw new UnsupportedOperationException();
            }
        }
    }

    public static void actualizarReaccion(ObservableList<ReactivoReaccion> reactivosReaccion, ObservableList<ReactivoReaccion> productosReaccion, Label labelReactivos, Label labelProductos) {
        labelReactivos.setText(generateLabel(reactivosReaccion));
        labelProductos.setText(generateLabel(productosReaccion));
    }

    public static String generateLabel(List<ReactivoReaccion> reactivos){
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

    public static void styleDeleteButton(Button cellButton) {
        Image image = new Image(MainApp.class.getResourceAsStream("icons/baseline_delete_black_24dp.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        cellButton.setGraphic(imageView);
        cellButton.getStyleClass().add("delete-button");
    }

    public static void styleGraphicButton(Button cellButton) {
        Image image = new Image(MainApp.class.getResourceAsStream("icons/baseline_query_stats_black_24dp.png"));
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

    public static Simulacion queryResult2Simulacion(QueryResult result){
        List<DatosComponente> datos = new ArrayList<>();
        List<Punto> datosComponente = new ArrayList<>();
        String titulo = result.getData().getDataTitles().stream().findFirst().get();
        DataSet2D data = result.getData().getData(titulo);
        for (Data2D componentData : data) {
            for (Point2D.Double punto : componentData) {
                datosComponente.add(new Punto(punto.x, punto.y));
            }
            datos.add(new DatosComponente(datosComponente, componentData.getTitle()));
            datosComponente = new ArrayList<>();
        }
        return new Simulacion(datos,titulo, null);
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

    public static Stage createModalWindow(Scene scene, Event event) {
        Stage stage = new Stage();
        Node node = (Node) event.getSource();
        Stage parentStage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    public static void showData(ActionEvent event, Simulacion simulacion){
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/grafico-simulacion.fxml"), bundle);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            GraficoSimulacionController controller = loader.getController();
            controller.receiveData(simulacion);
            Stage stage = createModalWindow(scene, event);
            stage.showAndWait();
        }
        catch (IOException e) {
            System.out.println("Error al cargar archivo XML");
        }
    }
}
