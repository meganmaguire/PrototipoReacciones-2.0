package com.mmaguire.prototiporeacciones2;

import com.mmaguire.prototiporeacciones2.controller.EditarReaccionController;
import com.mmaguire.prototiporeacciones2.controller.PrincipalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("views/principal.fxml"));
        VBox content = fxmlLoader.load();
        Scene scene = new Scene(content, 1300, 800);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        PrincipalController controller = fxmlLoader.getController();

        SplitPane splitPane = ((SplitPane) ((AnchorPane) content.getChildren().get(1)).getChildren().get(0));
        splitPane.getItems().remove(1);
        splitPane.getItems().add(controller.getComponentesPane());
        controller.getMenuComponentes().getStyleClass().add("menu-label-selected");
    }

    public static void main(String[] args) {
        launch();
    }
}
