package Workplace;

import People.Employee;
import StorageCell.StorageCell;

import java.util.List;

public interface Workplace {
    int getId();// Получить ID места работы
    String getAddress();
    List<StorageCell> getCells();
    double getBudget();
    List<Employee> getWorkers();          // Получить список всех работников
    Employee getManager();                // Получить текущего менеджера
    void setManager(Employee employee);   // Назначить нового менеджера
    void addWorker(Employee employee);    // Добавить работника
    void removeWorker(Employee employee); // Уволить работника
}