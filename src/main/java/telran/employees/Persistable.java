package telran.employees;

public interface Persistable {
    void saveOfFile(String fileName);
    void restoreFromFile(String fileName);

}
