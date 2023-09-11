module org.y1zh3e7.nettalking {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.y1zh3e7.y1zip to javafx.fxml;
    exports org.y1zh3e7.y1zip.Application;
    opens org.y1zh3e7.y1zip.Application to javafx.fxml;
}