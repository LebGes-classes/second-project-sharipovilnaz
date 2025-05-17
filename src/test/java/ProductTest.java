import Items.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        Product product = new Product(1, "Laptop", 999.99, "Electronics");
        assertEquals(1, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice());
        assertEquals("Electronics", product.getCategory());
    }

    @Test
    void testPriceChange() {
        Product product = new Product(1, "Laptop", 999.99, "Electronics");
        product.setPrice(899.99);
        assertEquals(899.99, product.getPrice());
    }

    @Test
    void testEqualsAndHashCode() {
        Product product1 = new Product(1, "Laptop", 999.99, "Electronics");
        Product product2 = new Product(1, "Smartphone", 699.99, "Electronics");
        Product product3 = new Product(2, "Laptop", 999.99, "Electronics");
        
        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

    @Test
    void testToString() {
        Product product = new Product(1, "Laptop", 999.99, "Electronics");
        String expected = "Product{id=1, name='Laptop', price=999.99, category='Electronics'}";
        assertEquals(expected, product.toString());
    }
}