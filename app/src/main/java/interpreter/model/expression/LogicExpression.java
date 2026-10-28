package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.LogicException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public record LogicExpression(LogicOperation op, IExpression left, IExpression right) implements IExpression{
    @Override
    public IValue evaluate(IDictionary<String, IValue> symTable) throws ExpressionException{
        if(!(left.evaluate(symTable) instanceof BoolValue(boolean first)))
            throw new LogicException("First operand must be boolean");
        if(!(right.evaluate(symTable) instanceof BoolValue(boolean second)))
            throw new LogicException("Second operand must be boolean");

        return switch(op){
            case AND -> new BoolValue(first && second);
            case OR -> new BoolValue(first || second);
        };
    }

    @Override
    public String toString(){
        return "(" + left + " " + op + " " + right + ")";
    }
}
