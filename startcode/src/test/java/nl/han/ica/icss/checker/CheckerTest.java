package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;
import nl.han.ica.icss.ast.selectors.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckerTest {

    // Creates an AST with given child nodes
    private AST createAST(ASTNode... children) {
        AST ast = new AST();
        Stylesheet stylesheet = new Stylesheet();
        for (ASTNode child : children) {
            stylesheet.addChild(child);
        }
        ast.setRoot(stylesheet);
        return ast;
    }

    // Creates a style rule (e.g., p { ... })
    private Stylerule createStylerule(String selector, ASTNode... declarations) {
        Stylerule rule = new Stylerule();
        rule.addChild(new TagSelector(selector));
        for (ASTNode decl : declarations) {
            rule.addChild(decl);
        }
        return rule;
    }

    // Creates a property declaration (e.g., width: 100px)
    private Declaration createDeclaration(String property, Expression value) {
        Declaration decl = new Declaration(property);
        decl.expression = value;
        return decl;
    }

    // Creates a variable assignment (e.g., Width := 100px)
    private VariableAssignment createVarAssignment(String name, Expression value) {
        VariableAssignment assignment = new VariableAssignment();
        assignment.name = new VariableReference(name);
        assignment.expression = value;
        return assignment;
    }

    // ==================== CH01: Undefined Variables ====================

    @Test
    public void testCH01_UndefinedVariableInDeclaration() {
        // width: UndefinedVar; should throw error
        Declaration decl = createDeclaration("width", new VariableReference("UndefinedVar"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(decl.hasError(), "Declaration should have error for undefined variable");
    }

    @Test
    public void testCH01_UndefinedVariableInAssignment() {
        // NewVar := UndefinedVar; should throw error
        VariableAssignment assignment = createVarAssignment("NewVar", new VariableReference("UndefinedVar"));
        AST ast = createAST(assignment);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(assignment.hasError(), "Assignment should have error for undefined variable");
    }

    @Test
    public void testCH01_DefinedVariableIsValid() {
        // Width := 100px; then use in declaration should be valid
        VariableAssignment assignment = createVarAssignment("Width", new PixelLiteral("100px"));
        Declaration decl = createDeclaration("width", new VariableReference("Width"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(assignment, rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(decl.hasError(), "Declaration should not have error for defined variable");
    }

    // ==================== CH02: Operation Type Compatibility ====================

    @Test
    public void testCH02_AddSameTypes_Valid() {
        // width: 100px + 50px; should be valid
        AddOperation add = new AddOperation();
        add.lhs = new PixelLiteral("100px");
        add.rhs = new PixelLiteral("50px");
        Declaration decl = createDeclaration("width", add);
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(add.hasError(), "Addition of same types should be valid");
    }

    @Test
    public void testCH02_AddDifferentTypes_Invalid() {
        // width: 100px + 50%; should be invalid
        AddOperation add = new AddOperation();
        add.lhs = new PixelLiteral("100px");
        add.rhs = new PercentageLiteral("50%");
        Declaration decl = createDeclaration("width", add);
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(add.hasError(), "Addition of different types should be invalid");
    }

    @Test
    public void testCH02_MultiplyWithScalar_Valid() {
        // width: 20px * 3; should be valid
        MultiplyOperation mult = new MultiplyOperation();
        mult.lhs = new PixelLiteral("20px");
        mult.rhs = new ScalarLiteral("3");
        Declaration decl = createDeclaration("width", mult);
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(mult.hasError(), "Multiplication with scalar should be valid");
    }

    @Test
    public void testCH02_MultiplyWithoutScalar_Invalid() {
        // width: 2px * 3px; should be invalid
        MultiplyOperation mult = new MultiplyOperation();
        mult.lhs = new PixelLiteral("2px");
        mult.rhs = new PixelLiteral("3px");
        Declaration decl = createDeclaration("width", mult);
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(mult.hasError(), "Multiplication without scalar should be invalid");
    }

    @Test
    public void testCH02_SubtractSameTypes_Valid() {
        // width: 100% - 20%; should be valid
        SubtractOperation sub = new SubtractOperation();
        sub.lhs = new PercentageLiteral("100%");
        sub.rhs = new PercentageLiteral("20%");
        Declaration decl = createDeclaration("width", sub);
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(sub.hasError(), "Subtraction of same types should be valid");
    }

    // ==================== CH03: No Colors in Operations ====================

    @Test
    public void testCH03_ColorInAddition_Invalid() {
        // #ff0000 + #00ff00; invalid
        AddOperation add = new AddOperation();
        add.lhs = new ColorLiteral("#ff0000");
        add.rhs = new ColorLiteral("#00ff00");
        Declaration decl = createDeclaration("color", add);
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(add.hasError(), "Addition with colors should be invalid");
    }

    @Test
    public void testCH03_ColorInMultiplication_Invalid() {
        // #ff0000 * 2; invalid
        MultiplyOperation mult = new MultiplyOperation();
        mult.lhs = new ColorLiteral("#ff0000");
        mult.rhs = new ScalarLiteral("2");
        Declaration decl = createDeclaration("color", mult);
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(mult.hasError(), "Multiplication with color should be invalid");
    }

    @Test
    public void testCH03_ColorInSubtraction_Invalid() {
        // #ff0000 - #00ff00; invalid
        SubtractOperation sub = new SubtractOperation();
        sub.lhs = new ColorLiteral("#ff0000");
        sub.rhs = new ColorLiteral("#00ff00");
        Declaration decl = createDeclaration("color", sub);
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(sub.hasError(), "Subtraction with colors should be invalid");
    }

    // ==================== CH04: Property-Value Type Matching ====================

    @Test
    public void testCH04_ColorProperty_PixelValue_Invalid() {
        // color: 12px; invalid
        Declaration decl = createDeclaration("color", new PixelLiteral("12px"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(decl.hasError(), "Color property with pixel value should be invalid");
    }

    @Test
    public void testCH04_WidthProperty_ColorValue_Invalid() {
        // width: #ff0000; invalid
        Declaration decl = createDeclaration("width", new ColorLiteral("#ff0000"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(decl.hasError(), "Width property with color value should be invalid");
    }

    @Test
    public void testCH04_ColorProperty_ColorValue_Valid() {
        // color: #ff0000; valid
        Declaration decl = createDeclaration("color", new ColorLiteral("#ff0000"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(decl.hasError(), "Color property with color value should be valid");
    }

    @Test
    public void testCH04_WidthProperty_PixelValue_Valid() {
        // width: 100px; valid
        Declaration decl = createDeclaration("width", new PixelLiteral("100px"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(decl.hasError(), "Width property with pixel value should be valid");
    }

    @Test
    public void testCH04_HeightProperty_PercentageValue_Valid() {
        // height: 50%; valid
        Declaration decl = createDeclaration("height", new PercentageLiteral("50%"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(decl.hasError(), "Height property with percentage value should be valid");
    }

    @Test
    public void testCH04_BackgroundColorProperty_ColorValue_Valid() {
        // background-color: #00ff00; valid
        Declaration decl = createDeclaration("background-color", new ColorLiteral("#00ff00"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(decl.hasError(), "Background-color property with color value should be valid");
    }

    // ==================== CH05: If-Condition is Boolean ====================

    @Test
    public void testCH05_BooleanLiteralCondition_Valid() {
        // if [TRUE] { width: 100px; } valid
        IfClause ifClause = new IfClause();
        ifClause.conditionalExpression = new BoolLiteral("TRUE");
        Declaration decl = createDeclaration("width", new PixelLiteral("100px"));
        ifClause.addChild(decl);
        Stylerule rule = createStylerule("p", ifClause);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(ifClause.hasError(), "If-condition with boolean literal should be valid");
    }

    @Test
    public void testCH05_BooleanVariableCondition_Valid() {
        // UseWidth := TRUE; if [UseWidth] { ... } valid
        VariableAssignment assignment = createVarAssignment("UseWidth", new BoolLiteral("TRUE"));
        IfClause ifClause = new IfClause();
        ifClause.conditionalExpression = new VariableReference("UseWidth");
        Declaration decl = createDeclaration("width", new PixelLiteral("100px"));
        ifClause.addChild(decl);
        Stylerule rule = createStylerule("p", ifClause);
        AST ast = createAST(assignment, rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(ifClause.hasError(), "If-condition with boolean variable should be valid");
    }

    @Test
    public void testCH05_PixelCondition_Invalid() {
        // if [100px] { ... } invalid
        IfClause ifClause = new IfClause();
        ifClause.conditionalExpression = new PixelLiteral("100px");
        Declaration decl = createDeclaration("width", new PixelLiteral("100px"));
        ifClause.addChild(decl);
        Stylerule rule = createStylerule("p", ifClause);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(ifClause.hasError(), "If-condition with pixel value should be invalid");
    }

    @Test
    public void testCH05_ColorCondition_Invalid() {
        // if [#ff0000] { ... } invalid
        IfClause ifClause = new IfClause();
        ifClause.conditionalExpression = new ColorLiteral("#ff0000");
        Declaration decl = createDeclaration("width", new PixelLiteral("100px"));
        ifClause.addChild(decl);
        Stylerule rule = createStylerule("p", ifClause);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(ifClause.hasError(), "If-condition with color value should be invalid");
    }

    @Test
    public void testCH05_NonBooleanVariableCondition_Invalid() {
        // Width := 100px; if [Width] { ... } invalid
        VariableAssignment assignment = createVarAssignment("Width", new PixelLiteral("100px"));
        IfClause ifClause = new IfClause();
        ifClause.conditionalExpression = new VariableReference("Width");
        Declaration decl = createDeclaration("width", new PixelLiteral("100px"));
        ifClause.addChild(decl);
        Stylerule rule = createStylerule("p", ifClause);
        AST ast = createAST(assignment, rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(ifClause.hasError(), "If-condition with non-boolean variable should be invalid");
    }

    // ==================== CH06: Variable Scope ====================

    @Test
    public void testCH06_VariableUsedInSameScope_Valid() {
        // p { Width := 100px; width: Width; } valid
        VariableAssignment assignment = createVarAssignment("Width", new PixelLiteral("100px"));
        Declaration decl = createDeclaration("width", new VariableReference("Width"));
        Stylerule rule = createStylerule("p", assignment, decl);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(decl.hasError(), "Variable used in same scope should be valid");
    }

    @Test
    public void testCH06_VariableUsedInChildScope_Valid() {
        // Width := 100px; p { width: Width; } valid
        VariableAssignment assignment = createVarAssignment("Width", new PixelLiteral("100px"));
        Declaration decl = createDeclaration("width", new VariableReference("Width"));
        Stylerule rule = createStylerule("p", decl);
        AST ast = createAST(assignment, rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(decl.hasError(), "Variable from parent scope should be accessible");
    }

    @Test
    public void testCH06_VariableUsedBeforeDeclaration_Invalid() {
        // p { width: Width; Width := 100px; } invalid
        Declaration decl = createDeclaration("width", new VariableReference("Width"));
        VariableAssignment assignment = createVarAssignment("Width", new PixelLiteral("100px"));
        Stylerule rule = createStylerule("p", decl, assignment);
        AST ast = createAST(rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(decl.hasError(), "Variable used before declaration should be invalid");
    }

    @Test
    public void testCH06_VariableInIfScope_Valid() {
        // UseWidth := TRUE; if [UseWidth] { Width := 100px; width: Width; } valid
        VariableAssignment boolAssignment = createVarAssignment("UseWidth", new BoolLiteral("TRUE"));

        IfClause ifClause = new IfClause();
        ifClause.conditionalExpression = new VariableReference("UseWidth");
        VariableAssignment assignment = createVarAssignment("Width", new PixelLiteral("100px"));
        Declaration decl = createDeclaration("width", new VariableReference("Width"));
        ifClause.addChild(assignment);
        ifClause.addChild(decl);

        Stylerule rule = createStylerule("p", ifClause);
        AST ast = createAST(boolAssignment, rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertFalse(decl.hasError(), "Variable in if scope should be accessible within that scope");
    }

    @Test
    public void testCH06_VariableFromIfScopeNotAccessibleOutside_Invalid() {
        // Variable defined in if scope should not be accessible outside
        VariableAssignment boolAssignment = createVarAssignment("UseWidth", new BoolLiteral("TRUE"));

        IfClause ifClause = new IfClause();
        ifClause.conditionalExpression = new VariableReference("UseWidth");
        VariableAssignment assignment = createVarAssignment("Width", new PixelLiteral("100px"));
        ifClause.addChild(assignment);

        Declaration decl = createDeclaration("width", new VariableReference("Width"));
        Stylerule rule = createStylerule("p", ifClause, decl);
        AST ast = createAST(boolAssignment, rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(decl.hasError(), "Variable from if scope should not be accessible outside");
    }

    // ==================== Combined/Complex Tests ====================

    @Test
    public void testComplex_MultipleErrors() {
        // Width := 100px; multiple errors in declarations and operations
        VariableAssignment assignment = createVarAssignment("Width", new PixelLiteral("100px"));

        Declaration decl1 = createDeclaration("color", new VariableReference("Width")); // wrong type

        AddOperation add = new AddOperation();
        add.lhs = new ColorLiteral("#ff0000");
        add.rhs = new PixelLiteral("10px");
        Declaration decl2 = createDeclaration("width", add); // color in operation

        IfClause ifClause = new IfClause();
        ifClause.conditionalExpression = new VariableReference("Width"); // non-boolean
        Declaration decl3 = createDeclaration("height", new VariableReference("UndefinedVar")); // undefined var
        ifClause.addChild(decl3);

        Stylerule rule = createStylerule("p", decl1, decl2, ifClause);
        AST ast = createAST(assignment, rule);

        Checker checker = new Checker();
        checker.check(ast);

        assertTrue(decl1.hasError(), "Should detect wrong property type");
        assertTrue(add.hasError(), "Should detect color in operation");
        assertTrue(ifClause.hasError(), "Should detect non-boolean condition");
        assertTrue(decl3.hasError(), "Should detect undefined variable");
    }
}
