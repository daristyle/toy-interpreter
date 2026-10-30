package interpreter.view;

import interpreter.controller.Controller;
import interpreter.controller.IController;
import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import interpreter.model.expression.ArithmeticExpression;
import interpreter.model.expression.ArithmeticOperation;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.statement.*;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.repository.MemoryRepository;

import java.util.List;
import java.util.Scanner;

public class ConsoleView implements IView{
    private final IController ctrl = new Controller(new MemoryRepository(List.of()), true, this::printState);
    private final IStatement[] examples;
    private final Scanner in;
    private int selected;

    private void printState(ProgramState state){
        System.out.println(state);
        System.out.println("---------------------------------------");
    }

    private void printMainMenu(){
        System.out.println("Main Menu");
        System.out.println("1. Select Program");
        System.out.println("2. Run selected Program");
        System.out.println("0. Exit");
    }

    private void selectProgram(){
        System.out.println("Select Program: ");
        for(int i = 0; i < examples.length; i++){
            System.out.println((i + 1) + ". " + examples[i]);
        }
        try{
            int choice = Integer.parseInt(in.nextLine().trim());
            if(choice <= 0 || choice > examples.length){
                System.out.println("Invalid choice!");
            }else{
                selected = choice - 1;
            }
        }catch (NumberFormatException e){
            System.out.println("Invalid choice!");
        }
    }

    private void runProgram() throws MyException{
        if(selected < 0 || selected >= examples.length){
            System.out.println("Select a program first!");
            return;
        }
        System.out.println("Running Program " + (selected + 1) + ": " + examples[selected]);
        ctrl.loadProgram(examples[selected]);
        ctrl.allSteps();
    }

    private IStatement compound(IStatement[] statements){
        if(statements.length == 0) return new NoOperationStatement();
        IStatement program = statements[statements.length - 1];
        for(int i = statements.length - 2; i >= 0; i--){
            program = new CompoundStatement(statements[i], program);
        }

        return program;
    }

    public ConsoleView(){
        in = new Scanner(System.in);
        selected = -1;

        //int v; v = 2; print(v)
        IStatement[] statements1 = new IStatement[]{
                new VariableDeclarationStatement(new IntType(),"v"),
                new AssignmentStatement("v",
                        new ValueExpression(new IntValue(2))
                ),
                new PrintStatement(new VariableExpression("v")),
        };
        //int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)
        IStatement[] statements2 = new IStatement[]{
                new VariableDeclarationStatement(new IntType(),"a"),
                new VariableDeclarationStatement(new IntType(),"b"),
                new AssignmentStatement("a",
                        new ArithmeticExpression(
                                ArithmeticOperation.ADD,
                                new ValueExpression(new IntValue(2)),
                                new ArithmeticExpression(
                                        ArithmeticOperation.MUL,
                                        new ValueExpression(new IntValue(3)),
                                        new ValueExpression(new IntValue(5))
                                )
                        )
                ),
                new AssignmentStatement("b",
                        new ArithmeticExpression(
                                ArithmeticOperation.ADD,
                                new VariableExpression("a"),
                                new ValueExpression(new IntValue(1))
                        )
                ),
                new PrintStatement(new VariableExpression("b")),
        };
        //bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)
        IStatement[] statements3 = new IStatement[]{
                new VariableDeclarationStatement(new BoolType(),"a"),
                new VariableDeclarationStatement(new IntType(),"v"),
                new AssignmentStatement("a",
                        new ValueExpression(new BoolValue(true))
                ),
                new IfStatement(
                        new VariableExpression("a"),
                        new AssignmentStatement("v",
                                new ValueExpression(new IntValue(2))
                        ),
                        new AssignmentStatement("v",
                                new ValueExpression(new IntValue(3)))
                ),
                new PrintStatement(new VariableExpression("v")),
        };

        IStatement[] statements4 = new IStatement[]{
                new VariableDeclarationStatement(new BoolType(), "a"),
                new AssignmentStatement("a",
                        new ValueExpression(new IntValue(2))
                ),
                new PrintStatement(new VariableExpression("a")),
        };

        examples = new IStatement[]{
                compound(statements1),
                compound(statements2),
                compound(statements3),
                compound(statements4),
        };
    }

    public void run(){
        boolean exited = false;
        while(!exited){
            try{
                printMainMenu();
                if(!in.hasNextLine()){
                    exited = true;
                    break;
                }
                switch(in.nextLine().trim()){
                    case "1" -> selectProgram();
                    case "2" -> runProgram();
                    case "0" -> {
                        System.out.println("Bye bye!");
                        exited = true;
                    }
                    default -> System.out.println("Unknown Option!");
                }
            }catch(MyException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
