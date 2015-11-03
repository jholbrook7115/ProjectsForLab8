package edu.temple.cis.c4324.microcompilerv1;

import edu.temple.cis.c4324.micro.MicroBaseVisitor;
import edu.temple.cis.c4324.micro.MicroParser;
import edu.temple.cis.c4324.micro.MicroParser.PrimitiveTypeContext;
import edu.temple.cis.c4324.micro.MicroParser.ProgramContext;
import edu.temple.cis.c4324.micro.MicroParser.RecordDeclarationContext;
import edu.temple.cis.c4324.micro.MicroParser.RecordTypeContext;
import edu.temple.cis.c4324.micro.MicroParser.RecordParamDeclContext;
import static edu.temple.cis.c4324.microcompilerv1.MicroCompilerV1.error;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTreeProperty;


/**
 * The definition phase visits the parse tree and defines all of the
 * identifiers.
 *
 * @author Paul
 */
public class DefinitionVisitor extends MicroBaseVisitor<Type> {

    private final ParseTreeProperty<Scope> scopeMap;
    private Scope globalScope;
    private Scope currentScope;
    private String programName;

    public DefinitionVisitor() {
        scopeMap = new ParseTreeProperty<>();
    }

    public ParseTreeProperty<Scope> getScopeMap() {
        return scopeMap;
    }

