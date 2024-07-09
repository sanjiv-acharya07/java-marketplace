import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileClass {

    public static ArrayList<Product> productDetails(ArrayList<Seller> sellers, String search) throws IOException { // takes the product name as input
        ArrayList<Product> results = new ArrayList<>();
        try {
            for (Seller sel : sellers) {
                for (Shop store : sel.getStores()) {
                    for (Product product : store.getProducts()) {
                        if (product.getName().equalsIgnoreCase(search) || product.getName().toLowerCase()
                                .contains(search)) {
                            results.add(product);
                        }
                    }
                }
            }

            if (results == null || results.isEmpty()) {
                System.out.println("Sorry, there are no results for your search!");
            } else {
                System.out.println("Here are the results:");
                int counter = 1;
                for (Product item : results) {
                    System.out.printf("Item %d: %s, Description: %s\n", counter, item.getName(), item.getDescription());
                    counter++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static ArrayList<String> printMarketPlace() throws IOException {
        ArrayList<String> cart = new ArrayList<>();
        /*
        System.out.println("Welcome to the Marketplace!");
        System.out.println("                    ");
        System.out.println("Our list of products:");

         */
        File marketFile = new File("detailedProduct.txt");
        FileReader marketfr = new FileReader(marketFile);
        BufferedReader bfrMarket = new BufferedReader(marketfr);

        ArrayList<String> marketPlace = new ArrayList<>();
        String line = bfrMarket.readLine();
        while (line != null) {
            marketPlace.add(line);
            line = bfrMarket.readLine();
        }

        /*
        for (int i = 0; i < marketPlace.size(); i++) {
            String[] temp = marketPlace.get(i).split(";");
            System.out.printf("Item %d - ", i+1);
            System.out.print(temp[1] + " ");
            System.out.print("(COST: " + temp[2] + ")" + " - ");
            System.out.println(temp[0]);
        }
        System.out.println("               ");

         */
        return marketPlace;
    }

    public static ArrayList<Product> searchProds(ArrayList<Seller> sellers, String query) throws FileNotFoundException {
        ArrayList<Product> newArray = new ArrayList<>();

        for (Seller seller : sellers) {
            for (Shop store : seller.getStores()) {
                for (Product product : store.getProducts()) {
                    if (product.getName().equalsIgnoreCase(query)) {
                        newArray.add(product);
                    } else if (product.getName().toLowerCase().contains(query)) {
                        newArray.add(product);
                    }
                }
            }
        }

        int counter = 0;
        for (Product item : newArray) {
            counter++;
            System.out.printf("Item %d: %s, Price: $%.2f, Details: %s, Quantity: %d\n",
                    counter, item.getName(), item.getPrice(), item.getDescription(), item.getQuantity());
        }

        return newArray;
    }

    public static String[] sortAscQuantity() {
        ArrayList<String> newArrayList = new ArrayList<>();
        try {
            File f = new File("detailedProduct.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<Integer> integerArrayList = new ArrayList<>();
            while (line != null) {
                String[] split = line.split(",");
                integerArrayList.add(Integer.valueOf(split[4]));
                line = bfr.readLine();
            }
            int[] intArray = new int[integerArrayList.size()];
            for (int i = 0; i < integerArrayList.size(); i++) {
                intArray[i] = integerArrayList.get(i);
            }
            Arrays.sort(intArray);
            String[] newStringArray = new String[intArray.length];
            File newf = new File("detailedProduct.txt");
            FileReader newfr = new FileReader(newf);
            BufferedReader newbfr = new BufferedReader(newfr);
            String newLine = newbfr.readLine();
            int i = 0;
            String[] sortedArray = new String[newStringArray.length];
            while (newLine != null) {
                newStringArray[i] = newLine;
                newLine = newbfr.readLine();
                i++;

            }
            for (int j = 0; j < intArray.length; j++) {
                for (int k = 0; k < newStringArray.length; k++) {
                    String[] split = newStringArray[k].split(",");
                    if (intArray[j] == Integer.parseInt(split[4]) && !Arrays.toString(sortedArray).contains(newStringArray[k])) {
                        sortedArray[j] = newStringArray[k];

                    }
                }
            }
            for (int o = 0; o < sortedArray.length; o++) {
                String[] split = sortedArray[o].split(",");
                System.out.printf("Item: %s, Price: %s$, Quantity: %s\n", split[1], split[2], split[4]);
            }
            System.out.println("                ");
            return sortedArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String[] sortDescQuantity() {
        ArrayList<String> newArrayList = new ArrayList<>();
        try {
            File f = new File("detailedProduct.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<Integer> integerArrayList = new ArrayList<>();
            while (line != null) {
                String[] split = line.split(",");
                integerArrayList.add(Integer.valueOf(split[4]));
                line = bfr.readLine();
            }
            int[] intArray = new int[integerArrayList.size()];
            for (int i = 0; i < integerArrayList.size(); i++) {
                intArray[i] = integerArrayList.get(i);
            }
            Arrays.sort(intArray);
            String[] newStringArray = new String[intArray.length];
            File newf = new File("detailedProduct.txt");
            FileReader newfr = new FileReader(newf);
            BufferedReader newbfr = new BufferedReader(newfr);
            String newLine = newbfr.readLine();
            int i = 0;
            String[] sortedArray = new String[newStringArray.length];
            while (newLine != null) {
                newStringArray[i] = newLine;
                newLine = newbfr.readLine();
                i++;

            }
            int l = 0;
            for (int j = intArray.length; j >= 1; j--) {
                for (int k = 0; k < newStringArray.length; k++) {
                    String[] split = newStringArray[k].split(",");
                    if (intArray[j - 1] == Integer.parseInt(split[4]) && !Arrays.toString(sortedArray).contains(newStringArray[k])) {
                        sortedArray[l] = newStringArray[k];

                    }
                }
                l++;
            }

            for (int o = 0; o < sortedArray.length; o++) {
                String[] split = sortedArray[o].split(",");
                System.out.printf("Item: %s, Price: %s$, Quantity: %s\n", split[1], split[2], split[4]);
            }
            System.out.println("                ");
            return sortedArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String[] sortAscPrice() {
        ArrayList<String> newArrayList = new ArrayList<>();
        try {
            File f = new File("detailedProduct.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<Double> doubleArrayList = new ArrayList<>();
            while (line != null) {
                String[] split = line.split(",");
                doubleArrayList.add(Double.valueOf(split[2]));
                line = bfr.readLine();
            }
            double[] doubleArray = new double[doubleArrayList.size()];
            for (int i = 0; i < doubleArrayList.size(); i++) {
                doubleArray[i] = doubleArrayList.get(i);
            }
            Arrays.sort(doubleArray);
            String[] newStringArray = new String[doubleArray.length];
            File newf = new File("detailedProduct.txt");
            FileReader newfr = new FileReader(newf);
            BufferedReader newbfr = new BufferedReader(newfr);
            String newLine = newbfr.readLine();
            int i = 0;
            String[] sortedArray = new String[newStringArray.length];
            while (newLine != null) {
                newStringArray[i] = newLine;
                newLine = newbfr.readLine();
                i++;

            }
            for (int j = 0; j < doubleArray.length; j++) {
                for (int k = 0; k < newStringArray.length; k++) {
                    String[] split = newStringArray[k].split(",");
                    if (doubleArray[j] == Double.parseDouble(split[2]) && !Arrays.toString(sortedArray).contains(newStringArray[k])) {
                        sortedArray[j] = newStringArray[k];

                    }
                }
            }
            for (int o = 0; o < sortedArray.length; o++) {
                String[] split = sortedArray[o].split(",");
                System.out.printf("Item: %s, Price: $%s, Quantity: %s\n", split[1], split[2], split[4]);
            }
            System.out.println("                ");
            return sortedArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] sortDescPrice() {
        ArrayList<String> newArrayList = new ArrayList<>();
        try {
            File f = new File("detailedProduct.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<Double> doubleArrayList = new ArrayList<>();
            while (line != null) {
                String[] split = line.split(",");
                doubleArrayList.add(Double.valueOf(split[2]));
                line = bfr.readLine();
            }
            double[] doubleArray = new double[doubleArrayList.size()];
            for (int i = 0; i < doubleArrayList.size(); i++) {
                doubleArray[i] = doubleArrayList.get(i);
            }
            Arrays.sort(doubleArray);
            String[] newStringArray = new String[doubleArray.length];
            File newf = new File("detailedProduct.txt");
            FileReader newfr = new FileReader(newf);
            BufferedReader newbfr = new BufferedReader(newfr);
            String newLine = newbfr.readLine();
            int i = 0;
            String[] sortedArray = new String[newStringArray.length];
            while (newLine != null) {
                newStringArray[i] = newLine;
                newLine = newbfr.readLine();
                i++;

            }
            int l = 0;
            for (int j = doubleArray.length; j >= 1; j--) {
                for (int k = 0; k < newStringArray.length; k++) {
                    String[] split = newStringArray[k].split(",");
                    if (doubleArray[j - 1] == Double.parseDouble(split[2]) && !Arrays.toString(sortedArray).contains(newStringArray[k])) {
                        sortedArray[l] = newStringArray[k];

                    }
                }
                l++;
            }
            for (int o = 0; o < sortedArray.length; o++) {
                String[] split = sortedArray[o].split(",");
                System.out.printf("Item: %s, Price: $%s, Quantity: %s\n", split[1], split[2], split[4]);
            }
            System.out.println("                ");
            return sortedArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
