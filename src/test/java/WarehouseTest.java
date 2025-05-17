
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Workplace.Warehouse;
import People.Employee;
import Items.Product;
import StorageCell.StorageCell;

import java.util.HashSet;

public class WarehouseTest {
    private Warehouse warehouse;
    private Employee manager;
    private Product product;

    @BeforeEach
    void setUp() {
        manager = new Employee(1, 1, "Иван Иванов", "Менеджер", 50000);
        warehouse = new Warehouse(1, "ул. Ленина, д. 1", manager);
        product = new Product(1, "Телефон", 10000, "Электроника");
    }

    @Test
    void testWarehouseInitialization() {
        assertEquals(1, warehouse.getId());
        assertEquals("ул. Ленина, д. 1", warehouse.getAddress());
        assertEquals(manager, warehouse.getManager());
        assertTrue(warehouse.isActive());
        assertEquals(1000, warehouse.getBudget());
        assertNotNull(warehouse.getWorkers());
        assertEquals(1, warehouse.getWorkers().size());
    }

    @Test
    void testAddWorker() {
        Employee worker = new Employee(2, -1, "Петр Петров", "Работник", 30000);
        warehouse.addWorker(worker);
        assertTrue(warehouse.getWorkers().contains(worker));
        assertEquals(2, warehouse.getWorkers().size());
        assertEquals(1, worker.getWorkplaceId());
    }

    @Test
    void testRemoveWorker() {
        Employee worker = new Employee(2, 1, "Петр Петров", "Работник", 30000);
        warehouse.addWorker(worker);
        warehouse.removeWorker(worker);
        assertFalse(warehouse.getWorkers().contains(worker));
        assertEquals(-1, worker.getWorkplaceId());
    }

    @Test
    void testAddProductToNewCell() {
        warehouse.addProduct(product, 1, 10);
        assertEquals(1, warehouse.getCells().size());
        StorageCell cell = warehouse.getCells().get(0);
        assertEquals(1, cell.getCellId());
        assertEquals(10, cell.getQuantity());
        assertEquals(product, cell.getProduct());
    }

    @Test
    void testAddProductToExistingCell() {
        warehouse.addProduct(product, 1, 10);
        warehouse.addProduct(product, 1, 5);
        StorageCell cell = warehouse.getCells().get(0);
        assertEquals(15, cell.getQuantity());
    }

    @Test
    void testRemoveProductFromCell() throws Exception {
        warehouse.addProduct(product, 1, 10);
        warehouse.removeProduct(1, 4);
        StorageCell cell = warehouse.getCells().get(0);
        assertEquals(6, cell.getQuantity());
    }

    @Test
    void testRemoveAllProductsFromCell() throws Exception {
        warehouse.addProduct(product, 1, 10);
        warehouse.removeProduct(1, 10);
        assertTrue(warehouse.getCells().isEmpty());
    }

    @Test
    void testGetAllQuantityOfProduct() {
        warehouse.addProduct(product, 1, 5);
        warehouse.addProduct(product, 2, 10);
        assertEquals(15, warehouse.getAllQuantityOfProduct(product));
    }

    @Test
    void testPurchaseProduct() {
        // Подготовка данных
        Product product = new Product(15, "Телевизор", 1000, "Электроника");
        warehouse.setBudget(10000);
        warehouse.purchaseProduct(product, 5, 0);
        assertEquals(5, warehouse.getAllQuantityOfProduct(product));

        boolean cellFound = false;
        for (StorageCell cell : warehouse.getCells()) {
            if (cell.getCellId() == 1 && cell.getProduct().equals(product)) {
                assertEquals(5, cell.getQuantity());
                cellFound = true;
            }
        }
        assertTrue(cellFound, "Ячейка не найдена или данные некорректны");
        assertEquals(10000 - 5 * product.getPrice(), warehouse.getBudget(), 0.01);
        warehouse.purchaseProduct(product, 3, 1);
        assertEquals(8, warehouse.getAllQuantityOfProduct(product));
        StorageCell updatedCell = warehouse.getCells().get(0);
        assertEquals(8, updatedCell.getQuantity());

    }

    @Test
    void testAddBudget() {
        warehouse.addBudget(5000);
        assertEquals(6000, warehouse.getBudget());
    }

    @Test
    void testRelocateWarehouse() {
        warehouse.relocate("ул. Пушкина, д. 10");
        assertEquals("ул. Пушкина, д. 10", warehouse.getAddress());
    }

    @Test
    void testGetAllProducts() {
        warehouse.addProduct(product, 1, 5);
        Product anotherProduct = new Product(2, "Чехол", 500, "Аксессуары");
        warehouse.addProduct(anotherProduct, 2, 20);
        HashSet<Product> products = warehouse.getAllProducts();
        assertEquals(2, products.size());
        assertTrue(products.contains(product));
        assertTrue(products.contains(anotherProduct));
    }

    @Test
    void testHireNewEmployee() {
        Employee newWorker = new Employee(2, -1, "Сергей Смирнов", "Грузчик", 30000);
        warehouse.HireNewEmployee(newWorker);
        assertTrue(warehouse.getWorkers().contains(newWorker));
    }

    @Test
    void testDismissEmployee() {
        Employee worker = new Employee(2, 1, "Анна Смирнова", "Работник", 30000);
        warehouse.addWorker(worker);
        warehouse.dismissEmployee(worker);
        assertFalse(warehouse.getWorkers().contains(worker));
        assertEquals(-1, worker.getWorkplaceId());
    }

    @Test
    void testDismissAndAssignNewManager() {
        Employee newManager = new Employee(3, -1, "Ольга Петрова", "Менеджер", 60000);
        warehouse.dismissManagerAndAssignNew(manager, newManager);
        assertEquals(newManager, warehouse.getManager());
        assertEquals(-1, manager.getWorkplaceId());
    }
}