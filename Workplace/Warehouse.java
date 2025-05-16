package Workplace;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

import StorageCell.StorageCell;
import People.Employee;
import Items.Product;

import java.util.ArrayList;

public class Warehouse implements Serializable, Workplace {
    private int id;
    private String address;
    private double budget;
    private Employee manager;
    private List<StorageCell> cells;
    private List<Employee> workers;
    private boolean isActive;

    public Warehouse(int id, String address, Employee manager) {
        this.id = id;
        this.address = address;
        this.budget = 1000;
        this.manager = manager;
        this.cells = new ArrayList<>();
        this.workers = new ArrayList<>();
        workers.add(manager);
        isActive = true;
    }

    public Warehouse() {
        this.id = 0;
        this.address = "Не указан";
        this.budget = 0;
        this.manager = null;
        this.cells = new ArrayList<>();
        this.workers = new ArrayList<>();
        this.isActive = true;
    }
    // добавить работника
    public void addWorker(Employee employee) {
        workers.add(employee);
        employee.setWorkplaceId(this.id);
    }
    // убрать работника
    public void removeWorker(Employee employee) {
        workers.remove(employee);
        employee.setWorkplaceId(-1);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Employee getManager() { return manager; }
    public void setManager(Employee manager) { this.manager = manager; }
    public List<StorageCell> getCells() { return cells; }
    public boolean isActive() { return isActive; }
    public void setBudget(double budget) {this.budget = budget;}
    public double getBudget() {return budget;}
    public void openWarehouse() {this.isActive = true;}
    public void closeWarehouse() {this.isActive = false;}
    public List<Employee> getWorkers() {return workers;}

    public void setActive(boolean active) {
        isActive = active;
    }

    // Добавляем товар в ячейку, если ее нет создаем новую (вспомогательный метод для закупки)
    public void addProduct(Product product, int cellId, int quantity) {
        if (product == null || quantity <= 0 || cellId <= 0) {
            throw new IllegalArgumentException("Некорректные данные для добавления товара.");
        }

        // Ищем существующую ячейку
        for (StorageCell cell : cells) {
            if (cell.getCellId() == cellId) {
                // Ячейка найдена, обновляем количество
                if (!cell.getProduct().equals(product)) {
                    throw new IllegalArgumentException("Ячейка с ID " + cellId + " занята другим товаром.");
                }
                cell.setQuantity(cell.getQuantity() + quantity);
                System.out.println("Товар " + product.getName() + " добавлен в существующую ячейку ID: " + cellId);
                return;
            }
        }

        // Ячейка не найдена — создаём новую
        StorageCell newCell = new StorageCell(cellId, quantity, product, true);
        cells.add(newCell);
        System.out.println("Создана новая ячейка ID: " + cellId + " с товаром " + product.getName());
    }
    // Убрать товар со склада
    public void removeProduct(int cellId, int quantity) throws Exception {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }

        StorageCell targetCell = null;
        for (StorageCell cell : cells) {
            if (cell.getCellId() == cellId) {
                targetCell = cell;
                break;
            }
        }

        if (targetCell == null) {
            throw new Exception("Ячейка с ID " + cellId + " не найдена.");
        }

        if (targetCell.getQuantity() < quantity) {
            throw new Exception("Недостаточно товара в ячейке. Доступно: " + targetCell.getQuantity());
        }

        targetCell.setQuantity(targetCell.getQuantity() - quantity);

        if (targetCell.getQuantity() == 0) {
            cells.remove(targetCell);
        }

        System.out.println("Удалено " + quantity + " ед. товара '" + targetCell.getProduct().getName() + "' из ячейки ID " + cellId);
    }
    // Количество доступного товара
    public int getAllQuantityOfProduct(Product product) { // Возвращает общее количество определенного товара во всем складе
        int quantitySum = 0;
        for (StorageCell cell : cells) {
            if (cell.getProduct().equals(product)) {
                quantitySum += cell.getQuantity();
            }
        }
        return quantitySum;
    }
    // Закупить товар
    public void purchaseProduct(Product product, int quantity) {
        if (product == null) {
            throw new NullPointerException("Товар не может быть null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }

        double totalCost = quantity * product.getPrice();
        if (budget < totalCost) {
            throw new IllegalStateException("Недостаточно средств на складе для закупки. Требуется: " + totalCost + ", доступно: " + budget);
        }

        // Добавляем товар на склад
        addProduct(product, generateNewCellId(), quantity);

        // Уменьшаем бюджет склада
        budget -= totalCost;

        System.out.println("Закуплено " + quantity + " единиц товара " + product.getName() +  "Общий расход: " + totalCost);
    }
    public int generateNewCellId() {
        return cells.size() + 1;
    }
    // Увеличить бюджет
    public void addBudget(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }
        budget += amount;
        System.out.println("Бюджет склада увеличен на " + amount + ". Текущий бюджет: " + budget);
    }
    // Перемещение склада на новый адрес
    public void relocate(String newAdress) {
        if (!isActive) {
            throw new IllegalStateException("Ваш склад неактивен. Его нельзя переместить");
        }
        if (newAdress == null || newAdress.isEmpty()) {
            throw new IllegalStateException("Новый адрес не может быть пустым");
        }
        this.address = newAdress;
        System.out.println("Ваш склад успешно перемещен. Новый адрес: " + newAdress);
    }
    @Override
    public String toString() {
        return "Склад{" +
                "id=" + id +
                ", Адрес ='" + address + '\'' +
                ", Менеджер =" + manager +
                ", Ячейки =" + cells +
                ", Активен =" + isActive +
                '}';
    }
    // Информация о склада
    public void printWarehouseInfo() {
        System.out.println("=== Информация о складе ===");
        System.out.println("ID склада: " + id);
        System.out.println("Адрес: " + address);
        System.out.println("Менеджер склада: " + (manager != null ? manager.toString() : "Не назначен"));
        System.out.println("Активен: " + (isActive ? "Да" : "Нет"));

        if (cells.isEmpty()) {
            System.out.println("На складе нет товаров.");
        } else {
            System.out.println("На складе есть товары");
        }
        System.out.println("===========================");
    }
    public HashSet<Product> getAllProducts() {
        HashSet<Product> products = new HashSet<>();
        for (StorageCell cell : cells) {
            products.add(cell.getProduct());
        }
        return products;
    }
    // Информация о товарах на складе
    public void printInfoAboutProductsAtWarehouse() {
        System.out.println("=== Информация о товарах ===");
        for (StorageCell cell : cells) {
            Product product = cell.getProduct();
            System.out.println("- Ячейка ID: " + cell.getCellId() +
                    ", Товар: " + product.getName() +
                    ", Количество: " + cell.getQuantity() +
                    ", Цена: " + product.getPrice() +
                    ", Категория: " + product.getCategory());
        }
        System.out.println("===========================");
    }
    // Нанять нового сотрудника
    public void HireNewEmployee(Employee employee) {
        addWorker(employee);
    }
    // Уволить сотрудника
    public void dismissEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Сотрудник не может быть null");
        }

        // Если сотрудник является менеджером склада
        if (manager != null && manager.equals(employee)) {
            System.out.println("Сотрудник " + employee.getName() + " является менеджером этого склада.");
            throw new IllegalStateException("Невозможно уволить менеджера без назначения нового.");
        }

        workers.remove(employee);
        employee.setWorkplaceId(-1);
        System.out.println("Сотрудник " + employee.getName() + " удален со склада ID " + id);
    }
    // уволить менеджера и нанять нового
    public void dismissManagerAndAssignNew(Employee oldManager, Employee newManager) {
        if (oldManager == null || newManager == null) {
            throw new IllegalArgumentException("Менеджер не может быть null");
        }
        if (!manager.equals(oldManager)) {
            throw new IllegalArgumentException("Указанный сотрудник не является менеджером склада");
        }

        manager = newManager;
        workers.remove(oldManager);
        oldManager.setWorkplaceId(-1);

        System.out.println("Менеджер склада ID " + id + " изменен: " + oldManager.getName() + " → " + newManager.getName());
    }
}

