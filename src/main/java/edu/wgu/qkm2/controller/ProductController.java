package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.data.Part;
import edu.wgu.qkm2.data.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static edu.wgu.qkm2.data.Inventory.*;

/**
 * @author Parham Modirniya
 */
public class ProductController implements Initializable {
    @FXML
    private Label lbPartNotFound;
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
    private int currentProductIdx, id, stock, min, max;
    private String name;
    private double price;
    private ObservableList<Part> associatedParts;

    /**
     * This method initializes the controller class when the FXML is loaded.
     * It sets up a listener on the TextField tfSearchPart to filter the parts in the TableView tvAllParts
     * based on the search query entered by the user. The listener is triggered whenever the text in the
     * TextField changes. The method tries to parse the entered value as an integer, and if successful,
     * searches for a part with the matching ID. If the entered value is not an integer, it searches for
     * parts with matching names.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the
     *                       location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the
     *                       root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfSearchPart.textProperty().addListener((observableValue, oldVal, newVal) -> {
            ObservableList<Part> list = FXCollections.observableArrayList();
            try {
                int id = Integer.parseInt(newVal);
                list.add(lookupPart(id));
                tvAllParts.setItems(list);
            } catch (NumberFormatException e) {
                list = lookupPart(newVal);
                tvAllParts.setItems(list);
            }
            lbPartNotFound.setVisible(list.size() == 0 || list.get(0) == null);
        });
    }

    /**
     * This method is called when the user clicks on the "Save" button. It first checks if the entered
     * values in the form are valid by calling the areEntriesValid() method. If the values are valid,
     * it creates a new Product instance and assigns the associated parts from the tvAssociatedParts
     * TableView to the Product. If the currentProductIdx is -1, the method adds the new product to
     * the inventory; otherwise, it updates the existing product in the inventory. Finally, it clears
     * the associated parts list and closes the stage.
     *
     * <p><b>LOGICAL ERROR:</b> The order of the {@code max} and {@code min} parameters passed to the {@code Product} constructor was incorrect,
     * which could lead to unexpected behavior when creating or updating a product. This error has been corrected by swapping the order of these parameters.</p>
     */
    @FXML
    private void save() {
        if (areEntriesValid()) {
            Product product = new Product(id, name, price, stock, min, max);
            product.getAllAssociatedParts().clear();
            product.getAllAssociatedParts().addAll(tvAssociatedParts.getItems());
            if (currentProductIdx == -1)
                addProduct(product);
            else
                updateProduct(currentProductIdx, product);
            product.getAllAssociatedParts().clear();
            product.getAllAssociatedParts().addAll(associatedParts);
            closeStage();
        }
    }

    /**
     * This method closes the current stage when the user clicks the "Cancel" button.
     * It is triggered by an FXML action and calls the closeStage() method to close the window (Product screen).
     */
    @FXML
    private void cancel() {
        closeStage();
    }

    /**
     * This method adds a selected part from the tvAllParts TableView to the tvAssociatedParts TableView.
     * It is triggered by an FXML action when the user clicks the "Add" button.
     */
    @FXML
    private void addPart() {
        var part = tvAllParts.getSelectionModel().getSelectedItem();
        if (part != null)
            tvAssociatedParts.getItems().add(part);
    }

    /**
     * This method removes a selected part from the tvAssociatedParts TableView.
     * It is triggered by an FXML action when the user clicks the "Remove" button.
     */
    @FXML
    private void removePart() {
        var part = tvAssociatedParts.getSelectionModel().getSelectedItem();
        if (part != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Part deletion");
            alert.setContentText("Are you sure you want to delete this item?");
            alert.showAndWait().filter(resp -> resp == ButtonType.OK).ifPresent(
                    e -> tvAssociatedParts.getItems().remove(part)
            );
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Unknown target");
            alert.setHeaderText("No item been selected");
            alert.show();
        }
    }

    /**
     * This method closes the current stage (window) of the application.
     */
    private void closeStage() {
        Stage stage = (Stage) tfName.getScene().getWindow();
        stage.close();
    }

    /**
     * This method updates the UI based on the given target product index.
     * If the target product index is -1, it sets up the UI for adding a new product.
     * Otherwise, it sets up the UI for modifying the existing product at the specified index.
     *
     * @param targetProductIdx The index of the target product to update, or -1 for adding a new product.
     */
    public void updateUI(int targetProductIdx) {
        clearAllFields();
        tvAllParts.setItems(getAllParts());
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

    /**
     * This method clears all the text fields and resets the associated parts list, error label,
     * and current product index. It is used to prepare the UI for a new product entry or to
     * reset the UI after modifying an existing product.
     */
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

    /**
     * This method populates all the text fields and associated parts list based on the selected product.
     * It retrieves the product from the inventory using the currentProductIdx and sets the text fields
     * for ID, name, price, inventory, min, and max, as well as populating the associated parts list.
     */
    private void populateAllFields() {
        var product = getAllProducts().get(currentProductIdx);
        tfId.setText(String.valueOf(product.getId()));
        tfName.setText(product.getName());
        tfPrice.setText(String.valueOf(product.getPrice()));
        tfInventory.setText(String.valueOf(product.getStock()));
        tfMax.setText(String.valueOf(product.getMax()));
        tfMin.setText(String.valueOf(product.getMin()));
        associatedParts.addAll(product.getAllAssociatedParts());
    }

    /**
     * This method checks the validity of the text field entries and sets error messages accordingly.
     * It validates the name, price, inventory, min, max, and machine ID or company name fields,
     * depending on the part type (in-house or outsourced).
     *
     * @return true if all entries are valid, otherwise false
     */
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

    /**
     * This method calculates and returns the next available ID for a new product.
     * It finds the last product in the inventory and increments its ID by 1 to ensure uniqueness.
     *
     * @return the next available ID for a new product
     */
    private int getAvailableId() {
        var size = getAllProducts().size();
        var lastElement = getAllProducts().get(size - 1);
        return lastElement.getId() + 1;
    }
}
