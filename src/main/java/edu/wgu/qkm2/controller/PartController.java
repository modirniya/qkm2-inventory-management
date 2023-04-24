package edu.wgu.qkm2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class PartController {


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


    protected void updateUI(int targetPartIdx) {
        if (targetPartIdx == -1) {
            lbTitle.setText("Add Part");
        } else {
            lbTitle.setText("Modify Part");
        }
    }

    @FXML
    private void cancel() {
    }

    @FXML
    private void save() {
    }
}
