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
    private final IStack<IStatement> execStack;
    private final IDictionary<String, IValue> symTable;
    private final IList<IValue> outList;
    private final IStatement originalProgram;

    ProgramState(IStack<IStatement> execStack, IDictionary<String, IValue> symTable, IList<IValue> outList,
                 IStatement originalProgram){
        this.execStack = execStack;
        this.symTable = symTable;
        this.outList = outList;
        this.originalProgram = originalProgram;

        this.execStack.push(this.originalProgram);
    }

    public IStack<IStatement> getExecStack(){
        return execStack;
    }

    public IDictionary<String, IValue> getSymTable(){
        return symTable;
    }

    public IList<IValue> getOutList(){
        return outList;
    }

    public IStatement getOriginalProgram(){
        return originalProgram;
    }

    @Override
    public String toString(){
        return "ExecStack: " + execStack + "\nSymTable: " + symTable + "\nOutList: " + outList;
    }
}
