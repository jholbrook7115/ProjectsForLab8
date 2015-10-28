// Generated from edu\temple\cis\c4324\micro\Micro.g4 by ANTLR 4.5.1

package edu.temple.cis.c4324.micro;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MicroParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MicroVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MicroParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MicroParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(MicroParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#procedureDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureDeclaration(MicroParser.ProcedureDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(MicroParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#recordDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordDeclaration(MicroParser.RecordDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(MicroParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayParamDecl(MicroParser.ArrayParamDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleParamDecl(MicroParser.SimpleParamDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code recordParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordParamDecl(MicroParser.RecordParamDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVariableDecl(MicroParser.ArrayVariableDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleVariableDecl(MicroParser.SimpleVariableDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code recordVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordVariableDecl(MicroParser.RecordVariableDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primType}
	 * labeled alternative in {@link MicroParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimType(MicroParser.PrimTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code recType}
	 * labeled alternative in {@link MicroParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecType(MicroParser.RecTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(MicroParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#recordType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordType(MicroParser.RecordTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#statement_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_list(MicroParser.Statement_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code read}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRead(MicroParser.ReadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code write}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWrite(MicroParser.WriteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(MicroParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code call}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(MicroParser.CallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code if_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stmt(MicroParser.If_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_stmt(MicroParser.While_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dountil_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDountil_stmt(MicroParser.Dountil_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code return_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(MicroParser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#read_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRead_statement(MicroParser.Read_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#lvalue_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLvalue_list(MicroParser.Lvalue_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#write_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWrite_statement(MicroParser.Write_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_list(MicroParser.Expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#assignment_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_statement(MicroParser.Assignment_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdLvalue(MicroParser.IdLvalueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLvalue(MicroParser.ArrayLvalueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code recordLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordLvalue(MicroParser.RecordLvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#call_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_statement(MicroParser.Call_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#return_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_statement(MicroParser.Return_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#if_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_statement(MicroParser.If_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#elsif_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElsif_part(MicroParser.Elsif_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#else_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_part(MicroParser.Else_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#while_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_statement(MicroParser.While_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MicroParser#do_until_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDo_until_statement(MicroParser.Do_until_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(MicroParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bool}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(MicroParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code powop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowop(MicroParser.PowopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryop(MicroParser.UnaryopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code float}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat(MicroParser.FloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code int}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt(MicroParser.IntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithop(MicroParser.ArithopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code compop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompop(MicroParser.CompopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fcnCall}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFcnCall(MicroParser.FcnCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code char}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChar(MicroParser.CharContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalop(MicroParser.LogicalopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(MicroParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayAccess}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayAccess(MicroParser.ArrayAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code recordAccess}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordAccess(MicroParser.RecordAccessContext ctx);
}