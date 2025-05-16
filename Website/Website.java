package Website;


import java.util.Map;
import java.util.List;
import java.util.Scanner;
import AppLaunch.Interface;
import Workplace.SalesPoint;
import People.Customer;
import Items.Product;
import StorageCell.StorageCell;

public class Website {
    private List<Customer> customers;
    private SalesPoint salesPoint;
    private Scanner scanner;

    public Website(SalesPoint salesPoint, List<Customer> customers) {
        this.customers = customers;
        this.salesPoint = salesPoint;
        this.scanner = new Scanner(System.in);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public SalesPoint getSalesPoint() {
        return salesPoint;
    }

    public void registerCustomer() {
        System.out.println("=== Регистрация ===");
        System.out.println("Введите ваш ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите ваше имя: ");
        String name = scanner.nextLine();
        System.out.println("Введите ваши контактные данные: ");
        String contactInfo = scanner.nextLine();
        Customer customer = new Customer(id, name, contactInfo);
        customers.add(customer);
        System.out.println("Клиент с именем " + name + " успешно зарегистрирован");
    }

    public Customer loginCustomer() {
        System.out.println("Введите ваше ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                System.out.println("Вход выполнен успешно. Добро пожаловать на наш сайт. Можете приступить к покупкам");
                return customer;
            }
        }
        System.out.println("Клиент с таким ID не найден");
        return null;
    }

    public void displayProducts() {
        System.out.println("=== Доступные товары ===");
        for (StorageCell cell : salesPoint.getCells()) {
            Product product = cell.getProduct();
            System.out.println(
                    "Название: " + product.getName() +
                            ", Id: " + product.getId() +
                            ", Цена: " + product.getPrice() +
                            ", Количество в наличии: " + cell.getQuantity()
            );
        }
    }

    public void purchaseProduct(Customer customer) {
        displayProducts();
        System.out.println("Введите ID продукта для покупки: ");
        int productID = scanner.nextInt();
        System.out.println("Введите количество: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        Product product = findProductById(productID);
        if (product == null) {
            System.out.println("Товар не найден.");
            return;
        }
        int availableQuantity = salesPoint.getAllQuantityOfProduct(product);
        if (availableQuantity < quantity) {
            System.out.println("Недостаточно товара. В наличии только: " + availableQuantity);
            return;
        }
        double totalCost = product.getPrice() * quantity;
        if (totalCost > customer.getBalance()) {
            System.out.println("У вас недостаточно средств. Пополните счет, чтобы продолжить покупку. Не хватает " + (totalCost - customer.getBalance()) + " рублей.");
            return;
        }
        customer.setBalance(customer.getBalance() - totalCost);
        salesPoint.sellProductByQuantity(product, quantity);
        customer.addPurchase(product, quantity);
        System.out.println("Покупка прошла успешно.");
    }

    public Product findProductById(int id) {
        for (StorageCell cell : salesPoint.getCells()) {
            Product product = cell.getProduct();
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public void run() {
        while (true) {
            System.out.println("=== Добро пожаловать в интернет магазин InStore ===");
            System.out.println("1. Зарегистрироваться.");
            System.out.println("2. Войти.");
            System.out.println("3. Выйти");
            System.out.println("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    Interface.clearConsole();
                    registerCustomer();
                    break;
                case 2:
                    Interface.clearConsole();
                    Customer customer = loginCustomer();
                    if (customer != null) {
                        customerMenu(customer);
                    }
                    break;
                case 3:
                    Interface.clearConsole();
                    System.out.println("До свидания. Будем ждать еще");
                    return;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }

    public void customerMenu(Customer customer) {
        while (true) {
            System.out.println("=== Личный кабинет ===");
            System.out.println("1. Просмотреть товары");
            System.out.println("2. Купить товар");
            System.out.println("3. Пополнить баланс");
            System.out.println("4. История покупок");
            System.out.println("5. Выйти");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    displayProducts();
                    break;
                case 2:
                    purchaseProduct(customer);
                    break;
                case 3:
                    System.out.print("Введите сумму для пополнения: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    customer.deposit(amount);
                    break;
                case 4:
                    System.out.println("=== История покупок ===");
                    for (Map.Entry<Product, Integer> entry : customer.getPurchaseHistory().entrySet()) {
                        System.out.println("Товар: " + entry.getKey().getName() + ". Количество: " + entry.getValue());
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
}