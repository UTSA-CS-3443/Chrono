module edu.utsa.cs3443.chrono {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;



    opens edu.utsa.cs3443.chrono to javafx.fxml;
    exports edu.utsa.cs3443.chrono;

}