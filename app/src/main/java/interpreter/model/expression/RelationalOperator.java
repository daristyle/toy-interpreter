package interpreter.model.expression;

public enum RelationalOperator{
    LT("<"), LE("<="), EQ("=="), NE("!="), GT(">"), GE(">=");

    private final String symbol;

    RelationalOperator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
