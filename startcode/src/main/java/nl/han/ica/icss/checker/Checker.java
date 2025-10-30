package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> scopes;
    private int currentScope = 0;

    public void check(AST ast) {
        scopes = new HANLinkedList<>();
        scopes.insert(currentScope, new HashMap<String, ExpressionType>());
        analyzeNode(ast.root);
    }

    private void analyzeNode(ASTNode node) {
        if (node instanceof VariableAssignment) {
            handleVariableAssignment(currentScope, (VariableAssignment) node);
            processChildren(node);
            printScope(currentScope);

        } else if (node instanceof IfClause) {
            enterScope();
            validateIfClause((IfClause) node);
            processChildren(node);
            exitScope();

        } else if (node instanceof Stylerule) {
            enterScope();
            processChildren(node);
            exitScope();

        } else if (node instanceof Declaration) {
            validateDeclaration((Declaration) node);
            processChildren(node);

        } else if (node instanceof Operation) {
            validateOperation((Operation) node);
            processChildren(node);

        } else {
            processChildren(node);
        }
    }

    private void processChildren(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            analyzeNode(child);
        }
    }

    private void handleVariableAssignment(int scopeDepth, VariableAssignment assignment) {
        String name = assignment.name.name;
        ExpressionType type = evaluateType(assignment.expression);

        if (type == ExpressionType.UNDEFINED) {
            assignment.setError("Variable '" + name + "' has an undefined type.");
        }

        HashMap<String, ExpressionType> currentVars = scopes.get(scopeDepth);
        currentVars.put(name, type);
    }

    private void validateIfClause(IfClause node) {
        ExpressionType conditionType = evaluateType(node.conditionalExpression);
        if (conditionType == ExpressionType.UNDEFINED) {
            node.setError("If-statement condition is undefined.");
        }
        if (conditionType != ExpressionType.BOOL) {
            node.setError("If-statement condition must be boolean.");
        }
    }

    private void validateDeclaration(Declaration node) {
        ExpressionType type = evaluateType(node.expression);
        String property = node.property.name.toLowerCase();

        System.out.println("Property: " + property + " ValueType: " + type);

        if (type == ExpressionType.UNDEFINED) {
            node.setError("Value of property '" + property + "' is undefined.");
        }

        if (property.equals("color") || property.equals("background-color")) {
            if (type != ExpressionType.COLOR) {
                node.setError("Property '" + property + "' must be a color.");
            }
        } else if (property.equals("width") || property.equals("height")) {
            if (type != ExpressionType.PIXEL && type != ExpressionType.PERCENTAGE) {
                node.setError("Property '" + property + "' must be pixel or percentage, not " + type);
            }
        }
    }

    private void validateOperation(Operation op) {
        ExpressionType leftType = evaluateType(op.lhs);
        ExpressionType rightType = evaluateType(op.rhs);

        if (leftType == ExpressionType.UNDEFINED) {
            op.setError("Left-hand side is undefined.");
        }
        if (rightType == ExpressionType.UNDEFINED) {
            op.setError("Right-hand side is undefined.");
        }
        if (leftType == ExpressionType.COLOR || rightType == ExpressionType.COLOR) {
            op.setError("Cannot use color in operation.");
        }
        if (leftType == ExpressionType.BOOL || rightType == ExpressionType.BOOL) {
            op.setError("Cannot use boolean in operation.");
        }

        if (op instanceof MultiplyOperation) {
            if (leftType != ExpressionType.SCALAR && rightType != ExpressionType.SCALAR) {
                op.setError("Multiplication requires at least one scalar operand.");
            }
        } else {
            if (leftType == ExpressionType.SCALAR || rightType == ExpressionType.SCALAR) {
                op.setError("Cannot use scalar with plus or minus operations.");
            }
            if (leftType != rightType) {
                op.setError("Operands are not of the same type.");
            }
        }
    }

    private ExpressionType evaluateType(Expression expr) {
        if (expr instanceof PixelLiteral) return ExpressionType.PIXEL;
        if (expr instanceof PercentageLiteral) return ExpressionType.PERCENTAGE;
        if (expr instanceof ColorLiteral) return ExpressionType.COLOR;
        if (expr instanceof ScalarLiteral) return ExpressionType.SCALAR;
        if (expr instanceof BoolLiteral) return ExpressionType.BOOL;

        if (expr instanceof VariableReference) {
            String name = ((VariableReference) expr).name;
            ExpressionType type = lookupVariableType(name);
            if (type == ExpressionType.UNDEFINED) {
                expr.setError("Variable '" + name + "' is not defined in this scope.");
            }
            return type;
        }

        if (expr instanceof Operation) {
            ExpressionType type = getOperationResultType((Operation) expr);
            if (type == ExpressionType.UNDEFINED) {
                expr.setError("Operation result is undefined.");
            }
            return type;
        }

        return ExpressionType.UNDEFINED;
    }

    private ExpressionType getOperationResultType(Operation op) {
        ExpressionType left = evaluateType(op.lhs);
        ExpressionType right = evaluateType(op.rhs);

        if (left == ExpressionType.UNDEFINED || right == ExpressionType.UNDEFINED) {
            return ExpressionType.UNDEFINED;
        }
        if (left == ExpressionType.SCALAR && right == ExpressionType.SCALAR) return ExpressionType.SCALAR;
        if (left == ExpressionType.SCALAR) return right;
        if (right == ExpressionType.SCALAR) return left;
        if (left == right) return left;

        return ExpressionType.UNDEFINED;
    }

    private ExpressionType lookupVariableType(String name) {
        for (int i = currentScope; i >= 0; i--) {
            HashMap<String, ExpressionType> vars = scopes.get(i);
            if (vars.containsKey(name)) return vars.get(name);
        }
        return ExpressionType.UNDEFINED;
    }

    private void enterScope() {
        currentScope++;
        scopes.insert(currentScope, new HashMap<String, ExpressionType>());
    }

    private void exitScope() {
        scopes.delete(currentScope);
        currentScope--;
    }

    private void printScope(int scope) {
        System.out.println("Scope " + scope + ": " + scopes.get(scope).toString());
    }
}
