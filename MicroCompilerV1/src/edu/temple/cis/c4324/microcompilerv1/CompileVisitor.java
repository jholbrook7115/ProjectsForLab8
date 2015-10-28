package edu.temple.cis.c4324.microcompilerv1;

import edu.temple.cis.c4324.codegen.CodeGenerator;
import edu.temple.cis.c4324.codegen.InstructionList;
import edu.temple.cis.c4324.micro.MicroBaseVisitor;
import edu.temple.cis.c4324.micro.MicroParser;
import edu.temple.cis.c4324.micro.MicroParser.ArithopContext;
import edu.temple.cis.c4324.micro.MicroParser.ArrayAccessContext;
import edu.temple.cis.c4324.micro.MicroParser.ArrayLvalueContext;
import edu.temple.cis.c4324.micro.MicroParser.Assignment_statementContext;
import edu.temple.cis.c4324.micro.MicroParser.BoolContext;
import edu.temple.cis.c4324.micro.MicroParser.CharContext;
import edu.temple.cis.c4324.micro.MicroParser.BodyContext;
import edu.temple.cis.c4324.micro.MicroParser.Call_statementContext;
import edu.temple.cis.c4324.micro.MicroParser.CompopContext;
import edu.temple.cis.c4324.micro.MicroParser.Do_until_statementContext;
import edu.temple.cis.c4324.micro.MicroParser.Else_partContext;
import edu.temple.cis.c4324.micro.MicroParser.Elsif_partContext;
import edu.temple.cis.c4324.micro.MicroParser.ExprContext;
import edu.temple.cis.c4324.micro.MicroParser.FloatContext;
import edu.temple.cis.c4324.micro.MicroParser.IdContext;
import edu.temple.cis.c4324.micro.MicroParser.IdLvalueContext;
import edu.temple.cis.c4324.micro.MicroParser.If_statementContext;
import edu.temple.cis.c4324.micro.MicroParser.IntContext;
import edu.temple.cis.c4324.micro.MicroParser.LogicalopContext;
import edu.temple.cis.c4324.micro.MicroParser.LvalueContext;
import edu.temple.cis.c4324.micro.MicroParser.ParensContext;
import edu.temple.cis.c4324.micro.MicroParser.PowopContext;
import edu.temple.cis.c4324.micro.MicroParser.ProgramContext;
import edu.temple.cis.c4324.micro.MicroParser.Read_statementContext;
import edu.temple.cis.c4324.micro.MicroParser.RecordAccessContext;
import edu.temple.cis.c4324.micro.MicroParser.RecordDeclarationContext;
import edu.temple.cis.c4324.micro.MicroParser.RecordLvalueContext;
import edu.temple.cis.c4324.micro.MicroParser.RecordVariableDeclContext;
import edu.temple.cis.c4324.micro.MicroParser.Statement_listContext;
import edu.temple.cis.c4324.micro.MicroParser.UnaryopContext;
import edu.temple.cis.c4324.micro.MicroParser.While_statementContext;
import edu.temple.cis.c4324.micro.MicroParser.Write_statementContext;
import static edu.temple.cis.c4324.microcompilerv1.Scope.Kind.GLOBAL;
import static edu.temple.cis.c4324.microcompilerv1.Scope.Kind.LOCAL;
import static edu.temple.cis.c4324.microcompilerv1.Scope.Kind.RECORD;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import static org.apache.bcel.Constants.ACC_PUBLIC;
import static org.apache.bcel.Constants.ACC_STATIC;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

/**
 * Visit the parse tree and generate Java class files
 *
 * @author Paul
 */
public class CompileVisitor extends MicroBaseVisitor<InstructionList> {

    private CodeGenerator cg;
    private final String sourceFileName;
    private boolean inDefined;
    private boolean clinitDefined;
    private MethodGen clinit;
    private final ParseTreeProperty<Scope> scopeMap;
    private final ParseTreeProperty<Type> typeMap;

    private Scope globalScope;
    private Scope currentScope;

    public ParseTreeProperty<Type> getTypeMap() {
        return typeMap;
    }

    /**
     * Construct the CompileVisitor
     *
     * @param scopeMap The scope map created by the Definition Visitor.
     * @param typeMap Tye type map created by the Reference visitor.
     * @param sourceFileName The source file name for error messages
     */
    public CompileVisitor(ParseTreeProperty<Scope> scopeMap,
            ParseTreeProperty<Type> typeMap,
            String sourceFileName) {
        this.sourceFileName = sourceFileName;
        inDefined = false;
        clinitDefined = false;
        this.scopeMap = scopeMap;
        this.typeMap = typeMap;
    }

