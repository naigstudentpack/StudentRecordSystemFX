module com.example.studentrecordsystemfx {
    // Required JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Open packages for JavaFX reflection
    opens com.example.studentrecordsystemfx.controller to javafx.fxml;
    opens com.example.studentrecordsystemfx.main to javafx.fxml;

    // Export main package
    exports com.example.studentrecordsystemfx.main;
}