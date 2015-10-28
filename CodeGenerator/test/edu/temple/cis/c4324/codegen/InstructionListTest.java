package edu.temple.cis.c4324.codegen;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class InstructionListTest {
    
    public InstructionListTest() {
    }

    @Test
    public void returnABoolean() {
        assertEquals(Boolean.TRUE, InstructionList.parseString("true"));
        assertEquals(Boolean.FALSE, InstructionList.parseString("false"));
    }
    
    @Test
    public void returnAnInteger() {
        assertEquals(new Integer(123), InstructionList.parseString("123"));
        assertEquals(new Integer(-123), InstructionList.parseString("-123"));
        assertEquals(new Integer(+123), InstructionList.parseString("+123"));
        assertFalse(new Long(123).equals(InstructionList.parseString("123")));
        assertEquals(new Long(123), InstructionList.parseString("123l"));
        assertEquals(new Long(-123), InstructionList.parseString("-123l"));
        assertEquals(new Long(+123), InstructionList.parseString("+123l"));
        assertFalse(new Integer(123).equals(InstructionList.parseString("123l")));
    }
    
    @Test
    public void returnADoubleOrFloat() {
        assertTrue(new Float(123.f).equals(InstructionList.parseString("123.f")));
        assertTrue(new Float(123.f).equals(InstructionList.parseString("123.F")));
        assertTrue(new Float(.123f).equals(InstructionList.parseString(".123f")));
        assertTrue(new Float(1e-1f).equals(InstructionList.parseString("1e-1f")));
        assertTrue(new Double(123.456e3).equals(InstructionList.parseString("123.456e3")));
        assertTrue(new Double(123.456e3).equals(InstructionList.parseString("123.456e3d")));
        assertTrue(new Double(123.456e3).equals(InstructionList.parseString("123.456e3D")));
    }
    
    @Test
    public void returnCharOrString() {
        assertTrue(new Character('x').equals(InstructionList.parseString("'x'")));
        assertEquals("foobar", InstructionList.parseString("foobar"));
    }
    
}
