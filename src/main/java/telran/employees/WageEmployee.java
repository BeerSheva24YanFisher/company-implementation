package telran.employees;

public class WageEmployee extends Employee{

    int wage;
    int hours;

        public WageEmployee(Long id, int salary, String department, int wage, int hours) {
        super(id, salary, department);
        this.wage = wage;
        this.hours = hours;
    }

    @Override
    public int computeSalary(){
        return super.computeSalary()+wage*hours;
    }



}
