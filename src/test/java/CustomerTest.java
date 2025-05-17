import Items.Product;
import People.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class CustomerTest {

    @Test
    void testCustomerCreation() {
        Customer customer = new Customer(1, "John Doe", "john@example.com");
        assertEquals(1, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getContactInfo());
        assertEquals(0, customer.getBalance());
        assertTrue(customer.getPurchaseHistory().isEmpty());
    }

    @Test
    void testDeposit() {
        Customer customer = new Customer(1, "John Doe", "john@example.com");
        customer.deposit(100.0);
        assertEquals(100.0, customer.getBalance());
        
        assertThrows(IllegalArgumentException.class, () -> customer.deposit(-50.0));
    }

    @Test
    void testAddPurchase() {
        Customer customer = new Customer(1, "John Doe", "john@example.com");
        Product product = new Product(1, "Laptop", 999.99, "Electronics");
        
        customer.addPurchase(product, 2);
        Map<Product, Integer> history = customer.getPurchaseHistory();
        assertEquals(1, history.size());
        assertEquals(2, history.get(product));
    }

    @Test
    void testToString() {
        Customer customer = new Customer(1, "John Doe", "john@example.com", 150.0);
        String expected = "Покупатель{id=1, Имя ='John Doe', Контактные данные ='john@example.com', Баланс =150.0}";
        assertEquals(expected, customer.toString());
    }
}