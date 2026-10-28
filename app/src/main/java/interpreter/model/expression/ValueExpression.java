package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.value.IValue;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public record ValueExpression(IValue value) implements IExpression{
    @Override
    public IValue evaluate(IDictionary<String, IValue> symTable) throws ExpressionException{
        return value;
    }

    @Override
    public String toString(){
        return value.toString();
    }
}
