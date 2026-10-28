package interpreter.model.adt.stack;

import interpreter.exceptions.StackException;

import java.util.ArrayDeque;
import java.util.Deque;

public class MyStack<T> implements IStack<T>{
    private final Deque<T> stack = new ArrayDeque<T>();

    @Override
    public void push(T value){
        stack.push(value);
    }

    @Override
    public T pop() throws StackException{
        if(stack.isEmpty()) throw new StackException("Stack is empty");
        return stack.pop();
    }

    @Override
    public T peek() throws StackException{
        if(stack.isEmpty()) throw new StackException("Stack is empty");
        return stack.peek();
    }

    @Override
    public boolean isEmpty(){
        return stack.isEmpty();
    }

    @Override
    public void clear(){
        stack.clear();
    }
}
