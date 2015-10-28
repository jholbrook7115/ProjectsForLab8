package edu.temple.cis.c4324.codegen;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.bcel.Constants;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DSTORE;
import org.apache.bcel.generic.FLOAD;
import org.apache.bcel.generic.FSTORE;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IFLE;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IFNULL;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.bcel.generic.IF_ACMPNE;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IF_ICMPGT;
import org.apache.bcel.generic.IF_ICMPLE;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import static org.apache.bcel.generic.InstructionFactory.createBinaryOperation;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.LLOAD;
import org.apache.bcel.generic.LSTORE;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;

/** 
 * This is an extension of org.apache.bcel.generic.InstructionList that adds
 * the addInstruction method.
 * @author Paul Wolfgang
 */
@SuppressWarnings("serial")
public class InstructionList extends org.apache.bcel.generic.InstructionList {
    private static final Map<String, Type> typeMap;
    static final Object[][] typeMapData = {
        {"boolean", Type.BOOLEAN},
        {"byte", Type.BYTE},
        {"char", Type.CHAR},
        {"double", Type.DOUBLE},
        {"int", Type.INT},
        {"long", Type.LONG},
        {"null", Type.NULL},
        {"short", Type.SHORT},
        {"String", Type.STRING},
        {"void", Type.VOID}};

    static {
        typeMap = new HashMap<>();
        for (Object[] typeData : typeMapData) {
            typeMap.put((String) typeData[0], (Type) typeData[1]);
        }
    }

    /**
     * Get the type from the typeName.
     *
     * @param typeName A string representing the type. Note that other than
     * String all object types must be fully qualified.
     * @return the Type object
     */
    static Type getTypeFromName(String typeName) {
        int dimensions = 0;
        int posRb;
        while ((posRb = typeName.lastIndexOf("[")) != -1) {
            dimensions++;
            typeName = typeName.substring(0, posRb);
        }
        Type baseType;
        baseType = typeMap.get(typeName);
        if (baseType == null) {
            baseType = new ObjectType(typeName);
        }
        if (dimensions == 0) {
            return baseType;
        } else {
            return new ArrayType(baseType, dimensions);
        }
    }
    
    private final InstructionFactory factory;
    private final Map<String, Integer> localVariables;
    
    public InstructionList(InstructionFactory factory, Map<String, Integer> localVariables) {
        this.factory = factory;
        this.localVariables = localVariables;
    }

