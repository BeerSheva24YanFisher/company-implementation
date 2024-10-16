package telran.employees;

public interface Persistable {
    void saveToFile(String fileName);
    void restoreFromFile(String fileName);

}
