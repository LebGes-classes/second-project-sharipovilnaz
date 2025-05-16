package AppLaunch;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import Workplace.*;
import Items.Product;
import StorageCell.StorageCell;
import People.Customer;
import People.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataRecordingManager {

    public static void writeAllData(String filePath, List<Warehouse> warehouses, List<SalesPoint> salesPoints, List<Customer> clients, List<Employee> employees, List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Записываем склады
            Sheet sheetWarehouses = workbook.createSheet("Склады");
            Row headerRowWarehouses = sheetWarehouses.createRow(0);
            headerRowWarehouses.createCell(0).setCellValue("Id");
            headerRowWarehouses.createCell(1).setCellValue("Адрес");
            headerRowWarehouses.createCell(2).setCellValue("Менеджер");
            headerRowWarehouses.createCell(3).setCellValue("Бюджет");
            headerRowWarehouses.createCell(4).setCellValue("Активен");

            int rowNumWarehouses = 1;
            for (Warehouse warehouse : warehouses) {
                Row rowWarehouses = sheetWarehouses.createRow(rowNumWarehouses++);
                rowWarehouses.createCell(0).setCellValue(warehouse.getId());
                rowWarehouses.createCell(1).setCellValue(warehouse.getAddress());
                rowWarehouses.createCell(2).setCellValue(warehouse.getManager() != null ? warehouse.getManager().getId() : -1);
                rowWarehouses.createCell(3).setCellValue(warehouse.getBudget());
                rowWarehouses.createCell(4).setCellValue(warehouse.isActive() ? "Да" : "Нет");
            }

            // Записываем пункты продаж
            Sheet sheetSalesPoints = workbook.createSheet("Пункты продаж");
            Row headerRowSalesPoints = sheetSalesPoints.createRow(0);
            headerRowSalesPoints.createCell(0).setCellValue("Id");
            headerRowSalesPoints.createCell(1).setCellValue("Адрес");
            headerRowSalesPoints.createCell(2).setCellValue("Менеджер");
            headerRowSalesPoints.createCell(3).setCellValue("Бюджет");
            headerRowSalesPoints.createCell(4).setCellValue("Доход");
            headerRowSalesPoints.createCell(5).setCellValue("Связанные склады");
            headerRowSalesPoints.createCell(6).setCellValue("Активен");

            int rowNumSalesPoints = 1;
            for (SalesPoint salesPoint : salesPoints) {
                Row rowSalesPoints = sheetSalesPoints.createRow(rowNumSalesPoints++);
                rowSalesPoints.createCell(0).setCellValue(salesPoint.getId());
                rowSalesPoints.createCell(1).setCellValue(salesPoint.getAddress());
                rowSalesPoints.createCell(2).setCellValue(salesPoint.getManager() != null ? salesPoint.getManager().getId() : -1);
                rowSalesPoints.createCell(3).setCellValue(salesPoint.getBudget());
                rowSalesPoints.createCell(4).setCellValue(salesPoint.getRevenue());
                StringBuilder sb = new StringBuilder();
                for (Warehouse wh : salesPoint.getWarehouses()) {
                    sb.append(wh.getId()).append(" ");
                }
                rowSalesPoints.createCell(5).setCellValue(sb.toString().trim());
                rowSalesPoints.createCell(6).setCellValue(salesPoint.isActive() ? "Да" : "Нет");
            }

            // Записываем товары
            Sheet sheetProducts = workbook.createSheet("Товары");
            Row headerRowProducts = sheetProducts.createRow(0);
            headerRowProducts.createCell(0).setCellValue("Id");
            headerRowProducts.createCell(1).setCellValue("Название");
            headerRowProducts.createCell(2).setCellValue("Цена");
            headerRowProducts.createCell(3).setCellValue("Категория");

            int rowNumProducts = 1;
            for (Product product : products) {
                Row rowProducts = sheetProducts.createRow(rowNumProducts++);
                rowProducts.createCell(0).setCellValue(product.getId());
                rowProducts.createCell(1).setCellValue(product.getName());
                rowProducts.createCell(2).setCellValue(product.getPrice());
                rowProducts.createCell(3).setCellValue(product.getCategory());
            }

            // Записываем сотрудников
            Sheet sheetEmployees = workbook.createSheet("Сотрудники");
            Row headerRowEmployees = sheetEmployees.createRow(0);
            headerRowEmployees.createCell(0).setCellValue("Id");
            headerRowEmployees.createCell(1).setCellValue("ФИО");
            headerRowEmployees.createCell(2).setCellValue("Должность");
            headerRowEmployees.createCell(3).setCellValue("Id места работы");
            headerRowEmployees.createCell(4).setCellValue("Зарплата");

            int rowNumEmployees = 1;
            for (Employee employee : employees) {
                Row rowEmployees = sheetEmployees.createRow(rowNumEmployees++);
                rowEmployees.createCell(0).setCellValue(employee.getId());
                rowEmployees.createCell(1).setCellValue(employee.getName());
                rowEmployees.createCell(2).setCellValue(employee.getPosition());
                rowEmployees.createCell(3).setCellValue(employee.getWorkplaceId());
                rowEmployees.createCell(4).setCellValue(employee.getSalary());
            }

            // Записываем клиентов
            Sheet sheetCustomers = workbook.createSheet("Клиенты");
            Row headerRowCustomers = sheetCustomers.createRow(0);
            headerRowCustomers.createCell(0).setCellValue("Id");
            headerRowCustomers.createCell(1).setCellValue("ФИО");
            headerRowCustomers.createCell(2).setCellValue("Контактные данные");
            headerRowCustomers.createCell(3).setCellValue("Баланс");

            int rowNumCustomers = 1;
            for (Customer client : clients) {
                Row rowCustomers = sheetCustomers.createRow(rowNumCustomers++);
                rowCustomers.createCell(0).setCellValue(client.getId());
                rowCustomers.createCell(1).setCellValue(client.getName());
                rowCustomers.createCell(2).setCellValue(client.getContactInfo());
                rowCustomers.createCell(3).setCellValue(client.getBalance());
            }

            // Записываем историю покупок клиентов
            Sheet sheetCustomersPurchaseInfo = workbook.createSheet("История покупок клиентов");
            Row headerRowPurchaseInfo = sheetCustomersPurchaseInfo.createRow(0);
            headerRowPurchaseInfo.createCell(0).setCellValue("Id клиента");
            headerRowPurchaseInfo.createCell(1).setCellValue("Id товара");
            headerRowPurchaseInfo.createCell(2).setCellValue("Количество");

            int rowNumPurchase = 1;
            for (Customer client : clients) {
                for (Entry<Product, Integer> entry : client.getPurchaseHistory().entrySet()) {
                    Row rowPurchaseHistory = sheetCustomersPurchaseInfo.createRow(rowNumPurchase++);
                    rowPurchaseHistory.createCell(0).setCellValue(client.getId());
                    rowPurchaseHistory.createCell(1).setCellValue(entry.getKey().getId());
                    rowPurchaseHistory.createCell(2).setCellValue(entry.getValue());
                }
            }
            // записываем подробную информацию о товарах
            Sheet sheetInformationAboutProducts = workbook.createSheet("Информация о товарах");
            Row headerRow = sheetInformationAboutProducts.createRow(0);
            headerRow.createCell(0).setCellValue("Id товара");
            headerRow.createCell(1).setCellValue("Id предприятия");
            headerRow.createCell(2).setCellValue("Тип предприятия");
            headerRow.createCell(3).setCellValue("Id ячейки");
            headerRow.createCell(4).setCellValue("Количество");
        
            int rowNumInfo = 1;
            for (Warehouse warehouse : warehouses) {
                for (StorageCell cell : warehouse.getCells()) {
                    Row row = sheetInformationAboutProducts.createRow(rowNumInfo++);
                    row.createCell(0).setCellValue(cell.getProduct().getId());
                    row.createCell(1).setCellValue(warehouse.getId());
                    row.createCell(2).setCellValue(0); 
                    row.createCell(3).setCellValue(cell.getCellId());
                    row.createCell(4).setCellValue(cell.getQuantity());
                }
            }
        
            for (SalesPoint salesPoint : salesPoints) {
                for (StorageCell cell : salesPoint.getCells()) {
                    Row row = sheetInformationAboutProducts.createRow(rowNumInfo++);
                    row.createCell(0).setCellValue(cell.getProduct().getId());
                    row.createCell(1).setCellValue(salesPoint.getId());
                    row.createCell(2).setCellValue(1);
                    row.createCell(3).setCellValue(cell.getCellId());
                    row.createCell(4).setCellValue(cell.getQuantity());
                }
            }
            // Автоматическое изменение ширины столбцов
            autoSizeColumns(sheetWarehouses, 5);
            autoSizeColumns(sheetSalesPoints, 7);
            autoSizeColumns(sheetProducts, 4);
            autoSizeColumns(sheetEmployees, 6);
            autoSizeColumns(sheetCustomers, 4);
            autoSizeColumns(sheetCustomersPurchaseInfo, 3);
            autoSizeColumns(sheetInformationAboutProducts, 5);

            // Сохраняем файл
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("Данные успешно сохранены в файл: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для автоматического изменения ширины столбцов
    public static void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}