package edu.temple.cis.c4324.microcompilerv1;

/**
 *
 * @author Paul
 */
public class Identifier {
    
    private final String name;
    private Type type;
    private Scope scope;
    
    public Identifier(String name, Type type, Scope scope) {
        this.name = name;
        this.type = type;
        this.scope = scope;
    }
    
    public Type getType() {return type;}
    
    public Scope getScope() {return scope;}
    
    public String getName() {return name;}
    
    public void setType(Type type) {
        if (this.type == null) {
            this.type = type;
        } else {
            throw new RuntimeException("Type cannot be set a second time");
        }
    }
    
}
