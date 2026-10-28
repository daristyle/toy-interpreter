package interpreter.exceptions;

public class UndefinedVariableException extends ExpressionException{
    public UndefinedVariableException(String message){
        super(message);
    }
}