    /**
     * Visit a Program node. Generates a Java class
     *
     * @param ctx The ProgramContext
     * @return null
     */
    @Override
    public InstructionList visitProgram(ProgramContext ctx) {
        InstructionList il;
        MethodGen mg;
        currentScope = scopeMap.get(ctx);
        globalScope = currentScope;
        cg = new CodeGenerator();
        cg.beginClass(sourceFileName, ctx.ID().getText());
        // Add an empty constructor
        mg = cg.beginMethod(ACC_PUBLIC, "void", "<init>");
        il = cg.newInstructionList();
        il.addInstruction("this");
        il.addInstruction("invokespecial", "java.lang.Object.<init>", "void");
        il.addInstruction("return");
        mg.getInstructionList().append(il);
        cg.endMethod();
        // Visit the record declarations
        ctx.recordDeclaration().forEach(decl -> visitRecordDeclaration(decl));
        // Visit the variable declarations. May create static initializers.
        ctx.variableDeclaration().forEach(decl -> visit(decl));
        // If static initializers were created, add the return instruction
        if (clinitDefined) {
            cg.endMethod();
            clinitDefined = false;
        }
        // Visit function and procedure declarations
        ctx.functionDeclaration().forEach(decl -> visit(decl));
        ctx.procedureDeclaration().forEach(decl -> visit(decl));
        // Generate the main method
        mg = cg.beginMain();
        il = cg.newInstructionList();
        il.append(visit(ctx.body()));
        il.addInstruction("return");
        mg.getInstructionList().append(il);
        cg.endMethod();
        cg.endClass();
        return null;
    }

