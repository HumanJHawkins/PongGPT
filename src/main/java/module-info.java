module com.codehawkins.ponggpt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.codehawkins.ponggpt to javafx.fxml;
    exports com.codehawkins.ponggpt;
}