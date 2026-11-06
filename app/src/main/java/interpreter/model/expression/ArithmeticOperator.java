package interpreter.model.expression;

public enum ArithmeticOperator{
    ADD("+"), SUB("-"), MUL("*"), DIV("/");
    private String symbol;

    ArithmeticOperator(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return symbol;
    }
}
