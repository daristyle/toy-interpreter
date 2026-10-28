package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import interpreter.model.expression.IExpression;
import interpreter.model.value.IValue;

public record PrintStatement(IExpression expression) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        IValue value = expression.evaluate(state.getSymTable());

        state.getOutList().add(value);
        return state;
    }

    @Override
    public String toString(){
        return "print(" + expression + ")";
    }
}
