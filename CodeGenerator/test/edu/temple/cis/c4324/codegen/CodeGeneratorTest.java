package edu.temple.cis.c4324.codegen;

import static org.apache.bcel.Constants.ACC_PUBLIC;
import static org.apache.bcel.Constants.ACC_STATIC;
import static org.apache.bcel.Constants.ASTORE;
import static org.apache.bcel.Constants.ASTORE_1;
import static org.apache.bcel.Constants.ASTORE_2;
import static org.apache.bcel.Constants.ASTORE_3;
import static org.apache.bcel.Constants.DUP;
import static org.apache.bcel.Constants.GETSTATIC;
import static org.apache.bcel.Constants.IADD;
import static org.apache.bcel.Constants.INVOKESPECIAL;
import static org.apache.bcel.Constants.INVOKEVIRTUAL;
import static org.apache.bcel.Constants.ISTORE;
import static org.apache.bcel.Constants.ISTORE_1;
import static org.apache.bcel.Constants.ISTORE_2;
import static org.apache.bcel.Constants.ISTORE_3;
import static org.apache.bcel.Constants.NEW;
import static org.apache.bcel.Constants.RETURN;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CodeGeneratorTest {
    
    public CodeGeneratorTest() {
    }

    @Test
    public void testBeginClassWithAllParameters() {
        System.out.println("beginClass");
        int modifiers = ACC_PUBLIC;
        String className = "add.Add";
        String superClassName = "java.lang.Object";
        String[] interfaces = null;
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", modifiers, className, superClassName, interfaces);
        assertEquals("add.Add", instance.cg.getClassName());
        assertEquals("java.lang.Object", instance.cg.getSuperclassName());
        assertTrue(instance.cg.isPublic());      
    }
    
    @Test
    public void testSimplifiedBeginClass() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        assertEquals("add.Add", instance.cg.getClassName());
        assertEquals("java.lang.Object", instance.cg.getSuperclassName());
        assertTrue(instance.cg.isPublic());              
    }
    
    @Test
    public void testBeginMethod() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMethod(ACC_PUBLIC | ACC_STATIC, "void", "main", "String[] args");
        assertEquals("([Ljava/lang/String;)V", instance.mg.getSignature());
        assertEquals("add.Add", instance.mg.getClassName());
        assertEquals("main", instance.mg.getName());
        assertEquals(ACC_PUBLIC|ACC_STATIC, instance.mg.getAccessFlags());
    }
    
    @Test 
    public void testBeginMain() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        assertEquals("([Ljava/lang/String;)V", instance.mg.getSignature());
        assertEquals("add.Add", instance.mg.getClassName());
        assertEquals("main", instance.mg.getName());
        assertEquals(ACC_PUBLIC|ACC_STATIC, instance.mg.getAccessFlags());
    }
    
    @Test
    public void testGetVoidType() {
        Type voidType = InstructionList.getTypeFromName("void");
        assertEquals(Type.VOID, voidType);
    }
    
    @Test
    public void testGetStringArrayType() {
        Type stringArrayType = InstructionList.getTypeFromName("String[]");
        Type expected = new ArrayType(Type.STRING, 1);
        assertEquals(expected, stringArrayType);
    }
    
    @Test
    public void testAddLocalVariable() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        instance.addLocalVariable("$in", "java.util.Scanner");
        LocalVariableGen[] localVariables = instance.mg.getLocalVariables();
        boolean fail = true;
        for (int i = 0; fail && i < localVariables.length; i++) {
            LocalVariableGen lvg = localVariables[i];
            fail = !lvg.getName().equals("$in") || !lvg.getType().equals(new ObjectType("java.util.Scanner"));
        }
        assertFalse(fail);  
    }
    
    @Test
    public void testAddInstructionNew() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        InstructionHandle end = il.addInstruction("new", "java.util.Scanner");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(NEW, lastInstruction.getOpcode());
        assertEquals("new java/util/Scanner", lastInstruction.toString(instance.cp.getConstantPool()));       
    }
    
    @Test
    public void testAddInstructionDup() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        InstructionHandle end = il.addInstruction("dup");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(DUP, lastInstruction.getOpcode());
    }
    
    @Test
    public void testAddIstructionStaticFieldAccess() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        InstructionHandle end = il.addInstruction("getstatic", "java.lang.System.in", "java.io.InputStream");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(GETSTATIC, lastInstruction.getOpcode());
        assertEquals("getstatic java.lang.System.in Ljava/io/InputStream;", lastInstruction.toString(instance.cp.getConstantPool()));       
    }
    
    @Test
    public void testAddInstructionAStore() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        instance.addLocalVariable("$in", "java.util.Scanner");
        instance.addLocalVariable("x", "int");
        instance.addLocalVariable("y", "int");
        instance.addLocalVariable("z", "int");
        InstructionHandle end = il.addInstruction("astore", "$in");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(ASTORE_1, lastInstruction.getOpcode());
        assertEquals("astore_1", lastInstruction.toString(instance.cp.getConstantPool())); 
        end = il.addInstruction("astore", "x");
        lastInstruction = end.getInstruction();
        assertEquals(ASTORE_2, lastInstruction.getOpcode());
        assertEquals("astore_2", lastInstruction.toString(instance.cp.getConstantPool())); 
        end = il.addInstruction("astore", "y");
        lastInstruction = end.getInstruction();
        assertEquals(ASTORE_3, lastInstruction.getOpcode());
        assertEquals("astore_3", lastInstruction.toString(instance.cp.getConstantPool())); 
        end = il.addInstruction("astore", "z");
        lastInstruction = end.getInstruction();
        assertEquals(ASTORE, lastInstruction.getOpcode());
        assertEquals("astore 4", lastInstruction.toString(instance.cp.getConstantPool()));       
    }

    @Test
    public void testAddInstructionIStore() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        instance.addLocalVariable("in", "int");
        instance.addLocalVariable("x", "int");
        instance.addLocalVariable("y", "int");
        instance.addLocalVariable("z", "int");
        InstructionHandle end = il.addInstruction("istore", "in");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(ISTORE_1, lastInstruction.getOpcode());
        assertEquals("istore_1", lastInstruction.toString(instance.cp.getConstantPool())); 
        end = il.addInstruction("istore", "x");
        lastInstruction = end.getInstruction();
        assertEquals(ISTORE_2, lastInstruction.getOpcode());
        assertEquals("istore_2", lastInstruction.toString(instance.cp.getConstantPool())); 
        end = il.addInstruction("istore", "y");
        lastInstruction = end.getInstruction();
        assertEquals(ISTORE_3, lastInstruction.getOpcode());
        assertEquals("istore_3", lastInstruction.toString(instance.cp.getConstantPool())); 
        end = il.addInstruction("istore", "z");
        lastInstruction = end.getInstruction();
        assertEquals(ISTORE, lastInstruction.getOpcode());
        assertEquals("istore 4", lastInstruction.toString(instance.cp.getConstantPool()));       
    }
    
    @Test
    public void testAddInstructionInvokeSpecial() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        InstructionHandle end = il.addInstruction("invokespecial", "java.util.Scanner.<init>", "void", "java.io.InputStream");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(INVOKESPECIAL, lastInstruction.getOpcode());
        assertEquals("invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V", lastInstruction.toString(instance.cp.getConstantPool())); 
    }

    @Test
    public void testAddInstructionInvokeVirtualNextInt() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        InstructionHandle end = il.addInstruction("invokevirtual", "java.util.Scanner.nextInt", "int");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(INVOKEVIRTUAL, lastInstruction.getOpcode());
        assertEquals("invokevirtual java/util/Scanner/nextInt()I", lastInstruction.toString(instance.cp.getConstantPool())); 
    }
 
    @Test
    public void testAddInstructionInvokeVirtualPrintln() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        InstructionHandle end = il.addInstruction("invokevirtual", "java.io.printStream.println", "void", "int");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(INVOKEVIRTUAL, lastInstruction.getOpcode());
        assertEquals("invokevirtual java/io/printStream/println(I)V", lastInstruction.toString(instance.cp.getConstantPool())); 
    }
    
    @Test
    public void testAddInstructionIAdd() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        InstructionHandle end = il.addInstruction("op", "+", "int");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(IADD, lastInstruction.getOpcode());
    }

    @Test
    public void testAddInstructionReturn() {
        CodeGenerator instance = new CodeGenerator();
        instance.beginClass("<generated>", "add.Add");
        instance.beginMain();
        InstructionList il = new InstructionList(instance.factory, instance.localVariables);
        InstructionHandle end = il.addInstruction("return");
        Instruction lastInstruction = end.getInstruction();
        assertEquals(RETURN, lastInstruction.getOpcode());
    }
    
        
        
}
