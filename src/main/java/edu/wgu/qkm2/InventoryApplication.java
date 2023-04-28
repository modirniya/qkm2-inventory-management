package edu.wgu.qkm2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Parham Modirniya
 * <p>
 * FUTURE ENHANCEMENT
 * Refactor the Inventory class to be an interface and implement different data sources, such as a SQL database as a local cache,
 * and a RestApi backend as a data backup. This enhancement would allow the application to work seamlessly with different data sources,
 * providing better data management and availability. By implementing this feature, users could easily switch between different data
 * sources without the need to modify the application code, and the application could be easily scaled to support larger data sets or
 * additional functionality.
 */
public class InventoryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryApplication.class.getResource("home-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    // Javadoc at "qkm2-inventory-management/Javadoc"
    public static void main(String[] args) {
        launch();
    }
}