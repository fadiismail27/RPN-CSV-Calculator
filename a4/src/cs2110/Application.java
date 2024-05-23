package cs2110;

import java.util.Objects;
import java.util.Set;

/**
 * An expression tree node representing the application of a unary function to an argument.
 * This node encapsulates a mathematical operation defined by a  UnaryFunction
 */

public class Application implements Expression{


    /**
     * The unary function to be applied.
     */
    private final UnaryFunction func;

    /**
     * The argument to the unary function.
     */
    private final Expression argument;


    /**
     * Constructs an Application node with a given unary function and its argument.
     *
     * param func The unary function to apply.
     * param argument The argument to the function.
     */

    public Application(UnaryFunction func, Expression argument) {
        assert func != null;
        assert argument != null;

        this.func = func;
        this.argument = argument;
    }


    /**
     * Evaluates the unary function with its argument by first evaluating the argument
     * and then applying the function to the result.
     *
     * param vars Variable table to resolve variable names to their values.
     * return The result of applying the unary function to the evaluated argument as a double.
     * throws UnboundVariableException If the argument evaluation requires a variable not present
     * in vars.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {

        assert vars != null;

        double val = argument.eval(vars);

        return func.apply(val);
    }


    /**
     * Counts the total number of operations required to evaluate this node,
     * including the operation represented by this node and those of its argument.
     *
     * return The total number of operations.
     */
    @Override
    public int opCount() {
        return 1 + argument.opCount();

    }


    /**
     * Returns the infix notation representation of this function application,
     * enclosing the argument in parentheses and prefixing it with the function name.
     *
     * return The infix notation string of this expression.
     */
    @Override
    public String infixString() {
        String string = func.name();
        return string + "("  + argument.infixString() + ")";
    }


    /**
     * Returns the postfix notation representation of this function application,
     * following the argument with the function name and a pair of parentheses.
     *
     * return The postfix notation string of this expression.
     */
    @Override
    public String postfixString() {
        String string = func.name();
        return argument.postfixString()+" "+string+"()";
    }

    /**
     * Optimizes the expression by attempting to evaluate it. If successful, returns
     * a Constant with the result. Otherwise, returns a new Application
     * with the optimized argument.
     *
     * param vars Variable table to resolve variable names to their values.
     * return The optimized expression.
     */

    @Override
    public Expression optimize(VarTable vars) {
        assert vars != null;

        Expression optimizedExpr = argument.optimize(vars);

        try{
            double res = func.apply(optimizedExpr.eval(vars));
            return new Constant(res);
        }
        catch(UnboundVariableException e){
            return new Application(func,optimizedExpr);
        }
    }


    /**
     * Returns the set of variable names that this expression depends on to be evaluated,
     * which are the dependencies of its argument.
     */

    @Override
    public Set<String> dependencies() {
        return argument.dependencies();
    }

    /**
     * Compares this Application to another object for equality. Two Application nodes
     * are equal if they apply the same unary function to equal arguments.
     *
     * param obj The object to compare with.
     * return true if the objects are equal, code false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        assert obj != null;
        Application otherObject = (Application)obj;
        return otherObject.func.name().equals(func.name())
                && argument.equals(otherObject.argument);


    }
}
