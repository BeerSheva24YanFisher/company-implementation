package telran.employees;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class CompanyImpl implements Company, Persistable{
   private TreeMap<Long, Employee> employees = new TreeMap<>();
   private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
   private TreeMap<Float, List<Manager>> managersFactor = new TreeMap<>();
private class CompanyIterator implements Iterator<Employee> {
    Iterator<Employee> iterator = employees.values().iterator();
    Employee lastIterated;
    @Override
    public boolean hasNext() {
       return iterator.hasNext();
    }

    @Override
    public Employee next() {
       lastIterated = iterator.next();
       return lastIterated;
    }
    @Override
    public void remove() {
       iterator.remove();
       removeFromIndexMaps(lastIterated);
    }
}
    @Override
    public Iterator<Employee> iterator() {
       return new CompanyIterator();
    }

    @Override
    public void addEmployee(Employee empl) {
        long id = empl.getId();
        if (employees.putIfAbsent(id, empl) != null) {
            throw new IllegalStateException("Already exists employee " + id);
        }
        addIndexMaps(empl);
    }

    private void addIndexMaps(Employee empl) {
       employeesDepartment.computeIfAbsent(empl.getDepartment(), k -> new ArrayList<>()).add(empl);
       if (empl instanceof Manager manager) {
            managersFactor.computeIfAbsent(manager.getFactor(), k -> new ArrayList<>()).add(manager);
       }
    }

    

    @Override
    public Employee getEmployee(long id) {
        return employees.get(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        Employee empl = employees.remove(id);
        if(empl == null) {
            throw new NoSuchElementException("Not found employee " + id);
        }
        removeFromIndexMaps(empl);
        return empl;
    }


    private void removeFromIndexMaps(Employee empl) {
        removeIndexMap(empl.getDepartment(), employeesDepartment, empl);
        if (empl instanceof Manager manager) {
            removeIndexMap(manager.getFactor(), managersFactor, manager);
        }
    }

    private <K, V extends Employee> void removeIndexMap(K key, Map<K, List<V>> map, V empl) {
        List<V> list = map.get(key);
        list.remove(empl);
        if (list.isEmpty()) {
            map.remove(key);
        }
    }

    @Override
    public int getDepartmentBudget(String department) {
        return employeesDepartment.getOrDefault(department, Collections.emptyList())
        .stream().mapToInt(Employee::computeSalary).sum();
    }

    @Override
    public String[] getDepartments() {
        return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        Manager [] res = new Manager[0];
        if (!managersFactor.isEmpty()) {
            res = managersFactor.lastEntry().getValue().toArray(res);
        }
        return res;
    }

    @Override
    public void saveOfFile(String fileName) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
            for (Employee empl : employees.values()) {
                out.write(empl.toString());
                out.newLine();
                
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving company to file", e);
        }
    }

    @Override
    public void restoreFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Employee empl = Employee.getEmployeeFromJSON(line);
                addEmployee(empl);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error restoring company from file", e);
        }
    }
}