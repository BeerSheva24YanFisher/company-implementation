package telran.employees;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CompanyImpl implements Company, Persistable {
    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> managersFactor = new TreeMap<>();
    
    private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static final Lock readLock = rwLock.readLock();
    private static final Lock writeLock = rwLock.writeLock();

    private class CompanyIterator implements Iterator<Employee> {
        Iterator<Employee> iterator = employees.values().iterator();
        Employee lastIterated;

        @Override
        public boolean hasNext() {
            readLock.lock();
            try {
                return iterator.hasNext();
            } finally {
                readLock.unlock();
            }
        }

        @Override
        public Employee next() {
            readLock.lock();
            try {
                lastIterated = iterator.next();
                return lastIterated;
            } finally {
                readLock.unlock();
            }
        }

        @Override
        public void remove() {
            writeLock.lock();
            try {
                iterator.remove();
                removeFromIndexMaps(lastIterated);
            } finally {
                writeLock.unlock();
            }
        }
    }

    @Override
    public Iterator<Employee> iterator() {
        return new CompanyIterator();
    }

    @Override
    public void addEmployee(Employee empl) {
        writeLock.lock();
        try {
            long id = empl.getId();
            if (employees.putIfAbsent(id, empl) != null) {
                throw new IllegalStateException("Already exists employee " + id);
            }
            addIndexMaps(empl);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Employee getEmployee(long id) {
        readLock.lock();
        try {
            return employees.get(id);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Employee removeEmployee(long id) {
        writeLock.lock();
        try {
            Employee empl = employees.remove(id);
            if (empl == null) {
                throw new NoSuchElementException("Not found employee " + id);
            }
            removeFromIndexMaps(empl);
            return empl;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int getDepartmentBudget(String department) {
        readLock.lock();
        try {
            return employeesDepartment.getOrDefault(department, Collections.emptyList())
                    .stream()
                    .mapToInt(Employee::computeSalary)
                    .sum();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String[] getDepartments() {
        readLock.lock();
        try {
            return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        readLock.lock();
        try {
            Manager[] res = new Manager[0];
            if (!managersFactor.isEmpty()) {
                res = managersFactor.lastEntry().getValue().toArray(res);
            }
            return res;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void saveToFile(String fileName) {
        writeLock.lock();
        try (PrintWriter writer = new PrintWriter(fileName)) {
            forEach(writer::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void restoreFromFile(String fileName) {
        writeLock.lock();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {
            reader.lines().map(Employee::getEmployeeFromJSON).forEach(this::addEmployee);
        } catch (NoSuchFileException e) {
            // Игнорируем отсутствие файла
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    private void addIndexMaps(Employee empl) {
        employeesDepartment.computeIfAbsent(empl.getDepartment(), k -> new ArrayList<>()).add(empl);
        if (empl instanceof Manager manager) {
            managersFactor.computeIfAbsent(manager.getFactor(), k -> new ArrayList<>()).add(manager);
        }
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
}
