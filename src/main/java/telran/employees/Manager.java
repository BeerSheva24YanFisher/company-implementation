package telran.employees;

public class Manager extends Employee{
    private float factor;

    public Manager(Long id, int salary, String department, float factor) {
        super(id, salary, department);
        this.factor = factor;
    }

    public float getFactor() {
        return factor;
    }

    @Override
    public int computeSalary(){
        return (int)(super.computeSalary() * factor);
    }

}
