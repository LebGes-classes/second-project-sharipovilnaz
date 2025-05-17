package People;
import java.util.HashMap;
import java.util.Map;

import Items.Product;

public class Customer {
    private int id;
    private String name;
    private String contactInfo;
    private double balance;
    private Map<Product, Integer> purchaseHistory; 

    public Customer(int id, String name, String contactInfo) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.balance = 0;
        this.purchaseHistory = new HashMap<>();
    }
    public Customer(int id, String name, String contactInfo, double balance) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.balance = balance;
        this.purchaseHistory = new HashMap<>();
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getContactInfo() { return contactInfo; }
    public double getBalance() { return balance; }
    public Map<Product, Integer> getPurchaseHistory() { return purchaseHistory; }

    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public void setBalance(double balance) { this.balance = balance; }

    // Пополнение баланса
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }
        balance += amount;
        System.out.println("Баланс пополнен на " + amount + ". Текущий баланс: " + balance);
    }

    // Добавление записи в историю покупок
    public void addPurchase(Product product, int quantity) {
        purchaseHistory.put(product, quantity);
    }

    public void printPurchaseHistory() {
        for (Product product : purchaseHistory.keySet()) {
            System.out.println(product.getName() + ": " + purchaseHistory.get(product));
        }
    }

    @Override
    public String toString() {
        return "Покупатель{" +
                "id=" + id +
                ", Имя ='" + name + '\'' +
                ", Контактные данные ='" + contactInfo + '\'' +
                ", Баланс =" + balance +
                '}';
    }
}