    /**
     * Method to add an instruction. Instructions are specified by a string. 
     * Zero or more argument strings may follow. Instructions supported are
     * <pre>
     * new  &lt;class name&gt;
     * dup  &lt;arg&gt; where &lt;arg&gt; is 1, 2, X1, X2, 2X1, or 2X2
     * getstatic &lt;class name&gt;.&lt;field&gt; &lt;type&gt;
     * putstatic &lt;class name&gt;.&lt;field&gt; &lt;type&gt;
     * getfield &lt;class name&gt;.&lt;field&gt; &lt;type&gt;
     * getfield &lt;class name&gt;.&lt;field&gt; &lt;type&gt;
     * astore &lt;local variable name&gt;
     * istore &lt;local variable name&gt;
     * aload &lt;local variable name&gt;
     * iload &lt;local variable name&gt;
     * invokespecial &lt;class name&gt;.&lt;method&gt; &lt;return type&gt; &lt;arg types&gt;
     * invokevirtual &lt;class name&gt;.&lt;method&gt; &lt;return type&gt; &lt;arg types&gt;
     * invokestatic &lt;class name&gt;.&lt;method&gt; &lt;return type&gt; &lt;arg types&gt;
     * invokeinterface &lt;class name&gt;.&lt;method&gt; &lt;return type&gt; &lt;arg types&gt;
     * op &lt;opcode&gt; where &lt;opcode&gt; is +, -, *, etc.
     * return &lt;return type&gt;
     * nop
     * const &lt;value&gt;
     * this
     * lload &lt;local variable&gt;
     * fload &lt;local variable&gt;
     * dload &lt;local variable&gt;
     * lstore &lt;local variable&gt;
     * fstore &lt;local variable&gt;
     * dstore &lt;local variable&gt;
     * arrayLoad &lt;type&gt;
     * arrayStore &lt;type&gt;
     * pop
     * pop2
     * swap
     * neg &lt;type&gt;
     * iinc &lt;local variable&gt;
     * cast &lt;from type&gt; &lt;to type&gt;
     * newarray &lt;type&gt; &lt;size&gt;
     * arraylength
     * throw 
     * instanceof &lt;target type&gt;
     * </pre>
     * @param instruction The instruction
     * @param args arguments to the instruction.
     * @return An InstructionHandle to the inserted instruction.
     */
    public InstructionHandle addInstruction(String instruction, String... args) {
        Type type;
        switch (instruction) {
            case "new":
                return append(factory.createNew(args[0]));
            case "dup":
                String arg;
                if (args == null || args.length == 0) {
                    arg = "1";
                } else {
                    arg = args[0];
                }
                switch (arg) {
                    case "1":
                        return append(InstructionConstants.DUP);
                    case "2":
                        return append(InstructionConstants.DUP2);
                    case "X1":
                        return append(InstructionConstants.DUP_X1);
                    case "X2":
                        return append(InstructionConstants.DUP_X2);
                    case "2X1":
                        return append(InstructionConstants.DUP2_X1);
                    case "2X2":
                        return append(InstructionConstants.DUP2_X2);
                    default:
                        throw new RuntimeException("Unrecognized argument to dup " + arg);
                }
            case "getstatic":
                return createFieldAccess(args, Constants.GETSTATIC);
            case "getfield":
                return createFieldAccess(args, Constants.GETFIELD);
            case "putstatic":
                return createFieldAccess(args, Constants.PUTSTATIC);
            case "putfield":
                return createFieldAccess(args, Constants.PUTFIELD);
            case "astore":
                return append(new ASTORE(localVariables.get(args[0])));
            case "istore":
                return append(new ISTORE(localVariables.get(args[0])));
            case "aload":
                return append(new ALOAD(localVariables.get(args[0])));
            case "iload":
                return append(new ILOAD(localVariables.get(args[0])));
            case "invokespecial":
                return addInvoke(Constants.INVOKESPECIAL, args);
            case "invokevirtual":
                return addInvoke(Constants.INVOKEVIRTUAL, args);
            case "invokestatic":
                return addInvoke(Constants.INVOKESTATIC, args);
            case "invokeinterface":
                return addInvoke(Constants.INVOKEINTERFACE, args);
            case "op":
                String op = args[0];
                type = getTypeFromName(args[1]);
                return append(createBinaryOperation(op, type));
            case "return":
                if (args == null || args.length == 0) {
                    type = Type.VOID;
                } else {
                    type = getTypeFromName(args[0]);
                }
                return append(InstructionFactory.createReturn(type));
            case "nop":
                return append(InstructionConstants.NOP);
            case "const":
                Object o = parseString(args[0]);
                return append(factory.createConstant(o));
            case "this":
                return append(InstructionConstants.THIS); // Push `this'
            case "lload":
                return append(new LLOAD(localVariables.get(args[0])));
            case "fload":
                return append(new FLOAD(localVariables.get(args[0])));
            case "dload":
                return append(new DLOAD(localVariables.get(args[0])));
            case "fstore":
                return append(new FSTORE(localVariables.get(args[0])));
            case "lstore":
                append(new LSTORE(localVariables.get(args[0])));
                break;
            case "dstore":
                return append(new DSTORE(localVariables.get(args[0])));
            case "arrayLoad":
                return append(InstructionFactory.createArrayLoad(getTypeFromName(args[0])));
            case "arrayStore":
                return append(InstructionFactory.createArrayStore(getTypeFromName(args[0])));
            case "arrayLength":
                return append(new ARRAYLENGTH());
            case "pop":
                return append(InstructionConstants.POP);
            case "pop2":
                return append(InstructionConstants.POP2);
            case "swap":
                return append(InstructionConstants.SWAP);
            case "neg":
                type = getTypeFromName(args[0]);
                byte typeCode = type.getType();
                switch (typeCode) {
                    case Constants.T_INT:
                        return append(InstructionConstants.INEG);
                    case Constants.T_LONG:
                        return append(InstructionConstants.LNEG);
                    case Constants.T_FLOAT:
                        return append(InstructionConstants.FNEG);
                    case Constants.T_DOUBLE:
                        return append(InstructionConstants.DNEG);
                    default:
                        throw new RuntimeException("Invalid Type Code " + args[0]);
                }
            case "iinc":
                return append(new IINC(localVariables.get(args[0]), Integer.parseInt(args[1])));
            case "cast":
                return append(factory.createCast(getTypeFromName(args[0]), getTypeFromName(args[1])));
            case "switch":
                throw new RuntimeException("Switch instruction not supported");
            case "newarray":
                type = getTypeFromName(args[0]);
                Short dim = (short) Integer.parseInt(args[1]);
                return append(factory.createNewArray(type, dim));
            case "arraylength":
                return append(new ARRAYLENGTH());
            case "throw":
                return append(new ATHROW());
            case "instanceof":
                type = getTypeFromName(args[0]);
                return append(factory.createInstanceOf((ReferenceType) type));
            default:
                throw new RuntimeException("Unrecognized instruction " + instruction);
        }
        // Redundant? to keep the compiler happy.
        throw new RuntimeException("Unrecognized instruction " + instruction);
    }
    
