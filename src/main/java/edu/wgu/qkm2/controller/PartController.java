package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.data.InHouse;
import edu.wgu.qkm2.data.Inventory;
import edu.wgu.qkm2.data.Outsourced;
import edu.wgu.qkm2.data.Part;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Parham Modirniya
 */
public class PartController implements Initializable {

    @FXML
    private Label lbTitle;
    @FXML
    private Label lbMachineIdOrCompany;
    @FXML
    private Label lbError;
    @FXML
    private RadioButton rbOutsourced;
    @FXML
    private RadioButton rbInHouse;
    @FXML
    private TextField tfMin;
    @FXML
    private TextField tfMax;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfInventory;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfMachineIdOrCompany;

    public enum PartType {
        IN_HOUSE,
        OUTSOURCED
    }

    private PartType currentPartType = PartType.IN_HOUSE;
    private final Inventory inventory = Inventory.getInstance();
    private int currentPartIdx, id, stock, min, max, machineId;
    private String name, companyName;
    private double price;

    /**
     * Initializes the controller class and sets up listeners.
     * <p>
     * This method is called after the FXMLLoader has loaded the FXML file and the
     * UI elements are available. It sets up listeners for the InHouse and Outsourced
     * radio buttons, ensuring that the updatePartType() method is called when the
     * selection changes.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rbInHouse.selectedProperty().addListener((ob, o, n) -> updatePartType());
        rbOutsourced.selectedProperty().addListener((ob, o, n) -> updatePartType());
    }

    /**
     * Handles the Save button click event.
     * <p>
     * Validates user input, creates a new Part object (InHouse or Outsourced), and
     * either adds it to the inventory or updates an existing part. Closes the stage
     * if the input is valid.
     * <p>
     * This method is called when the Save button is clicked in the UI. If the input
     * is valid, it creates a new Part object based on the currentPartType. If the
     * currentPartIdx is -1, it means the user is adding a new part, and the new part
     * is added to the inventory. If the currentPartIdx is not -1, the user is modifying
     * an existing part, and the inventory is updated with the new part.
     */
    @FXML
    private void save() {
        if (areEntriesValid()) {
            Part part;
            if (currentPartType == PartType.IN_HOUSE)
                part = new InHouse(id, name, price, stock, min, max, machineId);
            else
                part = new Outsourced(id, name, price, stock, min, max, companyName);
            if (currentPartIdx == -1)
                inventory.addPart(part);
            else
                inventory.updatePart(currentPartIdx, part);
            closeStage();
        }
    }

    /**
     * Handles the Cancel button click event.
     * <p>
     * Closes the stage without saving any changes.
     * <p>
     * This method is called when the Cancel button is clicked in the UI. It
     * immediately closes the stage, discarding any changes made by the user.
     */
    @FXML
    private void cancel() {
        closeStage();
    }

    /**
     * Updates the part type and corresponding UI elements.
     * <p>
     * This method updates the current part type based on the selected radio button
     * (InHouse or Outsourced) and updates the relevant UI label (Machine ID or Company).
     * It is typically called when the user changes the part type.
     */
    private void updatePartType() {
        if (rbInHouse.isSelected()) {
            lbMachineIdOrCompany.setText("Machine ID");
            currentPartType = PartType.IN_HOUSE;
        } else {
            lbMachineIdOrCompany.setText("Company");
            currentPartType = PartType.OUTSOURCED;
        }
    }

    /**
     * Updates the UI based on the target part index.
     * <p>
     * This method is responsible for preparing the form for adding or modifying a part.
     * It clears all input fields and sets the appropriate labels and input values
     * depending on whether a new part is being added or an existing part is being modified.
     * If targetPartIdx is -1, it prepares the form for adding a new part.
     * Otherwise, it prepares the form for modifying the part at the specified index.
     *
     * @param targetPartIdx The index of the part in the inventory to modify, or -1 to add a new part.
     */
    protected void updateUI(int targetPartIdx) {
        clearAllFields();
        if (targetPartIdx == -1) {
            lbTitle.setText("Add Part");
            tfId.setText(String.valueOf(getAvailableId()));
        } else {
            lbTitle.setText("Modify Part");
            currentPartIdx = targetPartIdx;
            populateAllFields();
        }
    }

