module com.y1zip.y1zip {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.y1zip to javafx.fxml;
    exports com.y1zip.Application;
    opens com.y1zip.Application to javafx.fxml;
}