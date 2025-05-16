package Workplace;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import StorageCell.StorageCell;
import People.Employee;
import Items.Product;

public class SalesPoint implements Serializable, Workplace {
    private int id; // айди пункта продаж
    private String address; // адрес пункта продаж
    private Employee manager; // менеджер пункта продаж
    private List<StorageCell> cells; // ячейки хранения товаров
    private boolean isActive; // активен ли склад
    private double budget; // бюджет пункта продаж
    private double revenue; // доход пункта продаж
    private List<Warehouse> warehouses; // склады пункта продаж
    private List<Employee> workers; // работники пункта продаж

    public SalesPoint(int id, String address, Employee manager) {
        this.id = id;
        this.address = address;
        this.budget = 1000; // начальный бюджет 1000 рублей
        this.manager = manager;
        this.cells = new ArrayList<>();
        this.warehouses = new ArrayList<>();
        this.workers = new ArrayList<>();
        this.isActive = true;
        this.revenue = 0;
    }

    @Override
    public int getId() { return id; }

    public String getAddress() { return address; }

    public List<StorageCell> getCells() { return cells; }
    public boolean isActive() { return isActive; }
    public double getRevenue() { return revenue; }
    public double getBudget() { return budget; }
    public List<Warehouse> getWarehouses() { return warehouses; }

    @Override
    public List<Employee> getWorkers() { return  workers; }

    @Override
    public Employee getManager() { return manager; }

    @Override
    public void setManager(Employee manager) { // Ставим менеджера на пункт продаж
        if (manager == null) {
            throw new NullPointerException("Менеджер не может быть null");
        }
        this.manager = manager;
    }

    @Override
    public void addWorker(Employee employee) {
        workers.add(employee);
    }

