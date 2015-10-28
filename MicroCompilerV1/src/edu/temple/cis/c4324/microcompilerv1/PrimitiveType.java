package edu.temple.cis.c4324.microcompilerv1;

/**
 *
 * @author Paul
 */
public enum PrimitiveType implements Type {
    
    VOID,
    INT,
    REAL,
    CHAR,
    BOOL;
    
    @Override
    public String getJavaTypeName() {
        switch(this) {
            case VOID: return "void";
            case INT: return "int";
            case REAL: return "double";
            case CHAR: return "char";
            case BOOL: return "boolean";
        }
        return "error";
    }
    
}
