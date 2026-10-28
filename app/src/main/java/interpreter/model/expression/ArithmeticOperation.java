package interpreter.model.expression;

public enum ArithmeticOperation{
    ADD("+"), SUB("-"), MUL("*"), DIV("/");
    private String symbol;

    ArithmeticOperation(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return symbol;
    }
}
