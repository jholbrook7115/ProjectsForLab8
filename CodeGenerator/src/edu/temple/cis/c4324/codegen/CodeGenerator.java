package edu.temple.cis.c4324.codegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static org.apache.bcel.Constants.ACC_PRIVATE;
import static org.apache.bcel.Constants.ACC_PUBLIC;
import static org.apache.bcel.Constants.ACC_STATIC;
import static org.apache.bcel.Constants.ACC_SUPER;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IfInstruction;
import static org.apache.bcel.generic.InstructionConstants.ARETURN;
import static org.apache.bcel.generic.InstructionConstants.DRETURN;
import static org.apache.bcel.generic.InstructionConstants.IRETURN;
import static org.apache.bcel.generic.InstructionConstants.RETURN;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionTargeter;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.generic.Type;
import static org.apache.bcel.generic.Type.NO_ARGS;
import org.apache.bcel.util.InstructionFinder;
import org.apache.bcel.util.InstructionFinder.CodeConstraint;

/**
 * Class to generate Java classes. Uses Apache BECL.
 * @author Paul
 */
public class CodeGenerator {

    // fields are pachage private to facilitate unit tests.
    ClassGen cg;
    ConstantPoolGen cp;
    InstructionList il;
    InstructionFactory factory;
    String className;
    MethodGen mg;
    BranchHandle branchHandle;
    CodeGenerator parent;
    List<InnerClass> innerClassesList;
    Map<String, Integer> localVariables;

    public CodeGenerator() {
    }

    /**
     * Begin the definition of a class.
     *
     * @param sourceFileName The name of the source file
     * @param modifiers The modifier flags a combination of modifiers. e.g. ACC_PRIVATE, ACC_PUBLIC, etc.
     * @param className The fully qualified class name
     * @param superClassName The fully qualified super class name
     * @param interfaces The fully qualified class names of implemented
     * interfaces
     */
    public void beginClass(String sourceFileName, int modifiers, String className, String superClassName, String... interfaces) {
        this.className = className;
        modifiers |= ACC_SUPER; // JVM Spec says should always be set. Ignored in Java 8 and above.
        cg = new ClassGen(className, superClassName, sourceFileName, modifiers, interfaces);
        cp = cg.getConstantPool();
        factory = new InstructionFactory(cp);
    }

    /**
     * Begin the definition of a top-level class
     *
     * @param sourceFileName The name of the source file
     * @param className The name of the class
     */
    public void beginClass(String sourceFileName, String className) {
        beginClass(sourceFileName, ACC_PUBLIC, className, "java.lang.Object", (String[]) null);
    }

    /**
     * Begin the definition of an inner class.
     * 
     * @param modifiers The modifier flags
     * @param className The class name
     * @param superClassName The super class name
     * @param interfaces Implemented interfaces
     * @return A new code generator instance.
     */
    public CodeGenerator beginInnerClass(int modifiers, String className, String superClassName, String... interfaces) {
        CodeGenerator innerClassCodeGenerator = new CodeGenerator();
        String innerClassName = this.className + "$" + className;
        innerClassCodeGenerator.beginClass(cg.getFileName(), modifiers, innerClassName, superClassName, interfaces);
        addInnerClass(innerClassName, this.className, className, modifiers);
        innerClassCodeGenerator.addInnerClass(innerClassName, this.className, className, modifiers);
        innerClassCodeGenerator.parent = this;
        return innerClassCodeGenerator;
    }

    /**
     * End an inner class. The generated class is written.
     * @return The code generator for the enclosing class.
     */
    public CodeGenerator endInnerClass() {
        endClass();
        return parent;
    }

