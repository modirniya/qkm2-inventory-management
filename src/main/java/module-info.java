module edu.wgu.qkm2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.wgu.qkm2 to javafx.fxml;
    exports edu.wgu.qkm2;
    exports edu.wgu.qkm2.data;
    exports edu.wgu.qkm2.controller;
    opens edu.wgu.qkm2.controller to javafx.fxml;
}