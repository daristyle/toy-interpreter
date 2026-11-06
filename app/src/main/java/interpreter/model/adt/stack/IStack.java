package interpreter.model.adt.stack;

import interpreter.exceptions.StackException;

import java.util.List;

public interface IStack<T>{
    void push(T value);
    T pop() throws StackException;
    T peek() throws StackException;
    boolean isEmpty();
    void clear();
    List<T> toList();
}
