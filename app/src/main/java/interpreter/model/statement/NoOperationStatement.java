package interpreter.model.statement;

public record NoopStatement() implements IStatement{

    @Override
    public String toString(){
        return "noop";
    }
}
