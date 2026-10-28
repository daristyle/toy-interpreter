package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.value.IValue;

public interface IExpression{
    IValue evaluate(IDictionary<String, IValue> symTable) throws ExpressionException;
}
