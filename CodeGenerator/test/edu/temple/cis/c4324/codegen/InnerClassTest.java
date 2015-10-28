package edu.temple.cis.c4324.codegen;

import static org.apache.bcel.Constants.ACC_PUBLIC;
import static org.apache.bcel.Constants.ACC_STATIC;
import org.junit.Test;

/**
 *
 * @author Paul
 */
public class InnerClassTest {

    @Test
    public void testInnerClass() throws Exception {
        CodeGenerator cg = new CodeGenerator();
        int modifiers = ACC_PUBLIC;
        String className = "RecordTest";
        String superClassName = "java.lang.Object";
        String[] interfaces = null;
        cg.beginClass("<generated>", modifiers, className, superClassName, interfaces);
        CodeGenerator innerClassCG = cg.beginInnerClass(ACC_PUBLIC | ACC_STATIC, "ARecord", superClassName, interfaces);
        innerClassCG.endInnerClass();
        innerClassCG = cg.beginInnerClass(ACC_PUBLIC | ACC_STATIC, "BRecord", superClassName, interfaces);
        innerClassCG.endInnerClass();
        cg.endClass();
    }
}
