import AppLaunch.*;
import Items.Product;
import Workplace.*;
import People.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;
import java.util.Map;

class DatamanagerTest {
    private static final String TEST_FILE = "test_data.xlsx";

    @Test
    void testDataReadingAndWriting() {
        // Создаем тестовые данные
        Employee employee = new Employee(1, 101, "Test Employee", "Manager", 50000);
        List<Employee> employees = List.of(employee);
        
        Warehouse warehouse = new Warehouse(1, "Test Warehouse Address", employee);
        List<Warehouse> warehouses = List.of(warehouse);
        
        SalesPoint salesPoint = new SalesPoint(1, "Test Sales Point Address", employee);
        salesPoint.addWarehouse(warehouse);
        List<SalesPoint> salesPoints = List.of(salesPoint);
        
        Product product = new Product(1, "Test Product", 99.99, "Test Category");
        List<Product> products = List.of(product);
        
        Customer customer = new Customer(1, "Test Customer", "test@example.com");
        customer.addPurchase(product, 2);
        List<Customer> customers = List.of(customer);

        // Записываем данные в файл
        DataRecordingManager.writeAllData(TEST_FILE, warehouses, salesPoints, customers, employees, products);
        assertTrue(new File(TEST_FILE).exists());

        // Читаем данные из файла
        Map<String, List<?>> readData = DataReadingManager.readAllData(TEST_FILE);
        
        // Проверяем корректность чтения
        assertEquals(1, ((List<Employee>) readData.get("Сотрудники")).size());
        assertEquals(1, ((List<Warehouse>) readData.get("Склады")).size());
        assertEquals(1, ((List<SalesPoint>) readData.get("Пункты продаж")).size());
        assertEquals(1, ((List<Product>) readData.get("Товары")).size());
        assertEquals(1, ((List<Customer>) readData.get("Клиенты")).size());
        
        // Удаляем тестовый файл
        new File(TEST_FILE).delete();
    }
}