
import static org.junit.jupiter.api.Assertions.*;

import Workplace.SalesPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import Workplace.Warehouse;
import People.Employee;
import Items.Product;
import StorageCell.StorageCell;

import java.util.List;

public class SalesPointTest {
    private SalesPoint salesPoint;
    private Warehouse warehouse;
    private Employee manager;
    private Employee worker;
    private Product product;

    @BeforeEach
    void setUp() {
        manager = new Employee(1, 1, "Иван Иванов", "Менеджер", 50000);
        worker = new Employee(2, -1, "Петр Петров", "Продавец", 30000);
        product = new Product(1, "Телефон", 10000, "Электроника");

        warehouse = new Warehouse(1, "ул. Ленина, д. 1", manager);
        salesPoint = new SalesPoint(1, "ул. Мира, д. 5", manager);
        salesPoint.addWorker(worker);
        salesPoint.addWarehouse(warehouse);
    }

    @Test
    void testSalesPointInitialization() {
        assertEquals(1, salesPoint.getId());
        assertEquals("ул. Мира, д. 5", salesPoint.getAddress());
        assertEquals(manager, salesPoint.getManager());
        assertTrue(salesPoint.isActive());
        assertEquals(1000, salesPoint.getBudget());
        assertNotNull(salesPoint.getWorkers());
        assertEquals(2, salesPoint.getWorkers().size());
        assertEquals(1, salesPoint.getWarehouses().size());
        assertEquals(0, salesPoint.getRevenue());
    }

    @Test
    void testAddWarehouse() {
        Warehouse newWarehouse = new Warehouse(2, "ул. Кирова, д. 2", manager);
        salesPoint.addWarehouse(newWarehouse);
        assertTrue(salesPoint.getWarehouses().contains(newWarehouse));
    }

    @Test
    void testRemoveWarehouse() {
        salesPoint.removeWarehouse(warehouse);
        assertFalse(salesPoint.getWarehouses().contains(warehouse));
    }

    @Test
    void testRelocateSalesPoint() {
        salesPoint.relocate("ул. Гагарина, д. 1");
        assertEquals("ул. Гагарина, д. 1", salesPoint.getAddress());
    }

    @Test
    void testAddProductFromWarehouse() throws Exception {
        warehouse.addProduct(product, 1, 10); // Добавляем товар на склад
        salesPoint.addProductFromWarehouse(1, 5, warehouse);

        StorageCell cell = salesPoint.getCells().get(0);
        assertEquals(5, cell.getQuantity());
        assertEquals(5, warehouse.getAllQuantityOfProduct(product)); // Осталось 5
    }

    @Test
    void testReturnProductToWarehouse() throws Exception {
        warehouse.addProduct(product, 1, 10);
        salesPoint.addProductFromWarehouse(1, 5, warehouse);
        salesPoint.returnProductToWarehouse(warehouse, 1, 3);

        assertEquals(8, warehouse.getAllQuantityOfProduct(product));
        assertEquals(2, salesPoint.getAllQuantityOfProduct(product));
    }

    @Test
    void testRemoveProduct() {
        warehouse.addProduct(product, 1, 10);
        salesPoint.addProductFromWarehouse(1, 10, warehouse);
        salesPoint.removeProduct(product, 4);
        assertEquals(6, salesPoint.getAllQuantityOfProduct(product));
    }

    @Test
    void testSellAllProduction() {
        warehouse.addProduct(product, 1, 5);
        salesPoint.addProductFromWarehouse(1, 5, warehouse);
        salesPoint.sellAllProduction();
        assertEquals(50000, salesPoint.getRevenue());
        assertTrue(salesPoint.getCells().isEmpty());
    }

    @Test
    void testSellAllProductsOfThisType() {
        warehouse.addProduct(product, 1, 5);
        salesPoint.addProductFromWarehouse(1, 5, warehouse);
        Product anotherProduct = new Product(2, "Чехол", 1000, "Аксессуары");
        warehouse.addProduct(anotherProduct, 2, 10);
        salesPoint.addProductFromWarehouse(2, 10, warehouse);

        salesPoint.sellAllProductsOfThisType(product);
        assertEquals(10000 * 5, salesPoint.getRevenue());
        assertEquals(10, salesPoint.getAllQuantityOfProduct(anotherProduct));
    }

    @Test
    void testSellProductByQuantity() {
        warehouse.addProduct(product, 1, 10);
        salesPoint.addProductFromWarehouse(1, 10, warehouse);
        salesPoint.sellProductByQuantity(product, 3);
        assertEquals(30000, salesPoint.getRevenue());
        assertEquals(7, salesPoint.getAllQuantityOfProduct(product));
    }

    @Test
    void testAddBudget() {
        salesPoint.addBudget(5000);
        assertEquals(6000, salesPoint.getBudget());
    }

    @Test
    void testSendMoneyToWarehouse() {
        salesPoint.addBudget(10000);
        double initialBudget = warehouse.getBudget();
        salesPoint.sendMoneyToWarehouse(warehouse, 5000);
        assertEquals(11000 - 5000, salesPoint.getBudget());
        assertEquals(initialBudget + 5000, warehouse.getBudget());
    }

    @Test
    void testHireNewEmployee() {
        Employee newWorker = new Employee(3, -1, "Сергей Смирнов", "Грузчик", 30000);
        salesPoint.HireNewEmployee(newWorker);
        assertTrue(salesPoint.getWorkers().contains(newWorker));
    }

    @Test
    void testDismissEmployee() {
        salesPoint.DismissEmployee(worker);
        assertFalse(salesPoint.getWorkers().contains(worker));
        assertEquals(-1, worker.getWorkplaceId());
    }

    @Test
    void testDismissAndAssignNewManager() {
        Employee newManager = new Employee(3, -1, "Ольга Петрова", "Менеджер", 60000);
        salesPoint.dismissManagerAndAssignNew(manager, newManager);
        assertEquals(newManager, salesPoint.getManager());
        assertEquals(-1, manager.getWorkplaceId());
    }
}