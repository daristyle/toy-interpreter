package interpreter.model.adt.dictionary;

import java.util.HashMap;

public class MyDictionary<K,V> implements IDictionary<K,V>{
    private final HashMap<K,V> dictionary = new HashMap<>();

    @Override
    public boolean containsKey(K key){
        return dictionary.containsKey(key);
    }

    @Override
    public V get(K key){
        return dictionary.get(key);
    }

    @Override
    public void put(K key, V value){
        dictionary.put(key, value);
    }

    @Override
    public void remove(K key){
        dictionary.remove(key);
    }

    @Override
    public void clear(){
        dictionary.clear();
    }
}
