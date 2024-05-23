package cs2110;

import java.util.HashSet;
import java.util.Set;

/**
 * An expression tree node representing a binary operation (e.g., addition, subtraction) between two expressions.
 */
public class Operation implements Expression {

    /**
     * The operator of this operation (e.g., +, -, *).
     */
    private final Operator op;

    /**
     * The left operand of the operation.
     */
    private final Expression leftOperand;

    /**
     * The right operand of the operation.
     */
    private final Expression rightOperand;

    /**
     * Constructs an Operation with a specified operator and two operands.
     *
     * param op The operator of the operation.
     * param leftOperand The left operand of the operation.
     * param rightOperand The right operand of the operation.
     */
    public Operation(Operator op, Expression leftOperand, Expression rightOperand) {
        this.op = op;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * Evaluates the operation by applying the operator to the evaluated left and right operands.
     *
     * param vars The variable table to use for evaluating variable expressions.
     * return The result of the operation.
     * throws UnboundVariableException If any variable in the operation is unbound.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        assert vars != null;
        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
    }

    /**
     * Returns the count of operations in this expression, including this operation and those in its operands.
     *
     * return The total count of operations.
     */
    @Override
    public int opCount() {
        return 1 + leftOperand.opCount() + rightOperand.opCount();
    }

    /**
     * Returns the infix string representation of the operation.
     *
     * return A string representing the operation in infix notation.
     */
    @Override
    public String infixString() {
        return "(" + leftOperand.infixString() + " " + op.symbol() + " " + rightOperand.infixString() + ")";
    }

    /**
     * Returns the postfix string representation of the operation.
     *
     * return A string representing the operation in postfix notation.
     */
    @Override
    public String postfixString() {
        return leftOperand.postfixString() + " " + rightOperand.postfixString() + " " + op.symbol();
    }

    /**
     * Attempts to optimize the operation by optimizing its operands and evaluating it if possible.
     *
     * @param vars The variable table to use for evaluating variable expressions.
     * @return An optimized expression, possibly a Constant if the operation can be fully evaluated.
     */
    @Override
    public Expression optimize(VarTable vars) {
        assert vars != null;
        Expression left = leftOperand.optimize(vars);
        Expression right = rightOperand.optimize(vars);

        try {
            double result = new Operation(op, left, right).eval(vars);
            return new Constant(result);
        } catch (UnboundVariableException e) {
            return new Operation(op, left, right);
        }
    }

    /**
     * Returns a set containing all variable names that this operation depends on.
     *
     * return A set of strings representing the variable names this operation depends on.
     */
    @Override
    public Set<String> dependencies() {
        Set<String> rightDeps = rightOperand.dependencies();
        Set<String> leftDeps = leftOperand.dependencies();
        Set<String> merged = new HashSet<>(rightDeps);
        merged.addAll(leftDeps);
        return merged;
    }

    /**
     * Checks whether another object is equal to this operation. Two operations are considered equal
     * if they have the same operator and their left and right operands are equal.
     *
     * param obj The object to compare with this operation.
     * return true if the specified object is an operation equal to this operation; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Operation other = (Operation) obj;
        return op.equals(other.op) &&
                leftOperand.equals(other.leftOperand) &&
                rightOperand.equals(other.rightOperand);
    }
}
