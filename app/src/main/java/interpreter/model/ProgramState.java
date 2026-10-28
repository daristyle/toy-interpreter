package interpreter.model;

import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.adt.dictionary.MyDictionary;
import interpreter.model.adt.list.IList;
import interpreter.model.adt.list.MyList;
import interpreter.model.adt.stack.IStack;
import interpreter.model.adt.stack.MyStack;
import interpreter.model.statement.IStatement;
import interpreter.model.value.IValue;


public class ProgramState{
    private final IStack<IStatement> execStack = new MyStack<>();
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();
    private final IList<IValue> outList = new MyList<>();

    public IStack<IStatement> getExecStack(){
        return execStack;
    }

    public IDictionary<String, IValue> getSymTable(){
        return symTable;
    }

    public IList<IValue> getOutList(){
        return outList;
    }
}
