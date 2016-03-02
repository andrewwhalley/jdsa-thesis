package thesis.pmd.fragment.initial;
// thesis.pmd.fragment.initial.DetectDataStructure
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.dfa.DataFlowNode;
import net.sourceforge.pmd.lang.dfa.VariableAccess;
import net.sourceforge.pmd.lang.dfa.pathfinder.CurrentPath;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTDoStatement;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTForInit;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTForUpdate;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DetectDataStructure extends AbstractJavaRule {
	
	private RuleContext rc;
	private ArrayList<DSUsageContainer> dataStructures;
	
	private int getIndexOfDS (String DSName) {
		int index = -1;
		if (dataStructures == null) {
			return index;
		}
		// Find index of ds related to DSName parameter
		for (int i = 0; i < dataStructures.size(); i++) {
			if (dataStructures.get(i).getVarName().equals(DSName)) {
				index = i;
				break;
			}
		}
		return index;
	}

    public Object visit(ASTVariableDeclaratorId node, Object data) {
    	if (dataStructures == null) {
    		dataStructures = new ArrayList<DSUsageContainer>();
    	}
    	VariableNameDeclaration varAnalyse = node.getNameDeclaration();
    	// List proof of concept
    	if ((!varAnalyse.getTypeImage().equals("List")) && (!varAnalyse.getTypeImage().equals("ArrayList"))
    			&& (!varAnalyse.getTypeImage().equals("LinkedList"))) {
    		return data;
    	}
    	int index = 0;
    	int loopLine;
    	String varName = varAnalyse.getImage();
    	String varDecType = varAnalyse.getTypeImage();
    	// Loop through all occurrences of the variable
        for (NameOccurrence occurrence : node.getUsages()) {
            loopLine = Integer.MIN_VALUE;
            // Check if the variable use is in a loop
            if ((loopLine = insideLoop(occurrence)) > -1) {
//        		System.out.println("Inside loop on line: " + loopLine);
        	}
            String usage = occurrence.getLocation().getImage();
            index = usage.lastIndexOf('.');
            // This means that no method has been invoked on the data structure.
            if (index == -1) {
            	// Check to see if it is a declaration or return value
            	if (getIndexOfDS(varName) == -1) {
            		// declaration
            		dataStructures.add(new DSUsageContainer(varName, getRuntimeType(occurrence)));
            	}
            	// Could be a function returning the ds.
            	
            	continue;
            }
            usage = usage.substring(index+1, usage.length());
            dataStructures.get(getIndexOfDS(varName)).addUsage(new DSUsage(usage, 
            		occurrence.getLocation().getBeginLine(),
            		occurrence.getLocation().getEndLine()));
        }
        System.out.println("--- Variables in use ---");
        System.out.println("--- Size of Array: " + dataStructures.size() + " ---");
        for (DSUsageContainer dsuc : dataStructures) {
        	System.out.println(dsuc.toString());
        }
        return data;
    }
    
    private String getRuntimeType(NameOccurrence occurrence) {
    	// NameOccurrence should be a primary prefix name
    	// Get the StatementExpression parent so that we can find the type declaration
    	ASTStatementExpression ase = occurrence.getLocation().
    			getFirstParentOfType(ASTStatementExpression.class);
    	Node avd = occurrence.getLocation().jjtGetParent();
    	if (avd instanceof ASTVariableDeclarator) {
    		System.out.println("We got a variable declarator");
    		ASTClassOrInterfaceType dsType2 = 
    				avd.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
    		System.out.println(dsType2 != null ? "Type: "+dsType2.getImage() : ":(");
    	}
    	// Check if the variable is a return value
    	if (ase == null) {
    		return "ReturnValue";
    	}
    	
    	// Get the type declaration of the variable
    	ASTClassOrInterfaceType dsType = ase.
    			getFirstDescendantOfType(ASTClassOrInterfaceType.class);
    	return dsType != null? dsType.getImage() : "";
    }
    
    private int insideLoop(NameOccurrence occurrence) {
        Node n = occurrence.getLocation().jjtGetParent();
        if (n == null) {
        	System.err.println("n is null");
        }
        while (n != null) {
            if (n instanceof ASTDoStatement) {
//            	System.out.println("Inside Do statement: " + n.getBeginLine());
                return n.getBeginLine();
            } else if (n instanceof ASTWhileStatement) {
//            	System.out.println("Inside While statement: " + n.getBeginLine());
            	return n.getBeginLine();
            } else if ( n instanceof ASTForStatement) {
            	// Get the expression in the for statement
            	// child nodes of interest: ForInit, Expression, ForUpdate
            	ASTForInit afi = (ASTForInit) n.getFirstChildOfType(ASTForInit.class);
            	ASTExpression ae = (ASTExpression) n.jjtGetChild(1);
            	ASTForUpdate afu = (ASTForUpdate) n.jjtGetChild(2);
            	
//            	ASTExpression expression = (ASTExpression)n.jjtGetChild(0);
//            	System.out.println("Inside For statement on line: " + n.getBeginLine());
            	return n.getBeginLine();
            } else if (n instanceof ASTForInit) {
                /*
                 * init part is not technically inside the loop.
                 * Skip parent ASTForStatement but continue higher
                 * up to detect nested loops
                 */
                n = n.jjtGetParent();
            } else if (n.jjtGetParent() instanceof ASTForStatement
                && n.jjtGetParent().jjtGetNumChildren() > 1
                && n == n.jjtGetParent().jjtGetChild(1)) {
                // it is the second child of a ForStatement - which means
                // we are dealing with a for-each construct
                // In that case, we can ignore this allocation expression, as the second child
                // is the expression, over which to iterate.
                // Skip this parent but continue higher up
                // to detect nested loops
                n = n.jjtGetParent();
            }
            n = n.jjtGetParent();
        }
        return -1;
    }
    
    
    public void execute(CurrentPath path) {
    	System.out.println("----");
    }
}