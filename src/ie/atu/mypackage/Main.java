package ie.atu.mypackage;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String DB_FILE = "resources/phones.ser";
    private static final String CSV_FILE = "resources/phones.csv";

    private final PhoneManager manager = new PhoneManager();

    private TableView<Phone> table;
    private ObservableList<Phone> tableData = FXCollections.observableArrayList();

    private TextField imeiField;
    private TextField brandField;
    private TextField modelField;
    private TextField yearField;
    private TextField priceField;
    private TextField searchField;

    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setTop(buildInputForm());
        root.setCenter(buildTable());
        root.setBottom(buildStatusBar());
        root.setRight(buildActionButtons());

        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("Phone Manager");
        stage.setScene(scene);
        stage.show();
    }

    private GridPane buildInputForm() {
        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.setPadding(new Insets(0, 0, 10, 0));

        imeiField = new TextField();
        imeiField.setPromptText("IMEI (unique)");
        brandField = new TextField();
        brandField.setPromptText("Brand");
        modelField = new TextField();
        modelField.setPromptText("Model");
        yearField = new TextField();
        yearField.setPromptText("Year");
        priceField = new TextField();
        priceField.setPromptText("Price");

        form.add(new Label("IMEI:"), 0, 0);
        form.add(imeiField, 1, 0);
        form.add(new Label("Brand:"), 2, 0);
        form.add(brandField, 3, 0);
        form.add(new Label("Model:"), 0, 1);
        form.add(modelField, 1, 1);
        form.add(new Label("Year:"), 2, 1);
        form.add(yearField, 3, 1);
        form.add(new Label("Price:"), 0, 2);
        form.add(priceField, 1, 2);

        searchField = new TextField();
        searchField.setPromptText("Search by IMEI, brand or model");
        form.add(new Label("Search:"), 2, 2);
        form.add(searchField, 3, 2);

        return form;
    }

    private TableView<Phone> buildTable() {
        table = new TableView<>();
        table.setItems(tableData);

        TableColumn<Phone, String> imeiCol = new TableColumn<>("IMEI");
        imeiCol.setCellValueFactory(new PropertyValueFactory<>("imei"));

        TableColumn<Phone, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Phone, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Phone, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Phone, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(imeiCol, brandCol, modelCol, yearCol, priceCol);
        return table;
    }

    private HBox buildStatusBar() {
        statusLabel = new Label("Ready.");
        HBox box = new HBox(statusLabel);
        box.setPadding(new Insets(10, 0, 0, 0));
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private HBox buildActionButtons() {
        Button loadBtn = new Button("Load DB");
        loadBtn.setOnAction(e -> loadDatabase());

        Button addBtn = new Button("Add Item");
        addBtn.setOnAction(e -> addItem());

        Button deleteBtn = new Button("Delete Item");
        deleteBtn.setOnAction(e -> deleteItem());

        Button findBtn = new Button("Find Item");
        findBtn.setOnAction(e -> findItem());

        Button totalBtn = new Button("Show Total");
        totalBtn.setOnAction(e -> showTotal());

        Button saveBtn = new Button("Save to DB");
        saveBtn.setOnAction(e -> saveDatabase());

        Button quitBtn = new Button("Quit");
        quitBtn.setOnAction(e -> Platform.exit());

        VBox buttonBox = new VBox(10,
                loadBtn, addBtn, deleteBtn, findBtn, totalBtn, saveBtn, quitBtn);
        buttonBox.setPadding(new Insets(0, 0, 0, 10));
        buttonBox.setAlignment(Pos.TOP_CENTER);

        return new HBox(buttonBox);
    }

    private void addItem() {
        try {
            String imei = imeiField.getText().trim();
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();

            if (imei.isEmpty() || brand.isEmpty() || model.isEmpty()) {
                setStatus("IMEI, Brand and Model are required.");
                return;
            }

            if (manager.findByImei(imei).isPresent()) {
                setStatus("A phone with that IMEI already exists.");
                return;
            }

            int year = Integer.parseInt(yearField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            Phone phone = new Phone(imei, brand, model, year, price);
            manager.addPhone(phone);
            refreshTable();
            clearInputFields();
            setStatus("Added: " + phone);
        } catch (NumberFormatException ex) {
            setStatus("Year must be a whole number and Price must be a number.");
        }
    }

    private void deleteItem() {
        String imei = imeiField.getText().trim();
        if (imei.isEmpty()) {
            imei = searchField.getText().trim();
        }
        if (imei.isEmpty()) {
            setStatus("Enter an IMEI (in the IMEI or Search field) to delete.");
            return;
        }
        boolean removed = manager.removePhone(imei);
        refreshTable();
        setStatus(removed ? "Deleted phone with IMEI " + imei : "No phone found with that IMEI.");
    }

    private void findItem() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            setStatus("Type an IMEI, brand or model into the Search field first.");
            return;
        }

        Optional<Phone> exact = manager.findByImei(query);
        if (exact.isPresent()) {
            tableData.setAll(exact.get());
            setStatus("Found 1 phone by IMEI.");
            return;
        }

        var matches = manager.findByBrandOrModel(query);
        tableData.setAll(matches);
        setStatus(matches.isEmpty() ? "No matches found." : "Found " + matches.size() + " match(es).");
    }

    private void showTotal() {
        int total = manager.getTotalPhones();
        double value = manager.getTotalValue();
        String message = String.format("Total phones: %d | Combined value: EUR%.2f", total, value);
        setStatus(message);
        showInfoAlert("Total Items", message);
    }

    private void saveDatabase() {
        try {
            manager.saveToFile(DB_FILE);
            manager.exportToCsv(CSV_FILE);
            setStatus("Saved " + manager.getTotalPhones() + " phone(s) to " + DB_FILE);
        } catch (Exception ex) {
            setStatus("Error saving database: " + ex.getMessage());
        }
    }

    private void loadDatabase() {
        try {
            manager.loadFromFile(DB_FILE);
            refreshTable();
            setStatus("Loaded " + manager.getTotalPhones() + " phone(s) from " + DB_FILE);
        } catch (Exception ex) {
            setStatus("Error loading database: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableData.setAll(manager.getPhones());
    }

    private void clearInputFields() {
        imeiField.clear();
        brandField.clear();
        modelField.clear();
        yearField.clear();
        priceField.clear();
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}