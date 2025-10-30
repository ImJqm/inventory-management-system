//imports Gson library and needed java libraries

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Inventory {
  // Needed File Paths
  // *********************IMPORTANT*************************//
  // PLEASE REPLACE SRC_DIR with the directory your ims is installed in
  private static final String FILE_PATH = "./inventory.json";
  private static final String TMP_FILE_PATH = "/tmp/ims_temp";
  private static final String SRC_DIR = "/home/james/ims/src/";

  // creates arraylist of products
  private ArrayList<Product> listings = new ArrayList<Product>();

  // makes new Gson object in order to read json
  public Gson gson = new Gson();

  // inventory constructor, whenever inventory is constructed, it reads the json
  public Inventory() {
    // sets the file equal to the file path
    File file = new File(FILE_PATH);

    // initialises file;
    this.init();

    // try reading it
    try (Reader reader = new FileReader(file)) {
      // Tells Gson what type to use because it needs to be an arraylist that stores
      // products;
      Type listType = new TypeToken<ArrayList<Product>>() {
      }.getType();
      // sets listings eqal to the deserialized file
      listings = gson.fromJson(reader, listType);
      // if the file is empty, reset the listings
      if (listings == null) {
        listings = new ArrayList<Product>();
      }
    } catch (IOException e) { // in case any any errors when reading the file
      e.printStackTrace();
    }

  }

  // add product method
  public void addProduct(Product prod) {
    // If negative price, then dont add
    if (prod.getPrice() < 0.0) {
      System.out.print("Error: Negative Price");
      return;
    }
    // same with negative quantity
    if (prod.getQuantity() < 0) {
      System.out.print("Error: Negative Quantity");
      return;
    }

    // iterates through products
    for (Product p : this.listings) {
      // if existing product already has the same pid, add combine the quantities
      if (p.getProductID().equals(prod.getProductID())) {
        p.setQuantity(p.getQuantity() + prod.getQuantity());
        System.out.println("Updated quantity of product with pid " + prod.getProductID());
        this.write();
        return;
      }
    }
    // otherwise add the product and write to inventory.json;
    this.listings.add(prod);
    this.write();
    System.out.print("Created product with Product ID: " + prod.getProductID());
  }

  // returns how many products are in the inventory
  public int getAmount() {
    return this.listings.size();
  }

  // returns index of product in the inventory;
  public int getIndexOf(Product p) {
    return this.listings.indexOf(p);
  }

  // rmoves a product given a product id
  public void removeProduct(String productID) {
    int index = -1; // default index
                    // finds new index, if applicable
    for (Product p : this.listings) {
      if (p.getProductID().equals(productID)) {
        index = this.listings.indexOf(p);
        break;
      }
    }

    // removes index if it can
    if (index == -1) {
      System.out.println("Couldn't find product with product ID \"" + productID + "\"");
    } else {
      System.out.println("Removed product with product ID " + "\"" + productID + "\"");
      this.listings.remove(index);
    }
    // writes to inventory.json
    this.write();

  }

  // update quantity method
  public void updateQuantity(String pid, int quantity) {
    // checks for invalid quantity
    if (quantity < 0) {
      System.out.print("Error: Neagtive quantity");
      return;
    }
    // finds product with the correct pid
    for (Product p : this.listings) {
      if (p.getProductID().equals(pid)) {
        // sets quantity
        p.setQuantity(quantity);
      }
    }
    // prints info and writes to file
    System.out.print("Updated quantity of item with Product ID: " + pid + " to " + quantity);
    this.write();
  }

  // search method
  public Product search() {
    // default product id
    String productID = "";

    // trys to run the search.sh script which pulls up an fzf with a custom input
    try {

      // Has to use a process builder in order to let the search.sh script output
      // stuff
      // It may not actually need a process builder, that was from an earlier version
      // TODO: look into this
      ProcessBuilder pb = new ProcessBuilder(SRC_DIR + "search.sh", FILE_PATH);
      pb.inheritIO(); // links process to java process
      Process p = pb.start();

      p.waitFor(); // waits for it to finish

      // search.sh outputs the selected pid to /tmp/ims_temp, this reads it and strips
      // any excess spacing
      productID = Files.readString(Path.of(TMP_FILE_PATH)).strip();
      // Process test = Runtime.getRuntime().exec("/home/james/ims/src/search.sh " +
      // FILE_PATH);
      // test.waitFor();
    } catch (Exception e) { // programs keeps running if there are any errors
      e.printStackTrace();
    }
    // finds and returns the product that has the pid the user selected
    for (Product p : this.listings) {
      if (p.getProductID().equals(productID)) {
        return p;
      }
    }
    return null;

  }

  // prints the products
  public void printProducts(int start, int end) {
    // makes sure that products exist
    if (this.listings.size() > 0) {

      // For each paramter of the products, it is going to find the longest of each
      // parameter, this lets the program know how to space the table
      String lName = this.listings.get(0).getName();
      String lPID = this.listings.get(0).getProductID();
      String lPrice = Double.toString(this.listings.get(0).getPrice());
      String lQuantity = Integer.toString(this.listings.get(0).getQuantity());
      for (Product p : this.listings) {
        if (p.getName().length() > lName.length()) {
          lName = p.getName();
        }
        if (p.getProductID().length() > lPID.length()) {
          lPID = p.getProductID();
        }
        if ((Double.toString(p.getPrice())).length() > lPrice.length()) {
          lPrice = Double.toString(p.getPrice());
        }
        if (Integer.toString(p.getQuantity()).length() > lQuantity.length()) {
          lQuantity = Integer.toString(p.getQuantity());
        }
      }
      // System.out.println(lName);
      // System.out.println(lPID);
      // System.out.println(lPrice);
      // System.out.println(lQuantity);
      // int totalWidth = lName.length() + lPID.length() + lPrice.length() +
      // lQuantity.length();
      //
      // For each of these parameters, name is the spacing in between the actually
      // value and the next one, ex: the space between between name and pid
      // nameTop is the spacing needed for the very top of the list, which specifies
      // the name of each category.
      // All of these are made using hte addString helper method, and the amount is
      // dependend on the length of the respective words,
      // ex: nameTop's spacing needs to be modifed by 5 because it includes " Name"
      String nameTop = this.addString(lName.length() + 5, "─");
      String name = this.addString(lName.length(), " ");
      String pidTop = this.addString(lPID.length() + 5, "─");
      String pid = this.addString(lPID.length(), " ");
      String priceTop = this.addString(lPrice.length() + 4, "─");
      String price = this.addString(lPrice.length() - 2, " ");
      String quantityTop = this.addString(lQuantity.length() + 5, "─");
      String quantity = this.addString(lQuantity.length() - 4, " ");

      // makes sure quantity top is at least 9 dashes
      if (quantityTop.length() < 9) {
        quantityTop = this.addString(9, "─");
      }

      // this constructs a filler string for the layers seperating each product
      String filler = "├" + nameTop + "┼" + pidTop + "┼" + priceTop + "┼" + quantityTop + "┤";

      // this prints the very top
      System.out.println("╭" + nameTop + "┬" + pidTop + "┬" + priceTop + "┬" + quantityTop + "╮");
      // this prints the row with the labels
      System.out.println("│ Name" + name + "│ Pid" + pid + " │ Price" + price + "│ Quantity" + quantity + "│");
      // adds a filler below the top section
      System.out.println(filler);

      // iterates through every product in the interval [start,end),
      // it gets that product's information and displays the products info with the
      // correct spacing and visual seperaters
      for (int i = start; i < end; i++) {
        Product p = this.listings.get(i);
        System.out.println("│" + p.getName() +
            this.addString(nameTop.length() - p.getName().length(), " ")
            + "│"
            + p.getProductID()
            + this.addString(pidTop.length() - p.getProductID().length(), " ") + "│"
            + "$"
            + p.getPrice()
            + this.addString(priceTop.length() - Double.toString(p.getPrice()).length() - 1, " ")
            + "│"
            + p.getQuantity()
            + this.addString(quantityTop.length() - Integer.toString(p.getQuantity()).length(), " ")
            + "│");
        if (this.listings.indexOf(p) != end - 1) {
          System.out.println(filler);
        }
      }

      // prints the very bottom of the box
      System.out.print("╰" + nameTop + "┴" + pidTop + "┴" + priceTop + "┴" + quantityTop + "╯");
      return;
    }

    System.out.print("No Products, use $ims -a {name} {productID} {price} {quantity} to add a product");

  }

  // Method that saves changes to the inventory to the json file
  public void write() {
    // attempts to write all things to json file
    try (Writer writer = new FileWriter(FILE_PATH)) {
      gson.toJson(this.listings, writer);
    } catch (Exception e) { // catches any exceptions
      e.printStackTrace();
    }
  }

  // helper method for when creating the chart
  // it creates a string with amount times the filler, then returns it
  private String addString(int amount, String filler) {
    String string = "";
    for (int i = 0; i < amount; i++) {
      string += filler;
    }
    return string;
  }

  // initialisation function, checks if the file exists, if it doesn't it creates
  // it
  private void init() {
    // sets file equal to the inventory.json in $PWD
    File file = new File(FILE_PATH);

    if (!file.exists()) {
      try {
        // runs the command needed to create the file
        Process proc = Runtime.getRuntime().exec(SRC_DIR + "command.sh");
        // waits for it to create it
        proc.waitFor();
        System.out.println("First Run: Initialized inventory.json in $PWD");
      } catch (Exception e) { // catches any errors
        e.printStackTrace();
      }
    }

  }

}
