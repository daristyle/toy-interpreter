package interpreter.model.adt.dictionary;

import java.util.Map;

public interface IDictionary<K, V>{
    boolean containsKey(K key);
    V get(K key);
    void put(K key, V value);
    void remove(K key);
    void clear();
    Map<K, V> toMap();
}
