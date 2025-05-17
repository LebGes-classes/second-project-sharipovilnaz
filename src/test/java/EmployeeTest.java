import People.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testEmployeeCreation() {
        Employee employee = new Employee(1, 101, "John Doe", "Manager", 50000);
        assertEquals(1, employee.getId());
        assertEquals(101, employee.getWorkplaceId());
        assertEquals("John Doe", employee.getName());
        assertEquals("Manager", employee.getPosition());
        assertEquals(50000, employee.getSalary());
    }

    @Test
    void testSetters() {
        Employee employee = new Employee(1, 101, "John Doe", "Manager", 50000);
        employee.setWorkplaceId(102);
        employee.setPosition("Senior Manager");
        employee.setSalary(60000);
        
        assertEquals(102, employee.getWorkplaceId());
        assertEquals("Senior Manager", employee.getPosition());
        assertEquals(60000, employee.getSalary());
    }

    @Test
    void testToString() {
        Employee employee = new Employee(1, 101, "John Doe", "Manager", 50000);
        String expected = "Работник{id=1, id места работы=101, Имя ='John Doe', Должность ='Manager', Зарплата =50000}";
        assertEquals(expected, employee.toString());
    }
}