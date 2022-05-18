package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.MainApp;
import com.mmaguire.prototiporeacciones2.model.Reactivo;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonCell extends TableCell<Reactivo, Boolean> {
    final Button cellButton = new Button();

    public ButtonCell(final TableView tableView, ObservableList values) {
        styleButton();
        cellButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                int selectedIndex = getTableRow().getIndex();
                Object toRemove = tableView.getItems().get(selectedIndex);
                values.remove(toRemove);
                tableView.refresh();
            }
        });
    }

    //Display button if the row is not empty
    @Override
    public void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty){
            setGraphic(cellButton);
        }
    }

    private void styleButton() {
        Image image = new Image(MainApp.class.getResourceAsStream("icons/baseline_delete_black_24dp.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        cellButton.setGraphic(imageView);
        cellButton.getStyleClass().add("delete-button");
    }
}
