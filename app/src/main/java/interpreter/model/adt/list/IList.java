package interpreter.model.adt.list;

import interpreter.exceptions.ListException;

import java.util.List;

public interface IList<T>{
    void add(T value);
    T get(int index) throws ListException;
    int size();
    void clear();
    List<T> toList();
}
