package nl.han.ica.icss.parser;

import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;
import nl.han.ica.icss.ast.selectors.*;

public class ASTListener extends ICSSBaseListener {

	// The AST being built during parsing
	private AST ast;

	// A stack to keep track of the current node hierarchy
	private IHANStack<ASTNode> currentContainer;

	// Constructor initializes the AST and an empty stack
	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}

	// Returns the completed AST after parsing
	public AST getAST() {
		return ast;
	}

	// ---------------- STYLESHEET ----------------
	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		// Create a new root Stylesheet node and push it onto the stack
		Stylesheet stylesheet = new Stylesheet();
		ast.root = stylesheet;
		currentContainer.push(stylesheet);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		// Remove stylesheet from the stack (we are done building it)
		currentContainer.pop();
	}

	// ---------------- STYLE RULE ----------------
	@Override
	public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
		// Create a new style rule
		Stylerule stylerule = new Stylerule();

		// Attach the stylerule to the current parent
		if (!currentContainer.isEmpty()) {
			currentContainer.peek().addChild(stylerule);
		} else {
			// Defensive fix in case Stylesheet wasn't pushed
			Stylesheet stylesheet = new Stylesheet();
			ast.root = stylesheet;
			currentContainer.push(stylesheet);
			currentContainer.peek().addChild(stylerule);
		}

		// Push the stylerule to become the current container
		currentContainer.push(stylerule);
	}

	@Override
	public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
		// Pop the stylerule after finishing its content
		currentContainer.pop();
	}

	// ---------------- SELECTORS ----------------
	@Override
	public void enterSelector(ICSSParser.SelectorContext ctx) {
		// Determine selector type: class, id, or tag
		Selector selector = null;
		if (ctx.CLASS_IDENT() != null) {
			selector = new ClassSelector(ctx.CLASS_IDENT().getText());
		} else if (ctx.ID_IDENT() != null) {
			selector = new IdSelector(ctx.ID_IDENT().getText());
		} else if (ctx.LOWER_IDENT() != null) {
			selector = new TagSelector(ctx.LOWER_IDENT().getText());
		}

		// Attach selector to the current stylerule
		if (selector != null) {
			currentContainer.peek().addChild(selector);
		}
	}

	// ---------------- DECLARATIONS ----------------
	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		// Create a new declaration
		Declaration declaration = new Declaration(ctx.PROPERTIES().getText());

		// Attach declaration to current container
		if (!currentContainer.isEmpty()) {
			currentContainer.peek().addChild(declaration);
		}

		// Push declaration for its value parsing
		currentContainer.push(declaration);
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		// Pop declaration after parsing value
		currentContainer.pop();
	}

	// ---------------- VARIABLE ASSIGNMENTS ----------------
	@Override
	public void enterVarAssign(ICSSParser.VarAssignContext ctx) {
		// Create a variable assignment
		VariableAssignment varAssignment = new VariableAssignment();

		// Create the variable reference
		if (ctx.CAPITAL_IDENT() != null) {
			VariableReference varRef = new VariableReference(ctx.CAPITAL_IDENT().getText());
			varAssignment.addChild(varRef);
		}

		// Attach assignment to stylesheet
		if (!currentContainer.isEmpty()) {
			currentContainer.peek().addChild(varAssignment);
		}

		// Push the assignment to attach its value later
		currentContainer.push(varAssignment);
	}

	@Override
	public void exitVarAssign(ICSSParser.VarAssignContext ctx) {
		currentContainer.pop();
	}

	// Variable values
	@Override
	public void enterVarValue(ICSSParser.VarValueContext ctx) {
		Expression expr = makeLiteralOrVar(ctx);
		if (expr != null) {
			currentContainer.peek().addChild(expr);
		}
	}

	// Property values
	@Override
	public void enterPropValue(ICSSParser.PropValueContext ctx) {
		Expression expr = makeLiteralOrVar(ctx);
		if (expr != null) {
			currentContainer.peek().addChild(expr);
		}
	}

	// ---------------- EXPRESSIONS ----------------
	@Override
	public void enterExpression(ICSSParser.ExpressionContext ctx) {
		Expression expr = null;

		// Handle variables and booleans
		if (ctx.CAPITAL_IDENT() != null) {
			expr = new VariableReference(ctx.CAPITAL_IDENT().getText());
		} else if (ctx.TRUE() != null) {
			expr = new BoolLiteral(ctx.TRUE().getText());
		} else if (ctx.FALSE() != null) {
			expr = new BoolLiteral(ctx.FALSE().getText());
		}

		// Attach expression to current container
		if (expr != null && !currentContainer.isEmpty()) {
			currentContainer.peek().addChild(expr);
		}
	}

	// ---------------- IF / ELSE CLAUSES ----------------
	@Override
	public void enterIfStmt(ICSSParser.IfStmtContext ctx) {
		// Create and push a new IfClause node
		IfClause ifClause = new IfClause();
		push(ifClause);
	}

	@Override
	public void exitIfStmt(ICSSParser.IfStmtContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterElseStmt(ICSSParser.ElseStmtContext ctx) {
		// Create and push a new ElseClause node
		ElseClause elseClause = new ElseClause();
		push(elseClause);
	}

	@Override
	public void exitElseStmt(ICSSParser.ElseStmtContext ctx) {
		currentContainer.pop();
	}

	// ---------------- CALCULATIONS ----------------
	@Override
	public void enterCalc(ICSSParser.CalcContext ctx) {
		// Binary operations: operand1 operator operand2
		if (ctx.getChildCount() == 3 && (ctx.MUL() != null || ctx.PLUS() != null || ctx.MIN() != null)) {
			char op = ctx.getChild(1).getText().charAt(0);
			Operation operation;

			// Create the right operation node type
			switch (op) {
				case '*': operation = new MultiplyOperation(); break;
				case '+': operation = new AddOperation(); break;
				default:  operation = new SubtractOperation(); break;
			}

			// Attach operation and push it to stack
			currentContainer.peek().addChild(operation);
			currentContainer.push(operation);
		}
		// Base case: single literal or variable
		else if (ctx.getChildCount() == 1 || (ctx.getChildCount() == 2 && ctx.MIN() != null)) {
			Expression expr = makeLiteralOrVar(ctx);
			if (expr != null) {
				currentContainer.peek().addChild(expr);
			}
		}
	}

	@Override
	public void exitCalc(ICSSParser.CalcContext ctx) {
		// Pop operation only if we pushed one
		if (ctx.getChildCount() == 3 && (ctx.MUL() != null || ctx.PLUS() != null || ctx.MIN() != null)) {
			currentContainer.pop();
		}
	}

	// ---------------- HELPER METHODS ----------------
	// Pushes a node onto the stack and attaches it to the current top node
	private void push(ASTNode node) {
		if (!currentContainer.isEmpty()) currentContainer.peek().addChild(node);
		currentContainer.push(node);
	}


	// Converts parse tree contexts into literal or variable expressions.
	// Handles types like pixels, percentages, colors, scalars, and variable references
	private Expression makeLiteralOrVar(org.antlr.v4.runtime.ParserRuleContext ctx) {
		if (ctx.getText() == null) return null;

		// Handle calculation literals or variables
		if (ctx instanceof ICSSParser.CalcContext) {
			ICSSParser.CalcContext c = (ICSSParser.CalcContext) ctx;
			if (c.PIXELSIZE() != null) return new PixelLiteral(c.PIXELSIZE().getText());
			if (c.PERCENTAGE() != null) return new PercentageLiteral(c.PERCENTAGE().getText());
			if (c.SCALAR() != null) return new ScalarLiteral(c.SCALAR().getText());
			if (c.CAPITAL_IDENT() != null) return new VariableReference(c.CAPITAL_IDENT().getText());
		}
		// Handle variable values
		else if (ctx instanceof ICSSParser.VarValueContext) {
			ICSSParser.VarValueContext v = (ICSSParser.VarValueContext) ctx;
			if (v.PIXELSIZE() != null) return new PixelLiteral(v.PIXELSIZE().getText());
			if (v.PERCENTAGE() != null) return new PercentageLiteral(v.PERCENTAGE().getText());
			if (v.COLOR() != null) return new ColorLiteral(v.COLOR().getText());
			if (v.CAPITAL_IDENT() != null) return new VariableReference(v.CAPITAL_IDENT().getText());
			if (v.TRUE() != null) return new BoolLiteral(v.TRUE().getText());
			if (v.FALSE() != null) return new BoolLiteral(v.FALSE().getText());
			if (v.SCALAR() != null) return new ScalarLiteral(v.SCALAR().getText());
		}
		// Handle property values
		else if (ctx instanceof ICSSParser.PropValueContext) {
			ICSSParser.PropValueContext p = (ICSSParser.PropValueContext) ctx;
			if (p.PIXELSIZE() != null) return new PixelLiteral(p.PIXELSIZE().getText());
			if (p.PERCENTAGE() != null) return new PercentageLiteral(p.PERCENTAGE().getText());
			if (p.COLOR() != null) return new ColorLiteral(p.COLOR().getText());
			if (p.CAPITAL_IDENT() != null) return new VariableReference(p.CAPITAL_IDENT().getText());
		}

		return null;
	}
}
