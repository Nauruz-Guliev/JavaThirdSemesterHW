module com.example.hwweatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;


    opens ru.kpfu.itis.gnt to javafx.fxml;
    exports ru.kpfu.itis.gnt;
    exports ru.kpfu.itis.gnt.gui;
    opens ru.kpfu.itis.gnt.gui to javafx.fxml;
}