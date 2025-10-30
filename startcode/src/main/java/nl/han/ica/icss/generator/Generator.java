package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.*;

public class Generator {

	public String generate(AST ast) {
		String output = "";

		for (ASTNode node : ast.root.getChildren()) {
			if (node instanceof Stylerule) {
				output += generateStylerule((Stylerule) node);
			}
		}

		return output;
	}

	private String generateStylerule(Stylerule stylerule) {
		String result = getTagString(stylerule) + "{\n";

		for (ASTNode child : stylerule.getChildren()) {
			if (child instanceof Declaration) {
				Declaration declaration = (Declaration) child;
				result += "  " + declaration.property.name + ": " + getExpressionValue(declaration.expression) + ";\n";
			}
		}

		result += "}\n";
		return result;
	}

	private String getExpressionValue(Expression expression) {
		if (expression instanceof ColorLiteral) {
			return ((ColorLiteral) expression).value;
		} else if (expression instanceof PercentageLiteral) {
			return ((PercentageLiteral) expression).value + "%";
		} else if (expression instanceof PixelLiteral) {
			return ((PixelLiteral) expression).value + "px";
		} else if (expression instanceof ScalarLiteral) {
			return ((ScalarLiteral) expression).value + "";
		}
		return "";
	}

	private String getTagString(Stylerule node) {
		String tagString = "";

		for (ASTNode selector : node.selectors) {
			if (selector instanceof TagSelector) {
				tagString += ((TagSelector) selector).tag + " ";
			} else if (selector instanceof ClassSelector) {
				tagString += ((ClassSelector) selector).cls + " ";
			} else if (selector instanceof IdSelector) {
				tagString += ((IdSelector) selector).id + " ";
			}
		}

		return tagString;
	}
}
