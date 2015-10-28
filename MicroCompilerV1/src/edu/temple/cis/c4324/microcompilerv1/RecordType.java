package edu.temple.cis.c4324.microcompilerv1;

/**
 *
 * @author Paul
 */
public class RecordType implements Type {
    
    private final String recordTypeName;
    private final String parentName;
    private Scope containedScope;
    
    public RecordType(String recordTypeName, String parentName) {
        this.recordTypeName = recordTypeName;
        this.parentName = parentName;
    }
    
    @Override
    public String getJavaTypeName() {
        return parentName + "$" + recordTypeName;
    }
    
    public String getRecordTypeName() {
        return recordTypeName;
    }
    
    public Scope getContainedScope() {
        return containedScope;
    }
    
    public void setContainedScope(Scope containedScope) {
        if (this.containedScope == null) {
            this.containedScope = containedScope;
        } else {
            throw new RuntimeException("Contained Scope can only be defined once");
        }
    }
}
