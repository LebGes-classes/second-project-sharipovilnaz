package AppLaunch;


import java.util.*;
import Workplace.*;
import People.*;
import Items.Product;
import Website.Website;
import StorageCell.StorageCell;

public class CompanyManagementSystem {
    private static List<Warehouse> warehouses = new ArrayList<>();
    private static List<SalesPoint> salesPoints = new ArrayList<>();
    private static List<Product> products = new ArrayList<>();
    private static List<Employee> employees = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static Website website;

    public static void main(String[] args) {
        // загружаю данные из Excel
        loadDataFromExcel("data.xlsx");

        if (!salesPoints.isEmpty()) {
            website = new Website(salesPoints.get(0), customers); 
        } else {
            website = new Website(null, customers); 
        }
        mainMenu();
    }

    public static List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public static List<SalesPoint> getSalesPoints() {
        return salesPoints;
    }

    private static void mainMenu() {
        while (true) {
            clearConsole();
            System.out.println("=== Система управления компанией ===");
            System.out.println("1. Управление складами");
            System.out.println("2. Управление пунктами продаж");
            System.out.println("3. Управление товарами");
            System.out.println("4. Управление сотрудниками");
            System.out.println("5. Управление клиентами");
            System.out.println("6. Открыть интернет-магазин");
            System.out.println("7. Сохранить данные в Excel");
            System.out.println("0. Выход");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    // меню работы со складами
                    warehouseMenu();
                    break;
                case 2:
                    // меню работы с пунктами продаж
                    salesPointMenu();
                    break;
                case 3:
                    // меню работы с товарами
                    productMenu();
                    break;
                case 4:
                    // меню работы с работниками
                    employeeMenu();
                    break;
                case 5:
                    // меню работы с клиентами
                    customerMenu();
                    break;
                case 6:
                    // меню для клиента
                    openWebsite();
                    break;
                case 7:
                    // сохранение данных
                    saveDataToExcel("data.xlsx");
                    break;
                case 0:
                    // выход из программы
                    System.out.println("Выход из программы...");
                    System.exit(0);
                default:
                    // на случай мис клика
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    pause();
            }
        }
    }
    // меню для клиента
    private static void openWebsite() {
        if (salesPoints.isEmpty()) {
            System.out.println("Нет доступных пунктов продаж для интернет-магазина.");
            pause();
            return;
        }
        listSalesPoints();
        System.out.print("Выберите ID пункта продаж для интернет-магазина: ");
        int salesPointId = scanner.nextInt();
        scanner.nextLine();
        SalesPoint selectedSalesPoint = findSalesPointById(salesPointId);
        if (selectedSalesPoint == null) {
            System.out.println("Пункт продаж с таким ID не найден.");
            pause();
            return;
        }

        website = new Website(selectedSalesPoint, customers);
        website.run();
    }
     

    // нахожу данные по id
    private static Warehouse findWarehouseById(int id) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getId() == id) {
                return warehouse;
            }
        }
        return null;
    }

    private static SalesPoint findSalesPointById(int id) {
        for (SalesPoint salesPoint : salesPoints) {
            if (salesPoint.getId() == id) {
                return salesPoint;
            }
        }
        return null;
    }

    private static Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private static Employee findEmployeeById(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    private static Customer findCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }
    // очистка консоли
    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    // пауза
    private static void pause() {
        System.out.println("Нажмите Enter для продолжения...");
        scanner.nextLine();
    }

    // загрузка данных с excel
    private static void loadDataFromExcel(String filePath) {
        Map<String, List<?>> data = DataReadingManager.readAllData(filePath);
        warehouses = (List<Warehouse>) data.get("Склады");
        salesPoints = (List<SalesPoint>) data.get("Пункты продаж");
        products = (List<Product>) data.get("Товары");
        employees = (List<Employee>) data.get("Сотрудники");
        customers = (List<Customer>) data.get("Клиенты");

        System.out.println("Загружено складов: " + warehouses.size());
        System.out.println("Загружено пунктов продаж: " + salesPoints.size());
        System.out.println("Загружено товаров: " + products.size());
        System.out.println("Загружено сотрудников: " + employees.size());
        System.out.println("Загружено клиентов: " + customers.size());
    }
    // сохранение данных в excel
    private static void saveDataToExcel(String filePath) {
        System.out.println("Сохраняем данные:");
        System.out.println("Склады: " + warehouses.size());
        System.out.println("Пункты продаж: " + salesPoints.size());
        System.out.println("Клиенты: " + customers.size());
        System.out.println("Сотрудники: " + employees.size());
        System.out.println("Товары: " + products.size());

        System.out.println("Склады пусты? " + warehouses.isEmpty());
        System.out.println("Пункты продаж пусты? " + salesPoints.isEmpty());
        System.out.println("Товары пусты? " + products.isEmpty());
        System.out.println("Сотрудники пусты? " + employees.isEmpty());
        System.out.println("Клиенты пусты? " + customers.isEmpty());
        
        DataRecordingManager.writeAllData(filePath, warehouses, salesPoints, customers, employees, products);
    }

    // Меню управления складами
    private static void warehouseMenu() {
        while (true) {
            clearConsole();
            System.out.println("=== Управление складами ===");
            System.out.println("1. Создать новый склад");
            System.out.println("2. Просмотреть список складов");
            System.out.println("3. Управление конкретным складом");
            System.out.println("4. Назад");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createWarehouse();
                    break;
                case 2:
                    listWarehouses();
                    pause();
                    break;
                case 3:
                    manageWarehouse();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    pause();
            }
        }
    }
    // создание склада
    private static void createWarehouse() {
        System.out.print("Введите ID склада: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите адрес склада: ");
        String address = scanner.nextLine();
        System.out.println("Выберите менеджера:");
        listEmployees();
        System.out.print("Введите ID менеджера: ");
        int managerId = scanner.nextInt();
        scanner.nextLine();
        Employee manager = findEmployeeById(managerId);
        if (manager == null) {
            System.out.println("Сотрудник с таким ID не найден.");
            pause();
            return;
        }
        Warehouse warehouse = new Warehouse(id, address, manager);
        warehouses.add(warehouse);
        System.out.println("Склад успешно создан!");
        pause();
    }
    // список складов
    private static void listWarehouses() {
        if (warehouses.isEmpty()) {
            System.out.println("Нет доступных складов.");
            return;
        }
        System.out.println("=== Список складов ===");
        for (Warehouse warehouse : warehouses) {
            warehouse.printWarehouseInfo();
        }
    }
    // выбор склада для управления
    private static void manageWarehouse() {
        listWarehouses();
        if (warehouses.isEmpty()) {
            pause();
            return;
        }
        System.out.print("Введите ID склада для управления: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Warehouse warehouse = findWarehouseById(id);
        if (warehouse == null) {
            System.out.println("Склад с таким ID не найден.");
            pause();
            return;
        }
        warehouseManagementSubMenu(warehouse);
    }
    // меню работы с конкретным складом
    private static void warehouseManagementSubMenu(Warehouse warehouse) {
        while (true) {
            clearConsole();
            warehouse.printWarehouseInfo();
            System.out.println("=== Управление складом ===");
            System.out.println("1. Закупить товары");
            System.out.println("2. Удалить товар");
            System.out.println("3. Просмотреть товары на складе");
            System.out.println("4. Добавить бюджет");
            System.out.println("5. Переместить склад");
            System.out.println("6. Открыть/Закрыть склад");
            System.out.println("7. Назначить нового менеджера");
            System.out.println("8. Назад");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    purchaseProductForWarehouse(warehouse);
                    break;
                case 2:
                    removeProductFromWarehouse(warehouse);
                    break;
                case 3:
                    warehouse.printInfoAboutProductsAtWarehouse();
                    pause();
                    break;
                case 4:
                    addBudgetToWarehouse(warehouse);
                    break;
                case 5:
                    relocateWarehouse(warehouse);
                    break;
                case 6:
                    toggleWarehouseStatus(warehouse);
                    break;
                case 7:
                    changeWarehouseManager(warehouse);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    pause();
            }
        }
    }

    // удалить товар со склада
    private static void removeProductFromWarehouse(Warehouse warehouse) {
        if (warehouse.getCells().isEmpty()) {
            System.out.println("На складе нет доступных ячеек с товарами.");
            pause();
            return;
        }

        System.out.println("=== Ячейки на складе ===");
        for (StorageCell cell : warehouse.getCells()) {
            System.out.println("Ячейка ID: " + cell.getCellId() +
                    ", Товар: " + cell.getProduct().getName() +
                    ", Количество: " + cell.getQuantity());
        }

        System.out.print("Введите ID ячейки для удаления: ");
        int cellId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите количество товара для удаления: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        try {
            warehouse.removeProduct(cellId, quantity);
        } catch (Exception e) {
            System.out.println("Ошибка при удалении товара: " + e.getMessage());
        }

        pause();
    }
    // купить товар
    private static void purchaseProductForWarehouse(Warehouse warehouse) {
        System.out.println("Доступные товары:");
        listProducts();
        System.out.print("Введите ID товара: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = findProductById(productId);
        if (product == null) {
            System.out.println("Товар с таким ID не найден.");
            pause();
            return;
        }
        System.out.print("Введите количество для закупки: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        try {
            warehouse.purchaseProduct(product, quantity);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        pause();
    }
    // закинуть деньги
    private static void addBudgetToWarehouse(Warehouse warehouse) {
        System.out.print("Введите сумму для добавления: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        try {
            warehouse.addBudget(amount);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        pause();
    }
    // перенести склад
    private static void relocateWarehouse(Warehouse warehouse) {
        System.out.print("Введите новый адрес: ");
        String newAddress = scanner.nextLine();
        try {
            warehouse.relocate(newAddress);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        pause();
    }
    // открыть/закрыть склад
    private static void toggleWarehouseStatus(Warehouse warehouse) {
        if (warehouse.isActive()) {
            warehouse.closeWarehouse();
            System.out.println("Склад закрыт.");
        } else {
            warehouse.openWarehouse();
            System.out.println("Склад открыт.");
        }
        pause();
    }
    // меняем менеджера склада
    private static void changeWarehouseManager(Warehouse warehouse) {
        System.out.println("Доступные сотрудники:");
        listEmployees();
        System.out.print("Введите ID нового менеджера: ");
        int managerId = scanner.nextInt();
        scanner.nextLine();
        Employee manager = findEmployeeById(managerId);
        if (manager == null) {
            System.out.println("Сотрудник с таким ID не найден.");
            pause();
            return;
        }
        warehouse.setManager(manager);
        System.out.println("Менеджер успешно изменен.");
        pause();
    }

    // Меню управления пунктами продаж
    private static void salesPointMenu() {
        while (true) {
            clearConsole();
            System.out.println("=== Управление пунктами продаж ===");
            System.out.println("1. Создать новый пункт продаж");
            System.out.println("2. Просмотреть список пунктов продаж");
            System.out.println("3. Управление конкретным пунктом продаж");
            System.out.println("4. Назад");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createSalesPoint();
                    break;
                case 2:
                    listSalesPoints();
                    pause();
                    break;
                case 3:
                    manageSalesPoint();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    pause();
            }
        }
    }
    // создаем пункт продаж
    private static void createSalesPoint() {
        System.out.print("Введите ID пункта продаж: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите адрес пункта продаж: ");
        String address = scanner.nextLine();
        System.out.println("Выберите менеджера:");
        listEmployees();
        System.out.print("Введите ID менеджера: ");
        int managerId = scanner.nextInt();
        scanner.nextLine();
        Employee manager = findEmployeeById(managerId);
        if (manager == null) {
            System.out.println("Сотрудник с таким ID не найден.");
            pause();
            return;
        }
        SalesPoint salesPoint = new SalesPoint(id, address, manager);
        salesPoints.add(salesPoint);
        System.out.println("Пункт продаж успешно создан!");
        // Добавление складов к пункту продаж
        while (true) {
            System.out.println("Добавить склад к пункту продаж? (y/n)");
            String answer = scanner.nextLine().toLowerCase();
            if (!answer.equals("y")) break;
            listWarehouses();
            System.out.print("Введите ID склада для добавления (0 для отмены): ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();
            if (warehouseId == 0) break;
            Warehouse warehouse = findWarehouseById(warehouseId);
            if (warehouse == null) {
                System.out.println("Склад с таким ID не найден.");
                continue;
            }
            salesPoint.addWarehouse(warehouse);
        }
        pause();
    }
    // вывод списка пункта продаж
    private static void listSalesPoints() {
        if (salesPoints.isEmpty()) {
            System.out.println("Нет доступных пунктов продаж.");
            return;
        }
        System.out.println("=== Список пунктов продаж ===");
        for (SalesPoint salesPoint : salesPoints) {
            salesPoint.printInfoAboutSalesPoint();
        }
    }
    // управление конкретным пунктом продаж
    private static void manageSalesPoint() {
        listSalesPoints();
        if (salesPoints.isEmpty()) {
            pause();
            return;
        }
        System.out.print("Введите ID пункта продаж для управления: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        SalesPoint salesPoint = findSalesPointById(id);
        if (salesPoint == null) {
            System.out.println("Пункт продаж с таким ID не найден.");
            pause();
            return;
        }
        salesPointManagementSubMenu(salesPoint);
    }
    // меню управления конкретным пунктом продаж
    private static void salesPointManagementSubMenu(SalesPoint salesPoint) {
        while (true) {
            clearConsole();
            salesPoint.printInfoAboutSalesPoint();
            System.out.println("=== Управление пунктом продаж ===");
            System.out.println("1. Добавить товар со склада");
            System.out.println("2. Удалить товар");
            System.out.println("3. Просмотреть товары в пункте продаж");
            System.out.println("4. Вернуть товары на склад");
            System.out.println("5. Продать все товары");
            System.out.println("6. Продать все товары определенного типа");
            System.out.println("7. Продать определенное количество товара");
            System.out.println("8. Добавить бюджет");
            System.out.println("9. Перевести деньги на склад");
            System.out.println("10. Просмотреть доступные товары для закупки");
            System.out.println("11. Просмотреть доходность");
            System.out.println("12. Переместить пункт продаж");
            System.out.println("13. Открыть/Закрыть пункт продаж");
            System.out.println("14. Назначить нового менеджера");
            System.out.println("15. Добавить склад к пункту продаж");
            System.out.println("16. Удалить склад из пункта продаж");
            System.out.println("17. Назад");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    addProductFromWarehouseToSalesPoint(salesPoint);
                    break;
                case 2:
                    removeProductFromSalesPoint(salesPoint);
                    break;
                case 3:
                    salesPoint.printInfoAboutProductsAtSalesPoint();
                    pause();
                    break;
                case 4:
                    returnProductsToWarehouse(salesPoint);
                    break;
                case 5:
                    salesPoint.sellAllProduction();
                    pause();
                    break;
                case 6:
                    sellAllProductsOfType(salesPoint);
                    break;
                case 7:
                    sellProductByQuantity(salesPoint);
                    break;
                case 8:
                    addBudgetToSalesPoint(salesPoint);
                    break;
                case 9:
                    sendMoneyToWarehouseFromSalesPoint(salesPoint);
                    break;
                case 10:
                    salesPoint.printAvailableProductsForPurchase();
                    pause();
                    break;
                case 11:
                    salesPoint.printEnterpriseRevenueInfo();
                    pause();
                    break;
                case 12:
                    relocateSalesPoint(salesPoint);
                    break;
                case 13:
                    toggleSalesPointStatus(salesPoint);
                    break;
                case 14:
                    changeSalesPointManager(salesPoint);
                    break;
                case 15:
                    addWarehouseToSalesPoint(salesPoint);
                    break;
                case 16:
                    removeWarehouseFromSalesPoint(salesPoint);
                    break;
                case 17:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    pause();
            }
        }
    }
    // перекинуть товары со склада на пункт продаж
    private static void addProductFromWarehouseToSalesPoint(SalesPoint salesPoint) {
        if (salesPoint.getWarehouses().isEmpty()) {
            System.out.println("Нет доступных складов для этого пункта продаж.");
            pause();
            return;
        }

        System.out.println("Доступные склады:");
        for (Warehouse warehouse : salesPoint.getWarehouses()) {
            System.out.println("Склад ID: " + warehouse.getId() + ", Адрес: " + warehouse.getAddress());
        }
        System.out.print("Введите ID склада: ");
        int warehouseId = scanner.nextInt();
        scanner.nextLine();

        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse == null || !salesPoint.getWarehouses().contains(warehouse)) {
            System.out.println("Склад с таким ID не найден или не принадлежит этому пункту продаж.");
            pause();
            return;
        }

        System.out.println("=== Ячейки на складе ===");
        for (StorageCell cell : warehouse.getCells()) {
            System.out.println("Ячейка ID: " + cell.getCellId() +
                    ", Товар: " + cell.getProduct().getName() +
                    ", Количество: " + cell.getQuantity());
        }

        System.out.print("Введите ID ячейки для перемещения: ");
        int cellId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите количество товара для перемещения: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        try {
            salesPoint.addProductFromWarehouse(cellId, quantity, warehouse);
            System.out.println("Товар успешно перемещён.");
        } catch (Exception e) {
            System.out.println("Ошибка при перемещении товара: " + e.getMessage());
        }

        pause();
    }
    // удалить товар с пункта продаж
    private static void removeProductFromSalesPoint(SalesPoint salesPoint) {
        salesPoint.printInfoAboutProductsAtSalesPoint();
        System.out.print("Введите ID товара для удаления: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = findProductById(productId);
        if (product == null) {
            System.out.println("Товар с таким ID не найден.");
            pause();
            return;
        }
        System.out.print("Введите количество для удаления: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        try {
            salesPoint.removeProduct(product, quantity);
            System.out.println("Товар успешно удален из пункта продаж.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при удалении товара: " + e.getMessage());
        }
        pause();
    }
    // возврат товара на склад
    private static void returnProductsToWarehouse(SalesPoint salesPoint) {
        if (salesPoint.getCells().isEmpty()) {
            System.out.println("Нет товаров в пункте продаж для возврата.");
            pause();
            return;
        }

        System.out.println("=== Ячейки в пункте продаж ===");
        for (StorageCell cell : salesPoint.getCells()) {
            System.out.println("Ячейка ID: " + cell.getCellId() +
                    ", Товар: " + cell.getProduct().getName() +
                    ", Количество: " + cell.getQuantity());
        }

        System.out.print("Введите ID ячейки для возврата: ");
        int cellId = scanner.nextInt();
        scanner.nextLine();

        if (salesPoint.getWarehouses().isEmpty()) {
            System.out.println("Нет доступных складов для возврата товара.");
            pause();
            return;
        }

        System.out.println("Доступные склады:");
        for (Warehouse warehouse : salesPoint.getWarehouses()) {
            System.out.println("Склад ID: " + warehouse.getId() + ", Адрес: " + warehouse.getAddress());
        }

        System.out.print("Введите ID склада для возврата: ");
        int warehouseId = scanner.nextInt();
        scanner.nextLine();

        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse == null || !salesPoint.getWarehouses().contains(warehouse)) {
            System.out.println("Склад с таким ID не найден или не принадлежит этому пункту продаж.");
            pause();
            return;
        }

        System.out.print("Введите количество товара для возврата: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        try {
            salesPoint.returnProductToWarehouse(warehouse, cellId, quantity);
        } catch (Exception e) {
            System.out.println("Ошибка при возврате товара: " + e.getMessage());
        }

        pause();
    }
    // продать все товары определенного типа
    private static void sellAllProductsOfType(SalesPoint salesPoint) {
        salesPoint.printInfoAboutProductsAtSalesPoint();

        System.out.print("Введите ID товара для продажи: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        Product product = findProductById(productId);

        if (product == null) {
            System.out.println("Товар с таким ID не найден.");
            pause();
            return;
        }

        try {
            salesPoint.sellAllProductsOfThisType(product);
            System.out.println("Доход после продажи: " + salesPoint.getRevenue());
        } catch (Exception e) {
            System.out.println("Ошибка при продаже: " + e.getMessage());
        }

        pause();
    }
    // продать определенное количество товара
    private static void sellProductByQuantity(SalesPoint salesPoint) {
        if (salesPoint.getCells().isEmpty()) {
            System.out.println("Нет товаров в пункте продаж для продажи.");
            pause();
            return;
        }

        salesPoint.printInfoAboutProductsAtSalesPoint();

        System.out.print("Введите ID товара для продажи: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        Product product = findProductById(productId);
        if (product == null) {
            System.out.println("Товар с таким ID не найден.");
            pause();
            return;
        }

        System.out.print("Введите количество для продажи: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        try {
            salesPoint.sellProductByQuantity(product, quantity);
            System.out.println("Доход после продажи: " + salesPoint.getRevenue());
        } catch (Exception e) {
            System.out.println("Ошибка при продаже: " + e.getMessage());
        }

        pause();
    }
    // закинуть денег на пункт продаж
    private static void addBudgetToSalesPoint(SalesPoint salesPoint) {
        System.out.print("Введите сумму для добавления: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        try {
            salesPoint.addBudget(amount);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        pause();
    }
    // закинуть деньги с пункта продаж на склад
    private static void sendMoneyToWarehouseFromSalesPoint(SalesPoint salesPoint) {
        if (salesPoint.getWarehouses().isEmpty()) {
            System.out.println("Нет доступных складов для перевода денег.");
            pause();
            return;
        }
        System.out.println("Доступные склады:");
        for (Warehouse warehouse : salesPoint.getWarehouses()) {
            System.out.println("Склад ID: " + warehouse.getId() + ", Адрес: " + warehouse.getAddress());
        }
        System.out.print("Введите ID склада: ");
        int warehouseId = scanner.nextInt();
        scanner.nextLine();
        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse == null || !salesPoint.getWarehouses().contains(warehouse)) {
            System.out.println("Склад с таким ID не найден или не принадлежит этому пункту продаж.");
            pause();
            return;
        }
        System.out.print("Введите сумму для перевода: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        try {
            salesPoint.sendMoneyToWarehouse(warehouse, amount);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        pause();
    }
    // переместить пункт продаж
    private static void relocateSalesPoint(SalesPoint salesPoint) {
        System.out.print("Введите новый адрес: ");
        String newAddress = scanner.nextLine();
        try {
            salesPoint.relocate(newAddress);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        pause();
    }
    // открыть/закрыть пункт
    private static void toggleSalesPointStatus(SalesPoint salesPoint) {
        if (salesPoint.isActive()) {
            salesPoint.closeSalesPoint();
            System.out.println("Пункт продаж закрыт.");
        } else {
            salesPoint.openSalesPoint();
            System.out.println("Пункт продаж открыт.");
        }
        pause();
    }
    // поменять менеджера пункт продаж
    private static void changeSalesPointManager(SalesPoint salesPoint) {
        System.out.println("Доступные сотрудники:");
        listEmployees();
        System.out.print("Введите ID нового менеджера: ");
        int managerId = scanner.nextInt();
        scanner.nextLine();
        Employee manager = findEmployeeById(managerId);
        if (manager == null) {
            System.out.println("Сотрудник с таким ID не найден.");
            pause();
            return;
        }
        salesPoint.setManager(manager);
        System.out.println("Менеджер успешно изменен.");
        pause();
    }
    // добавить склад к пункту продаж
    private static void addWarehouseToSalesPoint(SalesPoint salesPoint) {
        listWarehouses();
        System.out.print("Введите ID склада для добавления: ");
        int warehouseId = scanner.nextInt();
        scanner.nextLine();
        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse == null) {
            System.out.println("Склад с таким ID не найден.");
            pause();
            return;
        }
        try {
            salesPoint.addWarehouse(warehouse);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        pause();
    }
    // убрать связь склада с пунктом продаж
    private static void removeWarehouseFromSalesPoint(SalesPoint salesPoint) {
        if (salesPoint.getWarehouses().isEmpty()) {
            System.out.println("Нет складов, привязанных к этому пункту продаж.");
            pause();
            return;
        }
        System.out.println("Привязанные склады:");
        for (Warehouse warehouse : salesPoint.getWarehouses()) {
            System.out.println("Склад ID: " + warehouse.getId() + ", Адрес: " + warehouse.getAddress());
        }
        System.out.print("Введите ID склада для удаления: ");
        int warehouseId = scanner.nextInt();
        scanner.nextLine();
        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse == null || !salesPoint.getWarehouses().contains(warehouse)) {
            System.out.println("Склад с таким ID не найден или не принадлежит этому пункту продаж.");
            pause();
            return;
        }
        try {
            salesPoint.removeWarehouse(warehouse);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        pause();
    }

    // Меню управления товарами
    private static void productMenu() {
        while (true) {
            clearConsole();
            System.out.println("=== Управление товарами ===");
            System.out.println("1. Создать новый товар");
            System.out.println("2. Просмотреть список товаров");
            System.out.println("3. Изменить цену товара");
            System.out.println("4. Назад");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createProduct();
                    break;
                case 2:
                    listProducts();
                    pause();
                    break;
                case 3:
                    changeProductPrice();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    pause();
            }
        }
    }
    // создать товар
    private static void createProduct() {
        System.out.print("Введите ID товара: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите название товара: ");
        String name = scanner.nextLine();
        System.out.print("Введите цену товара: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Введите категорию товара: ");
        String category = scanner.nextLine();
        Product product = new Product(id, name, price, category);
        products.add(product);
        System.out.println("Товар успешно создан!");
        pause();
    }
    // вывод списка товаров
    private static void listProducts() {
        if (products.isEmpty()) {
            System.out.println("Нет доступных товаров.");
            return;
        }
        System.out.println("=== Список товаров ===");
        for (Product product : products) {
            System.out.println(product.toString());
        }
    }
    // поменять цену товара
    private static void changeProductPrice() {
        listProducts();
        if (products.isEmpty()) {
            pause();
            return;
        }
        System.out.print("Введите ID товара: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = findProductById(productId);
        if (product == null) {
            System.out.println("Товар с таким ID не найден.");
            pause();
            return;
        }
        System.out.print("Введите новую цену: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine();
        product.setPrice(newPrice);
        System.out.println("Цена товара успешно изменена.");
        pause();
    }

    // Меню управления сотрудниками
    private static void employeeMenu() {
        while (true) {
            clearConsole();
            System.out.println("=== Управление сотрудниками ===");
            System.out.println("1. Создать нового сотрудника");
            System.out.println("2. Просмотреть список сотрудников");
            System.out.println("3. Изменить должность сотрудника");
            System.out.println("4. Изменить зарплату сотрудника");
            System.out.println("5. Уволить сотрудника");
            System.out.println("6. Назад");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createEmployee();
                    break;
                case 2:
                    listEmployees();
                    pause();
                    break;
                case 3:
                    changeEmployeePosition();
                    break;
                case 4:
                    changeEmployeeSalary();
                    break;
                case 5:
                    dismissEmployee();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    pause();
            }
        }
    }
    // уволить сотрудника
    private static void dismissEmployee() {
        listEmployees();
        if (employees.isEmpty()) {
            pause();
            return;
        }

        System.out.print("Введите ID сотрудника для увольнения: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Сотрудник с таким ID не найден.");
            pause();
            return;
        }

        boolean isManagerInWarehouse = false;
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getManager() != null && warehouse.getManager().equals(employee)) {
                isManagerInWarehouse = true;
                System.out.println("Сотрудник является менеджером склада ID " + warehouse.getId());

                // Назначаем нового менеджера
                Employee newManager = chooseNewManager(warehouses);
                if (newManager == null) {
                    System.out.println("Не удалось уволить сотрудника: не выбран новый менеджер.");
                    continue;
                }

                warehouse.dismissManagerAndAssignNew(employee, newManager);
            } else {
                warehouse.dismissEmployee(employee);
            }
        }

        boolean isManagerInSalesPoint = false;
        for (SalesPoint salesPoint : salesPoints) {
            if (salesPoint.getManager() != null && salesPoint.getManager().equals(employee)) {
                isManagerInSalesPoint = true;
                System.out.println("Сотрудник является менеджером пункта продаж ID " + salesPoint.getId());

                Employee newManager = chooseNewManager(salesPoints);
                if (newManager == null) {
                    System.out.println("Не удалось уволить сотрудника: не выбран новый менеджер.");
                    continue;
                }

                salesPoint.dismissManagerAndAssignNew(employee, newManager);
            } else {
                salesPoint.DismissEmployee(employee);
            }
        }

        employees.remove(employee);
        System.out.println("Сотрудник " + employee.getName() + " успешно уволен.");
        pause();
    }
    // меняю должность сотрудника
    private static void changeEmployeePosition() {
        listEmployees();
        if (employees.isEmpty()) {
            pause();
            return;
        }
        System.out.print("Введите ID сотрудника для изменения должности: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Сотрудник с таким ID не найден.");
            pause();
            return;
        }
        System.out.print("Введите новую должность: ");
        String newPosition = scanner.nextLine();
        employee.setPosition(newPosition);
        System.out.println("Должность успешно изменена.");
        pause();
    }
    // меняю зарплату сотрудника
    private static void changeEmployeeSalary() {
        listEmployees();
        if (employees.isEmpty()) {
            pause();
            return;
        }
        System.out.print("Введите ID сотрудника для изменения зарплаты: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Сотрудник с таким ID не найден.");
            pause();
            return;
        }
        System.out.print("Введите новую зарплату: ");
        int newSalary = scanner.nextInt();
        scanner.nextLine();
        employee.setSalary(newSalary);
        System.out.println("Зарплата успешно изменена.");
        pause();
    }
    // Создание сотрудника
    private static void createEmployee() {
        System.out.print("Введите ID сотрудника: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите ID места работы сотрудника: ");
        listSalesPoints();
        listWarehouses();
        int workplaceId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите имя сотрудника: ");
        String name = scanner.nextLine();
        System.out.print("Введите должность: ");
        String position = scanner.nextLine();
        System.out.print("Введите зарплату: ");
        int salary = scanner.nextInt();
        scanner.nextLine();
        Employee employee = new Employee(id, workplaceId, name, position, salary);
        employees.add(employee);
        System.out.println("Сотрудник успешно создан!");
        pause();
    }
    // метод выбора нового менеджера
    private static Employee chooseNewManager(List<? extends Workplace> workplaces) {
        List<Employee> availableManagers = new ArrayList<>();
        for (Workplace workplace : workplaces) {
            availableManagers.addAll(workplace.getWorkers());
        }

        if (availableManagers.isEmpty()) {
            System.out.println("Нет доступных сотрудников для назначения менеджером.");
            return null;
        }

        System.out.println("Доступные кандидаты на должность менеджера:");
        for (Employee emp : availableManagers) {
            System.out.println("ID: " + emp.getId() + ", Имя: " + emp.getName());
        }

        System.out.print("Введите ID нового менеджера: ");
        int newManagerId = scanner.nextInt();
        scanner.nextLine();

        return findEmployeeById(newManagerId);
    }
    // Вывожу список сотрудников
    private static void listEmployees() {
        if (employees.isEmpty()) {
            System.out.println("Нет доступных сотрудников");
        } else {
            for (Employee emp : employees) {
                System.out.println(emp.toString());
            }
        }
    }

    // Вывожу меню клиентов
    private static void customerMenu() {
        while (true) {
            clearConsole();
            System.out.println("=== Управление клиентами ===");
            System.out.println("1. Добавить нового клиента");
            System.out.println("2. Просмотреть список клиентов");
            System.out.println("3. Просмотреть историю покупок клиента");
            System.out.println("4. Просмотреть историю покупок всех клиентов");
            System.out.println("5. Назад");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    listCustomers();
                    pause();
                    break;
                case 3:
                    viewCustomerPurchaseHistory();
                    break;
                case 4:
                    viewAllCustomerPurchaseHistory();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    pause();
            }
        }
    }
    // Добавляю клиента
    private static void addCustomer() {
        System.out.print("Введите ID клиента: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите имя клиента: ");
        String name = scanner.nextLine();
        System.out.print("Введите контактную информацию: ");
        String contactInfo = scanner.nextLine();
        Customer customer = new Customer(id, name, contactInfo);
        customers.add(customer);
        System.out.println("Клиент успешно добавлен!");
        pause();
    }
    // Вывожу список клиентов
    private static void listCustomers() {
        if (customers.isEmpty()) {
            System.out.println("Нет доступных клиентов");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer.toString());
            }
        }
    }
    // Вывожу список покупок клиента по id
    private static void viewCustomerPurchaseHistory() {
        listCustomers();
        if (customers.isEmpty()) {
            pause();
            return;
        }
        System.out.print("Введите ID клиента: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Клиент с таким ID не найден.");
            pause();
            return;
        }
        System.out.println("=== История покупок клиента " + customer.getName() + " ===");
        if (customer.getPurchaseHistory().isEmpty()) {
            System.out.println("У этого клиента нет истории покупок.");
        } else {
            for (Map.Entry<Product, Integer> entry : customer.getPurchaseHistory().entrySet()) {
                System.out.println("Товар: " + entry.getKey().getName() + ", Количество: " + entry.getValue());
            }
        }
        pause();
    }
    // Вывожу общую историю покупок клиентов
    private static void viewAllCustomerPurchaseHistory() {
        if (customers.isEmpty()) {
            System.out.println("Нет доступных клиентов");
            pause();
            return;
        }
        System.out.println("=== История покупок всех клиентов ===");
        for (Customer customer : customers) {
            System.out.println("Клиент: " + customer.getName());
            if (customer.getPurchaseHistory().isEmpty()) {
                System.out.println("Нет истории покупок.");
            } else {
                for (Map.Entry<Product, Integer> entry : customer.getPurchaseHistory().entrySet()) {
                    System.out.println("Товар: " + entry.getKey().getName() + ", Количество: " + entry.getValue());
                }
            }
            System.out.println("--------------------------------------");
        }
        pause();
    }
}