    @Override
    public Type visitProgram(ProgramContext ctx) {
        programName = ctx.ID().getText();
        globalScope = new Scope(Scope.Kind.GLOBAL, null, null);
        globalScope.define(programName, null);
        globalScope.setOwner(globalScope.resolve(programName));
        currentScope = globalScope;
        scopeMap.put(ctx, currentScope);
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitProcedureDeclaration(MicroParser.ProcedureDeclarationContext ctx) {
        String procedureName = ctx.ID().getText();
        currentScope.define(procedureName, null);
        currentScope = new Scope(Scope.Kind.LOCAL, currentScope, currentScope.resolve(procedureName));
        scopeMap.put(ctx, currentScope);
        List<String> parameterNameList = new ArrayList<>();
        List<String> parameterTypeList = new ArrayList<>();
        ctx.parameterList().parameterDeclaration().forEach(pd -> {
            visit(pd);
            String parameterName;
            if (pd instanceof MicroParser.SimpleParamDeclContext) {
                parameterName = ((MicroParser.SimpleParamDeclContext) pd).ID().getText();
            } else if (pd instanceof MicroParser.ArrayParamDeclContext){
                parameterName = ((MicroParser.ArrayParamDeclContext) pd).ID().getText();
            } else{
                parameterName = ((MicroParser.RecordParamDeclContext) pd).ID().getText();
            }
            parameterNameList.add(parameterName);
            Identifier parameterId = currentScope.resolve(parameterName);
            String parameterType = parameterId.getType().getJavaTypeName();
            parameterTypeList.add(parameterType);
        });
        ProcedureOrFunction procedureType = new ProcedureOrFunction(
                programName + "." + procedureName,
                PrimitiveType.VOID,
                parameterNameList,
                parameterTypeList);
        ctx.variableDeclaration().forEach(vd -> {
            visit(vd);
        });
        currentScope = currentScope.getParent();
        currentScope.resolve(procedureName).setType(procedureType);
        return null;
    }

    @Override
    public Type visitFunctionDeclaration(MicroParser.FunctionDeclarationContext ctx) {
        String functionName = ctx.ID().getText();
        currentScope.define(functionName, null);
        Type returnType = visit(ctx.type());
        currentScope = new Scope(Scope.Kind.LOCAL, currentScope, currentScope.resolve(functionName));
        scopeMap.put(ctx, currentScope);
        List<String> parameterNameList = new ArrayList<>();
        List<String> parameterTypeList = new ArrayList<>();
        ctx.parameterList().parameterDeclaration().forEach(pd -> {
            visit(pd);
            String parameterName;
            if (pd instanceof MicroParser.SimpleParamDeclContext) {
                parameterName = ((MicroParser.SimpleParamDeclContext) pd).ID().getText();
            } else if (pd instanceof MicroParser.ArrayParamDeclContext){
                parameterName = ((MicroParser.ArrayParamDeclContext) pd).ID().getText();
            } else {
                parameterName = ((MicroParser.RecordParamDeclContext) pd).ID().getText();
            }
            parameterNameList.add(parameterName);
            Identifier parameterId = currentScope.resolve(parameterName);
            String parameterType = parameterId.getType().getJavaTypeName();
            parameterTypeList.add(parameterType);
        });
        ProcedureOrFunction procedureType = new ProcedureOrFunction(
                programName + "." + functionName,
                returnType,
                parameterNameList,
                parameterTypeList);
        ctx.variableDeclaration().forEach(vd -> {
            visit(vd);
        });
        currentScope = currentScope.getParent();
        currentScope.resolve(functionName).setType(procedureType);
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     * @param ctx The RecordDeclarationContext to be visited
     * @return The type of this node.
     */
    @Override
    public Type visitRecordDeclaration(RecordDeclarationContext ctx) {
        String recordName = ctx.ID().getText();
        Identifier parent = currentScope.getOwner();
        RecordType recordType = new RecordType(recordName, parent.getName());
        currentScope.define(recordName, recordType);
        currentScope = new Scope(Scope.Kind.RECORD, currentScope, currentScope.resolve(recordName));
        scopeMap.put(ctx, currentScope);
        recordType.setContainedScope(currentScope);
        ctx.variableDeclaration().forEach(vd -> {visit(vd);});
        currentScope = currentScope.getParent();
        return recordType;
    }

    @Override
    public Type visitPrimitiveType(PrimitiveTypeContext ctx) {
        String text = ctx.getText();
        switch (text) {
            case "int":
                return PrimitiveType.INT;
            case "real":
                return PrimitiveType.REAL;
            case "char":
                return PrimitiveType.CHAR;
            case "bool":
                return PrimitiveType.BOOL;
        }
        return null;
    }
    
    
    
    @Override
    public Type visitRecordType(RecordTypeContext ctx) {
        String recordTypeName = ctx.ID().getText();
        Identifier recordTypeId = currentScope.resolve(recordTypeName);
        return recordTypeId.getType();
    }


    @Override
    public Type visitSimpleVariableDecl(MicroParser.SimpleVariableDeclContext ctx) {
        String idName = ctx.ID().getText();
        Type type = visit(ctx.primitiveType());
        if (!currentScope.define(idName, type)) {
            MicroCompilerV1.error(ctx, idName + " is already defined");
        }
        return type;
    }
    
    @Override 
    public Type visitRecordVariableDecl(MicroParser.RecordVariableDeclContext ctx) { 
        String idName = ctx.ID().getText();
        Type type = visit(ctx.recordType());
        if (!currentScope.define(idName, type)) {
            error(ctx, idName + " is already defined");
        }
        return type;
    }
    

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public Type visitArrayVariableDecl(MicroParser.ArrayVariableDeclContext ctx) {
        String arrayVariableName = ctx.ID().getText();
        Type componentType = visit(ctx.type());
        int length = Integer.parseInt(ctx.INT().getText());
        ArrayType arrayType = new ArrayType(componentType, length);
        Identifier id = new Identifier(arrayVariableName, arrayType, currentScope);
        currentScope.define(arrayVariableName, arrayType);
        return arrayType;
    }

    @Override
    public Type visitSimpleParamDecl(MicroParser.SimpleParamDeclContext ctx) {
        String idName = ctx.ID().getText();
        Type type = visit(ctx.primitiveType());
        if (!currentScope.define(idName, type)) {
            MicroCompilerV1.error(ctx, idName + " is already defined");
        }
        return type;
    }
    @Override
    public Type visitRecordParamDecl(MicroParser.RecordParamDeclContext ctx){
        String idName = ctx.ID().getText();
        String recordParentName = ctx.ID().getParent().getText();
        RecordType recordType = new RecordType(idName, recordParentName);
        if (!currentScope.define(idName, recordType)) {
            MicroCompilerV1.error(ctx, idName + " is already defined");
        }
        return recordType;
    }
    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     * @param ctx The current parse tree node
     * @return The type of this node.
     */
    @Override
    public Type visitArrayParamDecl(MicroParser.ArrayParamDeclContext ctx) {
        String idName = ctx.ID().getText();
        PrimitiveType componentType = (PrimitiveType) visit(ctx.type());
        ArrayType arrayType = new ArrayType(componentType, 0);
        if (!currentScope.define(idName, arrayType)) {
            MicroCompilerV1.error(ctx, idName + " is already defined");
        }
        return arrayType;
    }

}
