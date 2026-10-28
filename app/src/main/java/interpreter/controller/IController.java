package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.statement.IStatement;

public interface IController{
    void oneStep() throws MyException;
    void allSteps() throws MyException;
    void loadProgram(IStatement program);
}
