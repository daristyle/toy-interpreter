package interpreter.model.expression;

import interpreter.exceptions.ArithmeticException;
import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

import java.beans.Expression;

public record ArithmeticExpression(ArithmeticOperation op, IExpression left, IExpression right) implements IExpression{
    @Override
    public IntValue evaluate(IDictionary<String, IValue> symTable) throws ExpressionException{
        if(!(left.evaluate(symTable) instanceof IntValue(int first)))
            throw new ArithmeticException("First operand must be an integer");
        if(!(right.evaluate(symTable) instanceof IntValue(int second)))
            throw new ArithmeticException("Second operand must be an integer");

        return switch(op){
            case ADD -> new IntValue(first + second);
            case SUB ->  new IntValue(first - second);
            case MUL -> new IntValue(first * second);
            case DIV ->{
                if(second == 0) throw new ArithmeticException("Division by zero");
                yield new IntValue(first / second);
            }
        };
    }

    @Override
    public String toString(){
        return "(" + left + " " + op + " " + right + ")";
    }
}
