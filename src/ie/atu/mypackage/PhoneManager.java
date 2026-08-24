package ie.atu.mypackage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class PhoneManager {

    private ArrayList<Phone> phones = new ArrayList<>();

    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    public boolean removePhone(String imei) {
        return phones.removeIf(p -> p.getImei().equalsIgnoreCase(imei));
    }

    public Optional<Phone> findByImei(String imei) {
        return phones.stream()
                .filter(p -> p.getImei().equalsIgnoreCase(imei))
                .findFirst();
    }

    public List<Phone> findByBrandOrModel(String text) {
        String needle = text.toLowerCase();
        return phones.stream()
                .filter(p -> p.getBrand().toLowerCase().contains(needle)
                        || p.getModel().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }

    public int getTotalPhones() {
        return phones.size();
    }

    public double getTotalValue() {
        return phones.stream()
                .mapToDouble(Phone::getPrice)
                .sum();
    }

    public List<Phone> sortByBrand() {
        return phones.stream()
                .sorted(Comparator.comparing(Phone::getBrand, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Phone> sortByYear() {
        return phones.stream()
                .sorted(Comparator.comparingInt(Phone::getYear))
                .collect(Collectors.toList());
    }

    public void saveToFile(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(phones);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            phones = (ArrayList<Phone>) ois.readObject();
        }
    }

    public void exportToCsv(String path) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.println("IMEI,Brand,Model,Year,Price");
            phones.stream()
                    .map(p -> String.join(",",
                            p.getImei(),
                            p.getBrand(),
                            p.getModel(),
                            String.valueOf(p.getYear()),
                            String.valueOf(p.getPrice())))
                    .forEach(writer::println);
        }
    }

    public ArrayList<Phone> getPhones() {
        return phones;
    }
}