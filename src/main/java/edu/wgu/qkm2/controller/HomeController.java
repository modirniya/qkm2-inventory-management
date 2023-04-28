package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.InventorySampleDataPopulator;
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
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static edu.wgu.qkm2.data.Inventory.*;

/**
 * @author Parham Modirniya
 */
public class HomeController implements Initializable {
    @FXML
    private Label lbPartNotFound;
    @FXML
    private Label lbProductNotFound;
    @FXML
    private TextField tfSearchPart;
    @FXML
    private TextField tfSearchProduct;
    @FXML
    private TableView<Part> tvParts;
    @FXML
    private TableView<Product> tvProducts;
    private final Stage partStage = new Stage();
    private final Stage productStage = new Stage();
    private PartController partController;
    private ProductController productController;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param url            The location used to resolve relative paths for the root object, or
     *                       <tt>null</tt> if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or
     *                       <tt>null</tt> if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeScreens();
        InventorySampleDataPopulator.addSampleData();
        tvParts.setItems(getAllParts());
        tvProducts.setItems(getAllProducts());
        initializeSearchFields();
    }

    /**
     * Initializes the part and product screens.
     * Loads the fxml files for each screen and assigns the corresponding controller.
     * Sets the scene and modality of each stage to application modal.
     * If any errors occur while loading the fxml files an error alert is displayed.
     */
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to initialize screens");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * RUNTIME ERROR
     * When looking up a part by name that does not exist,
     * the list returned by lookupPart() method has a size of 0,
     * causing an IndexOutOfBoundsException when trying to access its first
     * element with list.get(0) == null. To fix this, a check was added to
     * the condition that sets the Part table view items.
     * <p>
     * Initializes the search fields and adds listeners to them.
     * The listeners detect changes in the text fields and update the table views accordingly.
     * If the search text can be parsed to an integer, it calls the lookupPart() or lookupProduct() method
     * on the inventory instance and sets the result in the corresponding table view.
     * If it cannot be parsed to an integer, it will perform a string search using lookupPart() or lookupProduct()
     * and update the table view with the search results.
     * </p>
     */
    private void initializeSearchFields() {
        tfSearchPart.textProperty().addListener((observableValue, oldVal, newVal) -> {
            ObservableList<Part> list = FXCollections.observableArrayList();
            try {
                int id = Integer.parseInt(newVal);
                list.add(lookupPart(id));
                tvParts.setItems(list);
            } catch (NumberFormatException e) {
                list = lookupPart(newVal);
                tvParts.setItems(list);
            }
            lbPartNotFound.setVisible(list.size() == 0 || list.get(0) == null);
        });
        tfSearchProduct.textProperty().addListener((observableValue, oldVal, newVal) -> {
            ObservableList<Product> list = FXCollections.observableArrayList();
            try {
                int id = Integer.parseInt(newVal);
                list.add(lookupProduct(id));
                tvProducts.setItems(list);
            } catch (NumberFormatException e) {
                list = lookupProduct(newVal);
                tvProducts.setItems(list);
            }
            lbProductNotFound.setVisible(list.size() == 0 || list.get(0) == null);
        });
    }

    /**
     * This method is called when the user clicks on the "Add Part" button.
     * It updates the UI of the PartController by passing in an index of -1 to indicate that a new part is being added.
     * Then it shows the part stage and waits for it to be closed by the user.
     */
    @FXML
    private void addPart() {
        partController.updateUI(-1);
        partStage.showAndWait();
    }

    /**
     * Modifies the selected part in the TableView.
     * If no item is selected, calls showAlertNoItemSelected()
     */
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

    /**
     * Removes a part from the inventory.
     * If the user confirms the deletion, the selected part will be deleted from the inventory.
     * If no part is selected, a warning alert is displayed to the user.
     */
    @FXML
    private void removePart() {
        var part = tvParts.getSelectionModel().getSelectedItem();
        if (part != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Part deletion");
            alert.setContentText("Are you sure you want to delete this item?");
            alert.showAndWait().filter(resp -> resp == ButtonType.OK).ifPresent(
                    e -> deletePart(part)
            );
        } else
            showAlertNoItemSelected();
    }


    /**
     * Initializes the UI for adding a new product and shows the product stage.
     */
    @FXML
    private void addProduct() {
        productController.updateUI(-1);
        productStage.showAndWait();
    }

    /**
     * Modifies the selected product by showing the product screen populated with the values of the selected product.
     * If no product is selected, an alert is shown.
     */
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

    /**
     * Removes the selected product from the inventory.
     * If the product has associated parts, a confirmation alert is shown, indicating that the parts need to be removed before the product can be deleted.
     * If the product has no associated parts, a confirmation alert is shown, asking if the user is sure they want to delete the product.
     * If the user confirms the deletion, the product is deleted from the inventory.
     * If no product is selected, a warning alert is shown, indicating that no product has been selected.
     */
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
                        resp -> resp == ButtonType.OK).ifPresent(e -> deleteProduct(product));
            }
        } else
            showAlertNoItemSelected();
    }

    /**
     * Exits the application after displaying a confirmation dialog box to the user.
     * If the user confirms the exit, the application is terminated using Platform.exit() method.
     * If the user cancels the exit, the dialog box is closed and the application continues running.
     */
    @FXML
    private void exit() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");
        alert.showAndWait().filter(resp ->
                resp == ButtonType.OK
        ).ifPresent(e -> Platform.exit());

    }

    /**
     * Shows an alert message indicating that no item has been selected in the table view.
     */
    private void showAlertNoItemSelected() {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Unknown target");
        alert.setHeaderText("No item been selected");
        alert.show();
    }
}