    /**
     * Closes the current stage (part screen).
     * <p>
     * This method retrieves the current stage using the scene from the 'tfName' TextField
     * and closes it.
     */
    private void closeStage() {
        Stage stage = (Stage) tfName.getScene().getWindow();
        stage.close();
    }

    /**
     * Clears all input fields and resets part type to 'InHouse'.
     * <p>
     * This method clears the content of all input fields, resets the part type to 'InHouse',
     * deselects the 'Outsourced' radio button, and clears any error messages displayed in 'lbError'.
     * It also resets the 'currentPartIdx' to -1.
     */
    private void clearAllFields() {
        currentPartType = PartType.IN_HOUSE;
        tfId.clear();
        tfName.clear();
        tfPrice.clear();
        tfInventory.clear();
        tfMax.clear();
        tfMin.clear();
        tfMachineIdOrCompany.clear();
        rbInHouse.setSelected(true);
        lbError.setText("");
        currentPartIdx = -1;
    }

    /**
     * Populates all input fields with the data from the current part.
     * <p>
     * This method retrieves the current part from the inventory using the currentPartIdx index,
     * and fills the corresponding input fields with its data. The part type radio buttons are
     * set accordingly, and the Machine ID or Company Name field is populated based on the part type.
     */
    private void populateAllFields() {
        var product = inventory.getAllParts().get(currentPartIdx);
        tfId.setText(String.valueOf(product.getId()));
        tfName.setText(product.getName());
        tfPrice.setText(String.valueOf(product.getPrice()));
        tfInventory.setText(String.valueOf(product.getStock()));
        tfMax.setText(String.valueOf(product.getMax()));
        tfMin.setText(String.valueOf(product.getMin()));
        if (product instanceof InHouse) {
            rbInHouse.setSelected(true);
            tfMachineIdOrCompany.setText(String.valueOf(((InHouse) product).getMachineId()));
        } else if (product instanceof Outsourced) {
            rbOutsourced.setSelected(true);
            tfMachineIdOrCompany.setText(((Outsourced) product).getCompanyName());
        }
        updatePartType();
    }

    /**
     * Checks if the user input for each field is valid.
     * <p>
     * This method checks if the provided user input for part ID, name, price, inventory, min, max,
     * and either machine ID or company name is valid, depending on the current part type.
     * If there are any errors in the input, they will be listed in the 'lbError' label.
     *
     * @return true if all entries are valid, false otherwise
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
        String machineIdOrCompany = tfMachineIdOrCompany.getText();
        if (currentPartType == PartType.IN_HOUSE) {
            try {
                machineId = Integer.parseInt(machineIdOrCompany);
            } catch (NumberFormatException e) {
                errorPrompt += "\t‣ Machine ID must be a valid number\n";
            }
        } else {
            if (machineIdOrCompany.isBlank())
                errorPrompt += "\t‣ Company name must be entered\n";
            else
                companyName = machineIdOrCompany;
        }
        if (!errorPrompt.isBlank())
            lbError.setText("Error: Storing values has failed\n"
                    + "Please fix the following issues:\n"
                    + errorPrompt);
        return errorPrompt.isBlank();
    }

    /**
     * Retrieves the next available ID for a new part.
     * <p>
     * This method calculates the next available ID by finding the last part in the inventory
     * and incrementing its ID by 1. It assumes that the part IDs are sequential and that
     * there are no gaps in the numbering.
     * <p>
     * Future enhancements:
     * - Implement a more robust ID generation method that can handle non-sequential IDs or
     * gaps in the numbering.
     *
     * @return the next available ID for a new part
     */
    private int getAvailableId() {
        var size = inventory.getAllParts().size();
        var lastElement = inventory.getAllParts().get(size - 1);
        return lastElement.getId() + 1;
    }

}
