import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        boolean runShop = true;
        ArrayList<Product> productList = new ArrayList<>(); // temporary list of products
        boolean keepSelling = true;
        try {
            ArrayList<Shop> stores = setup.matchProductsToShops();
            ArrayList<Seller> sellers = setup.matchShopsToSellers();
            do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Welcome!");
                String name;
                do {
                    System.out.println("Please enter your name:");
                    name = scanner.nextLine();
                } while (name == null || name.equals(""));
                System.out.println("Are you a");
                System.out.println("1. Customer?");
                System.out.println("2. Seller?");
                int type = scanner.nextInt();
                scanner.nextLine();

                if (type == 1) {
                    boolean returnPatron;
                    Customer customer = new Customer(name);
                    System.out.println("Are you a returning patron?");
                    String returning = scanner.nextLine();
                    if (returning.equalsIgnoreCase("yes")) {
                        returnPatron = true; // true if the customer is a returning customer
                        if (customer.checkFile()) {
                            customer.replenishCart();
                            for (String s : customer.getShoppingCart()) {
                                System.out.println(s);
                            }
                        }
                    } else {
                        returnPatron = false; // this is the first time that the customer is using the marketplace
                    }

                    try {
                        ArrayList<String> marketPlace = FileClass.printMarketPlace();
                        System.out.println("                    ");
                        System.out.println("Welcome to the Marketplace!");
                        System.out.println("                    ");
                        System.out.println("Our list of products:");
                        printMarketplace(sellers);
                        System.out.println("                ");
                        boolean closeStore = true;
                        do {
                            System.out.println("MENU");
                            System.out.println("1. Handle shopping cart");
                            System.out.println("2. Check an item's description");
                            System.out.println("3. Search");
                            System.out.println("4. Print transaction history");
                            System.out.println("5. Display entire marketplace");
                            System.out.println("6. Return to LOGIN screen");
                            System.out.println("7. EXIT");

                            int choice = scanner.nextInt();
                            scanner.nextLine();

                            if (choice == 1) {
                                System.out.println("MENU");
                                System.out.println("1. Add an item to cart");
                                System.out.println("2. Remove an item from cart");
                                System.out.println("3. Print the shopping cart");
                                System.out.println("4. Checkout - To Purchase all items in cart");
                                int cartChoice = scanner.nextInt();
                                scanner.nextLine();
                                if (cartChoice == 1) {
                                    printMarketplace(sellers);
                                    System.out.println("            ");
                                    System.out.println("Please enter the number of the item you need to buy");
                                    int itemCode = scanner.nextInt();
                                    scanner.nextLine();

                                    Product realChoice = null;

                                    for (Seller seller : sellers) {
                                        for (Shop shop : seller.getStores()) {
                                            for (Product product : shop.getProducts()) {
                                                if (product.getName().equals(marketPlace.get(itemCode - 1).
                                                        split(",")[1])) {
                                                    System.out.println(product.getName());
                                                    System.out.println(product.getQuantity());
                                                    realChoice = product;
                                                }
                                            }
                                        }
                                    }

                                    int amount;
                                    do {
                                        System.out.println("Please enter the amount of the item you need");
                                        amount = scanner.nextInt();
                                        scanner.nextLine();
                                    } while (amount > Objects.requireNonNull(realChoice).getQuantity());

                                    for (Seller sel : sellers) { // going through every seller
                                        for (Shop s : sel.getStores()) {
                                            if (s.getName().equals(marketPlace.get(itemCode - 1).split(",")[0])) {
                                                // this checks if the shop is the same shop as the one the product in
                                                for (Product p : s.getProducts()) {
                                                    if (p.getName().equals(marketPlace.get(itemCode - 1).
                                                            split(",")[1])) {
                                                        // checking if the product is the one we need to add to cart
                                                        customer.addToShoppingCart(sellers, p, amount);
                                                        setup.updateDetailedProduct(sellers);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                } else if (cartChoice == 2) {
                                    if (customer.getShoppingCart() == null || customer.getShoppingCart().isEmpty()) {
                                        System.out.println("Sorry, your shopping cart is empty.");
                                    } else {
                                        for (String s : customer.getShoppingCart()) {
                                            System.out.println(s);
                                        }

                                        System.out.println("Please enter the name of the item you need to " +
                                                "remove from the cart");
                                        String item = scanner.nextLine();

                                        for (Seller sel : sellers) {
                                            for (Shop shop : sel.getStores()) {
                                                for (Product p : shop.getProducts()) {
                                                    if (p.getName().toLowerCase().contains(item.toLowerCase())) {
                                                        customer.removeProductShoppingCart(sellers, p);
                                                        setup.updateDetailedProduct(sellers);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if (cartChoice == 3) {
                                    ArrayList<String> cart = customer.getShoppingCart();
                                    if (cart.equals(null) || cart.isEmpty()) {
                                        System.out.println("Cart is empty");
                                    } else {
                                        for (String s : cart) {
                                            System.out.println(s);
                                        }
                                    }
                                } else if (cartChoice == 4) {
                                    double total = 0.00;
                                    for (String s : customer.getShoppingCart()) {
                                        System.out.println(s);
                                    }
                                    customer.transactionHistory();
                                    customer.addUpSalesClearSellerCart(sellers);
                                    customer.clearCart();
                                }

                                System.out.println("Would you like to exit the marketplace?");
                                String exit = scanner.nextLine();
                                if (exit.equalsIgnoreCase("yes")) {
                                    closeStore = false;
                                    runShop = false;
                                }
                            } else if (choice == 2) {
                                System.out.println("Please enter the name of the item you need details of");
                                String itemName = scanner.nextLine();

                                ArrayList<Product> resultProducts = FileClass.productDetails(sellers, itemName);
                                if (resultProducts.isEmpty() || resultProducts == null) {
                                    continue;
                                } else {
                                    System.out.println("Would you like to purchase an item?");
                                    System.out.println("1. Yes");
                                    System.out.println("2. No");
                                    int purchaseItem = scanner.nextInt();
                                    scanner.nextLine();
                                    if (purchaseItem == 1) {
                                        int counter = 1;
                                        for (Product resultProduct : resultProducts) {
                                            System.out.printf("Item %d: Name - %s, Available units - %d, Price - %.2f\n",
                                                    counter, resultProduct.getName(),
                                                    resultProduct.getQuantity(), resultProduct.getPrice());
                                            counter++;
                                        }
                                        System.out.println("        ");
                                        System.out.println("Which product would you like to buy?");
                                        int productChoice = scanner.nextInt();
                                        scanner.nextLine();
                                        Product purchaseProduct = resultProducts.get(productChoice - 1);
                                        System.out.println(purchaseProduct.getName());

                                        System.out.println("How much of this item would you like?");
                                        int amount = scanner.nextInt();
                                        scanner.nextLine();
                                        try {
                                            if (customer.getShoppingCart().isEmpty()) {
                                                for (Seller seller : sellers) {
                                                    for (Shop store : seller.getStores()) {
                                                        for (Product item : store.getProducts()) {
                                                            if (item.equals(purchaseProduct)) {
                                                                if (item.getQuantity() >= amount) {
                                                                    customer.addToShoppingCart(sellers, item, amount);
                                                                    customer.transactionHistory();
                                                                    customer.addUpSalesClearSellerCart(sellers);
                                                                    customer.clearCart();
                                                                    setup.updateDetailedProduct(sellers);
                                                                    System.out.println("This purchase is successful!");
                                                                } else {
                                                                    throw new IllegalPurchaseException("Purchase failed, "
                                                                            + "too many units were " +
                                                                            "trying to be purchased!");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                System.out.println("Would you like to purchase the other items " +
                                                        "in your cart as well?");
                                                System.out.println("1. Yes");
                                                System.out.println("2. No");
                                                int otherChoice = scanner.nextInt();
                                                scanner.nextLine();

                                                if (otherChoice == 1) {
                                                    for (Seller seller : sellers) {
                                                        for (Shop store : seller.getStores()) {
                                                            for (Product item : store.getProducts()) {
                                                                if (item.equals(purchaseProduct)) {
                                                                    if (item.getQuantity() >= amount) {
                                                                        customer.addToShoppingCart(sellers, item, amount);
                                                                        customer.transactionHistory();
                                                                        customer.addUpSalesClearSellerCart(sellers);
                                                                        customer.clearCart();
                                                                        setup.updateDetailedProduct(sellers);
                                                                        System.out.println("This purchase is successful!");
                                                                    } else {
                                                                        throw new IllegalPurchaseException("Purchase failed, "
                                                                                + "too many units were " +
                                                                                "trying to be purchased!");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if (otherChoice == 2) {
                                                    for (Seller seller : sellers) {
                                                        for (Shop store : seller.getStores()) {
                                                            for (Product item : store.getProducts()) {
                                                                if (item.equals(purchaseProduct)) {
                                                                    if (item.getQuantity() >= amount) {
                                                                        customer.addUpSalesClearSellerCart(sellers);
                                                                        customer.clearCart();
                                                                        customer.addToShoppingCart(sellers, item,
                                                                                amount);
                                                                        customer.transactionHistory();
                                                                        customer.addUpSalesClearSellerCart(sellers);
                                                                        customer.clearCart();
                                                                        setup.updateDetailedProduct(sellers);
                                                                        System.out.println("This purchase is " +
                                                                                "successful!");
                                                                    } else {
                                                                        throw new IllegalPurchaseException("Purchase "
                                                                                + "failed, " + "too many units were " +
                                                                                "trying to be purchased!");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                System.out.println("Would you like to exit the marketplace?");
                                String exit = scanner.nextLine();
                                if (exit.equalsIgnoreCase("yes")) {
                                    closeStore = false;
                                    runShop = false;
                                }
                            } else if (choice == 3) {
                                System.out.println("MENU");
                                System.out.println("1. Search for a product");
                                System.out.println("2. Sort through products");
                                int searchChoice = scanner.nextInt();
                                scanner.nextLine();
                                if (searchChoice == 1) {
                                    System.out.println("Enter your search: ");
                                    String searchQuery = scanner.nextLine();
                                    ArrayList<Product> results = FileClass.searchProds(sellers, searchQuery);

                                    System.out.println("1. Would you like to purchase an item?");
                                    System.out.println("2. Would you like to add an item to cart?");
                                    System.out.println("3. Exit");
                                    int buyChoice = scanner.nextInt();
                                    scanner.nextLine();
                                    if (buyChoice == 1) {
                                        System.out.println("Which item would you like to purchase?");
                                        int buySelection = scanner.nextInt();
                                        scanner.nextLine();
                                        Product choiceName = results.get(buySelection - 1);

                                        System.out.println("How much of this item would you like?");
                                        int amount = scanner.nextInt();
                                        scanner.nextLine();
                                        try {
                                            for (Seller seller : sellers) {
                                                for (Shop store : seller.getStores()) {
                                                    for (Product item : store.getProducts()) {
                                                        if (item.equals(choiceName)) {
                                                            if (item.getQuantity() >= amount) {
                                                                customer.addToShoppingCart(sellers, item, amount);
                                                                customer.transactionHistory();
                                                                customer.addUpSalesClearSellerCart(sellers);
                                                                customer.clearCart();
                                                                setup.updateDetailedProduct(sellers);
                                                                System.out.println("This purchase is successful!");
                                                            } else {
                                                                throw new IllegalPurchaseException("Purchase failed, "
                                                                        + "too many units were " +
                                                                        "trying to be purchased!");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                            e.printStackTrace();
                                        }
                                    } else if (buyChoice == 2) {
                                        System.out.println("Which item would you like to add to cart?");
                                        int buySelection = scanner.nextInt();
                                        scanner.nextLine();
                                        Product choiceName = results.get(buySelection - 1);

                                        System.out.println("How much of this item would you like?");
                                        int amount = scanner.nextInt();
                                        scanner.nextLine();

                                        customer.addToShoppingCart(sellers, choiceName, amount);
                                    } else if (buyChoice == 3) {
                                    }
                                } else if (searchChoice == 2) {
                                    System.out.println("MENU");
                                    System.out.println("1. Sort by quantity");
                                    System.out.println("2. Sort by price");
                                    int sortChoice = scanner.nextInt();
                                    scanner.nextLine();
                                    if (sortChoice == 1) {
                                        System.out.println("1. Sort by quantity in ascending order");
                                        System.out.println("2. Sort by quantity in descending order");
                                        int sortOrder = scanner.nextInt();
                                        scanner.nextLine();
                                        if (sortOrder == 1) {
                                            FileClass.sortAscQuantity();
                                        } else {
                                            FileClass.sortDescQuantity();
                                        }
                                    } else {
                                        System.out.println("1. Sort by price in ascending order");
                                        System.out.println("2. Sort by price in descending order");
                                        int sortOrder = scanner.nextInt();
                                        scanner.nextLine();
                                        if (sortOrder == 1) {
                                            FileClass.sortAscPrice();
                                        } else {
                                            FileClass.sortDescPrice();
                                        }
                                    }
                                }
                            } else if (choice == 4) { // transaction history
                                customer.viewTransactionHistory();
                            } else if (choice == 5) {
                                printMarketplace(sellers);
                                System.out.println("                ");
                            } else if (choice == 6) {
                                closeStore = false;
                            } else if (choice == 7) {
                                runShop = false;
                                closeStore = false;
                            }

                        } while (closeStore);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (type == 2) {
                    Seller seller = new Seller(name);

                    System.out.printf("Welcome to the Seller's Homescreen, %s!\n", seller.getSellerName());
                    do {
                        System.out.println("MENU");
                        System.out.println("1. Create product");
                        System.out.println("2. Edit product");
                        System.out.println("3. Delete product");
                        System.out.println("4. View sales");
                        System.out.println("5. View Shopping Carts");
                        System.out.println("6. Export product details file");
                        System.out.println("7. Print entire marketplace");
                        System.out.println("8. Return to LOGIN screen");
                        System.out.println("9. EXIT");
                        int option = scanner.nextInt();
                        scanner.nextLine();

                        if (option == 1) {
                            System.out.println("Would you like to enter the updated product details " +
                                    "through a CSV file?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            int importChoice = scanner.nextInt();
                            scanner.nextLine();

                            if (importChoice == 1) {
                                System.out.println("Please enter the filename for the product(s) you would like to add");
                                String productFileName = scanner.nextLine();
                                seller.readProductFile(sellers, productFileName);
                                setup.updateDetailedProduct(sellers);
                            } else if (importChoice == 2) {
                                System.out.println("Enter the store which product is in: ");
                                String store = scanner.nextLine();
                                System.out.println("Enter product name: ");
                                String prodName = scanner.nextLine();
                                System.out.println("Enter product description: ");
                                String desc = scanner.nextLine();
                                System.out.println("Enter product quantity: ");
                                int qty = scanner.nextInt();
                                System.out.println("Enter product price: ");
                                double price = scanner.nextDouble();
                                Product newProduct = new Product(prodName, desc, qty, price, store);


                                for (Seller sel : sellers) {
                                    for (Shop shop : sel.getStores()) {
                                        if (shop.getName().equals(newProduct.getStore())) {
                                            ArrayList<Product> tempProducts = shop.getProducts();
                                            tempProducts.add(newProduct);
                                            shop.setProducts(tempProducts);
                                        }
                                    }
                                }

                                setup.updateDetailedProduct(sellers);
                            }
                        } else if (option == 2) {
                            System.out.println("Would you like to enter the updated product details " +
                                    "through a CSV file?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            int importChoice = scanner.nextInt();
                            scanner.nextLine();
                            if (importChoice == 1) {
                                System.out.println("Enter the filename of the old product details " +
                                        "(the ones which need to be changed)");
                                String oldFileName = scanner.nextLine();
                                System.out.println("Enter the filename of the new product details " +
                                        "(the details which those of the old products will change to)");
                                String newFileName = scanner.nextLine();

                                seller.editProductsThroughFile(sellers, oldFileName, newFileName);
                                setup.updateDetailedProduct(sellers);
                            } else if (importChoice == 2) {
                                System.out.println("Enter the store which contains product: ");
                                String store = scanner.nextLine();
                                System.out.println("Enter current product name: ");
                                String oldName = scanner.nextLine();
                                System.out.println("Enter updated product name: ");
                                String newName = scanner.nextLine();
                                System.out.println("Enter updated product description: ");
                                String desc = scanner.nextLine();
                                System.out.println("Enter updated product quantity: ");
                                int qty = scanner.nextInt();
                                System.out.println("Enter updated product price: ");
                                double price = scanner.nextDouble();

                                Product oldProduct = new Product();
                                Product newProduct = new Product(newName, desc, qty, price, store);

                                for (Seller sel : sellers) {
                                    for (Shop shop : sel.getStores()) {
                                        for (Product item : shop.getProducts()) {
                                            if (item.getName().equals(oldName)) {
                                                oldProduct = item;
                                            }
                                        }
                                    }
                                }

                                Shop tempShop = new Shop();
                                ArrayList<Product> temp = new ArrayList<>();

                                for (Seller sel : sellers) {
                                    for (Shop shop : sel.getStores()) {
                                        if (shop.getName().equals(store)) {
                                            tempShop = shop;
                                            temp = shop.getProducts();
                                        }
                                    }
                                }

                                int tempIndex = temp.indexOf(oldProduct);
                                temp.set(tempIndex, newProduct);

                                for (Seller sel : sellers) {
                                    for (Shop shop : sel.getStores()) {
                                        if (shop.equals(tempShop)) {
                                            shop.setProducts(temp);
                                        }
                                    }
                                }

                                setup.updateDetailedProduct(sellers);
                            }
                        } else if (option == 3) {
                            System.out.println("Would you like to enter the updated product details " +
                                    "through a CSV file?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            int importChoice = scanner.nextInt();
                            scanner.nextLine();

                            if (importChoice == 1) {
                                System.out.println("Enter the filename of the products which need to be deleted");
                                String filename = scanner.nextLine();
                                seller.deleteProductFile(sellers, filename);
                                setup.updateDetailedProduct(sellers);
                            } else if (importChoice == 2) {
                                System.out.println("Enter product to be deleted: ");
                                String delProduct = scanner.nextLine();
                                System.out.println("Enter the store in which the product is in: ");
                                String store = scanner.nextLine();
                                ArrayList<Product> temp = null;
                                for (Seller sel : sellers) {
                                    for (Shop shop : sel.getStores()) {
                                        if (shop.getName().equals(store)) {
                                            temp = shop.getProducts();
                                        }
                                    }
                                }
                                Product tempProduct = new Product();

                                for (Seller sel : sellers) {
                                    for (Shop shop : sel.getStores()) {
                                        if (shop.getName().equals(store)) {
                                            for (Product product : shop.getProducts()) {
                                                if (product.getName().equals(name)) {
                                                    tempProduct = product;
                                                }
                                            }
                                        }
                                    }
                                }
                                assert temp != null;
                                temp.remove(tempProduct);
                                for (Seller sel : sellers) {
                                    for (Shop shop : sel.getStores()) {
                                        if (shop.getName().equals(store)) {
                                            shop.setProducts(temp);
                                        }
                                    }
                                }
                                setup.updateDetailedProduct(sellers);
                            }
                        } else if (option == 4) {
                            for (Seller sell : sellers) {
                                String currentSellerName = name;
                                if (sell.getSellerName().equals(name)) {
                                    double sales = sell.getSales();

                                    System.out.printf("Your Total Sales So Far: %.2f\n", sales);
                                }
                            }

                        } else if (option == 5) {
                            for (Seller sell : sellers) {
                                if (name.equals(sell.getSellerName())) {
                                    System.out.println("All products in customer shopping carts:");
                                    sell.printSellerShoppingCart();
                                }
                            }

                        } else if (option == 6) {
                            seller.exportProductFile(sellers);
                        } else if (option == 7) {
                            printMarketplace(sellers);
                        } else if (option == 8) {
                            keepSelling = false;
                        } else if (option == 9) {
                            runShop = false;
                            keepSelling = false;
                        } else {
                            System.out.println("Invalid input");
                        }
                    } while (keepSelling);
                }
            } while (runShop);
            System.out.println("Thanks for using our marketplace!");
            setup.updateDetailedProduct(sellers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printMarketplace(ArrayList<Seller> sellers) {
        int counter = 1;
        for (Seller seller : sellers) {
            System.out.println("SELLER: " + seller.getSellerName());
            for (Shop shop : seller.getStores()) {
                System.out.println("STORE: " + shop.getName());
                for (Product product : shop.getProducts()) {
                    String temp = String.format("Item %d: %s, Price: %.2f", counter,
                            product.getName(), product.getPrice());
                    System.out.println(temp);
                    counter++;
                }
            }
        }
    }
}
