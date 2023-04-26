package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.InventorySampleDataPopulator;
import edu.wgu.qkm2.data.Inventory;
import edu.wgu.qkm2.data.Part;
import edu.wgu.qkm2.data.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final Inventory inventory = Inventory.getInstance();
    private final Stage partStage = new Stage();
    private final Stage productStage = new Stage();
    private PartController partController;
    private ProductController productController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeScreens();
        InventorySampleDataPopulator.addSampleData();
        tvParts.setItems(inventory.getAllParts());
        tvProducts.setItems(inventory.getAllProducts());
        initializeSearchFields();
    }

    private void initializeScreens() {
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
    }

    private void initializeSearchFields() {

        tfSearchPart.textProperty().addListener((observableValue, oldVal, newVal) -> {
            try {
                int id = Integer.parseInt(newVal);
                ObservableList<Part> singleElementList = FXCollections.observableArrayList();
                singleElementList.add(inventory.lookupPart(id));
                tvParts.setItems(singleElementList);
            } catch (NumberFormatException e) {
                tvParts.setItems(inventory.lookupPart(newVal));
            }

        });

        tfSearchProduct.textProperty().addListener((observableValue, oldVal, newVal) -> {
            try {
                int id = Integer.parseInt(newVal);
                ObservableList<Product> singleElementList = FXCollections.observableArrayList();
                singleElementList.add(inventory.lookupProduct(id));
                tvProducts.setItems(singleElementList);
            } catch (NumberFormatException e) {
                tvProducts.setItems(inventory.lookupProduct(newVal));
            }
        });
    }

    @FXML
    private void addPart() {
        partController.updateUI(-1);
        partStage.showAndWait();
    }

    @FXML
    private void modifyPart() {
        int index = tvParts.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            partController.updateUI(index);
            partStage.showAndWait();
        } else {
            showAlertNoItemSelected();
        }
    }

    @FXML
    private void removePart() {
        var part = tvParts.getSelectionModel().getSelectedItem();
        if (part != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Part deletion");
            alert.setContentText("Are you sure you want to delete this item?");
            alert.showAndWait().filter(resp -> resp == ButtonType.OK).ifPresent(
                    e -> inventory.deletePart(part)
            );
        } else
            showAlertNoItemSelected();
    }

    @FXML
    private void addProduct() {
        productController.updateUI(-1);
        productStage.showAndWait();
    }

    @FXML
    private void modifyProduct() {
        int index = tvProducts.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            productController.updateUI(index);
            productStage.showAndWait();
        } else {
            showAlertNoItemSelected();
        }
    }

    @FXML
    private void removeProduct() {
        var product = tvProducts.getSelectionModel().getSelectedItem();
        if (product != null) {
            if (product.getAllAssociatedParts().size() != 0) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Deletion failed");
                alert.setHeaderText("This product contains associated parts");
                alert.setContentText("Remove associated parts within this product before attempting to remove it.");
                alert.showAndWait();
            } else {
                var alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Product deletion");
                alert.setContentText("Are you sure you want to delete this item?");
                alert.showAndWait().filter(
                        resp -> resp == ButtonType.OK).ifPresent(e -> inventory.deleteProduct(product));
            }
        } else
            showAlertNoItemSelected();
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

    private void showAlertNoItemSelected() {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Unknown target");
        alert.setHeaderText("No item been selected");
        alert.show();
    }


}