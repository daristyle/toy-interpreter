package interpreter.model.adt.stack;

import interpreter.exceptions.ADTException;

public interface IStack<T>{
    void push(T value);
    T pop() throws ADTException;
    T peek() throws ADTException;
    boolean isEmpty();
    void clear();
}
