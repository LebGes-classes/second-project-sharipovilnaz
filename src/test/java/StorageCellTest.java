import Items.Product;
import StorageCell.StorageCell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class StorageCellTest {

    @Test
    void testStorageCellCreation() {
        Product product = new Product(1, "Laptop", 999.99, "Electronics");
        StorageCell cell = new StorageCell(1, 10, product, true);
        
        assertEquals(1, cell.getCellId());
        assertEquals(10, cell.getQuantity());
        assertEquals(product, cell.getProduct());
    }

    @Test
    void testQuantityChange() {
        Product product = new Product(1, "Laptop", 999.99, "Electronics");
        StorageCell cell = new StorageCell(1, 10, product, true);
        
        cell.setQuantity(15);
        assertEquals(15, cell.getQuantity());
    }

    @Test
    void testToString() {
        Product product = new Product(1, "Laptop", 999.99, "Electronics");
        StorageCell cell = new StorageCell(1, 10, product, true);
        
        String expected = "Ячейка хранения{Id ячейки =1, Количество =10, Товар =Product{id=1, name='Laptop', price=999.99, category='Electronics'}, Ячейка склада: Да}";
        assertEquals(expected, cell.toString());
    }
}