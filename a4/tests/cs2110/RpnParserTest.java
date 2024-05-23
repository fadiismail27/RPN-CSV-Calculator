package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RpnParserTest {

    @Test
    @DisplayName("Parsing an expression consisting of a single number should yield a Constant " +
            "node with that value")
    void testParseConstant() throws IncompleteRpnException, UndefinedFunctionException {
        Expression expr = RpnParser.parse("1.5", Map.of());
        assertEquals(new Constant(1.5), expr);
    }

    @Test
    @DisplayName("Parsing an expression consisting of a single identifier should yield a " +
            "Variable node with that name")
    void testParseVariable() throws IncompleteRpnException, UndefinedFunctionException {
        Expression expr = RpnParser.parse("x", Map.of());
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        assertEquals(new Variable("x"), expr);
    }

    @Test
    @DisplayName("Parsing an expression ending with an operator should yield an Operation node " +
            "evaluating to the expected value")
    void testParseOperation()
            throws UnboundVariableException, IncompleteRpnException, UndefinedFunctionException {
        Expression expr = RpnParser.parse("1 1 +", Map.of());
        assertInstanceOf(Operation.class, expr);
        assertEquals(2.0, expr.eval(MapVarTable.empty()));

        // TODO: This is not a very thorough test!  Both operands are the same, and the operator is
        // commutative.  Write additional test cases that don't have these properties.
        // You should also write a test case that requires recursive evaluation of the operands.

        Expression exprSubtract = RpnParser.parse("5 3 -", Map.of());
        assertInstanceOf(Operation.class, exprSubtract);
        assertEquals(2.0, exprSubtract.eval(MapVarTable.empty()));

        Expression exprDivide = RpnParser.parse("10 2 /", Map.of());
        assertInstanceOf(Operation.class, exprDivide);
        assertEquals(5.0, exprDivide.eval(MapVarTable.empty()));

        String complexExpr = "3 5 + 2 * 8 -";
        Expression expr2 = RpnParser.parse(complexExpr, Map.of());
        assertEquals(8.0, expr2.eval(MapVarTable.empty()));

        complexExpr = "3 4 * 5 +";
        expr2 = RpnParser.parse(complexExpr, Map.of());
        assertEquals(17.0, expr2.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("Parsing and evaluating an expression with variables and operations "
            + "yields the correct result.")
    void testVariableOperation()
        throws UnboundVariableException, IncompleteRpnException, UndefinedFunctionException {

        VarTable vars = new MapVarTable();
        vars.set("x", 3.0);
        vars.set("y", 5.0);

        String complexExpr = "x 2 3 * + 4 y / -";
        Expression expr2 = RpnParser.parse(complexExpr, Map.of());
        assertEquals(8.2, expr2.eval(vars));


        vars = new MapVarTable();
        vars.set("g", 1123);
        vars.set("z", 421);
        vars.set("f", 12);
        vars.set("c", -2);


        complexExpr = "z f g * + c g / -";
        expr2 = RpnParser.parse(complexExpr, Map.of());
        assertEquals(13897.0017809439, expr2.eval(vars));


    }

    @Test
    @DisplayName("Parsing an expression ending with a function should yield an Application node " +
            "evaluating to the expected value")
    void testParseApplication()
            throws UnboundVariableException, IncompleteRpnException, UndefinedFunctionException {
        Expression expr = RpnParser.parse("4 sqrt()", UnaryFunction.mathDefs());
        // TODO: Uncomment this test
        assertInstanceOf(Application.class, expr);
        assertEquals(2.0, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("Nested function application")
    void testNestedFunctionApplication()
            throws UnboundVariableException, IncompleteRpnException, UndefinedFunctionException {
        // Apply the sqrt function to the result of the exp function applied to 4
        Expression expr = RpnParser.parse("4 exp() sqrt()", UnaryFunction.mathDefs());
        assertEquals(7.38905609893065, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("Testing all of applications")
    void testAllApplication() throws UnboundVariableException, IncompleteRpnException,
            UndefinedFunctionException {

        Expression expr = RpnParser.parse("0 sin()", UnaryFunction.mathDefs());
        assertEquals(0, expr.eval(MapVarTable.empty()));

        expr = RpnParser.parse("0 cos()", UnaryFunction.mathDefs());
        assertEquals(1, expr.eval(MapVarTable.empty()));

        expr = RpnParser.parse("0 tan()", UnaryFunction.mathDefs());
        assertEquals(0, expr.eval(MapVarTable.empty()));

        expr = RpnParser.parse("y 2 / sin()", UnaryFunction.mathDefs());
        assertEquals(0.8414709848078965, expr.eval(MapVarTable.of("y",2)));


    }

    @Test
    @DisplayName("Nested function application and operation")
    void testNestedFunctionApplicationOperation()
            throws UnboundVariableException, IncompleteRpnException, UndefinedFunctionException {
        // Apply the sqrt function to the result of the exp function applied to 4
        Expression expr = RpnParser.parse("5 3 ^ 4 * 16 sqrt() + 8 2 * 4 2 ^ / -", UnaryFunction.mathDefs());
        assertEquals(503, expr.eval(MapVarTable.empty()));
    }






    @Test
    @DisplayName("Parsing an empty expression should throw an IncompleteRpnException")
    void testParseEmpty() {
        assertThrows(IncompleteRpnException.class, () -> RpnParser.parse("", Map.of()));
    }

    @Test
    @DisplayName("Parsing an expression that leave more than one term on the stack should throw " +
            "an IncompleteRpnException")
    void testParseIncomplete() {
        assertThrows(IncompleteRpnException.class, () -> RpnParser.parse("1 1 1 +", Map.of()));
    }

    @Test
    @DisplayName("Parsing an expression that consumes more terms than are on the stack should " +
            "throw an IncompleteRpnException")
    void testParseUnderflow() {
        assertThrows(IncompleteRpnException.class, () -> RpnParser.parse("1 1 + +", Map.of()));

    }

    @Test
    @DisplayName("Parsing an expression that applies an unknown function should throw an " +
            "UnknownFunctionException")
    void testParseUndefined() {
        assertThrows(UndefinedFunctionException.class, () -> RpnParser.parse("1 foo()", Map.of()));
    }
}
