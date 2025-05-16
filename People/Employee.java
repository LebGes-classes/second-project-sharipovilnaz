package People;
import java.io.Serializable;

public class Employee implements Serializable {
    private int id;
    private int workplaceId;
    private String name;
    private String position;
    private int salary;

    public Employee(int id, int workplaceId,  String name, String position, int salary) {
        this.id = id;
        this.workplaceId = workplaceId;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }
    public int getId() {return id;}
    public int getWorkplaceId() {return workplaceId;}
    public void setWorkplaceId(int workplaceId) {this.workplaceId = workplaceId;}
    public String getName() {return name;}
    public String getPosition() {return position;}
    public int getSalary() {return salary;}
    public void setPosition(String position) {this.position = position;}
    public void setSalary(int salary) {this.salary = salary;}

    @Override
    public String toString() {
        return "Работник{" +
                "id=" + id +
                ", id места работы=" + workplaceId +
                ", Имя ='" + name + '\'' +
                ", Должность ='" + position + '\'' +
                ", Зарплата =" + salary +
                '}';
    }
}
