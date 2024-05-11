module com.example.javasocket2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javasocket2 to javafx.fxml;
    exports com.example.javasocket2;
}