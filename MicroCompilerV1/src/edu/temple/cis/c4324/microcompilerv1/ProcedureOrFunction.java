package edu.temple.cis.c4324.microcompilerv1;

import java.util.List;

/**
 *
 * @author Paul
 */
public class ProcedureOrFunction implements Type {
    
    private final String name;
    private final Type returnType;
    private final String[] typeParameterPairs;
    private final String[] invocationArgs;
    
    public ProcedureOrFunction(String name, Type returnType,
            List<String> parameterNames, List<String> parameterTypeNames) {
        this.name = name;
        this.returnType = returnType;
        typeParameterPairs = new String[parameterNames.size()];
        invocationArgs = new String[parameterNames.size() + 2];
        invocationArgs[0] = name;
        invocationArgs[1] = returnType.getJavaTypeName();
        for (int i = 0; i < parameterNames.size(); i++) {
            String parameterTypeName = parameterTypeNames.get(i);
            invocationArgs[i + 2] = parameterTypeName;
            typeParameterPairs[i] = parameterTypeName + " " + parameterNames.get(i);
        }
    }
    
    public String getName() {return name;}
    public Type getReturnType() {return returnType;}
    public String[] getTypeParameterPairs() {return typeParameterPairs;}
    public String[] getInvocationArgs() {return invocationArgs;}
    
    
    @Override
    public String getJavaTypeName() {return null;}
    
}
