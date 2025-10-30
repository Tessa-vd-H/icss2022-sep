package nl.han.ica.icss.parser;

import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;
import nl.han.ica.icss.ast.selectors.*;

public class ASTListener extends ICSSBaseListener {

	private AST ast;
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}

	public AST getAST() {
		return ast;
	}

	// ---------------- Stylesheet ----------------
	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		Stylesheet stylesheet = new Stylesheet();
		ast.root = stylesheet;
		currentContainer.push(stylesheet);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
		Stylerule stylerule = new Stylerule();

		if (!currentContainer.isEmpty()) {
			currentContainer.peek().addChild(stylerule);
		} else {
			// If stylesheet hasn't been pushed (defensive fix)
			Stylesheet stylesheet = new Stylesheet();
			ast.root = stylesheet;
			currentContainer.push(stylesheet);
			currentContainer.peek().addChild(stylerule);
		}

		currentContainer.push(stylerule);
	}

	@Override
	public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterSelector(ICSSParser.SelectorContext ctx) {
		Selector selector = null;
		if (ctx.CLASS_IDENT() != null) {
			selector = new ClassSelector(ctx.CLASS_IDENT().getText());
		} else if (ctx.ID_IDENT() != null) {
			selector = new IdSelector(ctx.ID_IDENT().getText());
		} else if (ctx.LOWER_IDENT() != null) {
			selector = new TagSelector(ctx.LOWER_IDENT().getText());
		}
		if (selector != null) {
			// Attach selector to the current stylerule
			currentContainer.peek().addChild(selector);
		}
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = new Declaration(ctx.PROPERTIES().getText());
		// Attach declaration to current stylerule or if/else
		if (!currentContainer.isEmpty()) {
			currentContainer.peek().addChild(declaration);
		}
		currentContainer.push(declaration);
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		currentContainer.pop();
	}

	// ---------------- Variable Assignments ----------------
	@Override
	public void enterVarAssign(ICSSParser.VarAssignContext ctx) {
		VariableAssignment varAssignment = new VariableAssignment();

		// Add variable reference (the variable name being assigned)
		if (ctx.CAPITAL_IDENT() != null) {
			VariableReference varRef = new VariableReference(ctx.CAPITAL_IDENT().getText());
			varAssignment.addChild(varRef);
		}

		// Attach to stylesheet
		if (!currentContainer.isEmpty()) {
			currentContainer.peek().addChild(varAssignment);
		}

		currentContainer.push(varAssignment);
	}

	@Override
	public void exitVarAssign(ICSSParser.VarAssignContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterVarValue(ICSSParser.VarValueContext ctx) {
		Expression expr = makeLiteralOrVar(ctx);
		if (expr != null) {
			currentContainer.peek().addChild(expr);
		}
	}

	@Override
	public void enterPropValue(ICSSParser.PropValueContext ctx) {
		Expression expr = makeLiteralOrVar(ctx);
		if (expr != null) {
			currentContainer.peek().addChild(expr);
		}
	}

	@Override
	public void enterExpression(ICSSParser.ExpressionContext ctx) {
		Expression expr = null;

		if (ctx.CAPITAL_IDENT() != null) {
			expr = new VariableReference(ctx.CAPITAL_IDENT().getText());
		} else if (ctx.TRUE() != null) {
			expr = new BoolLiteral(ctx.TRUE().getText());
		} else if (ctx.FALSE() != null) {
			expr = new BoolLiteral(ctx.FALSE().getText());
		}

		if (expr != null && !currentContainer.isEmpty()) {
			currentContainer.peek().addChild(expr);
		}
	}

	// ---------------- If / Else ----------------
	@Override
	public void enterIfStmt(ICSSParser.IfStmtContext ctx) {
		IfClause ifClause = new IfClause();
		push(ifClause);
	}

	@Override
	public void exitIfStmt(ICSSParser.IfStmtContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterElseStmt(ICSSParser.ElseStmtContext ctx) {
		ElseClause elseClause = new ElseClause();
		push(elseClause);
	}

	@Override
	public void exitElseStmt(ICSSParser.ElseStmtContext ctx) {
		currentContainer.pop();
	}

	// ---------------- Calculations ----------------
	@Override
	public void enterCalc(ICSSParser.CalcContext ctx) {
		// Only create operation for binary operations (3 children: left op right)
		if (ctx.getChildCount() == 3 && (ctx.MUL() != null || ctx.PLUS() != null || ctx.MIN() != null)) {
			char op = ctx.getChild(1).getText().charAt(0);
			Operation operation;
			switch (op) {
				case '*': operation = new MultiplyOperation(); break;
				case '+': operation = new AddOperation(); break;
				default:  operation = new SubtractOperation(); break;
			}
			currentContainer.peek().addChild(operation);
			currentContainer.push(operation);
		}
		// For leaf nodes (single values), handle them directly
		else if (ctx.getChildCount() == 1 || (ctx.getChildCount() == 2 && ctx.MIN() != null)) {
			// This is a base case: variable, literal, or -literal
			Expression expr = makeLiteralOrVar(ctx);
			if (expr != null) {
				currentContainer.peek().addChild(expr);
			}
		}
	}

	@Override
	public void exitCalc(ICSSParser.CalcContext ctx) {
		// Only pop if we pushed an operation
		if (ctx.getChildCount() == 3 && (ctx.MUL() != null || ctx.PLUS() != null || ctx.MIN() != null)) {
			currentContainer.pop();
		}
	}
	// ---------------- Helper Methods ----------------
	private void push(ASTNode node) {
		if (!currentContainer.isEmpty()) currentContainer.peek().addChild(node);
		currentContainer.push(node);
	}

	private Expression makeLiteralOrVar(org.antlr.v4.runtime.ParserRuleContext ctx) {
		if (ctx.getText() == null) return null;

		if (ctx instanceof ICSSParser.CalcContext) {
			ICSSParser.CalcContext c = (ICSSParser.CalcContext) ctx;
			if (c.PIXELSIZE() != null) return new PixelLiteral(c.PIXELSIZE().getText());
			if (c.PERCENTAGE() != null) return new PercentageLiteral(c.PERCENTAGE().getText());
			if (c.SCALAR() != null) return new ScalarLiteral(c.SCALAR().getText());
			if (c.CAPITAL_IDENT() != null) return new VariableReference(c.CAPITAL_IDENT().getText());
		} else if (ctx instanceof ICSSParser.VarValueContext) {
			ICSSParser.VarValueContext v = (ICSSParser.VarValueContext) ctx;
			if (v.PIXELSIZE() != null) return new PixelLiteral(v.PIXELSIZE().getText());
			if (v.PERCENTAGE() != null) return new PercentageLiteral(v.PERCENTAGE().getText());
			if (v.COLOR() != null) return new ColorLiteral(v.COLOR().getText());
			if (v.CAPITAL_IDENT() != null) return new VariableReference(v.CAPITAL_IDENT().getText());
			if (v.TRUE() != null) return new BoolLiteral(v.TRUE().getText());
			if (v.FALSE() != null) return new BoolLiteral(v.FALSE().getText());
			if (v.SCALAR() != null) return new ScalarLiteral(v.SCALAR().getText());
		} else if (ctx instanceof ICSSParser.PropValueContext) {
			ICSSParser.PropValueContext p = (ICSSParser.PropValueContext) ctx;
			if (p.PIXELSIZE() != null) return new PixelLiteral(p.PIXELSIZE().getText());
			if (p.PERCENTAGE() != null) return new PercentageLiteral(p.PERCENTAGE().getText());
			if (p.COLOR() != null) return new ColorLiteral(p.COLOR().getText());
			if (p.CAPITAL_IDENT() != null) return new VariableReference(p.CAPITAL_IDENT().getText());
		}
		return null;
	}
}
