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
		if (ctx.PROPERTIES() != null) {
			declaration = new Declaration(ctx.PROPERTIES().getText());
		} else if (ctx.PROPERTIES() != null) {
			declaration = new Declaration(ctx.PROPERTIES().getText());
		}

		currentContainer.peek().addChild(declaration);
		currentContainer.push(declaration);
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterPropValue(ICSSParser.PropValueContext ctx) {
		if (ctx.PIXELSIZE() != null) {
			Literal literal = new PixelLiteral(ctx.getText());
			currentContainer.peek().addChild(literal);
			currentContainer.push(literal);
		} else if (ctx.PERCENTAGE() != null) {
			Literal literal = new PercentageLiteral(ctx.getText());
			currentContainer.peek().addChild(literal);
			currentContainer.push(literal);
		} else if (ctx.CAPITAL_IDENT() != null) {
			VariableReference vr = new VariableReference(ctx.getText());
			currentContainer.peek().addChild(vr);
			currentContainer.push(vr);
		} else if (ctx.COLOR() != null) {
			Literal literal = new ColorLiteral(ctx.getText());
			currentContainer.peek().addChild(literal);
			currentContainer.push(literal);
		} else if (ctx.calc() != null) {
			currentContainer.peek().addChild(currentContainer.peek());
			currentContainer.push(currentContainer.peek());
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

		currentContainer.peek().addChild(variableAssignment);
		currentContainer.push(variableAssignment);
	}

	@Override
	public void exitVarAssignment(ICSSParser.VarAssignContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterVarValue(ICSSParser.VarValueContext ctx) {
		ASTNode node = null;

		if (ctx.PIXELSIZE() != null) {
			node = new PixelLiteral(ctx.PIXELSIZE().getText());
		} else if (ctx.PERCENTAGE() != null) {
			node = new PercentageLiteral(ctx.PERCENTAGE().getText());
		} else if (ctx.COLOR() != null) {
			node = new ColorLiteral(ctx.COLOR().getText());
		} else if (ctx.CAPITAL_IDENT() != null) {
			node = new VariableReference(ctx.CAPITAL_IDENT().getText());
		} else if (ctx.TRUE() != null) {
			node = new BoolLiteral(ctx.TRUE().getText());
		} else if (ctx.FALSE() != null) {
			node = new BoolLiteral(ctx.FALSE().getText());
		} else if (ctx.SCALAR() != null) {
			node = new ScalarLiteral(ctx.SCALAR().getText());
		} else if (ctx.calc() != null) {
			currentContainer.push(currentContainer.peek());
		}

		if (node != null) {
			currentContainer.peek().addChild(node);
			currentContainer.push(node);
		}
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
	public void enterCalc(ICSSParser.CalcContext ctx) {
		ASTNode exp = null;

		// Handle binary operation: e.g., 10px + 5px
		if (ctx.getChildCount() == 3) {
			char op = ctx.getChild(1).getText().charAt(0);
			switch (op) {
				case '*':
					exp = new MultiplyOperation();
					break;
				case '+':
					exp = new AddOperation();
					break;
				default:
					exp = new SubtractOperation();
					break;
			}
			currentContainer.peek().addChild(exp);
			currentContainer.push(exp);
		}

		// Handle leaf node: e.g., 10px or VAR
		if (ctx.getChildCount() == 1) {
			if (ctx.PIXELSIZE() != null) {
				exp = new PixelLiteral(ctx.getChild(0).getText());
			} else if (ctx.PERCENTAGE() != null) {
				exp = new PercentageLiteral(ctx.getChild(0).getText());
			} else if (ctx.CAPITAL_IDENT() != null) {
				exp = new VariableReference(ctx.getChild(0).getText());
			} else if (ctx.SCALAR() != null) {
				exp = new ScalarLiteral(ctx.getChild(0).getText());
			}

			if (exp != null) {
				currentContainer.peek().addChild(exp);
				currentContainer.push(exp);
			}
		}
	}

	@Override
	public void exitCalc(ICSSParser.CalcContext ctx) {
		currentContainer.pop();
	}
}