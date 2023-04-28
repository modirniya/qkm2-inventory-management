package edu.wgu.qkm2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Parham Modirniya
 */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Adds a new part to the inventory.
     *
     * @param newPart the part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds the given Product to the list of all products.
     *
     * @param newProduct the Product object to be added to the inventory
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Searches for a Part object in the inventory by its ID.
     *
     * @param partId the ID of the Part to look up
     * @return the Part object with the specified ID, or null if not found
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts)
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
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts)
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
    public static ObservableList<Part> lookupPart(String partName) {
        if (partName.isBlank()) return allParts;
        ObservableList<Part> resultList = FXCollections.observableArrayList();
        for (Part part : allParts)
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
    public static ObservableList<Product> lookupProduct(String productName) {
        if (productName.isBlank()) return allProducts;
        ObservableList<Product> resultList = FXCollections.observableArrayList();
        for (Product product : allProducts)
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
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates the product at the given index with the given selectedProduct.
     *
     * @param index           The index of the product to update.
     * @param selectedProduct The updated product to replace the existing product at the given index.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes the given Part from the inventory.
     *
     * @param selectedPart The Part to be deleted.
     * @return true if the Part was found and removed from the inventory, false otherwise.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Removes the specified product from the inventory.
     *
     * @param selectedProduct the product to remove
     * @return true if the product was successfully removed, false otherwise
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Returns an ObservableList containing all parts in the inventory.
     *
     * @return the ObservableList of all parts in the inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns an observable list of all products in the inventory.
     *
     * @return an observable list of all products in the inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
