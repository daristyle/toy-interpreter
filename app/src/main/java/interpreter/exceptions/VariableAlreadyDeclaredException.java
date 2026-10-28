package interpreter.exceptions;

public class VariableAlreadyDeclaredException extends StatementException{
    public VariableAlreadyDeclaredException(String message){
        super(message);
    }
}
