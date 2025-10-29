// Generated from ICSS.g4 by ANTLR 4.8
package nl.han.ica.icss.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ICSSParser}.
 */
public interface ICSSListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void enterStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void exitStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#styleRule}.
	 * @param ctx the parse tree
	 */
	void enterStyleRule(ICSSParser.StyleRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#styleRule}.
	 * @param ctx the parse tree
	 */
	void exitStyleRule(ICSSParser.StyleRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(ICSSParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(ICSSParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#propColorValue}.
	 * @param ctx the parse tree
	 */
	void enterPropColorValue(ICSSParser.PropColorValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#propColorValue}.
	 * @param ctx the parse tree
	 */
	void exitPropColorValue(ICSSParser.PropColorValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#propValue}.
	 * @param ctx the parse tree
	 */
	void enterPropValue(ICSSParser.PropValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#propValue}.
	 * @param ctx the parse tree
	 */
	void exitPropValue(ICSSParser.PropValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#varAssign}.
	 * @param ctx the parse tree
	 */
	void enterVarAssign(ICSSParser.VarAssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#varAssign}.
	 * @param ctx the parse tree
	 */
	void exitVarAssign(ICSSParser.VarAssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(ICSSParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(ICSSParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#varValue}.
	 * @param ctx the parse tree
	 */
	void enterVarValue(ICSSParser.VarValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#varValue}.
	 * @param ctx the parse tree
	 */
	void exitVarValue(ICSSParser.VarValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(ICSSParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(ICSSParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#elseStmt}.
	 * @param ctx the parse tree
	 */
	void enterElseStmt(ICSSParser.ElseStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#elseStmt}.
	 * @param ctx the parse tree
	 */
	void exitElseStmt(ICSSParser.ElseStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#calc}.
	 * @param ctx the parse tree
	 */
	void enterCalc(ICSSParser.CalcContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#calc}.
	 * @param ctx the parse tree
	 */
	void exitCalc(ICSSParser.CalcContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#calcPixel}.
	 * @param ctx the parse tree
	 */
	void enterCalcPixel(ICSSParser.CalcPixelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#calcPixel}.
	 * @param ctx the parse tree
	 */
	void exitCalcPixel(ICSSParser.CalcPixelContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#calcPercent}.
	 * @param ctx the parse tree
	 */
	void enterCalcPercent(ICSSParser.CalcPercentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#calcPercent}.
	 * @param ctx the parse tree
	 */
	void exitCalcPercent(ICSSParser.CalcPercentContext ctx);
}