module com.gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gui to javafx.fxml;
    opens com.gui.core to javafx.fxml;
    opens com.gui.view to javafx.fxml;
    exports com.gui;
    exports com.gui.core;
    exports com.gui.view;
}