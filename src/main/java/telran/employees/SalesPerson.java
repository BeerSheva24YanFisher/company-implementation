package telran.employees;

public class SalesPerson extends WageEmployee{
    float percent;
    Long sales;

    public SalesPerson(Long id, int salary, String department, int wage, int hours, float percent, Long sales) {
        super(id, salary, department, wage, hours);
        this.percent = percent;
        this.sales = sales;
    }

    public float getPercent() {
        return percent;
    }

    public Long getSales() {
        return sales;
    }

    @Override
    public int computeSalary(){
        return (int)(super.computeSalary()+percent*sales/100);
    }

    

}