    @Override
    public void removeWorker(Employee employee) {
        workers.remove(employee);
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    // Открыть или закрыть пункт продаж
    public void openSalesPoint() { this.isActive = true; }
    public void closeSalesPoint() { this.isActive = false; }

    // Добавление и удаление складов
    public void addWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new NullPointerException("Склад не может быть null");
        }
        warehouses.add(warehouse);
        System.out.println("Склад ID " + warehouse.getId() + " добавлен к пункту продаж.");
    }

    public void removeWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new NullPointerException("Склад не может быть null");
        }
        if (!warehouses.contains(warehouse)) {
            throw new IllegalArgumentException("Склад не найден в списке складов пункта продаж.");
        }
        warehouses.remove(warehouse);
        System.out.println("Склад ID " + warehouse.getId() + " удален из пункта продаж.");
    }

    // перекинуть товары со склада на пункт продаж
    public void addProductFromWarehouse(int cellId, int quantityToMove, Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Склад не может быть null");
        }

        StorageCell sourceCell = null;
        for (StorageCell cell : warehouse.getCells()) {
            if (cell.getCellId() == cellId) {
                sourceCell = cell;
                break;
            }
        }

        if (sourceCell == null) {
            throw new IllegalArgumentException("Ячейка с ID " + cellId + " не найдена на указанном складе.");
        }

        Product product = sourceCell.getProduct();
        int availableQuantity = sourceCell.getQuantity();

        if (availableQuantity < quantityToMove) {
            throw new IllegalArgumentException("Недостаточно товара в ячейке. Доступно: " + availableQuantity);
        }

        // Уменьшаем количество товара на складе
        if (availableQuantity == quantityToMove) {
            warehouse.getCells().remove(sourceCell); // Полностью убираем ячейку
        } else {
            sourceCell.setQuantity(availableQuantity - quantityToMove);
        }

        // Добавляем товар в пункт продаж
        boolean added = false;
        for (StorageCell cell : cells) {
            if (cell.getProduct().equals(product)) {
                cell.setQuantity(cell.getQuantity() + quantityToMove);
                added = true;
                break;
            }
        }

        if (!added) {
            cells.add(new StorageCell(generateNewCellId(), quantityToMove, product, false));
        }

        System.out.println("Перемещено " + quantityToMove + " шт. товара \"" + product.getName() + "\" из ячейки " + cellId + ".");
    }
    // убрать товар
    public void removeProduct(Product product, int quantity){
        if (product == null) {
            throw new IllegalArgumentException("Товар не может быть null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля.");
        }

        int totalAvailable = getAllQuantityOfProduct(product);
        if (totalAvailable < quantity) {
            throw new IllegalArgumentException("Недостаточно товара '" + product.getName() + "' в пункте продаж. Доступно: " + totalAvailable);
        }

        Iterator<StorageCell> iterator = cells.iterator();
        while (iterator.hasNext() && quantity > 0) {
            StorageCell cell = iterator.next();
            if (cell.getProduct().equals(product)) {
                int availableInCell = cell.getQuantity();
                if (availableInCell <= quantity) {
                    quantity -= availableInCell;
                    iterator.remove(); // удаляем ячейку
                } else {
                    cell.setQuantity(availableInCell - quantity);
                    quantity = 0;
                }
            }
        }

        System.out.println("Удалено " + quantity + " ед. товара '" + product.getName() + "'.");
    }
    // вернуть товар на склад
    public void returnProductToWarehouse(Warehouse warehouse, int cellId, int quantity){
        if (warehouse == null) {
            throw new IllegalArgumentException("Склад не может быть null");
        }
        if (!warehouses.contains(warehouse)) {
            throw new IllegalArgumentException("Склад не прикреплён к этому пункту продаж");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }

        StorageCell sourceCell = null;
        for (StorageCell cell : cells) {
            if (cell.getCellId() == cellId) {
                sourceCell = cell;
                break;
            }
        }

        if (sourceCell == null) {
            throw new IllegalStateException("Ячейка с ID " + cellId + " не найдена.");
        }

        if (sourceCell.getQuantity() < quantity) {
            throw new IllegalArgumentException("Недостаточно товара в ячейке. Доступно: " + sourceCell.getQuantity());
        }

        // Уменьшаем количество в ячейке пункта продаж
        sourceCell.setQuantity(sourceCell.getQuantity() - quantity);

        // Пытаемся добавить товар в ячейку склада
        boolean added = false;
        for (StorageCell warehouseCell : warehouse.getCells()) {
            if (warehouseCell.getProduct().equals(sourceCell.getProduct())) {
                warehouseCell.setQuantity(warehouseCell.getQuantity() + quantity);
                added = true;
                break;
            }
        }

        if (!added) {
            warehouse.getCells().add(new StorageCell(generateNewCellIdForWarehouse(warehouse), quantity, sourceCell.getProduct(), true));
        }

        // Если ячейка опустела — удаляем её
        if (sourceCell.getQuantity() == 0) {
            cells.remove(sourceCell);
        }

        System.out.println("Товар \"" + sourceCell.getProduct().getName() + "\" успешно возвращён на склад ID " + warehouse.getId());
    }

    // Вспомогательный метод для генерации нового ID ячейки на складе
    private int generateNewCellIdForWarehouse(Warehouse warehouse) {
        int maxId = 0;
        for (StorageCell cell : warehouse.getCells()) {
            if (cell.getCellId() > maxId) {
                maxId = cell.getCellId();
            }
        }
        return maxId + 1;
    }

    // Продажа всех товаров
    public void sellAllProduction() {
        for (StorageCell cell : cells) {
            revenue += (cell.getProduct().getPrice()) * cell.getQuantity();
        }
        cells.clear();
        System.out.println("Проданы все товары. Теперь вы богатый, но без товаров :(");
    }
    // Продаже всего конкретного товара
    public void sellAllProductsOfThisType(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Товар не может быть null.");
        }

        boolean found = false;
        Iterator<StorageCell> iterator = cells.iterator();
        while (iterator.hasNext()) {
            StorageCell cell = iterator.next();
            if (cell.getProduct().equals(product)) {
                revenue += cell.getProduct().getPrice() * cell.getQuantity(); // обновляем доход
                iterator.remove(); // удаляем ячейку
                found = true;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Товар '" + product.getName() + "' не найден в пункте продаж.");
        }

        System.out.println("Все товары '" + product.getName() + "' успешно проданы.");
    }
    
    // Продать товар по количеству
    public void sellProductByQuantity(Product product, int requestedQuantity) {
        if (product == null || requestedQuantity <= 0) {
            throw new IllegalArgumentException("Товар или количество указаны некорректно.");
        }

        int soldQuantity = 0; // будем отслеживать, сколько реально продали

        Iterator<StorageCell> iterator = cells.iterator();
        while (iterator.hasNext() && requestedQuantity > 0) {
            StorageCell cell = iterator.next();
            if (cell.getProduct().equals(product)) {
                int available = cell.getQuantity();

                if (available <= requestedQuantity) {
                    soldQuantity += available;
                    revenue += product.getPrice() * available;
                    iterator.remove(); // удаляем ячейку
                } else {
                    cell.setQuantity(available - requestedQuantity);
                    soldQuantity += requestedQuantity;
                    revenue += product.getPrice() * requestedQuantity;
                    requestedQuantity = 0;
                }
            }
        }

        if (soldQuantity > 0) {
            System.out.println("Продано товара " + product.getName() + " в количестве " + soldQuantity);
        } else {
            System.out.println("Нет товара '" + product.getName() + "' для продажи.");
        }
    }
    // Увеличить бюджет
    public void addBudget(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }
        budget += amount;
        System.out.println("Бюджет пункта продаж увеличен на " + amount + ". Текущий бюджет: " + budget);
    }
    // Отправить деньги на конкретный склад
    public void sendMoneyToWarehouse(Warehouse warehouse, double money) {
        if (warehouse == null) {
            throw new NullPointerException("Склад не может быть null");
        }
        if (money <= 0) {
            throw new IllegalArgumentException("Деньги, которые вы хотите отправить на склад должны быть больше 0");
        }
        if (budget < money) {
            throw new IllegalStateException("Недостаточно средств для перевода");
        }
        budget -= money;
        warehouse.setBudget(warehouse.getBudget() + money);
        System.out.println("Переведено " + money + " денег на склад " + warehouse.getId());
    }
    // уволить сотрудника
    public void DismissEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Сотрудник не может быть null");
        }

        if (manager != null && manager.equals(employee)) {
            System.out.println("Сотрудник " + employee.getName() + " является менеджером пункта продаж.");
            throw new IllegalStateException("Невозможно уволить менеджера без назначения нового.");
        }

        workers.remove(employee);
        employee.setWorkplaceId(-1); // Сбрасываем ID места работы
        System.out.println("Сотрудник " + employee.getName() + " удален из пункта продаж ID " + id);
    }
    // уволить менеджера и нанять нового
    public void dismissManagerAndAssignNew(Employee oldManager, Employee newManager) {
        if (oldManager == null || newManager == null) {
            throw new IllegalArgumentException("Менеджер не может быть null");
        }
        if (!manager.equals(oldManager)) {
            throw new IllegalArgumentException("Указанный сотрудник не является менеджером пункта продаж");
        }

        manager = newManager;
        workers.remove(oldManager);
        oldManager.setWorkplaceId(-1);

        System.out.println("Менеджер пункта продаж ID " + id + " изменен: " + oldManager.getName() + " → " + newManager.getName());
    }
    // Информация о доступных товарах для закупки
    public void printAvailableProductsForPurchase() {
        System.out.println("=== Доступные товары для закупки ===");
        for (Warehouse warehouse : warehouses) {
            System.out.println("Склад ID: " + warehouse.getId());
            for (StorageCell cell : warehouse.getCells()) {
                Product product = cell.getProduct();
                System.out.println("- Товар: " + product.getName() +
                        ", Id товара: " + product.getId() +
                        ", Количество: " + cell.getQuantity() +
                        ", Цена: " + product.getPrice() +
                        ", Категория: " + product.getCategory());
            }
        }
        System.out.println("===========================");
    }

    // Информация о доходности предприятия
    public void printEnterpriseRevenueInfo() {
        System.out.println("=== Информация о доходности ===");
        System.out.println("Общий доход пункта продаж: " + revenue + " рублей");
        System.out.println("Текущий бюджет пункта продаж: " + budget + " рублей");

        double totalBudget = budget; // Общий бюджет с учетом всех складов
        for (Warehouse warehouse : warehouses) {
            totalBudget += warehouse.getBudget();
            System.out.println("Бюджет склада ID " + warehouse.getId() + ": " + warehouse.getBudget() + " рублей");
        }

        System.out.println("Общий бюджет предприятия: " + totalBudget + " рублей");
        System.out.println("===========================");
    }

    // Количество доступного товара
    public int getAllQuantityOfProduct(Product product) {
        if (product == null) {
            throw new NullPointerException("Товар не может быть null");
        }
        int quantitySum = 0;
        for (StorageCell cell : cells) {
            if (cell.getProduct().equals(product)) {
                quantitySum += cell.getQuantity();
            }
        }
        return quantitySum;
    }

    public int generateNewCellId() {
        return cells.size() + 1;
    }

    // Перемещение пункта продаж на новый адрес
    public void relocate(String newAddress) {
        if (!isActive) {
            throw new IllegalStateException("Ваш пункт продаж неактивен. Его нельзя переместить");
        }
        if (newAddress == null || newAddress.isEmpty()) {
            throw new IllegalStateException("Новый адрес не может быть пустым");
        }
        this.address = newAddress;
        System.out.println("Ваш пункт продаж успешно перемещен. Новый адрес: " + newAddress);
    }
    // Нанять нового сотрудника
    public void HireNewEmployee(Employee employee) {
        workers.add(employee);
    }
    // Вся информация о доступных товарах на пункте продаж
    public void printInfoAboutProductsAtSalesPoint() {
        System.out.println("=== Информация о товарах ===");
        for (StorageCell cell : cells) {
            Product product = cell.getProduct();
            System.out.println("- Ячейка ID: " + cell.getCellId() +
                    ", Товар: " + product.getName() +
                    ", Id товара: " + product.getId() +
                    ", Количество: " + cell.getQuantity() +
                    ", Цена: " + product.getPrice() +
                    ", Категория: " + product.getCategory());
        }
        System.out.println("===========================");
    }
    // Вывод информации о пункте продаж
    public void printInfoAboutSalesPoint() {
        System.out.println("=== Информация о пункте продаж ===");
        System.out.println("ID: " + id);
        System.out.println("Адрес: " + address);
        System.out.println("Менеджер: " + (manager != null ? manager.toString() : "Не назначен"));
        System.out.println("Доход: " + revenue + " рублей");
        System.out.println("Бюджет: " + budget + " рублей");
        System.out.println("Склады: ");
        for (Warehouse warehouse : warehouses) {
            System.out.println("- Склад ID: " + warehouse.getId() + ", Адрес: " + warehouse.getAddress());
        }
        System.out.println("Активен: " + (isActive ? "Да" : "Нет"));
        System.out.println("===========================");
    }
}