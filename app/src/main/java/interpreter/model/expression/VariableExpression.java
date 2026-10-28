package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.UndefinedVariableException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.value.IValue;

public record VariableExpression(String name) implements IExpression{
    @Override
    public IValue evaluate(IDictionary<String, IValue> symTable) throws ExpressionException{
        IValue value = symTable.get(name);

        if(value == null) throw new UndefinedVariableException("Variable " + name + " was not defined");

        return value;
    }

    @Override
    public String toString(){
        return name;
    }
}
