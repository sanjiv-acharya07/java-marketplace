import java.io.*;
import java.util.ArrayList;

public class Customer {
    private static String customerName;
    private static ArrayList<String> shoppingCart = new ArrayList<>();

    public Customer(String customerName) {
        this.customerName = customerName;
    }

    public static ArrayList<String> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ArrayList<String> cart) {
        this.shoppingCart = cart;
    }

    public static void transactionHistory() {
        String name = customerName;
        String fileName = name + "TransactionHistory.txt";
        File f = new File(fileName);
        File newf = new File(name + "ShoppingCart.txt");

        try {
            if (f.createNewFile()) {
                FileReader fr = new FileReader(newf);
                BufferedReader bfr = new BufferedReader(fr);
                String line = bfr.readLine();
                ArrayList<String> data = new ArrayList<>();
                while (line != null) {
                    if (!line.contains("Shopping")) {
                        data.add(line);
                    }
                    line = bfr.readLine();
                }
                FileOutputStream fos = new FileOutputStream(f, true);

                PrintWriter pw = new PrintWriter(fos);
                pw.printf("%s's Transaction History\n", name);
                for (int i = 0; i < data.size(); i++) {
                    pw.println(data.get(i));
                }
                pw.flush();
                pw.close();

            } else {
                FileReader fr = new FileReader(newf);
                BufferedReader bfr = new BufferedReader(fr);
                String line = bfr.readLine();
                ArrayList<String> data = new ArrayList<>();
                while (line != null) {
                    if (!line.contains("Shopping")) {
                        data.add(line);
                    }
                    line = bfr.readLine();
                }
                FileOutputStream fos = new FileOutputStream(f, true);
                PrintWriter pw = new PrintWriter(fos);
                for (int i = 0; i < data.size(); i++) {
                    pw.println(data.get(i));
                }
                pw.flush();
                pw.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void viewTransactionHistory() throws FileNotFoundException {
        try {
            File f = new File(customerName + "TransactionHistory.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                System.out.println(line);
                line = bfr.readLine();
            }
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void purchase(ArrayList<Seller> sellers, Product product, int amount) throws IllegalPurchaseException {
        // when a purchase happens
        // its amount decreases

        for (Seller seller : sellers) {
            for (Shop store : seller.getStores()) {
                for (Product item : store.getProducts()) {
                    if (item.getName().equals(product.getName())) {
                        System.out.printf("The prevQuantity: %d\n", item.getQuantity());
                        if (amount > item.getQuantity()) {
                            throw new IllegalPurchaseException("Purchase failed, " +
                                    "too many units are trying to be purchased!");
                        } else {
                            item.setQuantity(item.getQuantity() - amount);
                        }
                    }
                }
            }
        }
    }

    public boolean checkFile() { // checking if the customer's shopping cart had to be created
        try {
            String fileName = customerName + "ShoppingCart.txt";
            File file = new File(fileName);
            if (!file.createNewFile()) {
                FileReader fr = new FileReader(file);
                BufferedReader bfr = new BufferedReader(fr);
                String line = bfr.readLine();

                if (line == null) {
                    return false;
                } else {
                    return true;
                }

            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void replenishCart() { // when a shopping cart has items and the program has been ended, putting those
        // items back in the customer shopping cart field
        try {
            ArrayList<String> cart = new ArrayList<>();
            String fileName = customerName + "ShoppingCart.txt";
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);

            String line = bfr.readLine();
            while (line != null) {
                if (!line.contains("Current")) {
                    cart.add(line);
                }
                line = bfr.readLine();
            }
            shoppingCart = cart;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Adds to the shopping cart, product is already there, adds amount, otherwise

    public void clearCart() {
        try {
            String fileName = customerName + "ShoppingCart.txt";
            File file = new File(fileName);
            FileOutputStream fs = new FileOutputStream(file);
            shoppingCart.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUpSalesClearSellerCart(ArrayList<Seller> sellers) {
        for (Seller sell : sellers) {
            double totalSales = sell.getSales();
            String fileName = sell.getSellerName() + "ShoppingCartSeller.txt";

            File f = new File(fileName);

            try {
                FileReader fr = new FileReader(f);

                BufferedReader bfr = new BufferedReader(fr);

                ArrayList<String> sellerCart = new ArrayList<>();

                while (true) {
                    String line = bfr.readLine();
                    if (line == null) {
                        break;
                    }
                    if (!line.contains("TOTAL SALES")) {
                        sellerCart.add(line);
                    }

                }

                FileOutputStream fos = new FileOutputStream(f);

                PrintWriter pw = new PrintWriter(fos);

                for (String s : sellerCart) {
                    boolean doesContain = false;
                    for (String c : shoppingCart) {
                        String itemName = c.substring(0, c.indexOf(','));
                        if (s.contains(itemName)) {
                            doesContain = true;
                            String amountOfItem = c.substring(c.indexOf(':') + 1, c.lastIndexOf(','));

                            amountOfItem = amountOfItem.substring(amountOfItem.indexOf(':') + 1);

                            amountOfItem = amountOfItem.trim();

                            int amountInCart = Integer.parseInt(amountOfItem);

                            String nameInSellerCart = s.substring(s.indexOf(':') + 1, s.indexOf(','));

                            nameInSellerCart = nameInSellerCart.trim();

                            String priceInCustCart = c.substring(c.lastIndexOf(':') + 1);

                            priceInCustCart = priceInCustCart.trim();

                            double priceInCust = Double.parseDouble(priceInCustCart);

                            String amountOfItemInSeller = s.substring(s.indexOf(':') + 1, s.lastIndexOf(','));

                            amountOfItemInSeller = amountOfItemInSeller.substring(
                                    amountOfItemInSeller.indexOf(':') + 1);

                            amountOfItemInSeller = amountOfItemInSeller.trim();

                            int amountInSellerCart = Integer.parseInt(amountOfItemInSeller);

                            int currentAmount = amountInSellerCart - amountInCart;

                            if (currentAmount >= 1) {
                                String item = String.format("Item: %s, Amount: %d, Price: %.2f", nameInSellerCart,
                                        currentAmount, priceInCust);
                                pw.write(item);
                                pw.println();
                                pw.flush();
                            }

                            totalSales = totalSales + (amountInCart * priceInCust);
                        }
                    }
                    if (!doesContain) {
                        pw.write(s);
                        pw.println();
                        pw.flush();
                    }
                }
                String salesString = String.format("TOTAL SALES: %.2f", totalSales);

                pw.write(salesString);
                pw.println();
                pw.flush();

                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addToShoppingCart(ArrayList<Seller> sellers, Product product, int amount) throws
            IllegalPurchaseException {


        boolean containsProduct = false;

        for (String s : shoppingCart) {
            String itemName = product.getName();

            if (s.contains(itemName)) {
                containsProduct = true;
                int indexOfProduct = shoppingCart.indexOf(s);
                System.out.println(s);
                String[] sStringArray = s.split(":");
                for (String string : sStringArray) {
                    System.out.println(string);
                }
                //String sAmount = sStringArray[2].substring(1 , ',');
                String[] splits = sStringArray[2].split(" ");
                String sAmount = splits[1].substring(0, 1);
                System.out.println(sAmount);
                int prevAmount = Integer.parseInt(sAmount);

                int currentAmount = prevAmount + amount;

                String item = "Item: " + product.getName() + ", Amount: " + currentAmount + ", Price: " +
                        product.getPrice();
                shoppingCart.set(indexOfProduct, item);
            }
        }
        if (!containsProduct) {
            String item = "Item: " + product.getName() + ", Amount: " + amount + ", Price: " +
                    product.getPrice();
            shoppingCart.add(item);
        }


        String fileName = customerName + "ShoppingCart.txt";

        ArrayList<String> prevShoppingCart = new ArrayList<>();

        File f = new File(fileName);

        try {
            if (f.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(f);

                PrintWriter pw = new PrintWriter(fos);

                pw.write("Current items in Shopping Cart:");
                pw.println();
                pw.flush();

                pw.write("Item: " + product.getName());
                pw.printf(", Amount: %d", amount);
                pw.printf(", Price: %.2f", product.getPrice());
                pw.println();
                pw.flush();
            } else {
                FileReader fr = new FileReader(f);

                BufferedReader bfr = new BufferedReader(fr);

                String existingProduct = "";
                while (true) {
                    String line = bfr.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.contains(product.getName())) {
                        existingProduct = line;
                    }
                }

                if (existingProduct.equals("")) {
                    FileOutputStream fos = new FileOutputStream(f, true);

                    PrintWriter pw = new PrintWriter(fos);

                    pw.printf("Item: %s", product.getName());
                    pw.printf(", Amount: %d", amount);

                    pw.printf(", Price: %.2f", product.getPrice());
                    pw.println();
                    pw.flush();
                    pw.close();
                } else {
                    while (true) {
                        String line = bfr.readLine();
                        if (line == null) {
                            break;
                        }
                        prevShoppingCart.add(line);
                    }

                    FileOutputStream fos = new FileOutputStream(f);

                    PrintWriter pw = new PrintWriter(fos);

                    for (String currentProduct : prevShoppingCart) {
                        if (currentProduct.equals(existingProduct)) {
                            int indexOfAmount = currentProduct.lastIndexOf(':');
                            int prevAmount = Integer.parseInt(currentProduct.substring(indexOfAmount + 1));

                            int currentAmount = prevAmount + amount;

                            String item = String.format("Item: %s, Amount: %d, Price: %.2f", product.getName(),
                                    currentAmount, product.getPrice());
                            pw.print(item);
                            pw.println();
                            pw.flush();

                        } else {
                            pw.write(currentProduct);
                        }
                    }
                    pw.close();

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for (Seller sell : sellers) {
                for (Shop shop : sell.getStores()) {
                    for (Product prod : shop.getProducts()) {
                        if (prod.getName().equals(product.getName())) {
                            String sellerFileName = sell.getSellerName() + "ShoppingCartSeller.txt";

                            File sellerFile = new File(sellerFileName);

                            FileReader sellerFR = new FileReader(sellerFile);

                            BufferedReader reader = new BufferedReader(sellerFR);

                            ArrayList<String> sellerShoppingCart = new ArrayList<>();

                            boolean wasInShoppingCart = false;


                            while (true) {
                                String line = reader.readLine();

                                if (line == null) {
                                    break;
                                }

                                if (!line.contains("TOTAL SALES")) {
                                    sellerShoppingCart.add(line);
                                }

                            }

                            FileOutputStream fileOutputStream = new FileOutputStream(sellerFile);

                            PrintWriter writer = new PrintWriter(fileOutputStream);
                            if (sellerShoppingCart.size() != 0) {

                            }
                            for (String s : sellerShoppingCart) {
                                if (s.contains(product.getName())) {
                                    wasInShoppingCart = true;

                                    String line = s.substring(s.indexOf(':') + 1, s.lastIndexOf(','));

                                    line = line.substring(line.indexOf(':') + 1);

                                    line = line.trim();

                                    int sellerShopAmount = Integer.parseInt(line);

                                    int newAmount = sellerShopAmount + amount;
                                    String item = String.format("Item: %s, Amount: %d, Price: %.2f", product.getName(),
                                            newAmount, product.getPrice());
                                    if (newAmount >= 1) {
                                        writer.write(item);
                                        writer.println();
                                        writer.flush();
                                    }

                                } else {
                                    writer.write(s);
                                    writer.println();
                                    writer.flush();
                                }
                            }
                            if (!wasInShoppingCart) {
                                String item = String.format("Item: %s, Amount: %d, Price: %.2f", product.getName(),
                                        amount, product.getPrice());
                                writer.write(item);
                                writer.println();
                                writer.flush();
                            }
                            String totalSales = String.format("TOTAL SALES: %.2f", sell.getSales());

                            writer.write(totalSales);
                            writer.println();
                            writer.flush();
                            writer.close();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Sets the number of
        for (Seller sell : sellers) {
            for (Shop store : sell.getStores()) {
                for (Product prod : store.getProducts()) {
                    if (prod.getName().equals(product.getName())) {
                        //product.setQuantity(amount);

                        System.out.printf("Previous quantity: %d\n", prod.getQuantity());
                        System.out.println("The amount requested: " + amount);

                        if (prod.getQuantity() >= amount) {
                            prod.setQuantity(prod.getQuantity() - amount);
                        } else {
                            throw new IllegalPurchaseException();
                        }
                    }
                }
            }
        }
    }

    public void removeProductShoppingCart(ArrayList<Seller> sellers, Product product) {
        int index = 0;
        for (String s : shoppingCart) {
            if (s.contains(product.getName())) {
                index = shoppingCart.indexOf(s);
            }
        }
        String itemInCustCart = shoppingCart.get(index);
        String custAmount = itemInCustCart.substring(itemInCustCart.indexOf(':') + 1,
                itemInCustCart.lastIndexOf(','));

        custAmount = custAmount.substring(custAmount.indexOf(':') + 1);

        custAmount = custAmount.trim();

        int custAmountInt = Integer.parseInt(custAmount);

        shoppingCart.remove(index);


        String fileName = customerName + "ShoppingCart.txt";
        File f = new File(fileName);

        ArrayList<String> prevShoppingCart = new ArrayList<>();

        FileReader fr = null;
        BufferedReader bfr = null;
        try {
            fr = new FileReader(f);
            bfr = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String prodRemoved = null;

        while (true) {
            String line;
            try {
                line = bfr.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) {
                break;
            }
            if (!line.contains(product.getName())) {
                prevShoppingCart.add(line);
            } else {
                prodRemoved = line;
            }

        }

        FileOutputStream fos = null;
        PrintWriter pw = null;

        try {
            fos = new FileOutputStream(f);

            pw = new PrintWriter(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String s : prevShoppingCart) {
            pw.write(s);
            pw.println();
            pw.flush();
        }
        pw.close();

        try {
            for (Seller sell : sellers) {
                for (Shop shop : sell.getStores()) {
                    for (Product prod : shop.getProducts()) {
                        if (prod.getName().equals(product.getName())) {
                            String sellerFileName = sell.getSellerName() + "ShoppingCartSeller.txt";

                            File sellerFile = new File(sellerFileName);

                            FileReader sellerFR = new FileReader(sellerFile);

                            BufferedReader reader = new BufferedReader(sellerFR);

                            ArrayList<String> sellerShoppingCart = new ArrayList<>();


                            while (true) {
                                String line = reader.readLine();

                                if (line == null) {
                                    break;
                                }
                                if (!line.contains("TOTAL SALES")) {
                                    sellerShoppingCart.add(line);
                                }

                            }

                            FileOutputStream fileOutputStream = new FileOutputStream(sellerFile);

                            PrintWriter writer = new PrintWriter(fileOutputStream);

                            for (String s : sellerShoppingCart) {
                                if (s.contains(product.getName())) {
                                    String line = s.substring(s.indexOf(':') + 1, s.lastIndexOf(','));

                                    line = line.substring(line.indexOf(':') + 1);

                                    line = line.trim();

                                    int sellerShopAmount = Integer.parseInt(line);

                                    int newAmount = sellerShopAmount - custAmountInt;
                                    String item = String.format("Item: %s, Amount: %d, Price: %.2f", product.getName(),
                                            newAmount, product.getPrice());
                                    if (newAmount >= 1) {
                                        writer.write(item);
                                        writer.println();
                                        writer.flush();
                                    }

                                } else {
                                    writer.write(s);
                                    writer.println();
                                    writer.flush();
                                }
                                String totalSales = String.format("TOTAL SALES: %.2f", sell.getSales());
                                writer.write(totalSales);
                                writer.println();
                                writer.flush();
                                writer.close();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Seller sell : sellers) {
            for (Shop store : sell.getStores()) {
                for (Product prod : store.getProducts()) {
                    if (prod.getName().equals(product.getName())) {
                        prod.setQuantity(prod.getQuantity() + custAmountInt);
                    }
                }
            }
        }
    }
}