    /**
     * Visit a ProcedureDeclaration. Generates an equivalent Java method.
     *
     * @param ctx ProcedureDeclaration context
     * @return
     */
    @Override
    public InstructionList visitProcedureDeclaration(MicroParser.ProcedureDeclarationContext ctx) {
        currentScope = scopeMap.get(ctx);
        String procedureName = ctx.ID().getText();
        Identifier procId = currentScope.resolve(procedureName);
        ProcedureOrFunction procType = (ProcedureOrFunction) procId.getType();
        MethodGen mg = cg.beginMethod(ACC_PUBLIC | ACC_STATIC, "void", procedureName, procType.getTypeParameterPairs());
        InstructionList il = cg.newInstructionList();
        ctx.variableDeclaration().forEach(decl -> il.append(visit(decl)));
        il.append(visit(ctx.body()));
        il.addInstruction("return");
        mg.getInstructionList().append(il);
        cg.endMethod();
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * Visit Function Declaration. Generates an equivalent Java Method
     *
     * @param ctx FunctionDeclaration context
     * @return null
     */
    @Override
    public InstructionList visitFunctionDeclaration(MicroParser.FunctionDeclarationContext ctx) {
        currentScope = scopeMap.get(ctx);
        String functionName = ctx.ID().getText();
        Identifier procId = currentScope.resolve(functionName);
        ProcedureOrFunction procType = (ProcedureOrFunction) procId.getType();
        MethodGen mg = cg.beginMethod(ACC_PUBLIC | ACC_STATIC,
                procType.getReturnType().getJavaTypeName(),
                functionName,
                procType.getTypeParameterPairs());
        InstructionList il = cg.newInstructionList();
        ctx.variableDeclaration().forEach(decl -> il.append(visit(decl)));
        il.append(visit(ctx.body()));
        mg.getInstructionList().append(il);
        cg.endMethod();
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * Visit Record Declaration. Generates a Java Class.
     *
     * @param ctx The RecordDeclaration context
     * @return null
     */
    @Override
    public InstructionList visitRecordDeclaration(RecordDeclarationContext ctx) {
        String recordName = ctx.ID().getText();
        currentScope = scopeMap.get(ctx);
        cg = cg.beginInnerClass(ACC_PUBLIC | ACC_STATIC, recordName,
                "java/lang/Object", (String[]) null);
        // Generate a constructor
        MethodGen mg = cg.beginMethod(ACC_PUBLIC, "void", "<init>");
        InstructionList il = cg.newInstructionList();
        il.addInstruction("this");
        il.addInstruction("invokespecial", "java.lang.Object.<init>", "void");
        // Generate field definitions -- may add initialization code to the constructor
        ctx.variableDeclaration().forEach(vd -> {
            il.append(visit(vd));
        });
        // Finish the constructor
        il.addInstruction("return");
        mg.getInstructionList().append(il);
        cg.endMethod();
        cg = cg.endInnerClass();
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * Visit the Body. Generates Java code
     *
     * @param ctx The BodyContext
     * @return null
     */
    @Override
    public InstructionList visitBody(BodyContext ctx) {
        InstructionList il = cg.newInstructionList();
        il.append(visit(ctx.statement_list()));
        return il;
    }

    /**
     * Visit a Simple Variable Declaration. If the variable is global, a static
     * field is created. If the variable is local, a locl variable is created.
     * If the variable is a member of a record, a public field is created.
     *
     * @param ctx SimpleVariableDecl Context
     * @return null
     */
    @Override
    public InstructionList visitSimpleVariableDecl(MicroParser.SimpleVariableDeclContext ctx) {
        InstructionList il = cg.newInstructionList();
        Identifier id = currentScope.resolve(ctx.ID().getText());
        String variableName = id.getName();
        Type variableType = id.getType();
        String variableTypeName = variableType.getJavaTypeName();
        Scope.Kind kind = id.getScope().getKind();
        switch (kind) {
            case GLOBAL:
                cg.addStaticField(variableName, variableTypeName);
                break;
            case LOCAL:
                cg.addLocalVariable(variableName, variableTypeName);
                break;
            case RECORD:
                cg.addPublicField(variableName, variableTypeName);
        }
        return il;
    }

    /**
     * Visit a Record Variable Declaration. If the variable is global, a static
     * field is created and a call to the constructor is added to the static
     * initializers. If the variable is local, local variable is created, and a
     * call to the constructor is added to the method body. If the variable is a
     * member of a record, a call to the constructor is added to the parent
     * record constructor.
     *
     * @param ctx RecordVariableDecl Context
     * @return
     */
    @Override
    public InstructionList visitRecordVariableDecl(RecordVariableDeclContext ctx) {
        InstructionList il = cg.newInstructionList();
        Identifier id = currentScope.resolve(ctx.ID().getText());
        String variableName = id.getName();
        Type variableType = id.getType();
        String variableTypeName = variableType.getJavaTypeName();
        Scope.Kind kind = id.getScope().getKind();
        switch (kind) {
            case GLOBAL:
                cg.addStaticField(variableName, variableTypeName);
                if (!clinitDefined) {
                    clinit = cg.beginMethod(ACC_STATIC, "void", "<clinit>");
                    clinitDefined = true;
                }
                il.addInstruction("new", variableTypeName);
                il.addInstruction("dup");
                il.addInstruction("invokespecial", variableTypeName + ".<init>", "void");
                il.addInstruction("putstatic", cg.getClassName() + "." + variableName, variableTypeName);
                clinit.getInstructionList().append(il);
                break;
            case LOCAL:
                cg.addLocalVariable(variableName, variableTypeName);
                il.addInstruction("new", variableTypeName);
                il.addInstruction("dup");
                il.addInstruction("invokespecial", variableTypeName + ".<init>", "void");
                il.addInstruction("astore", variableName);
                break;
            case RECORD:
                cg.addPublicField(variableName, variableTypeName);
                il.addInstruction("this");
                il.addInstruction("new", variableTypeName);
                il.addInstruction("dup");
                il.addInstruction("invokespecial", variableTypeName + ".<init>", "void");
                il.addInstruction("putfield", cg.getClassName() + "." + variableName, variableTypeName);
        }
        return il;
    }

    /**
     * Visit an Array Variable Declaration. If the variable is global, a static
     * field is created and a newarray instruction is added to the static
     * initializers. If the variable is local, local variable is created, and
     * newarray instruction is added to the method body. If the variable is a
     * member of a record, a newarray instruction is added to the parent record
     * constructor.
     *
     * @param ctx ArrayVariableDecl context
     * @return InstructionList containing generated code.
     */
    @Override
    public InstructionList visitArrayVariableDecl(MicroParser.ArrayVariableDeclContext ctx) {
        InstructionList il = cg.newInstructionList();
        Identifier id = currentScope.resolve(ctx.ID().getText());
        String arrayName = id.getName();
        ArrayType arrayType = (ArrayType) id.getType();
        Type componentType = arrayType.getComponentType();
        String componentTypeName = componentType.getJavaTypeName();
        String javaTypeName = arrayType.getJavaTypeName();
        String arraySize = ctx.INT().getText();
        Scope.Kind kind = id.getScope().getKind();
        switch (kind) {
            case GLOBAL:
                cg.addStaticField(arrayName, javaTypeName);
                if (!clinitDefined) {
                    clinit = cg.beginMethod(ACC_STATIC, "void", "<clinit>");
                    clinitDefined = true;
                }
                il.addInstruction("const", arraySize);
                il.addInstruction("newarray", componentTypeName, "1");
                il.addInstruction("putstatic", cg.getClassName() + "." + arrayName, javaTypeName);
                clinit.getInstructionList().append(il);
                break;
            case LOCAL:
                cg.addLocalVariable(arrayName, javaTypeName);
                il.addInstruction("const", arraySize);
                il.addInstruction("newarray", componentTypeName, "1");
                il.addInstruction("astore", arrayName);
                break;
            case RECORD:
                cg.addPublicField(arrayName, javaTypeName);
                il.addInstruction("this");
                il.addInstruction("const", arraySize);
                il.addInstruction("newarray", componentTypeName, "1");
                il.addInstruction("putfield", cg.getClassName() + "." + arrayName, javaTypeName);
                break;
        }
        return il;
    }

    @Override

    public InstructionList visitStatement_list(Statement_listContext ctx) {
        InstructionList il = cg.newInstructionList();
        int numChildren = ctx.getChildCount();
        for (int i = 0; i < numChildren; i++) {
            il.append(visit(ctx.getChild(i)));
        }
        return il;
    }

    /**
     * Visit a Read Statement
     *
     * @param ctx Read Statement Context
     * @return InstructionList containing generated code.
     */
    @Override
    public InstructionList visitRead_statement(Read_statementContext ctx) {
        InstructionList il = cg.newInstructionList();
        // Add a local variable reference to a Scanner if not already defined
        if (!inDefined) {
            cg.addLocalVariable("$in", "java.util.Scanner");
            il.addInstruction("new", "java.util.Scanner");
            il.addInstruction("dup");
            il.addInstruction("getstatic", "java.lang.System.in", "java.io.InputStream");
            il.addInstruction("invokespecial", "java.util.Scanner.<init>", "void", "java.io.InputStream");
            il.addInstruction("astore", "$in");
            inDefined = true;
        }
        ctx.lvalue_list().lvalue().forEach((LvalueContext lvalueCtx) -> {
            String idTypeName = typeMap.get(lvalueCtx).getJavaTypeName();
            String scannerMethodName = "next" + toInitalUc(idTypeName);
            if (lvalueCtx instanceof IdLvalueContext) {
                il.addInstruction("aload", "$in");
                il.addInstruction("invokevirtual", "java.util.Scanner." + scannerMethodName, idTypeName);
                il.append(visit(lvalueCtx));
            } else if (lvalueCtx instanceof ArrayLvalueContext) {
                il.append(visit(lvalueCtx));
                il.addInstruction("aload", "$in");
                il.addInstruction("invokevirtual", "java.util.Scanner." + scannerMethodName, idTypeName);
                il.addInstruction("arrayStore", idTypeName);
            } else if (lvalueCtx instanceof RecordLvalueContext) {
            } else {
                MicroCompilerV1.error(ctx, "Invalid target of assignment");
            }
        });
        return il;
    }

    private void genStoreInstruction(InstructionList il, Identifier id, ParserRuleContext ctx) {
        if (id.getScope().getKind() == Scope.Kind.GLOBAL) {
            String idTypeName = id.getType().getJavaTypeName();
            il.addInstruction("putstatic", cg.getClassName() + "." + id.getName(), idTypeName);
        } else {
            // Generate an appropriate store instruction to a local variable
            switch ((PrimitiveType) id.getType()) {
                case INT:
                case BOOL:
                case CHAR:
                    il.addInstruction("istore", id.getName());
                    break;
                case REAL:
                    il.addInstruction("dstore", id.getName());
                    break;
                default:
                    MicroCompilerV1.error(ctx, id.getType() + " is not a supported variable type");
                    break;
            }
        }
    }

    /**
     * Visit an Assignment Statement
     *
     * @param ctx Assignment Statement Context
     * @return null.
     */
    @Override
    public InstructionList visitAssignment_statement(Assignment_statementContext ctx) {
        InstructionList il = cg.newInstructionList();
        LvalueContext lvalueContext = ctx.lvalue();
        Type rhsType = typeMap.get(ctx.expr());
        Type lhsType = typeMap.get(lvalueContext);
        // If the target is an identifier
        if (lvalueContext instanceof IdLvalueContext) {
            il.append(visit(ctx.expr()));
            genCastIfNeeded(il, rhsType, lhsType);
            il.append(visit(ctx.lvalue()));
            // If the target is an array    
        } else if (lvalueContext instanceof ArrayLvalueContext) {
            il.append(visit(ctx.lvalue()));
            il.append(visit(ctx.expr()));
            genCastIfNeeded(il, rhsType, lhsType);
            il.addInstruction("arrayStore", typeMap.get(lvalueContext).getJavaTypeName());
            // If the target is a record
        } else if (lvalueContext instanceof RecordLvalueContext) {
            RecordLvalueContext recordLvalueContext = (RecordLvalueContext) lvalueContext;
            il.append(visit(ctx.lvalue()));
            il.append(visit(ctx.expr()));
            genCastIfNeeded(il, lhsType, rhsType);
            RecordType recordType = (RecordType) typeMap.get(recordLvalueContext.expr());
            String recordClassName = recordType.getJavaTypeName();
            String fieldName = recordLvalueContext.ID().getText();
            il.addInstruction("putfield", recordClassName + "." + fieldName, lhsType.getJavaTypeName());
        } else {
            MicroCompilerV1.error(ctx, "Invalid target of assignment");
        }
        return il;
    }

    public void genCastIfNeeded(InstructionList il, Type lhsType, Type rhsType) {
        if (lhsType == rhsType
                || (lhsType == PrimitiveType.CHAR && rhsType == PrimitiveType.INT)) {
            return;
        }
        il.addInstruction("cast", rhsType.getJavaTypeName(), lhsType.getJavaTypeName());
    }

    /**
     * Generate a store instruction for an Identifier LValue
     *
     * @param ctx IdLvalue Context
     * @return
     */
    @Override
    public InstructionList visitIdLvalue(IdLvalueContext ctx) {
        InstructionList il = cg.newInstructionList();
        Identifier id = currentScope.resolve(ctx.ID().getText());
        genStoreInstruction(il, id, ctx);
        return il;
    }

    /**
     * Place the Array object reference and index onto the stack.
     *
     * @param ctx
     * @param ArrayLvalue Context
     * @return
     */
    @Override
    public InstructionList visitArrayLvalue(ArrayLvalueContext ctx) {
        InstructionList il = cg.newInstructionList();
        // Put array object reference onto the stack
        il.append(visit(ctx.expr(0)));
        // Put the index value onto the stack
        il.append(visit(ctx.expr(1)));
        return il;
    }
    
    /**
     * Place the Record object reference onto the stack
     * @param ctx
     * @return 
     */
    @Override 
    public InstructionList visitRecordLvalue(RecordLvalueContext ctx) {
        InstructionList il = cg.newInstructionList();
        il.append(visit(ctx.expr()));
        return il;
    }

    /**
     * Place the result of an array access onto the stack
     *
     * @param ctx ArrayAccessContext
     * @return
     */
    @Override
    public InstructionList visitArrayAccess(ArrayAccessContext ctx) {
        InstructionList il = cg.newInstructionList();
        // Put array object reference onto the stack
        il.append(visit(ctx.expr(0)));
        // Put the index value onto the stack
        il.append(visit(ctx.expr(1)));
        il.addInstruction("arrayLoad", typeMap.get(ctx).getJavaTypeName());
        return il;
    }

    /**
     * Place the result an a record field access onto the stack
     *
     * @param ctx RecordAccess Context
     * @return
     */
    @Override
    public InstructionList visitRecordAccess(RecordAccessContext ctx) {
        InstructionList il = cg.newInstructionList();
        ExprContext recordContext = ctx.expr();
        // Place the record object reference onto the stack
        il.append(visit(recordContext));
        // Determine the record class name
        RecordType recordType = (RecordType) typeMap.get(recordContext);
        String recordClassName = recordType.getJavaTypeName();
        // Get the field name
        String fieldName = ctx.ID().getText();
        // Get the field type
        Type exprType = typeMap.get(ctx);
        // Generate getfield instruction
        il.addInstruction("getfield", recordClassName + "." + fieldName, exprType.getJavaTypeName());
        return il;
    }

    /**
     * Generate code for a function call
     *
     * @param ctx FcnCall Context
     * @return
     */
    @Override
    public InstructionList visitFcnCall(MicroParser.FcnCallContext ctx) {
        InstructionList il = cg.newInstructionList();
        ctx.expr_list().expr().forEach(expr -> {
            il.append(visit(expr));
        });
        // Invoke the method
        Identifier id = currentScope.resolve(ctx.ID().getText());
        if (id != null) {
            Type procType = id.getType();
            if (procType instanceof ProcedureOrFunction) {
                il.addInstruction("invokestatic", ((ProcedureOrFunction) procType).getInvocationArgs());
            } else {
                MicroCompilerV1.error(ctx, ctx.ID().getText() + " is not a function");
            }
        } else {
            MicroCompilerV1.error(ctx, ctx.ID().getText() + " is not defined");
        }
        return il;
    }

    /**
     * Generate code for unary operations.
     *
     * @param ctx Unaryop Context
     * @return
     */
    @Override
    public InstructionList visitUnaryop(UnaryopContext ctx) {
        InstructionList il = cg.newInstructionList();
        il.append(visit(ctx.expr()));
        Type exprType = typeMap.get(ctx.expr());
        String typeName = exprType.getJavaTypeName();
        switch (ctx.op.getText()) {
            case "+":
                break;  // The + unary operator does nothing.
            case "-": // The neg operator can only be applied to int and double
                switch (typeName) {
                    case "int":
                    case "double":
                        il.addInstruction("neg", typeName);
                        break;
                    default:
                        MicroCompilerV1.error(ctx, "- cannot be applied to " + exprType.toString());
                        break;
                }
                break;
            case "~": // The bitwise not can only be applied to int
                if (exprType == PrimitiveType.INT) {
                    il.addInstruction("const", "-1");
                    il.addInstruction("op", "^", "int");
                } else {
                    MicroCompilerV1.error(ctx, "~ cannot be applied to " + exprType.toString());
                }
                break;
            case "\u00ac": // The logical not can only be applied to booleans
                if (exprType == PrimitiveType.BOOL) {
                    il.addInstruction("const", "1");
                    il.addInstruction("op", "^", "int");
                } else {
                    MicroCompilerV1.error(ctx, "~ cannot be applied to " + exprType.toString());
                }
                break;
        }
        return il;
    }

    /**
     * Evaluate binary arithmetic operations. Except for POW
     *
     * @param ctx Arithop Context
     * @return
     */
    @Override
    public InstructionList visitArithop(ArithopContext ctx) {
        InstructionList il = cg.newInstructionList();
        Type lhsType = typeMap.get(ctx.expr(0));
        Type rhsType = typeMap.get(ctx.expr(1));
        Type resultType = typeMap.get(ctx);
        // Put left operand onto the stack, casting to result type if needed
        il.append(visit(ctx.expr(0)));
        genCastIfNeeded(il, lhsType, resultType);
        // Put the right operand onto the stack, cating to result type if needed
        il.append(visit(ctx.expr(1)));
        genCastIfNeeded(il, lhsType, resultType);
        il.addInstruction("op", ctx.op.getText(), resultType.getJavaTypeName());
        if (resultType == PrimitiveType.CHAR) {
            il.addInstruction("cast", "int", "char");
        }
        return il;
    }

    /**
     * Evaluate the POW operator
     *
     * @param ctx Powop Context
     * @return
     */
    @Override
    public InstructionList visitPowop(PowopContext ctx) {
        InstructionList il = cg.newInstructionList();
        Type lhsType = typeMap.get(ctx.expr(0));
        Type rhsType = typeMap.get(ctx.expr(1));
        Type resultType = typeMap.get(ctx);
        // Put left operand onto the stack, casting to double if needed
        il.append(visit(ctx.expr(0)));
        if (lhsType == PrimitiveType.INT) {
            il.addInstruction("cast", "int", "double");
        }
        // Put the right operand onto the stack, casting to double if needed
        il.append(visit(ctx.expr(1)));
        if (rhsType == PrimitiveType.INT) {
            il.addInstruction("cast", "int", "double");
        }
        // Call the Math.pow method
        il.addInstruction("invokestatic", "java.lang.Math.pow", "double", "double", "double");
        if (resultType == PrimitiveType.INT) {
            il.addInstruction("cast", "double", "int");
        }
        return il;
    }

    /**
     * Generate code for comparison operations
     *
     * @param ctx Compop Context
     * @return
     */
    @Override
    public InstructionList visitCompop(CompopContext ctx) {
        InstructionList il = cg.newInstructionList();
        InstructionList endIl = cg.newInstructionList();
        InstructionHandle endHandle = endIl.addInstruction("nop");
        InstructionList trueIl = cg.newInstructionList();
        InstructionHandle trueHandle = trueIl.addInstruction("const", "true");
        InstructionList falseIl = cg.newInstructionList();
        InstructionHandle falseHandle = falseIl.addInstruction("const", "false");
        falseIl.createGoTo(endHandle);
        Type lhsType = typeMap.get(ctx.expr(0));
        Type rhsType = typeMap.get(ctx.expr(1));
        Type resultType = ReferenceVisitor.determineExpressionResult(lhsType, rhsType);
        // Place left operand onto the stack, casting if needed
        il.append(visit(ctx.expr(0)));
        genCastIfNeeded(il, lhsType, resultType);
        // Place right operand onto the stack, casting if needed
        il.append(visit(ctx.expr(1)));
        genCastIfNeeded(il, rhsType, resultType);
        // Get the operator (changing "=" to "==")
        String cmpop = ctx.op.getText();
        if (cmpop.equals("=")) {
            cmpop = "==";
        }
        il.createIf(cmpop, resultType.getJavaTypeName(), trueHandle);
        il.append(falseIl);
        il.append(trueIl);
        il.append(endIl);
        return il;
    }

    /**
     * Evaluate a logical operation
     *
     * @param ctx Logicalop Context
     * @return
     */
    @Override
    public InstructionList visitLogicalop(LogicalopContext ctx) {
        InstructionList il = cg.newInstructionList();
        InstructionList trueIl = cg.newInstructionList();
        InstructionList falseIl = cg.newInstructionList();
        InstructionList endIl = cg.newInstructionList();
        InstructionHandle trueIh = trueIl.addInstruction("const", "true");
        InstructionHandle falseIh = falseIl.addInstruction("const", "false");
        InstructionHandle endIh = endIl.addInstruction("nop");
        il.append(visit(ctx.expr(0)));
        il.append(visit(ctx.expr(1)));
        il.createGoTo(endIh);
        switch (ctx.op.getText()) {
            case "\u2227":  // Logical and
                il.append(falseIl);
                break;
            case "\u2228": // Logical or
                il.append(trueIl);
                break;
        }
        il.append(endIl);
        return il;
    }

    /**
     * Place the value of a variable onto the stack
     *
     * @param ctx Id Context
     * @return
     */
    @Override
    public InstructionList visitId(IdContext ctx) {
        InstructionList il = cg.newInstructionList();
        Identifier id = currentScope.resolve(ctx.getText());
        Type idType = id.getType();
        String javaTypeName = idType.getJavaTypeName();
        // If it is a global variable, generate a getstatic instruction
        if (id.getScope().getKind() == Scope.Kind.GLOBAL) {
            il.addInstruction("getstatic", cg.getClassName() + "." + id.getName(), javaTypeName);
        } else {
            if (id.getType() instanceof PrimitiveType) {
                switch ((PrimitiveType) id.getType()) {
                    case INT:
                    case BOOL:
                    case CHAR:
                        il.addInstruction("iload", id.getName());
                        break;
                    case REAL:
                        il.addInstruction("dload", id.getName());
                        break;
                    default:
                        MicroCompilerV1.error(ctx, id.getType() + " is not a supported variable type");
                        break;
                }
            } else {
                il.addInstruction("aload", id.getName());
            }
        }
        return il;
    }

    /**
     * Place an integer constant value onto the stack
     *
     * @param ctx Int Context
     * @return
     */
    @Override
    public InstructionList visitInt(IntContext ctx
    ) {
        InstructionList il = cg.newInstructionList();
        il.addInstruction("const", ctx.getText(), "int");
        return il;
    }

    /**
     * Place an float constant value onto the stack
     *
     * @param ctx Float Context
     * @return
     */
    @Override
    public InstructionList visitFloat(FloatContext ctx
    ) {
        InstructionList il = cg.newInstructionList();
        il.addInstruction("const", ctx.getText(), "double");
        return il;
    }

    @Override
    public InstructionList visitChar(CharContext ctx) {
        InstructionList il = cg.newInstructionList();
        il.addInstruction("const", ctx.getText(), "char");
        return il;
    }

    @Override
    public InstructionList visitBool(BoolContext ctx) {
        InstructionList il = cg.newInstructionList();
        il.addInstruction("const", ctx.getText(), "boolean");
        return il;
    }

    @Override
    public InstructionList visitParens(ParensContext ctx) {
        return visit(ctx.expr());
    }

    /**
     * Generate code for a write statement
     *
     * @param ctx Write Statement Context
     * @return
     */
    @Override
    public InstructionList visitWrite_statement(Write_statementContext ctx) {
        InstructionList il = cg.newInstructionList();
        ctx.expr_list().expr().forEach(expr -> {
            il.addInstruction("getstatic", "java.lang.System.out", "java.io.PrintStream");
            il.append(visit(expr));
            Type exprType = typeMap.get(expr);
            il.addInstruction("invokevirtual", "java.io.PrintStream.print", "void", exprType.getJavaTypeName());
        });
        return il;
    }

    /**
     * Generate code for a call statement
     *
     * @param ctx Call Statement Context
     * @return
     */
    @Override
    public InstructionList visitCall_statement(Call_statementContext ctx) {
        InstructionList il = cg.newInstructionList();
        // Evauate arguments and place values onto the stack
        ctx.expr_list().expr().forEach(expr -> {
            il.append(visit(expr));
        });
        Identifier id = currentScope.resolve(ctx.ID().getText());
        if (id != null) {
            Type procType = id.getType();
            if (procType instanceof ProcedureOrFunction) {
                il.addInstruction("invokestatic", ((ProcedureOrFunction) procType).getInvocationArgs());
            } else {
                MicroCompilerV1.error(ctx, ctx.ID().getText() + " is not a procedure");
            }
        } else {
            MicroCompilerV1.error(ctx, ctx.ID().getText() + " is not defined");
        }
        return il;
    }

    /**
     * Generate code for a While Statement
     *
     * @param ctx While Statement Context
     * @return
     */
    @Override
    public InstructionList visitWhile_statement(While_statementContext ctx) {
        InstructionList il = cg.newInstructionList();
        InstructionHandle topOfLoop = il.addInstruction("nop");
        InstructionHandle endOfLoop = il.createGoTo(topOfLoop);
        InstructionHandle outOfLoop = il.addInstruction("nop");
        InstructionList ifStatement = cg.newInstructionList();
        ifStatement.createIf("==0", "int", outOfLoop);
        il.append(topOfLoop, ifStatement);
        il.append(topOfLoop, visit(ctx.expr()));
        il.insert(endOfLoop, visit(ctx.statement_list()));
        return il;
    }

    @Override
    public InstructionList visitDo_until_statement(Do_until_statementContext ctx) {
        InstructionList il = cg.newInstructionList();
        InstructionHandle topOfLoop = il.addInstruction("nop");
        il.append(visit(ctx.statement_list()));
        il.append(visit(ctx.expr()));
        il.createIf("==0", "int", topOfLoop);
        return il;
    }

    /**
     * Generate code for If Statement
     *
     * @param ctx If Statement Context
     * @return
     */
    @Override
    public InstructionList visitIf_statement(If_statementContext ctx) {
        InstructionList il = cg.newInstructionList();
        il.append(visit(ctx.expr()));
        InstructionList il1 = cg.newInstructionList();
        InstructionHandle theEnd = il1.addInstruction("nop");
        InstructionList il2 = cg.newInstructionList();
        InstructionHandle falseTarget = il2.addInstruction("nop");
        il.createIf("==0", "int", falseTarget);
        il.append(visit(ctx.statement_list()));
        List<Elsif_partContext> elsifPart = ctx.elsif_part();
        Else_partContext elsePart = ctx.else_part();
        if (ctx.else_part() != null || (ctx.elsif_part() != null && !ctx.elsif_part().isEmpty())) {
            il.createGoTo(theEnd);
        }
        il.append(il2);
        if (ctx.elsif_part() != null && !ctx.elsif_part().isEmpty()) {
            ctx.elsif_part().forEach(elif -> {
                InstructionList il3 = cg.newInstructionList();
                InstructionHandle falseTarget2 = il3.addInstruction("nop");
                il.append(visit(elif.expr()));
                il.createIf("==0", "int", falseTarget2);
                il.append(visit(elif.statement_list()));
                il.createGoTo(theEnd);
                il.append(il3);
            });
        }
        if (ctx.else_part() != null) {
            il.append(visit(ctx.else_part().statement_list()));
        }
        if (ctx.else_part() != null || (ctx.elsif_part() != null && !ctx.elsif_part().isEmpty())) {
            il.append(il1);
        } else {
            il1.dispose();
        }
        return il;
    }

    /**
     * Generate a return statement
     *
     * @param ctx Return Statement Context
     * @return
     */
    @Override
    public InstructionList visitReturn_statement(MicroParser.Return_statementContext ctx) {
        InstructionList il = cg.newInstructionList();
        il.append(visit(ctx.expr()));
        Type returnType = typeMap.get(ctx.expr());
        if (returnType == null) {
            returnType = PrimitiveType.VOID;
        }
        il.addInstruction("return", returnType.getJavaTypeName());
        return il;
    }

    /**
     * Method to change the first character of a string to upper case
     *
     * @param s The string to be converted
     * @return The converted string.
     */
    public String toInitalUc(String s) {
        char[] charArray = s.toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return new String(charArray);
    }

}
