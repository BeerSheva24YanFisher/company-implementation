package telran.employees.db.jpa;
import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import telran.employees.Employee;
@Table(name="employees")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EmployeeEntity {
    @Id
    private long id;
    @Column(name="basic_salary")
    private int basicSalary;
    private String department;
    protected void fromEmployeeDto(Employee empl) {
        id = empl.getId();
        basicSalary = empl.getBasicSalary();
        department = empl.getDepartment();
    }
    protected void toJsonObject(JSONObject jsonObj) {
        jsonObj.put("department", department);
        jsonObj.put("basicSalary", basicSalary);
        jsonObj.put("id", id);
        
    }
    
}