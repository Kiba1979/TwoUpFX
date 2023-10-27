module com.kiba.twoupfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.kiba.twoupfx to javafx.fxml;
    exports com.kiba.twoupfx;
}