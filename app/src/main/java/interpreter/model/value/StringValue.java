package interpreter.model.value;

import interpreter.model.type.*;

public record StringValue(String value) implements IValue {

    @Override
    public IType getType(){
        return new StringType();
    }

    @Override
    public String toString(){
        return "\"" + value + "\"";
    }
}
