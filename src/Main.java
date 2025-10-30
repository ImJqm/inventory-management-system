import java.io.File;
import java.lang.*;

public class Main {
  // name of json file that contains data
  private static final String FILE_PATH = "inventory.json";

  public static void main(String[] args) {
    // initialiszes inventory
    Inventory inv = new Inventory();

    // attampts to fecth arguements from when command is run
    try {
      if (args.length > 0) { // checks arguement lenght
        switch (args[0]) { // intrerprets 1st arguement
          case "-p": // if first arg is -p, print the products
                     //
            inv.printProducts(0, inv.getAmount());

            break;
          case "-a": // if first arg is -a, try to fetch the next arguements needed for creating a
                     // product
            try {
              // get need arguements
              String name = args[1];
              String pid = args[2];
              double price = Double.valueOf(args[3]);
              int quantity = Integer.valueOf(args[4]);

              // add the product
              inv.addProduct(new Product(name, pid, price, quantity));
              // catches possible eceptions that can occur
            } catch (ArrayIndexOutOfBoundsException e) {

              System.out.print("Error: Not enough arguements for -a");

            } catch (NumberFormatException e) {

              System.out.print("Error: invalid arguements");

            }
            break;
          case "-s": // if -s then run a search, which returns the selected product
            Product p = inv.search();

            // print the selected product from the search, starts at the index of the
            // product, and ends one after so it only prints that product
            inv.printProducts(inv.getIndexOf(p), inv.getIndexOf(p) + 1);
            break;
          case "-r": // if -r arguement
            try {
              // try and get the product id
              String pid = args[1];
              inv.removeProduct(pid); // remove product widith that id
            } catch (ArrayIndexOutOfBoundsException e) { // if nothing is porvided
              System.out.println("Error: Not enough arguements for -r");
            }
            break;
          case "-q": // if -q arguemnt
            try { // try and get the product id and quantity

              String pid = args[1];
              int quantity = Integer.valueOf(args[2]);
              inv.updateQuantity(pid, quantity); // updates thw quantity with the provided product with the new quantity
            } catch (ArrayIndexOutOfBoundsException e) { // catches input error
              System.out.println("Error: Not enough arguements for -q");
            }
            break;
          case "-h": // prints info for when they run -h
            System.out.println("Usage: $ims -{arg}");
            System.out.println("-p : prints products stored in the inventory ($PWD's inventory.json)");
            System.out.println("-s : searchs inventory.json using a fuzzy finder and prints results");
            System.out.println(
                "-a {name} {productID} {price} {quantity} : adds product with those attributes to the inventory | adding a product with a pre-existing product ID will incriment the existing product's quantity by the new quantity");
            System.out.println("-r {productID} : removes Product from inventory with that product ID");
            System.out.println(
                "-q {productID} {quantity} : changes the quantity of the product with that product ID to the quantity specified");
            break;
          default: // defualt if they provide an unknown arguement
            System.out.print("Error: Unkown command " + args[0]);
        }
      } else { // tells them to use -h to see arguements when they provide none
        System.out.print("Please provide an arguement, see $ims -h for help");
      }
    } catch (ArrayIndexOutOfBoundsException e) { // catches when there are no arguements
      e.printStackTrace();
    }
  }

}
