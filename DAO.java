import java.util.List;

public interface DAO<T> {

    void add(T o);
    void delete();
    List<T> getAll();

}
