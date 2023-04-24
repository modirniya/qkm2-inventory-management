package edu.wgu.qkm2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private final ObservableList<Part> allParts;
    private final ObservableList<Product> allProducts;

    public Inventory() {
        this.allParts = FXCollections.observableArrayList();
        this.allProducts = FXCollections.observableArrayList();
    }

    public void addPart(Part newPart) {
        this.allParts.add(newPart);
    }

    public void addProduct(Product newProduct) {
        this.allProducts.add(newProduct);
    }

    public Part lockupPart(int partId) {
        for (Part part : this.allParts)
            if (part.id == partId)
                return part;
        return null;
    }

    public Product lockupProduct(int productId) {
        for (Product product : this.allProducts)
            if (product.id == productId)
                return product;
        return null;
    }

    public ObservableList<Part> lockupPart(String partName) {
        if (partName.isBlank()) return this.allParts;
        ObservableList<Part> resultList = FXCollections.observableArrayList();
        for (Part part : this.allParts)
            if (part.name.contains(partName))
                resultList.add(part);
        return resultList;
    }

    public ObservableList<Product> lockupProduct(String productName) {
        if (productName.isBlank()) return this.allProducts;
        ObservableList<Product> resultList = FXCollections.observableArrayList();
        for (Product product : this.allProducts)
            if (product.name.contains(productName))
                resultList.add(product);
        return null;
    }

    public void updatePart(int index, Part selectedPart) {
        this.allParts.add(index, selectedPart);
    }

    public void updateProduct(int index, Product selectedProduct) {
        this.allProducts.add(index, selectedProduct);
    }

    public boolean deletePart(Part selectedPart) {
        return this.allParts.remove(selectedPart);
    }

    public boolean deleteProduct(Product selectedProduct) {
        return this.allProducts.remove(selectedProduct);
    }

    public ObservableList<Part> getAllParts() {
        return this.allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return this.allProducts;
    }
}
