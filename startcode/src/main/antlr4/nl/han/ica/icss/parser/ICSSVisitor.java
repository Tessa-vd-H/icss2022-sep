// Generated from ICSS.g4 by ANTLR 4.8
package nl.han.ica.icss.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ICSSParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ICSSVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#styleRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleRule(ICSSParser.StyleRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(ICSSParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#propColorValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropColorValue(ICSSParser.PropColorValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#propValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropValue(ICSSParser.PropValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#varAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAssign(ICSSParser.VarAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(ICSSParser.VarContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#varValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarValue(ICSSParser.VarValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(ICSSParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#elseStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStmt(ICSSParser.ElseStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#calc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalc(ICSSParser.CalcContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#calcPixel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalcPixel(ICSSParser.CalcPixelContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#calcPercent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalcPercent(ICSSParser.CalcPercentContext ctx);
}