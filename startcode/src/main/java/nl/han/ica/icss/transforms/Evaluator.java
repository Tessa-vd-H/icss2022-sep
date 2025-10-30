package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Evaluator implements Transform {

    private IHANLinkedList<HashMap<String, Literal>> variableValues;
    private int scope = 0;

    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues.insert(scope, new HashMap<>());
        walkTree(null, ast.root);
    }

    private void walkTree(ASTNode parent, ASTNode node) {
        if (node instanceof VariableAssignment) {
            evaluateVariableAssignment(scope, (VariableAssignment) node);
            childWalkTree(node);
            printVariables(scope);
        } else if (node instanceof IfClause) {
            createNewScope();
            evaluateIfStatement(parent, (IfClause) node);
            childWalkTree(node);
            deleteScope();
        } else if (node instanceof Stylerule) {
            createNewScope();
            childWalkTree(node);
            deleteScope();
        } else if (node instanceof Declaration) {
            evaluateDeclaration(parent, (Declaration) node);
            childWalkTree(node);
        } else {
            childWalkTree(node);
        }
    }

    private void childWalkTree(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            walkTree(node, child);
        }
    }

    private void evaluateDeclaration(ASTNode parent, Declaration node) {
        node.expression = getValue(node.expression);
    }

    private void evaluateVariableAssignment(int depth, VariableAssignment assignment) {
        String name = assignment.name.name;
        Literal value = getValue(assignment.expression);

        if (value == null) {
            assignment.setError("Variable " + name + " has no value.");
        }

        assignment.expression = value;

        Literal existing = getVariableValue(name);
        if (existing != null) {
            System.out.println("Variable " + name + " is already defined in scope " + getVariableScope(name));
            variableValues.get(depth).put(name, value);
        } else {
            variableValues.get(depth).put(name, value);
        }
    }

    private int getVariableScope(String name) {
        for (int i = scope; i >= 0; i--) {
            if (variableValues.get(i).containsKey(name)) return i;
        }
        return -1;
    }

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

    private Literal getVariableValue(String name) {
        for (int i = scope; i >= 0; i--) {
            if (variableValues.get(i).containsKey(name)) {
                return variableValues.get(i).get(name);
            }
        }
        return null;
    }

    private Literal evaluateOperation(Operation op) {
        Literal left = getValue(op.lhs);
        Literal right = getValue(op.rhs);

        if (op instanceof AddOperation) return add(left, right);
        if (op instanceof SubtractOperation) return subtract(left, right);
        if (op instanceof MultiplyOperation) return multiply(left, right);
        return null;
    }

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

    private Literal add(Literal left, Literal right) {
        if (left instanceof PixelLiteral && right instanceof PixelLiteral)
            return new PixelLiteral(((PixelLiteral) left).value + ((PixelLiteral) right).value);
        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral)
            return new PercentageLiteral(((PercentageLiteral) left).value + ((PercentageLiteral) right).value);
        return null;
    }

    private Literal subtract(Literal left, Literal right) {
        if (left instanceof PixelLiteral && right instanceof PixelLiteral)
            return new PixelLiteral(((PixelLiteral) left).value - ((PixelLiteral) right).value);
        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral)
            return new PercentageLiteral(((PercentageLiteral) left).value - ((PercentageLiteral) right).value);
        return null;
    }

    private void evaluateIfStatement(ASTNode parent, IfClause node) {
        ArrayList<ASTNode> body = null;
        Literal condition = getValue(node.conditionalExpression);

        if (condition instanceof BoolLiteral) {
            if (((BoolLiteral) condition).value) body = node.body;
            else if (node.elseClause != null) body = node.elseClause.body;
        }

        parent.removeChild(node);

        if (body != null) {
            for (ASTNode child : body) {
                if (child instanceof IfClause) {
                    evaluateIfStatement(parent, (IfClause) child);
                } else if (child instanceof VariableAssignment) {
                    // Find which scope the variable exists in and update THAT scope
                    String varName = ((VariableAssignment) child).name.name;
                    int targetScope = getVariableScope(varName);

                    // If variable doesn't exist, update parent scope
                    if (targetScope == -1) {
                        targetScope = scope - 1;
                    }

                    evaluateVariableAssignment(targetScope, (VariableAssignment) child);
                    // DO NOT add to parent
                } else if (child instanceof Declaration) {
                    // Check if a declaration with same property already exists
                    removeExistingDeclaration(parent, ((Declaration) child).property.name);
                    parent.addChild(child);
                } else {
                    parent.addChild(child);
                }
            }
        }
    }

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

    private void createNewScope() {
        scope++;
        variableValues.insert(scope, new HashMap<>());
    }

    private void deleteScope() {
        variableValues.delete(scope);
        scope--;
    }

    private void printVariables(int scope) {
        System.out.println("Scope " + scope + ": " + variableValues.get(scope));
    }
}
