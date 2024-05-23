package cs2110;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An expression tree node representing a variable that can hold different values.
 */
public class Variable implements Expression {

    /**
     * The name of this variable.
     */
    private final String name;

    /**
     * Creates a variable with the specified name.
     *
     * param name The name of the variable.
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Evaluates this variable using the given variable table to fetch its value.
     *
     * param vars The variable table containing values for variables.
     * return The value of this variable as determined by the variable table.
     * throws UnboundVariableException If the variable's value is not found in the variable table.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        assert vars != null;
        return vars.get(name);
    }

    /**
     * Returns the operation count, which is 0 for a variable.
     *
     * return The operation count (0).
     */
    @Override
    public int opCount() {
        return 0;
    }

    /**
     * Returns the name of this variable as its infix string representation.
     *
     * return The name of the variable.
     */
    @Override
    public String infixString() {
        return name;
    }

    /**
     * Returns the name of this variable as its postfix string representation.
     *
     * @return The name of the variable.
     */
    @Override
    public String postfixString() {
        return name;
    }

    /**
     * Attempts to optimize this variable into a constant if its value is known; otherwise, returns itself.
     *
     * param vars The variable table containing values for variables.
     * return A Constant expression if the variable's value is known; otherwise, returns this variable.
     */
    @Override
    public Expression optimize(VarTable vars) {
        assert vars != null;
        try {
            return new Constant(eval(vars));
        } catch(UnboundVariableException e){
            return this;
        }
    }

    /**
     * Returns a set containing this variable's name as its only dependency.
     *
     * return A set containing the name of this variable.
     */
    @Override
    public Set<String> dependencies() {
        Set<String> depend = new HashSet<>();
        depend.add(name);
        return depend;
    }

    /**
     * Compares this variable to another object for equality. Two variables are equal if they have the same name.
     *
     * param other The object to compare with.
     * return true if the other object is a variable with the same name; false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Variable otherVar = (Variable) other;
        return Objects.equals(name, otherVar.name);
    }
}
