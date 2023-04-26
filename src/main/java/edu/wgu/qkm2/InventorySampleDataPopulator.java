package edu.wgu.qkm2;

import edu.wgu.qkm2.data.*;

public class InventorySampleDataPopulator {
    public static void addSampleData() {
        Inventory inventory = Inventory.getInstance();

        // An array containing all the parts
        Part[] parts = {
                new InHouse(1, "Brake Disc", 69.99, 12, 2, 20, 101),
                new InHouse(2, "Suspension Spring", 89.99, 10, 1, 15, 102),
                new InHouse(3, "Air Filter", 19.99, 20, 10, 30, 103),
                new InHouse(4, "Fuel Pump", 149.99, 6, 1, 10, 104),
                new InHouse(5, "Alternator", 199.99, 5, 1, 8, 105),
                new Outsourced(6, "Windshield Wiper", 14.99, 50, 20, 80, "WiperCo"),
                new Outsourced(7, "Spark Plug", 4.99, 100, 50, 150, "SparkTech"),
                new Outsourced(8, "Car Battery", 99.99, 20, 10, 30, "PowerCells"),
                new Outsourced(9, "Radiator", 149.99, 8, 2, 12, "CoolingPro"),
                new Outsourced(10, "Tire", 79.99, 16, 8, 24, "TireMaster")
        };

        // Loop through the array and add each part to the inventory
        for (Part part : parts) {
            inventory.addPart(part);
        }

        Product[] products = {
                new Product(1, "Brake System", 299.99, 10, 5, 15),
                new Product(2, "Suspension Kit", 499.99, 8, 3, 12),
                new Product(3, "Exhaust System", 399.99, 12, 4, 20),
                new Product(4, "Air Filter", 19.99, 30, 10, 50),
                new Product(5, "Fuel Pump", 89.99, 15, 5, 25),
                new Product(6, "Ignition Coil", 59.99, 20, 5, 30),
                new Product(7, "Radiator", 149.99, 10, 3, 15),
                new Product(8, "Alternator", 159.99, 8, 2, 12),
                new Product(9, "Transmission", 999.99, 5, 1, 10),
                new Product(10, "Battery", 129.99, 25, 10, 40),
        };

// Associate parts with products
        // Associate parts with products
        products[0].addAssociatedPart(parts[0]);
        products[0].addAssociatedPart(parts[6]);
        products[1].addAssociatedPart(parts[1]);
        products[1].addAssociatedPart(parts[2]);
        products[2].addAssociatedPart(parts[3]);
        products[2].addAssociatedPart(parts[4]);
        products[3].addAssociatedPart(parts[5]);
        products[4].addAssociatedPart(parts[6]);
        products[4].addAssociatedPart(parts[7]);
        products[5].addAssociatedPart(parts[8]);
        products[6].addAssociatedPart(parts[9]);
        products[7].addAssociatedPart(parts[0]);
        products[7].addAssociatedPart(parts[1]);
        products[8].addAssociatedPart(parts[2]);
        products[8].addAssociatedPart(parts[3]);
        products[9].addAssociatedPart(parts[4]);
        products[9].addAssociatedPart(parts[5]);


// Add products to the inventory
        for (Product product : products) {
            inventory.addProduct(product);
        }

    }
}
