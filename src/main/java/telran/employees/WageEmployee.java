package telran.employees;

import org.json.JSONObject;

public class WageEmployee extends Employee{

    int wage;
    int hours;

    public WageEmployee() {
    }

    @Override
    protected void setObject(JSONObject jsonObj) {
        super.setObject(jsonObj);
        wage = jsonObj.getInt("wage");
        hours = jsonObj.getInt("hours");

    }

    public WageEmployee(Long id, int salary, String department, int wage, int hours) {
        super(id, salary, department);
        this.wage = wage;
        this.hours = hours;
    }

    @Override
    public int computeSalary(){
        return super.computeSalary()+wage*hours;
    }

        @Override
    protected void fillJSON(JSONObject jsonObj) {
        super.fillJSON(jsonObj);
        jsonObj.put("wage", wage);
        jsonObj.put("hours", hours);
    }    



}
