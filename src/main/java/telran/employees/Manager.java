package telran.employees;

import org.json.JSONObject;

public class Manager extends Employee{
    private float factor;

    public Manager() {
    }

    @Override
    protected void setObject(JSONObject jsonObj) {
        super.setObject(jsonObj);
        factor = jsonObj.getFloat("factor");
    }

    

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

    // @Override
    // public String toString() {
    //     JSONObject jsonObj = new JSONObject();
    //     jsonObj.put("className", getClass().getName());
    //     fillJSON(jsonObj);
    //     return jsonObj.toString();
    // }

    @Override
    protected void fillJSON(JSONObject jsonObj) {
        super.fillJSON(jsonObj);
        jsonObj.put("factor", factor);
    }

    

    
}
