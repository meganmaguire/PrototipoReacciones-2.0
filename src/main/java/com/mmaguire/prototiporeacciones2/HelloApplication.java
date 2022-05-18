package com.mmaguire.prototiporeacciones2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("principal.fxml"));
        VBox content = fxmlLoader.load();
        Scene scene = new Scene(content, 1300, 800);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


/*        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("componentes.fxml"));
        AnchorPane subsection = fxmlLoader.load();
        SplitPane splitPane = ((SplitPane) ((AnchorPane) content.getChildren().get(1)).getChildren().get(0));
        splitPane.getItems().remove(1);
        splitPane.getItems().add(subsection);
*/
    }

    public static void main(String[] args) {
        launch();
    }
}
