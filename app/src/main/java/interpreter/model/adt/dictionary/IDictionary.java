package interpreter.model.adt.dictionary;

public interface IDictionary<K, V>{
    boolean containsKey(K key);
    V get(K key);
    void put(K key, V value);
    void remove(K key);
    void clear();
}
