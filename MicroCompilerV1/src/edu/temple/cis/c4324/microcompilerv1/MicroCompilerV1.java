package edu.temple.cis.c4324.microcompilerv1;

import edu.temple.cis.c4324.codegen.CodeGenerator;
import edu.temple.cis.c4324.micro.MicroLexer;
import edu.temple.cis.c4324.micro.MicroParser;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.apache.bcel.classfile.JavaClass;

public class MicroCompilerV1 {
    
    private static boolean errorOccured = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String inputFileName = args[0];
        int lastDot = inputFileName.lastIndexOf(".");
        String outputFileName;
        if (lastDot != -1) {
            outputFileName = inputFileName.substring(0, lastDot) + ".class";
        } else {
            outputFileName = inputFileName + ".class";
        }
        InputStream inStream = new FileInputStream(inputFileName);      
        ANTLRInputStream input = new ANTLRInputStream(new InputStreamReader(inStream, "UTF-8"));
        MicroLexer lexer = new MicroLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MicroParser parser = new MicroParser(tokens);
        ParseTree tree = parser.program();
        DefinitionVisitor defVisitor = new DefinitionVisitor();
        defVisitor.visit(tree);
        ParseTreeProperty<Scope> scopeMap = defVisitor.getScopeMap();
        ReferenceVisitor refVisitor = new ReferenceVisitor(scopeMap);
        refVisitor.visit(tree);
        if (errorOccured) return;
        ParseTreeProperty<Type> typeMap = refVisitor.getTypeMap();
        CodeGenerator cg = new CodeGenerator();
        CompileVisitor visitor = new CompileVisitor(scopeMap, typeMap, inputFileName);
        visitor.visit(tree);
    }

    public static void error(Token t, String msg) {
        errorOccured = true;
        System.err.printf("line %d:%d %s\n", t.getLine(), t.getCharPositionInLine(),
                msg);
    }

    public static void error(ParserRuleContext ctx, String msg) {
        error(ctx.getStart(), msg);
    }

}
