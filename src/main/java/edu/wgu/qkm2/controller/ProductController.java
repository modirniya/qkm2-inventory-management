package edu.wgu.qkm2.controller;

import edu.wgu.qkm2.data.Part;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProductController {
    public Label lbTitle;
    public TextField tfId;
    public TextField tfName;
    public TextField tfInventory;
    public TextField tfPrice;
    public TextField tfMax;
    public TextField tfMin;
    public Label lbError;
    public TableView<Part> tvAllParts;
    public TableView<Part> tvAssociatedParts;
    public TextField tfSearchPart;

    public void removePart(ActionEvent actionEvent) {
    }

    public void addPart(ActionEvent actionEvent) {
    }

    public void cancel(ActionEvent actionEvent) {
    }

    public void save(ActionEvent actionEvent) {
    }
}
