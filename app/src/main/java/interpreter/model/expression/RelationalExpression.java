package interpreter.model.expression;

import interpreter.exceptions.ArithmeticException;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.RelationalException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public record RelationalExpression(RelationalOperator op, IExpression left, IExpression right) implements IExpression {
    @Override
    public IValue evaluate(IDictionary<String, IValue> symTable) throws ExpressionException{
        if(!(left.evaluate(symTable) instanceof IntValue(int first)))
            throw new RelationalException("First operand must be an integer");
        if(!(right.evaluate(symTable) instanceof IntValue(int second)))
            throw new RelationalException("Second operand must be an integer");

        return new BoolValue(switch(op){
            case LT -> first < second;
            case LE -> first <= second;
            case EQ -> first == second;
            case NE -> first != second;
            case GT -> first > second;
            case GE -> first >= second;
        });
    }

    @Override
    public String toString(){
        return "(" + left + " " + op + " " + right + ")";
    }
}
