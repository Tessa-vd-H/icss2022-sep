package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.*;

public class Generator {

	// Entry point for generating CSS output from the AST
	public String generate(AST ast) {
		String output = "";

		// Loop through all top-level nodes in the AST
		for (ASTNode node : ast.root.getChildren()) {
			// Only generate output for style rules
			if (node instanceof Stylerule) {
				output += generateStylerule((Stylerule) node);
			}
		}

		return output;
	}

	// Converts a Stylerule node into a CSS block
	private String generateStylerule(Stylerule stylerule) {
		// Start with selector(s) and opening brace
		String result = getTagString(stylerule) + "{\n";

		// Add each declaration
		for (ASTNode child : stylerule.getChildren()) {
			if (child instanceof Declaration) {
				Declaration declaration = (Declaration) child;
				result += "  " + declaration.property.name + ": "
						+ getExpressionValue(declaration.expression) + ";\n";
			}
		}

		// Close the CSS block
		result += "}\n";
		return result;
	}

	// Converts an expression node to its corresponding CSS string value
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

	// Builds the selector string
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
