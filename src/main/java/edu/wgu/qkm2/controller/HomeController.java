package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.data.InHousePart;
import edu.wgu.qkm2.data.Inventory;
import edu.wgu.qkm2.data.Part;
import edu.wgu.qkm2.data.Product;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private TextField tfSearchPart;
    @FXML
    private TextField tfSearchProduct;
    @FXML
    private TableView<Part> tvParts;
    @FXML
    private TableView<Product> tvProducts;

    private final Stage partStage = new Stage();
    private PartController partController;

    private final Stage productStage = new Stage();
    private ProductController productController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader partLoader = new FXMLLoader(getClass().getResource("/edu/wgu/qkm2/part-screen.fxml"));
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/edu/wgu/qkm2/product-screen.fxml"));

        try {
            Parent partScreen = partLoader.load();
            partController = partLoader.getController();
            Parent productScreen = productLoader.load();
            productController = productLoader.getController();
            partStage.initModality(Modality.APPLICATION_MODAL);
            partStage.setScene(new Scene(partScreen));
            productStage.initModality(Modality.APPLICATION_MODAL);
            productStage.setScene(new Scene(productScreen));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Inventory inventory = Inventory.getInstance();
        var part = new InHousePart(
                1, "Disc", 69.99, 12, 2, 20, 233);
        inventory.addPart(part);
        var product = new Product(
                1, "Disc Brake", 99.99, 10, 15, 5);
        inventory.addProduct(product);
        updatePartsTable();
        updateProductsTable();
    }

    @FXML
    private void addPart() {
        partController.updateUI(-1);
        partStage.setTitle("Add Part");
        partStage.showAndWait();
    }

    @FXML
    private void modifyPart() {
        int index = tvParts.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            partController.updateUI(index);
            partStage.setTitle("Modify Screen");
            partStage.showAndWait();
        } else {
            showAlertNoItemSelected();
        }
    }

    private void showAlertNoItemSelected() {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Unknown target");
        alert.setHeaderText("No item been selected");
        alert.setContentText("Please select the item you wish to modify.");
        alert.show();
    }

    @FXML
    private void removePart() {
    }

    @FXML
    private void addProduct() {
        productController.updateUI(-1);
        productStage.setTitle("Add Product");
        productStage.showAndWait();
    }

    @FXML
    private void modifyProduct() {
        int index = tvProducts.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            productController.updateUI(index);
            productStage.setTitle("Modify Product");
            productStage.showAndWait();
        } else {
            showAlertNoItemSelected();
        }
    }

    @FXML
    private void removeProduct() {
    }

    @FXML
    private void exit() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");
        alert.showAndWait().filter(resp ->
                resp == ButtonType.OK
        ).ifPresent(e -> Platform.exit());

    }

    private void updatePartsTable() {
        tvParts.setItems(Inventory.getInstance().getAllParts());
    }

    private void updateProductsTable() {
        tvProducts.setItems(Inventory.getInstance().getAllProducts());
    }


}