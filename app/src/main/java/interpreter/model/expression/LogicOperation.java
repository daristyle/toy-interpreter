package interpreter.model.expression;

public enum LogicOperation{
    AND("and"), OR("or");

    private final String symbol;

    LogicOperation(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return symbol;
    }
}
