package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.data.Part;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

    public void updateUI(int targetProductIdx) {
        if (targetProductIdx == -1) {
            lbTitle.setText("Add Product");
        } else {
            lbTitle.setText("Modify Product");
        }
    }

    @FXML
    private void removePart() {
    }

    @FXML
    private void addPart() {
    }

    @FXML
    private void cancel() {
    }

    @FXML
    private void save() {
    }
}
