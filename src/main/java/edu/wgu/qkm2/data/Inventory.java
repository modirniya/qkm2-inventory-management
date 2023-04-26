package edu.wgu.qkm2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Parham Modirniya
 */
public class Inventory {
    private final ObservableList<Part> allParts;
    private final ObservableList<Product> allProducts;

    /**
     * A private constructor for the Inventory class.
     * Initializes the allParts and allProducts lists with empty observable array lists.
     */
    private Inventory() {
        this.allParts = FXCollections.observableArrayList();
        this.allProducts = FXCollections.observableArrayList();
    }

    /**
     * Singleton instance of the {@link Inventory} class.
     * This instance is created at the time the class is loaded and is accessed by calling this static variable.
     */
    private static final Inventory instance = new Inventory();

    /**
     * Returns the single instance of the Inventory class. If no instance of Inventory exists, one will be created.
     *
     * @return the single instance of the Inventory class.
     */
    public static Inventory getInstance() {
        return instance;
    }

    /**
     * Adds a new part to the inventory.
     *
     * @param newPart the part to add
     */
    public void addPart(Part newPart) {
        this.allParts.add(newPart);
    }

    /**
     * Adds the given Product to the list of all products.
     *
     * @param newProduct the Product object to be added to the inventory
     */
    public void addProduct(Product newProduct) {
        this.allProducts.add(newProduct);
    }

    /**
     * Searches for a Part object in the inventory by its ID.
     *
     * @param partId the ID of the Part to look up
     * @return the Part object with the specified ID, or null if not found
     */
    public Part lookupPart(int partId) {
        for (Part part : this.allParts)
            if (part.getId() == partId)
                return part;
        return null;
    }

    /**
     * Looks up a {@link Product} by its ID.
     *
     * @param productId the ID of the {@link Product} to lookup
     * @return the found {@link Product} or null if not found
     */
    public Product lookupProduct(int productId) {
        for (Product product : this.allProducts)
            if (product.getId() == productId)
                return product;
        return null;
    }

    /**
     * Looks up and returns a list of parts whose names contain the given partName.
     * If partName is empty or null, returns all parts in the inventory.
     *
     * @param partName the string to search for in the names of the parts
     * @return an observable list of parts that contain partName in their names
     */
    public ObservableList<Part> lookupPart(String partName) {
        if (partName.isBlank()) return this.allParts;
        ObservableList<Part> resultList = FXCollections.observableArrayList();
        for (Part part : this.allParts)
            if (part.getName().toLowerCase().contains(partName.toLowerCase()))
                resultList.add(part);
        return resultList;
    }

    /**
     * Searches for products that contain the provided product name.
     * If the provided name is blank, returns all products in the inventory.
     *
     * @param productName the name of the product to search for.
     * @return an ObservableList of products containing the provided name or all products if the provided name is blank.
     */
    public ObservableList<Product> lookupProduct(String productName) {
        if (productName.isBlank()) return this.allProducts;
        ObservableList<Product> resultList = FXCollections.observableArrayList();
        for (Product product : this.allProducts)
            if (product.getName().toLowerCase().contains(productName.toLowerCase()))
                resultList.add(product);
        return resultList;
    }

    /**
     * Updates an existing part in the inventory.
     *
     * @param index        the index of the part to be updated
     * @param selectedPart the updated part object to replace the existing part
     */
    public void updatePart(int index, Part selectedPart) {
        this.allParts.set(index, selectedPart);
    }

    /**
     * Updates the product at the given index with the given selectedProduct.
     *
     * @param index           The index of the product to update.
     * @param selectedProduct The updated product to replace the existing product at the given index.
     */
    public void updateProduct(int index, Product selectedProduct) {
        this.allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes the given Part from the inventory.
     *
     * @param selectedPart The Part to be deleted.
     * @return true if the Part was found and removed from the inventory, false otherwise.
     */
    public boolean deletePart(Part selectedPart) {
        return this.allParts.remove(selectedPart);
    }

    /**
     * Removes the specified product from the inventory.
     *
     * @param selectedProduct the product to remove
     * @return true if the product was successfully removed, false otherwise
     */
    public boolean deleteProduct(Product selectedProduct) {
        return this.allProducts.remove(selectedProduct);
    }

    /**
     * Returns an ObservableList containing all parts in the inventory.
     *
     * @return the ObservableList of all parts in the inventory
     */
    public ObservableList<Part> getAllParts() {
        return this.allParts;
    }

    /**
     * Returns an observable list of all products in the inventory.
     *
     * @return an observable list of all products in the inventory.
     */
    public ObservableList<Product> getAllProducts() {
        return this.allProducts;
    }
}
