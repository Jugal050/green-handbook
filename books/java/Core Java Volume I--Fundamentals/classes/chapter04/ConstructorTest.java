package chapter04;

import java.util.Random;

/**
 * Constructor Test
 * @author binvi
 * @version 1.0
 */
public class ConstructorTest {

    public static void main(String[] args) {
        Employee[] staff = new Employee[3];
        staff[0] = new Employee("green", 1000);
        staff[1] = new Employee(2000);
        staff[2] = new Employee();

        for (Employee employee : staff) {
            System.out.printf("name=%s, id=%d, salary=%.2f\n",
                    employee.getName(), employee.getId(), employee.getSalary());
        }
    }

}

class Employee {
    private static int nextId;

    private int id;
    private String name = ""; // instance field initialization
    private double salary;

    // static initialization block
    static
    {
        Random generator = new Random();
        nextId = generator.nextInt(10000);
    }

    // object initialization block
    {
        id = nextId;
        nextId++;
    }

    // three overloading constructors
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public Employee(double salary) {
        // calls the Employee(String name, Double salary) constructor
        this("Employee #" + nextId, salary);
    }

    // the default constructor
    public Employee() {
        // name initialized to "" -- see above
        // salary not explicitly set -- initialized to 0
        // id initialized in initialization block
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public int getId() {
        return id;
    }
}