package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.data.Inventory;
import edu.wgu.qkm2.data.Part;
import edu.wgu.qkm2.data.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductController {
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbError;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfInventory;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfMax;
    @FXML
    private TextField tfMin;
    @FXML
    private TextField tfSearchPart;
    @FXML
    private TableView<Part> tvAllParts;
    @FXML
    private TableView<Part> tvAssociatedParts;
    private final Inventory inventory = Inventory.getInstance();
    private int currentProductIdx = -1;
    private ObservableList<Part> associatedParts;

    @FXML
    private void removePart() {
        var part = tvAssociatedParts.getSelectionModel().getSelectedItem();
        if (part != null)
            tvAssociatedParts.getItems().remove(part);
    }

    @FXML
    private void addPart() {
        var part = tvAllParts.getSelectionModel().getSelectedItem();
        if (part != null)
            tvAssociatedParts.getItems().add(part);
    }

    @FXML
    private void save() {
        Product product = extractAllFields();
        if (currentProductIdx == -1)
            inventory.addProduct(product);
        else
            inventory.updateProduct(currentProductIdx, product);
        product.getAllAssociatedParts().clear();
        product.getAllAssociatedParts().addAll(associatedParts);
        closeStage();
    }

    @FXML
    private void cancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) tfName.getScene().getWindow();
        stage.close();
    }

    public void updateUI(int targetProductIdx) {
        clearAllFields();
        tvAllParts.setItems(inventory.getAllParts());
        if (targetProductIdx == -1) {
            lbTitle.setText("Add Product");
            int availableId = getAvailableId();
            tfId.setText(String.valueOf(availableId));
        } else {
            lbTitle.setText("Modify Product");
            currentProductIdx = targetProductIdx;
            populateAllFields();
        }
        tvAssociatedParts.setItems(associatedParts);
    }

    private void clearAllFields() {
        tfId.clear();
        tfName.clear();
        tfPrice.clear();
        tfInventory.clear();
        tfMax.clear();
        tfMin.clear();
        associatedParts = FXCollections.observableArrayList();
        currentProductIdx = -1;
    }

    private Product extractAllFields() {
        int id = Integer.parseInt(tfId.getText());
        String name = tfName.getText();
        double price = Double.parseDouble(tfPrice.getText());
        int stock = Integer.parseInt(tfInventory.getText());
        int min = Integer.parseInt(tfMin.getText());
        int max = Integer.parseInt(tfMax.getText());
        var product = new Product(id, name, price, stock, max, min);
        product.getAllAssociatedParts().clear();
        product.getAllAssociatedParts().addAll(tvAssociatedParts.getItems());
        return product;
    }

    private void populateAllFields() {
        var product = inventory.getAllProducts().get(currentProductIdx);
        tfId.setText(String.valueOf(product.getId()));
        tfName.setText(product.getName());
        tfPrice.setText(String.valueOf(product.getPrice()));
        tfInventory.setText(String.valueOf(product.getStock()));
        tfMax.setText(String.valueOf(product.getMax()));
        tfMin.setText(String.valueOf(product.getMin()));
        associatedParts.addAll(product.getAllAssociatedParts());
    }

    private int getAvailableId() {
        var size = inventory.getAllProducts().size();
        var lastElement = inventory.getAllProducts().get(size - 1);
        return lastElement.getId() + 1;
    }
}
