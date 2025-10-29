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
	private int nestedCalcs = 0;

	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}

	public AST getAST() {
		return ast;
	}

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
	public void enterStylerule(ICSSParser.StyleRuleContext ctx) {
		Stylerule stylerule = new Stylerule();
		currentContainer.peek().addChild(stylerule);
		currentContainer.push(stylerule);
	}

	@Override
	public void exitStylerule(ICSSParser.StyleRuleContext ctx) {
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
			currentContainer.peek().addChild(selector);
			currentContainer.push(selector);
		}
	}

	@Override
	public void exitSelector(ICSSParser.SelectorContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = null;
		if (ctx.COLOR_PROPERTIES() != null) {
			declaration = new Declaration(ctx.COLOR_PROPERTIES().getText());
		} else if (ctx.WIDTH_HEIGHT_PROPERTIES() != null) {
			declaration = new Declaration(ctx.WIDTH_HEIGHT_PROPERTIES().getText());
		}

		if (declaration != null) {
			currentContainer.peek().addChild(declaration);
			currentContainer.push(declaration);
		}
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterPropColorValue(ICSSParser.PropColorValueContext ctx) {
		ASTNode node = null;
		if (ctx.COLOR() != null) {
			node = new ColorLiteral(ctx.getText());
		} else if (ctx.CAPITAL_IDENT() != null) {
			node = new VariableReference(ctx.getText());
		}

		if (node != null) {
			currentContainer.peek().addChild(node);
			currentContainer.push(node);
		}
	}

	@Override
	public void exitPropColorValue(ICSSParser.PropColorValueContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterPropValue(ICSSParser.PropValueContext ctx) {
		ASTNode node = null;
		if (ctx.PIXELSIZE() != null) {
			node = new PixelLiteral(ctx.getText());
		} else if (ctx.PERCENTAGE() != null) {
			node = new PercentageLiteral(ctx.getText());
		} else if (ctx.CAPITAL_IDENT() != null) {
			node = new VariableReference(ctx.getText());
		}

		if (node != null) {
			currentContainer.peek().addChild(node);
			currentContainer.push(node);
		}
	}

	@Override
	public void exitPropValue(ICSSParser.PropValueContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterVarAssignment(ICSSParser.VarAssignContext ctx) {
		VariableAssignment variableAssignment = new VariableAssignment();
		variableAssignment.name = new VariableReference(ctx.CAPITAL_IDENT().getText());

		ICSSParser.VarValueContext valueCtx = ctx.varValue();
		if (valueCtx != null) {
			if (valueCtx.PIXELSIZE() != null) {
				variableAssignment.expression = new PixelLiteral(valueCtx.PIXELSIZE().getText());
			} else if (valueCtx.PERCENTAGE() != null) {
				variableAssignment.expression = new PercentageLiteral(valueCtx.PERCENTAGE().getText());
			} else if (valueCtx.COLOR() != null) {
				variableAssignment.expression = new ColorLiteral(valueCtx.COLOR().getText());
			} else if (valueCtx.CAPITAL_IDENT() != null) {
				variableAssignment.expression = new VariableReference(valueCtx.CAPITAL_IDENT().getText());
			} else if (valueCtx.TRUE() != null) {
				variableAssignment.expression = new BoolLiteral(valueCtx.TRUE().getText());
			} else if (valueCtx.FALSE() != null) {
				variableAssignment.expression = new BoolLiteral(valueCtx.FALSE().getText());
			} else if (valueCtx.SCALAR() != null) {
				variableAssignment.expression = new ScalarLiteral(valueCtx.SCALAR().getText());
			}
		}

		currentContainer.peek().addChild(variableAssignment);
		currentContainer.push(variableAssignment);
	}

	@Override
	public void exitVarAssignment(ICSSParser.VarAssignContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterVarValue(ICSSParser.VarValueContext ctx) {
		VariableReference variableReference = new VariableReference(ctx.getText());
		currentContainer.peek().addChild(variableReference);
		currentContainer.push(variableReference);
	}

	@Override
	public void exitVarValue(ICSSParser.VarValueContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterIfStatement(ICSSParser.IfStmtContext ctx) {
		IfClause ifClause = new IfClause();
		currentContainer.peek().addChild(ifClause);
		currentContainer.push(ifClause);
	}

	@Override
	public void exitIfStatement(ICSSParser.IfStmtContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterElseStatement(ICSSParser.ElseStmtContext ctx) {
		ElseClause elseClause = new ElseClause();
		currentContainer.peek().addChild(elseClause);
		currentContainer.push(elseClause);
	}

	@Override
	public void exitElseStatement(ICSSParser.ElseStmtContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterExpression(ICSSParser.ExpressionContext ctx) {
		ASTNode node = null;
		if (ctx.CAPITAL_IDENT() != null) {
			node = new VariableReference(ctx.CAPITAL_IDENT().getText());
		} else if (ctx.TRUE() != null) {
			node = new BoolLiteral(ctx.TRUE().getText());
		} else if (ctx.FALSE() != null) {
			node = new BoolLiteral(ctx.FALSE().getText());
		}

		if (node != null) {
			currentContainer.peek().addChild(node);
			currentContainer.push(node);
		}
	}

	@Override
	public void exitExpression(ICSSParser.ExpressionContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterScalar(ICSSParser.ScalarContext ctx) {
		ScalarLiteral scalarLiteral = new ScalarLiteral(ctx.getText());
		currentContainer.peek().addChild(scalarLiteral);
	}

	@Override
	public void enterCalcPixel(ICSSParser.CalcPixelContext ctx) {
		ASTNode exp = null;

		if (ctx.getChildCount() == 1) { // Leaf node
			if (ctx.PIXELSIZE() != null) {
				exp = new PixelLiteral(ctx.getText());
			} else if (ctx.CAPITAL_IDENT() != null) {
				exp = new VariableReference(ctx.getText());
			}

			if (exp != null) {
				currentContainer.peek().addChild(exp);
			}
		} else if (ctx.getChildCount() == 3) { // Binary operation
			char op = ctx.getChild(1).getText().charAt(0);
			switch (op) {
				case '*': exp = new MultiplyOperation(); break;
				case '+': exp = new AddOperation(); break;
				case '-': exp = new SubtractOperation(); break;
			}

			if (exp != null) {
				currentContainer.peek().addChild(exp);
				currentContainer.push(exp);
				nestedCalcs++;
			}
		}
	}

	@Override
	public void exitCalcPixel(ICSSParser.CalcPixelContext ctx) {
		if (ctx.getChildCount() == 3 && nestedCalcs > 0) {
			currentContainer.pop();
			nestedCalcs--;
		}
	}
}
