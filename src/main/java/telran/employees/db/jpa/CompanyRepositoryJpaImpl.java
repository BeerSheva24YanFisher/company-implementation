package telran.employees.db.jpa;

import java.util.*;

import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceUnitInfo;
import telran.employees.Employee;
import telran.employees.db.CompanyRepository;

public class CompanyRepositoryJpaImpl implements CompanyRepository{
    private EntityManager em;
    public CompanyRepositoryJpaImpl(PersistenceUnitInfo persistenceUnit,
     HashMap<String, Object> properties) {
        
    }
    @Override
    public List<Employee> getEmployees() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmployees'");
    }

}