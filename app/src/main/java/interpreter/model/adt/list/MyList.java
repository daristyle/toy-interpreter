package interpreter.model.adt.list;

import interpreter.exceptions.ListException;

import java.util.ArrayList;

public class MyList<T> implements IList<T>{
    private final ArrayList<T> list = new ArrayList<>();

    @Override
    public void add(T value){
        list.add(value);
    }

    @Override
    public T get(int index) throws ListException{
        if(index < 0 || index >= size()) throw new ListException("Index out of bounds");
        return list.get(index);
    }

    @Override
    public int size(){
        return list.size();
    }

    @Override
    public void clear(){
        list.clear();
    }
}
