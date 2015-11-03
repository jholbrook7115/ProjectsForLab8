// Generated from edu/temple/cis/c4324/micro/Micro.g4 by ANTLR 4.5.1

package edu.temple.cis.c4324.micro;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MicroParser}.
 */
public interface MicroListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MicroParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MicroParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MicroParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(MicroParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(MicroParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#procedureDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterProcedureDeclaration(MicroParser.ProcedureDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#procedureDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitProcedureDeclaration(MicroParser.ProcedureDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(MicroParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(MicroParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#recordDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterRecordDeclaration(MicroParser.RecordDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#recordDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitRecordDeclaration(MicroParser.RecordDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(MicroParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(MicroParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterArrayParamDecl(MicroParser.ArrayParamDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitArrayParamDecl(MicroParser.ArrayParamDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSimpleParamDecl(MicroParser.SimpleParamDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSimpleParamDecl(MicroParser.SimpleParamDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code recordParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterRecordParamDecl(MicroParser.RecordParamDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code recordParamDecl}
	 * labeled alternative in {@link MicroParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitRecordParamDecl(MicroParser.RecordParamDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterArrayVariableDecl(MicroParser.ArrayVariableDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitArrayVariableDecl(MicroParser.ArrayVariableDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSimpleVariableDecl(MicroParser.SimpleVariableDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSimpleVariableDecl(MicroParser.SimpleVariableDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code recordVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterRecordVariableDecl(MicroParser.RecordVariableDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code recordVariableDecl}
	 * labeled alternative in {@link MicroParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitRecordVariableDecl(MicroParser.RecordVariableDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primType}
	 * labeled alternative in {@link MicroParser#type}.
	 * @param ctx the parse tree
	 */
	void enterPrimType(MicroParser.PrimTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primType}
	 * labeled alternative in {@link MicroParser#type}.
	 * @param ctx the parse tree
	 */
	void exitPrimType(MicroParser.PrimTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code recType}
	 * labeled alternative in {@link MicroParser#type}.
	 * @param ctx the parse tree
	 */
	void enterRecType(MicroParser.RecTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code recType}
	 * labeled alternative in {@link MicroParser#type}.
	 * @param ctx the parse tree
	 */
	void exitRecType(MicroParser.RecTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(MicroParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(MicroParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#recordType}.
	 * @param ctx the parse tree
	 */
	void enterRecordType(MicroParser.RecordTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#recordType}.
	 * @param ctx the parse tree
	 */
	void exitRecordType(MicroParser.RecordTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void enterStatement_list(MicroParser.Statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void exitStatement_list(MicroParser.Statement_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code read}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRead(MicroParser.ReadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code read}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRead(MicroParser.ReadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code write}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWrite(MicroParser.WriteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code write}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWrite(MicroParser.WriteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign(MicroParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign(MicroParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code call}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCall(MicroParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code call}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCall(MicroParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(MicroParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(MicroParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stmt(MicroParser.While_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stmt(MicroParser.While_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dountil_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDountil_stmt(MicroParser.Dountil_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dountil_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDountil_stmt(MicroParser.Dountil_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code return_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(MicroParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code return_stmt}
	 * labeled alternative in {@link MicroParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(MicroParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#read_statement}.
	 * @param ctx the parse tree
	 */
	void enterRead_statement(MicroParser.Read_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#read_statement}.
	 * @param ctx the parse tree
	 */
	void exitRead_statement(MicroParser.Read_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#lvalue_list}.
	 * @param ctx the parse tree
	 */
	void enterLvalue_list(MicroParser.Lvalue_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#lvalue_list}.
	 * @param ctx the parse tree
	 */
	void exitLvalue_list(MicroParser.Lvalue_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#write_statement}.
	 * @param ctx the parse tree
	 */
	void enterWrite_statement(MicroParser.Write_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#write_statement}.
	 * @param ctx the parse tree
	 */
	void exitWrite_statement(MicroParser.Write_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void enterExpr_list(MicroParser.Expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void exitExpr_list(MicroParser.Expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_statement(MicroParser.Assignment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_statement(MicroParser.Assignment_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterIdLvalue(MicroParser.IdLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitIdLvalue(MicroParser.IdLvalueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterArrayLvalue(MicroParser.ArrayLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitArrayLvalue(MicroParser.ArrayLvalueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code recordLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterRecordLvalue(MicroParser.RecordLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code recordLvalue}
	 * labeled alternative in {@link MicroParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitRecordLvalue(MicroParser.RecordLvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#call_statement}.
	 * @param ctx the parse tree
	 */
	void enterCall_statement(MicroParser.Call_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#call_statement}.
	 * @param ctx the parse tree
	 */
	void exitCall_statement(MicroParser.Call_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_statement(MicroParser.Return_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_statement(MicroParser.Return_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_statement(MicroParser.If_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_statement(MicroParser.If_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#elsif_part}.
	 * @param ctx the parse tree
	 */
	void enterElsif_part(MicroParser.Elsif_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#elsif_part}.
	 * @param ctx the parse tree
	 */
	void exitElsif_part(MicroParser.Elsif_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#else_part}.
	 * @param ctx the parse tree
	 */
	void enterElse_part(MicroParser.Else_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#else_part}.
	 * @param ctx the parse tree
	 */
	void exitElse_part(MicroParser.Else_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile_statement(MicroParser.While_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile_statement(MicroParser.While_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MicroParser#do_until_statement}.
	 * @param ctx the parse tree
	 */
	void enterDo_until_statement(MicroParser.Do_until_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MicroParser#do_until_statement}.
	 * @param ctx the parse tree
	 */
	void exitDo_until_statement(MicroParser.Do_until_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParens(MicroParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParens(MicroParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bool}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBool(MicroParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bool}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBool(MicroParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code powop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPowop(MicroParser.PowopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code powop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPowop(MicroParser.PowopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryop(MicroParser.UnaryopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryop(MicroParser.UnaryopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code float}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFloat(MicroParser.FloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code float}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFloat(MicroParser.FloatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code int}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInt(MicroParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code int}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInt(MicroParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArithop(MicroParser.ArithopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArithop(MicroParser.ArithopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompop(MicroParser.CompopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompop(MicroParser.CompopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fcnCall}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFcnCall(MicroParser.FcnCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fcnCall}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFcnCall(MicroParser.FcnCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code char}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterChar(MicroParser.CharContext ctx);
	/**
	 * Exit a parse tree produced by the {@code char}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitChar(MicroParser.CharContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalop(MicroParser.LogicalopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalop}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalop(MicroParser.LogicalopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterId(MicroParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitId(MicroParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayAccess}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess(MicroParser.ArrayAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayAccess}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess(MicroParser.ArrayAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code recordAccess}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRecordAccess(MicroParser.RecordAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code recordAccess}
	 * labeled alternative in {@link MicroParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRecordAccess(MicroParser.RecordAccessContext ctx);
}