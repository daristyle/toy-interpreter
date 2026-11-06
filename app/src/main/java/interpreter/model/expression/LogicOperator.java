package interpreter.model.expression;

public enum LogicOperator{
    AND("and"), OR("or");

    private final String symbol;

    LogicOperator(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return symbol;
    }
}
