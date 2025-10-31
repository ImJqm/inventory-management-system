**Inventory Management System**
--------------------------------------------
A java-based product management system

Usage: $ims -{arg} 
- -p : prints products stored in the inventory ($PWD's inventory.json)
- -s : searchs inventory.json using a fuzzy finder and prints results
- -a {name} {productID} {price} {quantity} : adds product with those attributes to the inventory | adding a product with a pre-existing product ID will incriment the existing product's quantity by the new quantity
- -r {productID} : removes Product from inventory with that product ID
- -q {productID} {quantity} : changes the quantity of the product with that product ID to the quantity specified

**Demo**
---------------------------------------------



https://github.com/user-attachments/assets/41dcc8de-e11e-4bf0-9cf6-7e2c21e03e42




**Dependencies**
--------------------------------------------
- pq
- cut
- java
- Gson (2.10.1+)

**Installation**
------------------------------------------
Clone Repo: 
```git clone https://github.com/ImJqm/inventory-management-system.git```

Change Directory: 
```cd inventory-management-system```

In ```Inventory.java``` , replace SRC_DIR with your actual installation directory, don't use ~, $HOME, or $PWD

Build: 
```javac -cp "lib/gson-2.10.1.jar" src/*.java```

Add this to your .bashrc:
```
ims() {
  local project_dir="$HOME/inventory-management-system"
  java -cp "$project_dir/lib/gson-2.10.1.jar:$project_dir/src" -Duser.dir="$project_dir" Main "$@"
}
```


**Known Limitations**
-------------------------------------------
- Before compiling you must update the SRC_DIR variable in Inventry.java
- Only works on Linux filesystems due to needing /tmp/

**TODO**
-------------------------------------------
- Find a way to get $PWD working in java so that the user doesn't have to specify it themselves
- Make -q and -r also us fzf

