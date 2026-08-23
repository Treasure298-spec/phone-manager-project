package ie.atu.mypackage;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a single Phone.
 * Implements Serializable so that lists of Phone objects can be
 * written to / read from a file using Java's serialization mechanism.
 */
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    // Unique identifier for this phone (no two phones should share an IMEI)
    private String imei;

    // Brand of the phone (e.g. Apple, Samsung)
    private String brand;

    // Model name of the phone (e.g. iPhone 15)
    private String model;

    // Year the phone was released
    private int year;

    // Price of the phone in euro
    private double price;

    public Phone(String imei, String brand, String model, int year, double price) {
        this.imei = imei;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phone)) return false;
        Phone phone = (Phone) o;
        return Objects.equals(imei, phone.imei);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imei);
    }

    @Override
    public String toString() {
        return String.format("%s | %s %s (%d) - EUR%.2f", imei, brand, model, year, price);
    }
}