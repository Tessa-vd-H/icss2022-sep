package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;
    private int scope = 0;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        variableTypes.insert(scope, new HashMap<>());
        walkTree(ast.root);
    }

    private void walkTree(ASTNode node) {
        if (node instanceof VariableAssignment) {
            checkVariableAssignment(scope, (VariableAssignment) node);
            childWalkTree(node);
            printVariables(scope);
        } else if (node instanceof IfClause) {
            createNewScope();
            checkIfStatement((IfClause) node);
            childWalkTree(node);
            deleteScope();
        } else if (node instanceof Stylerule) {
            createNewScope();
            childWalkTree(node);
            deleteScope();
        } else if (node instanceof Declaration) {
            checkDeclaration((Declaration) node);
            childWalkTree(node);
        } else if (node instanceof Operation) {
            checkOperation((Operation) node);
            childWalkTree(node);
        } else {
            childWalkTree(node);
        }
    }

    private void childWalkTree(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            walkTree(child);
        }
    }

    private void checkDeclaration(Declaration node) {
        ExpressionType valueType = getType(node.expression);
        String property = node.property.name.toLowerCase();

        if (valueType == ExpressionType.UNDEFINED) {
            node.setError("Value of property " + property + " is undefined");
        }

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

    private void checkOperation(Operation node) {
        ExpressionType left = getType(node.lhs);
        ExpressionType right = getType(node.rhs);

        if (left == ExpressionType.UNDEFINED) node.setError("Left-hand side of operation is undefined");
        if (right == ExpressionType.UNDEFINED) node.setError("Right-hand side of operation is undefined");

        if (left == ExpressionType.COLOR || right == ExpressionType.COLOR)
            node.setError("Cannot perform operation with color");
        if (left == ExpressionType.BOOL || right == ExpressionType.BOOL)
            node.setError("Cannot perform operation with boolean");

        if (node instanceof MultiplyOperation) {
            if (left != ExpressionType.SCALAR && right != ExpressionType.SCALAR)
                node.setError("At least one operand of multiplication should be scalar");
        } else {
            if (left == ExpressionType.SCALAR || right == ExpressionType.SCALAR)
                node.setError("Cannot perform addition/subtraction with scalar");
            if (left != right)
                node.setError("Operands of operation are not of the same type");
        }
    }

    private void checkVariableAssignment(int depth, VariableAssignment assignment) {
        String name = assignment.name.name;
        ExpressionType type = getType(assignment.expression);

        if (type == ExpressionType.UNDEFINED) {
            assignment.setError("Variable " + name + " has undefined type");
        }

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

    private void checkIfStatement(IfClause node) {
        ExpressionType conditionType = getType(node.conditionalExpression);

        if (conditionType == ExpressionType.UNDEFINED)
            node.setError("Condition in if-statement is undefined");
        if (conditionType != ExpressionType.BOOL)
            node.setError("If statement condition is not boolean");
    }

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

    private ExpressionType getVariableType(String name) {
        for (int i = scope; i >= 0; i--) {
            if (variableTypes.get(i).containsKey(name)) return variableTypes.get(i).get(name);
        }
        return ExpressionType.UNDEFINED;
    }

    private void createNewScope() {
        scope++;
        variableTypes.insert(scope, new HashMap<>());
    }

    private void deleteScope() {
        variableTypes.delete(scope);
        scope--;
    }

    private void printVariables(int scope) {
        System.out.println("Scope " + scope + ": " + variableTypes.get(scope));
    }
}
