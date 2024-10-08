package telran.employees;

import org.json.JSONObject;

public class Employee {

    private Long id;
    private int salary;
    private String department;

    public Employee() {
    }
    
    @SuppressWarnings("unchecked")
    static public Employee getEmployeeFromJSON(String jsonSTR) {
        JSONObject jsonObj = new JSONObject(jsonSTR);
        String className = jsonObj.getString("className");
        try {
            Class<Employee> clazz = (Class<Employee>) Class.forName(className);
            Employee empl = clazz.getConstructor().newInstance();
            empl.setObject(jsonObj);
            return empl;
        } catch (Exception e) {
            throw new RuntimeException(e);        
        }   
    }

    protected void setObject(JSONObject jsonObj) {
        id = jsonObj.getLong("id");
        salary = jsonObj.getInt("basicSalary");
        department = jsonObj.getString("department");
    }

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

    @Override
    public String toString() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("className", getClass().getName());
        fillJSON(jsonObj);
        return jsonObj.toString();
    }

    protected void fillJSON(JSONObject jsonObj) {
        jsonObj.put("id", id);
        jsonObj.put("basicSalary", salary);
        jsonObj.put("department", department);
    }



    

}