    /**
     * Add an inner class to this class.
     * @param innerClassName The fully qualified inner class name
     * @param outerClassName The fully qualified outer class name
     * @param className The simple inner class name
     * @param modifiers The modifiers of the inner class
     */
    public void addInnerClass(String innerClassName, String outerClassName, String className, int modifiers) {
        int innerClassIndex = cp.addClass(innerClassName);
        int outerClassIndex = cp.addClass(outerClassName);
        int innerNameIndex = cp.addUtf8(className);
        if (innerClassesList == null) {
            innerClassesList = new ArrayList<>();
        }
        innerClassesList.add(new InnerClass(innerClassIndex, outerClassIndex, innerNameIndex, modifiers));
    }

    /**
     * Begin the definition of a method
     *
     * @param accessFlags The access flags
     * @param returnTypeName The return type
     * @param methodName The method name
     * @param arguments The arguments consisting of type and name separated by
     * space.
     * @return A MethodGen object for this method.
     */
    public MethodGen beginMethod(int accessFlags, String returnTypeName, String methodName, String... arguments) {
        if (mg != null) {
            endMethod();
        }
        factory = new InstructionFactory(cp);
        Type[] argTypes;
        String[] argNames;
        if (arguments != null) {
            argTypes = new Type[arguments.length];
            argNames = new String[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                String[] arg = arguments[i].split("\\s+");
                argTypes[i] = InstructionList.getTypeFromName(arg[0]);
                argNames[i] = arg[1];
            }
        } else {
            argTypes = NO_ARGS;
            argNames = null;
        }
        Type returnType = InstructionList.getTypeFromName(returnTypeName);
        localVariables = new HashMap<>();
        il = newInstructionList();
        mg = new MethodGen(accessFlags, returnType, argTypes, argNames, methodName, className, il, cp);
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                localVariables.put(argNames[i], i);
            }
        }
        return mg;
    }

    /**
     * Method to begin a main method
     * @return a MethodGen for this method
     */
    public MethodGen beginMain() {
        return beginMethod(ACC_PUBLIC | ACC_STATIC, "void", "main", "String[] args");
    }

    /**
     * Method to add a static private field.
     *
     * @param name The name of the field.
     * @param typeName Type of the field.
     */
    public void addStaticField(String name, String typeName) {
        Type type = InstructionList.getTypeFromName(typeName);
        FieldGen fg = new FieldGen(ACC_PRIVATE | ACC_STATIC, type, name, cp);
        cg.addField(fg.getField());
    }

    /**
     * Method to add a public field
     *
     * @param name The name of the field.
     * @param typeName Type of the field.
     */
    public void addPublicField(String name, String typeName) {
        Type type = InstructionList.getTypeFromName(typeName);
        FieldGen fg = new FieldGen(ACC_PUBLIC, type, name, cp);
        cg.addField(fg.getField());
    }

    /**
     * Method to add a local variable.
     *
     * @param name Name of the local variable
     * @param typeName Type of the local variable
     */
    public void addLocalVariable(String name, String typeName) {
        if (!localVariables.containsKey(name)) {
            Type type = InstructionList.getTypeFromName(typeName);
            LocalVariableGen lg = mg.addLocalVariable(name, type, null, null);
            localVariables.put(name, lg.getIndex());
        }
    }


    /**
     * End a method. The method code is optimized and the
     * method is then added to the class.
     */
    public void endMethod() {
//        optimizeIfStatements(il);
//        removeNops(il);
//        removeRedundantGoTo(il);
        mg.setMaxStack();
        mg.setMaxLocals();
        if (!(il.contains(ARETURN) || il.contains(DRETURN) || il.contains(IRETURN) || il.contains(RETURN))) {
            il.addInstruction("return");
        }
        cg.addMethod(mg.getMethod());
        il.dispose();
        mg = null;
    }

    /**
     * Optimize If Statements by removing redundant constant loads.
     * @param il The instruction list.
     */
    private void optimizeIfStatements(InstructionList il) {
        InstructionFinder f = new InstructionFinder(il);
        String pat = "IfInstruction ICONST_0 GOTO ICONST_1 NOP (IFEQ|IFNE)";
        CodeConstraint constraint = (match -> {
            IfInstruction if1 = (IfInstruction) match[0].getInstruction();
            GOTO g = (GOTO) match[2].getInstruction();
            return (if1.getTarget() == match[3] && g.getTarget() == match[4]);
        });
        boolean removed;
        do {
            removed = false;
            for (Iterator<InstructionHandle[]> e = f.search(pat, constraint); e.hasNext();) {
                InstructionHandle[] match = e.next();
                ((BranchHandle) match[0]).setTarget(((BranchHandle) match[5]).getTarget());
                removed = true;
                try {
                    il.delete(match[1], match[5]);
                } catch (TargetLostException ex) {
                    for (InstructionHandle target : ex.getTargets()) {
                        for (InstructionTargeter targeter : target.getTargeters()) {
                            targeter.updateTarget(target, match[5].getNext());
                        }
                    }
                }
            }
            if (removed) {
                f.reread();
            }
        } while (removed);
    }

    /**
     * Remove nop instructions
     * @param il The instruction list
     */
    private void removeNops(InstructionList il) {
        InstructionFinder f = new InstructionFinder(il);
        String pat = "NOP+";
        InstructionHandle next;
        for (Iterator<InstructionHandle[]> iter = f.search(pat); iter.hasNext();) {
            InstructionHandle[] match = iter.next();
            InstructionHandle first = match[0];
            InstructionHandle last = match[match.length - 1];
            next = last.getNext();
            if (next == null) {
                break;
            }
            try {
                il.delete(first, last);
            } catch (TargetLostException ex) {
                for (InstructionHandle target : ex.getTargets()) {
                    for (InstructionTargeter targeter : target.getTargeters()) {
                        targeter.updateTarget(target, next);
                    }
                }
            }
        }
    }

    /**
     * Remove redundant goto instructions. A redundant goto is one that jumps
     * to the immediately following instruction.
     * @param il 
     */
    private void removeRedundantGoTo(InstructionList il) {
        InstructionFinder f = new InstructionFinder(il);
        String pat = "GOTO";
        InstructionHandle next;
        for (Iterator<InstructionHandle[]> iter = f.search(pat); iter.hasNext();) {
            InstructionHandle[] match = iter.next();
            BranchHandle gotoInstruction = (BranchHandle) match[0];
            next = gotoInstruction.getNext();
            if (gotoInstruction.getTarget() == next) {
                try {
                    il.delete(gotoInstruction);
                } catch (TargetLostException ex) {
                    for (InstructionHandle target : ex.getTargets()) {
                        for (InstructionTargeter targeter : target.getTargeters()) {
                            targeter.updateTarget(target, next);
                        }
                    }
                }
            }
        }
    }

    /**
     * Return the Java class from the code generator. This finalizes the class.
     * @return The JavaClass object
     */
    public JavaClass getJavaClass() {
        return cg.getJavaClass();
    }

    /**
     * Method to end a class. Adds am inner classes attribute if this is an
     * inner class or if it contains inner classes. The class is then written
     * to a file using the Java naming convention.
     */
    public void endClass() {
        if (innerClassesList != null) {
            int nameIndex = cp.addUtf8("InnerClasses");
            int numInnerClasses = innerClassesList.size();
            InnerClasses innerClasses = new InnerClasses(nameIndex,
                    2 + 8 * numInnerClasses,
                    innerClassesList.toArray(new InnerClass[numInnerClasses]),
                    cp.getConstantPool());
            cg.addAttribute(innerClasses);
        }
        String outputFileName = cg.getClassName() + ".class";
        JavaClass javaClass = cg.getJavaClass();
        try {
            javaClass.dump(outputFileName);
        } catch (IOException ioex) {
            System.err.println(ioex);
            System.exit(1);
        }
    }


    /**
     * Return the class name
     * @return the class name
     */
    public String getClassName() {
        return className;
    }
       
    /**
     * Get a new InstructionList
     * @return a new InstrucitonList
     */
    public InstructionList newInstructionList() {
        return new InstructionList(factory, localVariables);
    }

}