    /**
     * Add a goto instruction to the instruction list
     * @param target InstructionHandle of the target
     * @return InstructionHandle of the inserted instruction
     */
    public InstructionHandle createGoTo(InstructionHandle target) {
        return append(new GOTO(target));
    }
    
    /**
     * Create a field access
     * @param args field name followed by the field type
     * @param kind how to access: GETFIELD, PUTFIELD, GETSTATIC, PUTSTATIC
     */
    private InstructionHandle createFieldAccess(String[] args, Short kind) {
        String fieldName = args[0];
        int lastDot = fieldName.lastIndexOf(".");
        String fieldClass = fieldName.substring(0, lastDot);
        String name = fieldName.substring(lastDot + 1);
        String fieldTypeName = args[1];
        Type fieldType = getTypeFromName(fieldTypeName);
        return append(factory.createFieldAccess(fieldClass, name, fieldType, kind));
    }

    /**
     * Method to create an if statement. The following operations are
     * supported:
     * <pre>
     * &gt;
     * &gt;=
     * &lt;
     * &lt;=
     * ==
     * !=
     * &lt;0
     * &lt;=0
     * &gt;0
     * &gt;=0
     * ==0
     * !=0
     * ==null
     * !=null
     * </pre>
     * If the type is not integer, appropriate compare instructions are also
     * created.
     * @param op The operation code.
     * @param typeName The type of the operands
     * @param trueTarget The destination if the condition is true
     * @return InstructionHandle to inserted instruction
     */
    public InstructionHandle createIf(String op, String typeName, InstructionHandle trueTarget) {
        return createIf(op, getTypeFromName(typeName), trueTarget);
    }

    /**
     * Method to create an if statement
     * @param op The operation code
     * @param type The type of the operands
     * @param trueTarget The destination if the if statement is true
     * @return InstructionHandle to inserted instruction
     */
    private InstructionHandle createIf(String op, Type type, InstructionHandle trueTarget) {
        if (type instanceof BasicType) {
            byte typeCode = type.getType();
            switch (typeCode) {
                case Constants.T_DOUBLE:
                    if (op.startsWith(">")) {
                        append(InstructionConstants.DCMPG);
                    } else {
                        append(InstructionConstants.DCMPL);
                    }
                    return insertCompareOp(op, trueTarget);
                case Constants.T_FLOAT:
                    if (op.startsWith(">")) {
                        append(InstructionConstants.FCMPG);
                    } else {
                        append(InstructionConstants.FCMPL);
                    }
                    return insertCompareOp(op, trueTarget);
                case Constants.T_LONG:
                    append(InstructionConstants.LCMP);
                    return insertCompareOp(op, trueTarget);
                default:
                    switch (op) {
                        case "<0":
                            return append(new IFLT(trueTarget));
                        case "<=0":
                            return append(new IFLE(trueTarget));
                        case "==0":
                            return append(new IFEQ(trueTarget));
                        case "!=0":
                            return append(new IFNE(trueTarget));
                        case ">=0":
                            return append(new IFGE(trueTarget));
                        case ">0":
                            return append(new IFGT(trueTarget));
                        case "<":
                            return append(new IF_ICMPLT(trueTarget));
                        case "<=":
                            return append(new IF_ICMPLE(trueTarget));
                        case "==":
                            return append(new IF_ICMPEQ(trueTarget));
                        case "!=":
                            return append(new IF_ICMPNE(trueTarget));
                        case ">=":
                            return append(new IF_ICMPGE(trueTarget));
                        case ">":
                            return append(new IF_ICMPGT(trueTarget));
                        default:
                            throw new RuntimeException("Unrecognized compare operator " + op);
                    }
            }
        } else {
            switch (op) {
                case "==null":
                    return append(new IFNULL(trueTarget));
                case "!=null":
                    return append(new IFNONNULL(trueTarget));
                case "==":
                    return append(new IF_ACMPEQ(trueTarget));
                case "!=":
                    return append(new IF_ACMPNE(trueTarget));
            }
            throw new RuntimeException("Unrecognized comparison operator " + op);
        }
    }

