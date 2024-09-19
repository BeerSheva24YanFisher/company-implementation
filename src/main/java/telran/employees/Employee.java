package telran.employees;

public class Employee {

    private Long id;
    private int salary;
    private String department;

    public Employee(Long id, int salary, String department) {
        this.id = id;
        this.salary = salary;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public int computeSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        Employee employee = (Employee) obj;
        return employee==null? false : id == employee.id && salary == employee.salary;
    }

    

}
