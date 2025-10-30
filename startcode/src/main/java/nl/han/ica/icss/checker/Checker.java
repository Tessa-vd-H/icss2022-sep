package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();

        walkTree(ast);
    }

    private void walkTree(AST ast) {
        for (ASTNode node : ast.root.getChildren()) {
            readNode(node, 0);
        }
    }

    private void readNode(ASTNode node, int level) {
        for (ASTNode child : node.getChildren()) {
            readNode(child, level + 1);
        }
    }
    
}
