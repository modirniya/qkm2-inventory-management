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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rbInHouse.selectedProperty().addListener((ob, o, n) -> updatePartType());
        rbOutsourced.selectedProperty().addListener((ob, o, n) -> updatePartType());
    }

    @FXML
    private void save() {
        if (areEntriesValid()) {
            Part part;
            if (currentPartType == PartType.IN_HOUSE)
                part = new InHouse(id, name, price, stock, min, max, machineId);
            else
                part = new Outsourced(id, name, price, stock, min, max, companyName);
            // currentPartIdx will hold value -1 only if the user is adding a new part
            // and if it holds anything other than that, the user is modifying a part.
            if (currentPartIdx == -1)
                inventory.addPart(part);
            else
                inventory.updatePart(currentPartIdx, part);
            closeStage();
        }
    }

    @FXML
    private void cancel() {
        closeStage();
    }

    private void updatePartType() {
        if (rbInHouse.isSelected()) {
            lbMachineIdOrCompany.setText("Machine ID");
            currentPartType = PartType.IN_HOUSE;
        } else {
            lbMachineIdOrCompany.setText("Company");
            currentPartType = PartType.OUTSOURCED;
        }
    }

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

    private void closeStage() {
        Stage stage = (Stage) tfName.getScene().getWindow();
        stage.close();
    }

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

    private int getAvailableId() {
        var size = inventory.getAllParts().size();
        var lastElement = inventory.getAllParts().get(size - 1);
        return lastElement.getId() + 1;
    }

}
