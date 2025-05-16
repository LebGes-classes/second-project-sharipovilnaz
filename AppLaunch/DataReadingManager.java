package AppLaunch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import Workplace.*;
import People.*;
import Items.Product;
import StorageCell.StorageCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataReadingManager {
    private static final DataFormatter dataFormatter = new DataFormatter();

    public static Map<String, List<?>> readAllData(String filePath) {
        Map<String, List<?>> data = new HashMap<>();
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
            // чтение сотрудников
            List<Employee> employees = readEmployees(workbook.getSheet("Сотрудники"));
            data.put("Сотрудники", employees);
            Map<Integer, Employee> employeeMap = createEmployeeMap(employees);

            // чтение складов
            List<Warehouse> warehouses = readWarehouses(workbook.getSheet("Склады"), employeeMap);
            data.put("Склады", warehouses);

            // чтение пунктов продаж
            List<SalesPoint> salesPoints = readSalesPoints(workbook.getSheet("Пункты продаж"), warehouses, employeeMap);
            data.put("Пункты продаж", salesPoints);

            // чтение товаров
            List<Product> products = readProducts(workbook.getSheet("Товары"));
            data.put("Товары", products);
            Map<Integer, Product> productMap = createProductMap(products);

            // чтение клиентов
            List<Customer> customers = readCustomers(workbook.getSheet("Клиенты"));
            data.put("Клиенты", customers);
            Map<Integer, Customer> customerMap = createCustomerMap(customers);

            // чтение истории покупок
            readPurchaseHistory(workbook.getSheet("История покупок клиентов"), customerMap, productMap);
            // чтение информации о товарах
            readProductInformation(workbook.getSheet("Информация о товарах"), warehouses, salesPoints, productMap);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + e.getMessage(), e);
        }
        return data;
    }

    private static Map<Integer, Employee> createEmployeeMap(List<Employee> employees) {
        Map<Integer, Employee> map = new HashMap<>();
        employees.forEach(e -> map.put(e.getId(), e));
        return map;
    }

    private static Map<Integer, Product> createProductMap(List<Product> products) {
        Map<Integer, Product> map = new HashMap<>();
        products.forEach(p -> map.put(p.getId(), p));
        return map;
    }

    private static Map<Integer, Customer> createCustomerMap(List<Customer> customers) {
        Map<Integer, Customer> map = new HashMap<>();
        customers.forEach(c -> map.put(c.getId(), c));
        return map;
    }

    private static Map<Integer, Warehouse> createWarehouseMap(List<Warehouse> warehouses) {
        Map<Integer, Warehouse> map = new HashMap<>();
        warehouses.forEach(w -> map.put(w.getId(), w));
        return map;
    }
    // читаем данные
    private static List<Warehouse> readWarehouses(Sheet sheet, Map<Integer, Employee> employeeMap) {
        List<Warehouse> warehouses = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            try {
                int id = getIntValue(row.getCell(0));
                String address = getStringValue(row.getCell(1));
                int managerId = getIntValue(row.getCell(2));
                double budget = getDoubleValue(row.getCell(3));
                boolean isActive = "Да".equals(getStringValue(row.getCell(4)));
                
                Employee manager = employeeMap.get(managerId);
                if (manager == null) {
                    throw new RuntimeException("Менеджер с ID " + managerId + 
                        " не найден для склада с ID " + id + ". Проверьте данные в файле.");
                }
                
                Warehouse warehouse = new Warehouse(id, address, manager);
                warehouse.setBudget(budget);
                warehouse.setActive(isActive);
                warehouses.add(warehouse);
            } catch (Exception e) {
                System.err.println("Ошибка чтения склада в строке " + (i + 1) + ": " + e.getMessage());
            }
        }
        return warehouses;
    }

    private static List<SalesPoint> readSalesPoints(Sheet sheet, List<Warehouse> warehouses, Map<Integer, Employee> employeeMap) {
        List<SalesPoint> salesPoints = new ArrayList<>();
        Map<Integer, Warehouse> warehouseMap = createWarehouseMap(warehouses);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            try {
                int id = getIntValue(row.getCell(0));
                String address = getStringValue(row.getCell(1));
                int managerId = getIntValue(row.getCell(2));
                double budget = getDoubleValue(row.getCell(3));
                double revenue = getDoubleValue(row.getCell(4));
                
                Employee manager = employeeMap.get(managerId);
                if (manager == null) {
                    throw new RuntimeException("Менеджер с ID " + managerId + 
                        " не найден для пункта продаж с ID " + id + ". Проверьте данные в файле.");
                }
                
                SalesPoint sp = new SalesPoint(id, address, manager);
                sp.setBudget(budget);
                sp.setRevenue(revenue);
                sp.setActive("Да".equals(getStringValue(row.getCell(6))));
                
                // Привязка складов
                String[] warehouseIds = getStringValue(row.getCell(5)).split(" ");
                for (String idStr : warehouseIds) {
                    try {
                        int warehouseId = Integer.parseInt(idStr.trim());
                        Warehouse wh = warehouseMap.get(warehouseId);
                        if (wh != null) sp.addWarehouse(wh);
                    } catch (NumberFormatException ignored) {}
                }
                salesPoints.add(sp);
            } catch (Exception e) {
                System.err.println("Ошибка чтения пункта продаж в строке " + (i + 1) + ": " + e.getMessage());
            }
        }
        return salesPoints;
    }

    private static List<Product> readProducts(Sheet sheet) {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            try {
                int id = getIntValue(row.getCell(0));
                String name = getStringValue(row.getCell(1));
                double price = getDoubleValue(row.getCell(2));
                String category = getStringValue(row.getCell(3));
                products.add(new Product(id, name, price, category));
            } catch (Exception e) {
                System.err.println("Ошибка чтения товара в строке " + (i + 1) + ": " + e.getMessage());
            }
        }
        return products;
    }

    private static List<Employee> readEmployees(Sheet sheet) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            try {
                int id = getIntValue(row.getCell(0));
                String name = getStringValue(row.getCell(1));
                String position = getStringValue(row.getCell(2));
                int workplaceId = getIntValue(row.getCell(3));
                int salary = getIntValue(row.getCell(4));
                Employee employee = new Employee(id, workplaceId, name, position, salary);
                employees.add(employee);
            } catch (Exception e) {
                System.err.println("Ошибка чтения сотрудника в строке " + (i + 1) + ": " + e.getMessage());
            }
        }
        return employees;
    }

    private static List<Customer> readCustomers(Sheet sheet) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            try {
                int id = getIntValue(row.getCell(0));
                String name = getStringValue(row.getCell(1));
                String contact = getStringValue(row.getCell(2));
                double balance = getDoubleValue(row.getCell(3));
                customers.add(new Customer(id, name, contact, balance));
            } catch (Exception e) {
                System.err.println("Ошибка чтения клиента в строке " + (i + 1) + ": " + e.getMessage());
            }
        }
        return customers;
    }

    private static void readPurchaseHistory(Sheet sheet, Map<Integer, Customer> customerMap, Map<Integer, Product> productMap) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            try {
                int clientId = getIntValue(row.getCell(0));
                int productId = getIntValue(row.getCell(1));
                int quantity = getIntValue(row.getCell(2));
                Customer customer = customerMap.get(clientId);
                Product product = productMap.get(productId);
                if (customer == null || product == null) {
                    System.err.printf("Пропуск записи: Клиент %d или товар %d не найдены%n", clientId, productId);
                    continue;
                }
                customer.addPurchase(product, quantity);
            } catch (Exception e) {
                System.err.println("Ошибка чтения истории покупок в строке " + (i + 1) + ": " + e.getMessage());
            }
        }
    }
    private static void readProductInformation(Sheet sheet, List<Warehouse> warehouses, List<SalesPoint> salesPoints, Map<Integer, Product> productMap) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            try {
                int productId = getIntValue(row.getCell(0));
                int enterpriseId = getIntValue(row.getCell(1));
                int enterpriseType = getIntValue(row.getCell(2)); // 0 - склад, 1 - пункт продаж
                int cellId = getIntValue(row.getCell(3));
                int quantity = getIntValue(row.getCell(4));
    
                Product product = productMap.get(productId);
                if (product == null) {
                    System.err.printf("Пропуск записи: Товар с ID %d не найден%n", productId);
                    continue;
                }
    
                if (enterpriseType == 0) {
                    Warehouse warehouse = findWarehouseById(enterpriseId, warehouses);
                    if (warehouse != null) {
                        StorageCell newCell = new StorageCell(cellId, quantity, product, true); // isInWarehouse = true
                        warehouse.getCells().add(newCell);
                    }
                } else if (enterpriseType == 1) {
                    SalesPoint salesPoint = findSalesPointById(enterpriseId, salesPoints);
                    if (salesPoint != null) {
                        // Проверяем, есть ли товар на складах, связанных с этим пунктом продаж
                        boolean productAdded = false;
                        for (Warehouse warehouse : salesPoint.getWarehouses()) {
                            int availableQuantity = warehouse.getAllQuantityOfProduct(product);
                            if (availableQuantity >= quantity) {
                                // Добавляем продукт в пункт продаж из данного склада
                                StorageCell newCell = new StorageCell(cellId, quantity, product, false);
                                salesPoint.getCells().add(newCell);
                                productAdded = true;
                                break;
                            }
                        }
                        if (!productAdded) {
                            System.err.printf("Недостаточно товара на складах пункта продаж %d%n", salesPoint.getId());
                        }
                    }
                }
    
            } catch (Exception e) {
                System.err.println("Ошибка чтения информации о товаре в строке " + (i + 1) + ": " + e.getMessage());
            }
        }
    }
    
    private static Warehouse findWarehouseById(int id, List<Warehouse> warehouses) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getId() == id) {
                return warehouse;
            }
        }
        return null;
    }
    
    private static SalesPoint findSalesPointById(int id, List<SalesPoint> salesPoints) {
        for (SalesPoint salesPoint : salesPoints) {
            if (salesPoint.getId() == id) {
                return salesPoint;
            }
        }
        return null;
    }

    // чтение ячеек
    private static String getStringValue(Cell cell) {
        return cell != null ? dataFormatter.formatCellValue(cell).trim() : "";
    }

    private static int getIntValue(Cell cell) {
        if (cell == null) return 0;
    
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                String value = cell.getStringCellValue().trim();
                if (value.isEmpty()) return 0;
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    private static double getDoubleValue(Cell cell) {
        if (cell == null) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
            try {
                return Double.parseDouble(cell.getStringCellValue().trim());
            } catch (NumberFormatException ignored) {}
        }
        return 0.0;
    }
}