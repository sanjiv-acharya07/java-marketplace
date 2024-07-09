public class Product {
    private String name;

    private String store;

    private String description;

    private int quantity;

    private double price;

    private int sold;

    public Product(String name, String description, int quantity, double price, String store) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.store = store;
        this.sold = 0;
    }

    public Product() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //Decrease the amount of available product after purchase
    public void decreaseQuantity(int amountPurchases) {
        boolean availability = canPurchase(amountPurchases, false);

        if (!availability) {
            canPurchase(amountPurchases, true);
            return;
        } else {
            this.quantity = quantity - amountPurchases;
        }
    }

    //Checks if the product can be purchased.
    public boolean canPurchase(int amountPurchased, boolean print) {
        int difference = quantity - amountPurchased;

        if (difference < 0) {
            if (print) {
                System.out.println("Can't Purchase this product, not enough in stock!");
            }


            String word;
            String plural;

            if (quantity == 1) {
                word = "is";
                ;
                plural = "s";
            } else {
                word = "are";
                plural = "";
            }

            if (print) {
                System.out.printf("There %s only %d %s%S left!", word, quantity, name, plural);
            }
            return false;
        } else {
            if (print) {
                System.out.println("The product is available in the requested amount!");
            }
            return true;
        }

    }

    public double revenue() { // returns the total revenue for the product
        return sold * price;
    }

    @Override
    public String toString() {
        return String.format("-%s Description: %s,Store: %s,Amount: %s,Price: %2f.2", name
                , description, store, quantity, price);
    }
}
