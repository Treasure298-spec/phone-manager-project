# Advanced Object Oriented Computing Project

**Title:** Phone Manager 
**Name:** Treasure Akhigbe Napoleon 
**Student ID:** G00467671  
**Screencast Link:**

> 💻 **Open this project in GitHub Codespaces:** on the repository page, click the green **Code** button, choose the **Codespaces** tab, then **Create codespace on `main`**. A ready-to-code cloud environment (with Java and JavaFX) builds automatically from the `.devcontainer` folder — no local install needed.

## Application Function

Phone Manager lets a user mainteain a list of phones, each uniquely identified by an imei, that users can add a new phone (imei, brand, model, year, price), delete a phone by IMEI, search for a phone by exact imei or by partial brand/model, view the total number of phones and their combined value, and save or reload the entire list from a file so data persists between sessions. All 7 core operations are accessible as buttons on the right-hand side of the GUI: Load DB, Add Item, Delete Item, Find Item, Show Total, Save to DB, and Quit.
![Phone Manager app showing a saved phone list](images/image-2.png)
![Phone Manager showing search results](images/image-3.png)

## Running the Application
1. Open the repository in GitHub Codespaces.
2. Wait for the devcontainer to finish building.
3. Open Main.java (src/ie/atu/mypackage/Main.java).
4. Click "Run and Debug" in the sidebar or run it from the terminal.
5. Open the forwarded port (noVNC virtual desktop) shown in the Ports tab to view the running application window.
No manual installs are required.

## Project Requirements
-  Three classes: Main, Phone, PhoneManager
-  ArrayList<Phone> used in PhoneManager
- Add, remove, serialize, deserialize, total, and search      implemented in PhoneManager
- Stream API + lambda used in removePhone, findByImei, findByBrandOrModel, getTotalValue, sortByBrand, sortByYear
-  File I/O with try-with-resources and exception handling in saveToFile/loadFromFile
-  Serialization to resources/phones.ser
-  JavaFX GUI in Main with all 7 required operations as buttons.

## Project Requirements Above and Beyond

Beyond the minimum requirements, PhoneManager includes extra methods researched independently: sortByBrand() and sortByYear(), and getTotalValue() which sums prices using the Stream API. The app also exports a human-readable CSV file (resources/phones.csv) alongside the serialized .ser save, giving two forms of persistence. Input validation and exception handling prevent crashes from invalid numbers or missing files.
## Application Architecture

The application is built around three classes. Phone is a serializable data class representing a single phone, with IMEI treated as its unique identifier (used in equals/hashCode). PhoneManager owns an ArrayList<Phone> and provides all logic: adding, removing (removeIf with a lambda), searching (stream().filter(...)), totalling, sorting, and file I/O — serialization to .ser and a CSV export via the Stream API. Main is the JavaFX entry point; it builds the GUI and have  the 7 buttons to a PhoneManager method.


## JavaFX

The GUI uses a BorderPane layout: the input form (imei, Brand, Model, Year, Price, Search) sits at the top, a TableView displaying all phones sits in the center, the 7 action buttons are grouped vertically on the right, and a status bar along the bottom shows feedback after every action. A TableView was chosen over a plain list so multiple phone attributes are visible at once, and grouping the buttons together on one side keeps the core actions easy to find and separate from the input form.


## Roadblocks and Unfinished Functionality
I struggled a bit with the coding part — there was a lot of trial and error, and a lot of mistakes along the way. I watched several YouTube videos to help me understand it; some were useful, others weren't, but in the end I got through it.


## Resources

* Oracle Java Documentation - https://docs.oracle.com/en/java/
* JavaFX Documentation - https://openjfx.io/