    /**
     * Method to insert a compare operation
     * @param op The op code.
     */
    private InstructionHandle insertCompareOp(String op, InstructionHandle trueTarget) {
        switch (op) {
            case "==":
                return append(new IFEQ(trueTarget));
            case "!=":
                return append(new IFNE(trueTarget));
            case "<":
                return append(new IFLT(trueTarget));
            case "<=":
                return append(new IFLE(trueTarget));
            case ">":
                return append(new IFGT(trueTarget));
            case ">=":
                return append(new IFGE(trueTarget));
            default:
                throw new RuntimeException("Unrecognized comparison operator " + op);
        }
    }

    /**
     * Add an invoke instruction.
     * @param instruction The invoke instruction: INVOKEINTERFACE, INVOKESTATIC, INVOKEVIRTUAL, or INVOKESPECIAL.
     * @param args args[0] is the class and method, args[1] return type, args[2..] method argument types.
     * @return InstructionHandle to the inserted instruction.
     */
    private InstructionHandle addInvoke(short instruction, String... args) {
        String classAndMethod = args[0];
        int lastDot = classAndMethod.lastIndexOf(".");
        String cName = classAndMethod.substring(0, lastDot);
        String methodName = classAndMethod.substring(lastDot + 1);
        String returnTypeName = args[1];
        Type returnType = getTypeFromName(returnTypeName);
        int numArgs = args.length - 2;
        Type[] paramTypes;
        if (numArgs == 0) {
            paramTypes = Type.NO_ARGS;
        } else {
            paramTypes = new Type[numArgs];
            for (int i = 0; i < numArgs; i++) {
                paramTypes[i] = getTypeFromName(args[i + 2]);
            }
        }
        return append(factory.createInvoke(cName, methodName, returnType, paramTypes, instruction));
    }
    
    private static final Pattern intPattern = Pattern.compile("([+-]?\\d+)([Ll]?)");
    private static final Pattern floatPattern = Pattern.compile("([+-]?)(\\d*)(\\.)?(\\d*)?([eE][+-]?\\d+)?([fFdD])?");

    /**
     * Method to parse a string and return the appropriate constant type.
     *
     * @param s The string to be converted
     * @return A Number, Boolean, Character, or String object
     */
    static Object parseString(String s) {
        switch (s) {
            case "true":
                return Boolean.TRUE;
            case "false":
                return Boolean.FALSE;
        }
        Matcher m = intPattern.matcher(s);
        if (m.matches()) {
            if (m.group(2).isEmpty()) {
                return new Integer(s);
            } else {
                return new Long(m.group(1));
            }
        }
        m = floatPattern.matcher(s);
        if (m.matches()) {
            String sign = m.group(1);
            if (sign == null) {
                sign = "";
            }
            String intPart = m.group(2);
            if (intPart == null) {
                intPart = "";
            }
            String point = m.group(3);
            if (point == null) {
                point = "";
            }
            String fraction = m.group(4);
            if (fraction == null) {
                fraction = "";
            }
            String expPart = m.group(5);
            if (expPart == null) {
                expPart = "";
            }
            String suffix = m.group(6);
            if (suffix == null) {
                suffix = "";
            }
            if (!point.isEmpty() || !expPart.isEmpty()) {
                String ss = sign + intPart + point + fraction + expPart;
                if (suffix.equals("f") || suffix.equals("F")) {
                    return new Float(ss);
                } else {
                    return new Double(ss);
                }
            }
        }

        if (s.startsWith("'") && s.endsWith("'")) {
            String ss = s.substring(1, s.length() - 1);
            char c = ss.charAt(0);
            if (c == '\\') {
                char c2 = ss.charAt(1);
                switch (c2) {
                    case 'b': return '\b';
                    case 'n': return '\n';
                    case 't': return '\t';
                    case 'f': return '\f';
                    case 'r': return '\r';
                    case '\"': return '\"';
                    case '\'': return '\'';
                    case '\\': return '\\';
                    case 'u':
                        String hexValue = ss.substring(2, ss.length()-1);
                        int intValue = Integer.parseInt(hexValue, 16);
                        return (char)intValue;       
                }
            } else {
                return c;
            }
        }

        return s;

    }
    
}
