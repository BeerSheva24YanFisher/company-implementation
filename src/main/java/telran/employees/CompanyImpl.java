package telran.employees;

import java.util.*;

public class CompanyImpl implements Company {
    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> managersFactor = new TreeMap<>();

    @Override
    public Iterator<Employee> iterator() {
        return new Iterator<Employee>() {
            private Iterator<Map.Entry<Long, Employee>> it = employees.entrySet().iterator();
            private Map.Entry<Long, Employee> lastReturned = null;
            private boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Employee next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = it.next();
                canRemove = true;
                return lastReturned.getValue();
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException("Cannot remove before calling next or after already removing.");
                }
                Employee employee = lastReturned.getValue();
                removeFromDepartment(employee);
                removeFromManagersFactor(employee);
                it.remove();
                canRemove = false;
                lastReturned = null;
            }
        };
    }

    @Override
    public void addEmployee(Employee empl) {
        if (employees.containsKey(empl.getId())) {
            throw new IllegalStateException("Employee with the same ID already exists");
        }
        employees.put(empl.getId(), empl);
        addToDepartment(empl);
        if (empl instanceof Manager) {
            addToManagersFactor((Manager) empl);
        }
    }

    @Override
    public Employee getEmployee(long id) {
        return employees.get(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        Employee removed = employees.remove(id);
        if (removed == null) {
            throw new NoSuchElementException("No employee with ID " + id);
        }
        removeFromDepartment(removed);
        if (removed instanceof Manager) {
            removeFromManagersFactor(removed);
        }
        return removed;
    }

    @Override
    public int getDepartmentBudget(String department) {
        return employeesDepartment.getOrDefault(department, Collections.emptyList()).stream()
                .mapToInt(Employee::computeSalary)
                .sum();
    }

    @Override
    public String[] getDepartments() {
        return employeesDepartment.keySet().stream()
                .sorted()
                .toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        return managersFactor.isEmpty() ? new Manager[0] : managersFactor.get(managersFactor.lastKey()).toArray(new Manager[0]);
    }

    private void addToDepartment(Employee employee) {
        employeesDepartment.computeIfAbsent(employee.getDepartment(), k -> new ArrayList<>()).add(employee);
    }

    private void removeFromDepartment(Employee employee) {
        List<Employee> departmentEmployees = employeesDepartment.get(employee.getDepartment());
        if (departmentEmployees != null) {
            departmentEmployees.remove(employee);
            if (departmentEmployees.isEmpty()) {
                employeesDepartment.remove(employee.getDepartment());
            }
        }
    }

    private void addToManagersFactor(Manager manager) {
        managersFactor.computeIfAbsent(manager.getFactor(), k -> new ArrayList<>()).add(manager);
    }

    private void removeFromManagersFactor(Employee employee) {
        if (employee instanceof Manager) {
            Manager manager = (Manager) employee;
            List<Manager> managers = managersFactor.get(manager.getFactor());
            if (managers != null) {
                managers.remove(manager);
                if (managers.isEmpty()) {
                    managersFactor.remove(manager.getFactor());
                }
            }
        }
    }
}
