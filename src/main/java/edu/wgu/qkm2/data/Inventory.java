package edu.wgu.qkm2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private final ObservableList<Part> allParts;
    private final ObservableList<Product> allProducts;

    private Inventory() {
        this.allParts = FXCollections.observableArrayList();
        this.allProducts = FXCollections.observableArrayList();
    }

    private static final Inventory instance = new Inventory();

    public static Inventory getInstance() {
        return instance;
    }

    public void addPart(Part newPart) {
        this.allParts.add(newPart);
    }

    public void addProduct(Product newProduct) {
        this.allProducts.add(newProduct);
    }

    public Part lookupPart(int partId) {
        for (Part part : this.allParts)
            if (part.getId() == partId)
                return part;
        return null;
    }

    public Product lookupProduct(int productId) {
        for (Product product : this.allProducts)
            if (product.getId() == productId)
                return product;
        return null;
    }

    public ObservableList<Part> lookupPart(String partName) {
        if (partName.isBlank()) return this.allParts;
        ObservableList<Part> resultList = FXCollections.observableArrayList();
        for (Part part : this.allParts)
            if (part.getName().toLowerCase().contains(partName.toLowerCase()))
                resultList.add(part);
        return resultList;
    }

    public ObservableList<Product> lookupProduct(String productName) {
        if (productName.isBlank()) return this.allProducts;
        ObservableList<Product> resultList = FXCollections.observableArrayList();
        for (Product product : this.allProducts)
            if (product.getName().toLowerCase().contains(productName.toLowerCase()))
                resultList.add(product);
        return resultList;
    }

    public void updatePart(int index, Part selectedPart) {
        this.allParts.set(index, selectedPart);
    }

    public void updateProduct(int index, Product selectedProduct) {
        this.allProducts.set(index, selectedProduct);
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
