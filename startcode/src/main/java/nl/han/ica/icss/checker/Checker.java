package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class Checker {

    // Linked list to store variable types for each scope
    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    // Keeps track of the current scope depth
    private int scope = 0;

    // Entry point of the checker
    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        variableTypes.insert(scope, new HashMap<>()); // create root scope
        walkTree(ast.root); // start recursive traversal
    }

    // Recursively traverses the AST and performs type checking
    private void walkTree(ASTNode node) {
        if (node instanceof VariableAssignment) {
            // Handle variable assignments
            checkVariableAssignment(scope, (VariableAssignment) node);
            childWalkTree(node);
            printVariables(scope);
        } else if (node instanceof IfClause) {
            // New scope for if-clauses
            createNewScope();
            checkIfStatement((IfClause) node);
            childWalkTree(node);
            deleteScope();
        } else if (node instanceof Stylerule) {
            // New scope for each style rule
            createNewScope();
            childWalkTree(node);
            deleteScope();
        } else if (node instanceof Declaration) {
            // Check property declarations
            checkDeclaration((Declaration) node);
            childWalkTree(node);
        } else if (node instanceof Operation) {
            // Check arithmetic operations
            checkOperation((Operation) node);
            childWalkTree(node);
        } else {
            // Generic traversal
            childWalkTree(node);
        }
    }

    // Helper to traverse all children of a node
    private void childWalkTree(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            walkTree(child);
        }
    }

    // Checks if a declaration has the correct type for its property
    private void checkDeclaration(Declaration node) {
        ExpressionType valueType = getType(node.expression);
        String property = node.property.name.toLowerCase();

        // Undefined expression type
        if (valueType == ExpressionType.UNDEFINED) {
            node.setError("Value of property " + property + " is undefined");
        }

        // Type validation per CSS property
        if (property.equals("color") || property.equals("background-color")) {
            if (valueType != ExpressionType.COLOR) {
                node.setError("Value of property " + property + " should be of type color");
            }
        } else if (property.equals("width") || property.equals("height")) {
            if (valueType != ExpressionType.PIXEL && valueType != ExpressionType.PERCENTAGE) {
                node.setError("Value of property " + property + " should be pixel or percentage");
            }
        }
    }

    // Checks correctness of arithmetic or logical operations
    private void checkOperation(Operation node) {
        ExpressionType left = getType(node.lhs);
        ExpressionType right = getType(node.rhs);

        // Check for undefined operands
        if (left == ExpressionType.UNDEFINED) node.setError("Left-hand side of operation is undefined");
        if (right == ExpressionType.UNDEFINED) node.setError("Right-hand side of operation is undefined");

        // Disallow operations involving colors or booleans
        if (left == ExpressionType.COLOR || right == ExpressionType.COLOR)
            node.setError("Cannot perform operation with color");
        if (left == ExpressionType.BOOL || right == ExpressionType.BOOL)
            node.setError("Cannot perform operation with boolean");

        // Multiplication rules
        if (node instanceof MultiplyOperation) {
            if (left != ExpressionType.SCALAR && right != ExpressionType.SCALAR)
                node.setError("At least one operand of multiplication should be scalar");
        } else {
            // Addition/subtraction rules
            if (left == ExpressionType.SCALAR || right == ExpressionType.SCALAR)
                node.setError("Cannot perform addition/subtraction with scalar");
            if (left != right)
                node.setError("Operands of operation are not of the same type");
        }
    }

    // Checks a variable assignment and stores its type
    private void checkVariableAssignment(int depth, VariableAssignment assignment) {
        String name = assignment.name.name;
        ExpressionType type = getType(assignment.expression);

        // Undefined type detection
        if (type == ExpressionType.UNDEFINED) {
            assignment.setError("Variable " + name + " has undefined type");
        }

        // Check for redefinition with conflicting types
        ExpressionType existing = getVariableType(name);
        if (existing != ExpressionType.UNDEFINED) {
            if (existing != type) {
                assignment.setError("Variable " + name + " is already defined with different type");
            }
            variableTypes.get(depth).put(name, type);
        } else {
            variableTypes.get(depth).put(name, type);
        }
    }

    // Validates an if-statement’s condition type
    private void checkIfStatement(IfClause node) {
        ExpressionType conditionType = getType(node.conditionalExpression);

        if (conditionType == ExpressionType.UNDEFINED)
            node.setError("Condition in if-statement is undefined");
        if (conditionType != ExpressionType.BOOL)
            node.setError("If statement condition is not boolean");
    }

    // Determines the type of an expression node
    private ExpressionType getType(Expression expression) {
        if (expression instanceof PixelLiteral) return ExpressionType.PIXEL;
        if (expression instanceof PercentageLiteral) return ExpressionType.PERCENTAGE;
        if (expression instanceof ColorLiteral) return ExpressionType.COLOR;
        if (expression instanceof ScalarLiteral) return ExpressionType.SCALAR;
        if (expression instanceof BoolLiteral) return ExpressionType.BOOL;
        if (expression instanceof VariableReference) return getVariableType(((VariableReference) expression).name);
        if (expression instanceof Operation) return getOperationType((Operation) expression);

        return ExpressionType.UNDEFINED;
    }

    // Determines result type of an operation based on operand types
    private ExpressionType getOperationType(Operation op) {
        ExpressionType left = getType(op.lhs);
        ExpressionType right = getType(op.rhs);

        if (left == ExpressionType.UNDEFINED || right == ExpressionType.UNDEFINED) return ExpressionType.UNDEFINED;
        if (left == ExpressionType.SCALAR && right == ExpressionType.SCALAR) return ExpressionType.SCALAR;
        if (left == ExpressionType.SCALAR) return right;
        if (right == ExpressionType.SCALAR) return left;
        if (left == right) return left;

        return ExpressionType.UNDEFINED;
    }

    // Finds the type of a variable by searching through active scopes
    private ExpressionType getVariableType(String name) {
        for (int i = scope; i >= 0; i--) {
            if (variableTypes.get(i).containsKey(name)) return variableTypes.get(i).get(name);
        }
        return ExpressionType.UNDEFINED;
    }

    // Creates a new variable scope
    private void createNewScope() {
        scope++;
        variableTypes.insert(scope, new HashMap<>());
    }

    // Removes the current scope and all its variables
    private void deleteScope() {
        variableTypes.delete(scope);
        scope--;
    }

    // Debug helper to print variable types per scope
    private void printVariables(int scope) {
        System.out.println("Scope " + scope + ": " + variableTypes.get(scope));
    }
}
