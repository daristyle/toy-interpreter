package interpreter.model.statement;

import interpreter.exceptions.IfStatementException;
import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import interpreter.model.expression.IExpression;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record IfStatement(IExpression condition, IStatement thenS, IStatement elseS) implements IStatement{

    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        IValue cond = condition.evaluate(state.getSymTable());
        if(!(cond instanceof BoolValue(boolean c))) throw new IfStatementException("Condition is not a boolean");

        if(c){
            state.getExecStack().push(thenS);
        }else{
            state.getExecStack().push(elseS);
        }

        return state;
    }

    @Override
    public String toString(){
        return "(if " + condition +  " then " + thenS + " else " + elseS + ")";
    }
}
