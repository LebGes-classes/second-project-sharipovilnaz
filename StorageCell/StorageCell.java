package StorageCell;
import Items.Product;

import java.io.Serializable;

public class StorageCell implements Serializable {
    private int cellId;
    private int quantity;
    private Product product;
    private boolean isInWarehouse; // true если ячейка принадлежит складу, false если пункту продаж

    public StorageCell(int cellId, int quantity, Product product, boolean isInWarehouse) {
        this.cellId = cellId;
        this.quantity = quantity;
        this.product = product;
        this.isInWarehouse = isInWarehouse;
    }

    // Геттеры и сеттеры
    public boolean isInWarehouse() { return isInWarehouse; }
    public void setInWarehouse(boolean inWarehouse) { isInWarehouse = inWarehouse; }
    public int getCellId() {return cellId;}
    public int getQuantity() {return quantity;}
    public Product getProduct() {return product;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    @Override
    public String toString() {
        return "Ячейка хранения{" +
                "Id ячейки =" + cellId +
                ", Количество =" + quantity +
                ", Товар =" + product +
                ", Ячейка склада: " + (isInWarehouse ? "Да" : "Нет") +
                '}';
    }
}
