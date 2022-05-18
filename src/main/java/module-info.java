module com.example.prototiporeacciones2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires model;
    requires com.fasterxml.jackson.databind;

    opens com.mmaguire.prototiporeacciones2 to javafx.fxml;
    exports com.mmaguire.prototiporeacciones2;
}
