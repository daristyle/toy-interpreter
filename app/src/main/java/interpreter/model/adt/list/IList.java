package interpreter.model.adt.list;

import interpreter.exceptions.ADTException;

public interface IList<T>{
    void add(T value);
    T get(int index) throws ADTException;
    int size();
    void clear();
}
