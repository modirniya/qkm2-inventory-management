package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.data.Inventory;
import edu.wgu.qkm2.data.Part;
import edu.wgu.qkm2.data.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
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
    private int currentProductIdx, id, stock, min, max;
    private String name;
    private double price;
    private ObservableList<Part> associatedParts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfSearchPart.textProperty().addListener((observableValue, oldVal, newVal) -> {
            try {
                int id = Integer.parseInt(newVal);
                ObservableList<Part> singleElementList = FXCollections.observableArrayList();
                singleElementList.add(inventory.lookupPart(id));
                tvAllParts.setItems(singleElementList);
            } catch (NumberFormatException e) {
                tvAllParts.setItems(inventory.lookupPart(newVal));
            }

        });
    }

    @FXML
    private void save() {
        if (areEntriesValid()) {
            Product product = new Product(id, name, price, stock, max, min);
            product.getAllAssociatedParts().clear();
            product.getAllAssociatedParts().addAll(tvAssociatedParts.getItems());
            if (currentProductIdx == -1)
                inventory.addProduct(product);
            else
                inventory.updateProduct(currentProductIdx, product);
            product.getAllAssociatedParts().clear();
            product.getAllAssociatedParts().addAll(associatedParts);
            closeStage();
        }
    }

    @FXML
    private void cancel() {
        closeStage();
    }

    @FXML
    private void addPart() {
        var part = tvAllParts.getSelectionModel().getSelectedItem();
        if (part != null)
            tvAssociatedParts.getItems().add(part);
    }

    @FXML
    private void removePart() {
        var part = tvAssociatedParts.getSelectionModel().getSelectedItem();
        if (part != null)
            tvAssociatedParts.getItems().remove(part);
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
            tfId.setText(String.valueOf(getAvailableId()));
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
        lbError.setText("");
        currentProductIdx = -1;
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

    private boolean areEntriesValid() {
        String errorPrompt = "";
        id = Integer.parseInt(tfId.getText());
        name = tfName.getText();
        if (name.isBlank()) {
            errorPrompt += "\t‣ Name field is empty\n";
        }
        try {
            price = Double.parseDouble(tfPrice.getText());
        } catch (NumberFormatException e) {
            errorPrompt += "\t‣ Price field must be a valid number\n";
        }
        try {
            stock = Integer.parseInt(tfInventory.getText());
            min = Integer.parseInt(tfMin.getText());
            max = Integer.parseInt(tfMax.getText());
            if (max < min) {
                errorPrompt += "\t‣ Maximum capacity must be greater than the minimum\n";
            } else if (!(min < stock && stock < max)) {
                errorPrompt += "\t‣ Inventory is outside of min/max range\n";
            }
        } catch (NumberFormatException e) {
            errorPrompt += "\t‣ Inventory, min, and max values must be valid numbers\n";
        }
        if (!errorPrompt.isBlank())
            lbError.setText("Error: Storing values has failed\n"
                    + "Please fix the following issues:\n"
                    + errorPrompt);
        return errorPrompt.isBlank();
    }

    private int getAvailableId() {
        var size = inventory.getAllProducts().size();
        var lastElement = inventory.getAllProducts().get(size - 1);
        return lastElement.getId() + 1;
    }
}
