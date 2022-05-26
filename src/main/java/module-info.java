module com.example.prototiporeacciones2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires model;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.jfree.fxgraphics2d;
    requires jlatexmath;

    opens com.mmaguire.prototiporeacciones2 to javafx.fxml;
    exports com.mmaguire.prototiporeacciones2;
    opens com.mmaguire.prototiporeacciones2.model to javafx.fxml;
    exports com.mmaguire.prototiporeacciones2.model;
    exports com.mmaguire.prototiporeacciones2.manager;
    opens com.mmaguire.prototiporeacciones2.manager to javafx.fxml;
    exports com.mmaguire.prototiporeacciones2.controller;
    opens com.mmaguire.prototiporeacciones2.controller to javafx.fxml;
}
