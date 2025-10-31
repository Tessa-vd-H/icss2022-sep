package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Evaluator implements Transform {

    // Stack-like linked list of scopes; each scope holds variable-value mappings
    private IHANLinkedList<HashMap<String, Literal>> variableValues;

    // Current scope index (starts at 0)
    private int scope = 0;

    // Constructor initializes the variable scope list
    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    // Entry point of the transform — called once the AST has been built
    @Override
    public void apply(AST ast) {
        // Create the root (global) scope
        variableValues.insert(scope, new HashMap<>());

        // Recursively evaluate all nodes starting from the root
        walkTree(null, ast.root);
    }

    // Recursively traverses the AST and evaluates nodes based on their type.
    private void walkTree(ASTNode parent, ASTNode node) {
        if (node instanceof VariableAssignment) {
            // Handle variable definitions
            evaluateVariableAssignment(scope, (VariableAssignment) node);
            childWalkTree(node);
            printVariables(scope);

        } else if (node instanceof IfClause) {
            // New scope for conditional logic
            createNewScope();
            evaluateIfStatement(parent, (IfClause) node);
            childWalkTree(node);
            deleteScope();

        } else if (node instanceof Stylerule) {
            // Each style rule gets its own scope
            createNewScope();
            childWalkTree(node);
            deleteScope();

        } else if (node instanceof Declaration) {
            // Resolve any expressions inside declarations
            evaluateDeclaration(parent, (Declaration) node);
            childWalkTree(node);

        } else {
            // Generic recursion for other node types
            childWalkTree(node);
        }
    }

    // Helper method that recursively calls walkTree() for all children
    private void childWalkTree(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            walkTree(node, child);
        }
    }

    // Replaces a declaration's expression with its evaluated literal value
    private void evaluateDeclaration(ASTNode parent, Declaration node) {
        node.expression = getValue(node.expression);
    }

    // Evaluates a variable assignment and stores its literal value in the current scope
    private void evaluateVariableAssignment(int depth, VariableAssignment assignment) {
        String name = assignment.name.name;
        Literal value = getValue(assignment.expression);

        if (value == null) {
            assignment.setError("Variable " + name + " has no value.");
        }

        assignment.expression = value;

        // Store or overwrite variable in the current scope
        Literal existing = getVariableValue(name);
        if (existing != null) {
            System.out.println("Variable " + name + " is already defined in scope " + getVariableScope(name));
            variableValues.get(depth).put(name, value);
        } else {
            variableValues.get(depth).put(name, value);
        }
    }

    // Returns the scope index where a variable is defined
    private int getVariableScope(String name) {
        for (int i = scope; i >= 0; i--) {
            if (variableValues.get(i).containsKey(name)) return i;
        }
        return -1;
    }

    // Evaluates an expression and returns a concrete Literal
    private Literal getValue(Expression expr) {
        if (expr instanceof BoolLiteral) return (BoolLiteral) expr;
        if (expr instanceof PixelLiteral) return (PixelLiteral) expr;
        if (expr instanceof ColorLiteral) return (ColorLiteral) expr;
        if (expr instanceof PercentageLiteral) return (PercentageLiteral) expr;
        if (expr instanceof ScalarLiteral) return (ScalarLiteral) expr;
        if (expr instanceof VariableReference) return getVariableValue(((VariableReference) expr).name);
        if (expr instanceof Operation) return evaluateOperation((Operation) expr);
        return null;
    }

    // Retrieves a variable’s value from the nearest scope where it exists
    private Literal getVariableValue(String name) {
        for (int i = scope; i >= 0; i--) {
            if (variableValues.get(i).containsKey(name)) {
                return variableValues.get(i).get(name);
            }
        }
        return null;
    }

    // Evaluates arithmetic operations
    private Literal evaluateOperation(Operation op) {
        Literal left = getValue(op.lhs);
        Literal right = getValue(op.rhs);

        if (op instanceof AddOperation) return add(left, right);
        if (op instanceof SubtractOperation) return subtract(left, right);
        if (op instanceof MultiplyOperation) return multiply(left, right);
        return null;
    }

    // ---------------- Arithmetic Evaluation ----------------

    // Handles multiplication between scalars and size types
    private Literal multiply(Literal left, Literal right) {
        if (left instanceof PixelLiteral && right instanceof ScalarLiteral)
            return new PixelLiteral(((PixelLiteral) left).value * ((ScalarLiteral) right).value);
        if (left instanceof ScalarLiteral && right instanceof PixelLiteral)
            return new PixelLiteral(((ScalarLiteral) left).value * ((PixelLiteral) right).value);
        if (left instanceof PercentageLiteral && right instanceof ScalarLiteral)
            return new PercentageLiteral(((PercentageLiteral) left).value * ((ScalarLiteral) right).value);
        if (left instanceof ScalarLiteral && right instanceof PercentageLiteral)
            return new PercentageLiteral(((ScalarLiteral) left).value * ((PercentageLiteral) right).value);
        if (left instanceof ScalarLiteral && right instanceof ScalarLiteral)
            return new ScalarLiteral(((ScalarLiteral) left).value * ((ScalarLiteral) right).value);
        return null;
    }

    // Handles addition between literals
    private Literal add(Literal left, Literal right) {
        if (left instanceof PixelLiteral && right instanceof PixelLiteral)
            return new PixelLiteral(((PixelLiteral) left).value + ((PixelLiteral) right).value);
        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral)
            return new PercentageLiteral(((PercentageLiteral) left).value + ((PercentageLiteral) right).value);
        return null;
    }

    // Handles subtraction between literals
    private Literal subtract(Literal left, Literal right) {
        if (left instanceof PixelLiteral && right instanceof PixelLiteral)
            return new PixelLiteral(((PixelLiteral) left).value - ((PixelLiteral) right).value);
        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral)
            return new PercentageLiteral(((PercentageLiteral) left).value - ((PercentageLiteral) right).value);
        return null;
    }

    // ---------------- IF / ELSE Evaluation ----------------

    //Evaluates an if/else clause. Determines which body to keep, removes the original IfClause node, and inlines the correct body into the parent
    private void evaluateIfStatement(ASTNode parent, IfClause node) {
        ArrayList<ASTNode> body = null;
        Literal condition = getValue(node.conditionalExpression);

        // Choose which body to evaluate based on the boolean condition
        if (condition instanceof BoolLiteral) {
            if (((BoolLiteral) condition).value) body = node.body;
            else if (node.elseClause != null) body = node.elseClause.body;
        }

        // Remove the original if-statement node
        parent.removeChild(node);

        // Insert the chosen body directly into the parent node
        if (body != null) {
            for (ASTNode child : body) {
                if (child instanceof IfClause) {
                    // Nested if
                    evaluateIfStatement(parent, (IfClause) child);

                } else if (child instanceof VariableAssignment) {
                    // Evaluate variable inside correct scope
                    String varName = ((VariableAssignment) child).name.name;
                    int targetScope = getVariableScope(varName);

                    // If variable not found, assume parent scope
                    if (targetScope == -1) {
                        targetScope = scope - 1;
                    }

                    evaluateVariableAssignment(targetScope, (VariableAssignment) child);

                } else if (child instanceof Declaration) {
                    // Replace previous declarations of same property
                    removeExistingDeclaration(parent, ((Declaration) child).property.name);
                    parent.addChild(child);

                } else {
                    // Add generic nodes
                    parent.addChild(child);
                }
            }
        }
    }

    // Removes any existing declaration for the same CSS property to avoid duplicates when if/else overwrites values
    private void removeExistingDeclaration(ASTNode parent, String propertyName) {
        ArrayList<ASTNode> toRemove = new ArrayList<>();
        for (ASTNode child : parent.getChildren()) {
            if (child instanceof Declaration) {
                if (((Declaration) child).property.name.equals(propertyName)) {
                    toRemove.add(child);
                }
            }
        }
        for (ASTNode node : toRemove) {
            parent.removeChild(node);
        }
    }

    // ---------------- Scope Management ----------------

    // Creates a new nested variable scope
    private void createNewScope() {
        scope++;
        variableValues.insert(scope, new HashMap<>());
    }

    // Removes the current variable scope after exiting a block
    private void deleteScope() {
        variableValues.delete(scope);
        scope--;
    }

    // Debug helper: prints variables in the current scope to console
    private void printVariables(int scope) {
        System.out.println("Scope " + scope + ": " + variableValues.get(scope));
    }
}
