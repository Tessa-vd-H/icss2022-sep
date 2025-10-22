package nl.han.ica.icss.parser;

import java.util.ArrayList;
import java.util.List;
import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.*;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}

    public AST getAST() {
        return ast;
    }

	// --- 1. LEAF NODES (Literals en Selectoren) ---

	@Override
	public void exitColor_literal(ICSSParser.Color_literalContext ctx) {
		currentContainer.push(new ColorLiteral(ctx.getChild(0).getText()));
	}

	@Override
	public void exitPixel_literal(ICSSParser.Pixel_literalContext ctx) {
		currentContainer.push(new PixelLiteral(ctx.getChild(0).getText()));
	}

	@Override
	public void exitPercentage_literal(ICSSParser.Percentage_literalContext ctx) {
		currentContainer.push(new PercentageLiteral(ctx.getChild(0).getText()));
	}

	@Override
	public void exitTag_selector(ICSSParser.Tag_selectorContext ctx) {
		currentContainer.push(new TagSelector(ctx.getChild(0).getText()));
	}

	@Override
	public void exitId_selector(ICSSParser.Id_selectorContext ctx) {
		String id = ctx.getChild(0).getText().substring(1);
		currentContainer.push(new IdSelector(id));
	}

	@Override
	public void exitClass_selector(ICSSParser.Class_selectorContext ctx) {
		String rawText = ctx.getChild(0).getText();
		String className = rawText.substring(1);
		currentContainer.push(new ClassSelector(className));
	}

	// --- 2. COMPOSITE NODES (Declaration) ---

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		Expression value = (Expression) currentContainer.pop();

		String propertyNameText = ctx.propertyName().getText();

		PropertyName propNameNode = new PropertyName(propertyNameText);

		currentContainer.push(new Declaration(propNameNode, value));
	}

	// --- 3. TOP-LEVEL NODES (Stylerule en Stylesheet) ---

	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		List<ASTNode> body = new ArrayList<>();

		while (!currentContainer.isEmpty() && currentContainer.peek() instanceof Declaration) {
			body.add(0, currentContainer.pop());
		}

		Selector selector = (Selector) currentContainer.pop();

		currentContainer.push(new Stylerule(selector, body));
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		List<ASTNode> body = new ArrayList<>();

		while (!currentContainer.isEmpty()) {
			body.add(0, currentContainer.pop());
		}

		Stylesheet stylesheet = new Stylesheet(body);
		ast.setRoot(stylesheet);
	}
}