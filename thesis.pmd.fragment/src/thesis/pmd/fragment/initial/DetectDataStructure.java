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
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPostfixExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTRelationalExpression;
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
            	getWhileDetails(n);
            	return n.getBeginLine();
            } else if ( n instanceof ASTForStatement) {
            	/*
            	 *  This can either be a:
            	 *  for (Integer k : myList)
            	 *  or
            	 *  for (int i=1; i<n; i++)
            	 *  style of for loop.
            	 *  If it is the latter there should be a ForInit child of n.
            	 */
            	if (n.hasDescendantOfType(ASTForInit.class)) {
            		getForInitDetails((ASTForInit) n.getFirstChildOfType(ASTForInit.class));
            	} else {
            		getForStatementDetails(n);
            	}
//            	ASTExpression expression = (ASTExpression)n.jjtGetChild(0);
//            	System.out.println("Inside For statement on line: " + n.getBeginLine());
            	return n.getBeginLine();
            } else if (n instanceof ASTForInit) {
                /*
                 * init part is not technically inside the loop.
                 * Skip parent ASTForStatement but continue higher
                 * up to detect nested loops
                 * 
                 * This is a for of the: for(int i=1;i<n;i++) variety
                 * Want to delve into the Expression and update part
                 * to calculate the complexity the loop adds
                 */
//            	System.out.println("In ForInit, Line - " + n.getBeginLine());
            	getForInitDetails(n);
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
    
    /**
     * While statements are challenging. The increment amount isn't 
	 * specified in the expression itself. The body of the loop is
	 * stored in a statement node and most likely contains the 
	 * increment part. This part can occur multiple times however:
	 * 
	 * while (j < 1000) {
	 * 	if (j%6 == 2) {
	 * 		j += 16;
	 * 	} else {
	 * 		j++;
	 * 	}
	 * 	j+=7;
	 * }
	 * 
	 *  This is challenging to calculate the complexity of. That's before
	 *  considering non literal upper bounds and things like files:
	 *  
	 *  while (fileReader.readLine != null) { ... }
     * 
     * @param n - ASTWhileStatement node
     */
    private void getWhileDetails(Node n) {
		ASTRelationalExpression whileComp = (ASTRelationalExpression) 
				n.getFirstDescendantOfType(ASTRelationalExpression.class);
		if (whileComp == null) {
			System.err.println("An error occurred processing while loop");
			return;
		}
		String comp = whileComp.getImage();
		
	}

	/**
	 * We have a ForInit node. A child will give the starting 
	 * value of the loop variable.
	 * Going up to the parent gives the ForStatement node which
	 * we can then go into the children nodes:
	 * Expression - gives us the upper bound of the loop variable
	 * ForUpdate - gives us the rate the loop variable changes
	 * 
	 * This code makes the assumption that the loop variable is not
	 * incremented in the for statement itself as well. It also ignores
	 * any continue or break lines, analysing the loop as if it were to
	 * run from beginning to end.
     * 
     * @param n - ForInit node
     */
    private void getForInitDetails(Node n) {
    	ASTPrimaryPrefix ppInit = (ASTPrimaryPrefix) n.getFirstDescendantOfType(ASTPrimaryPrefix.class);
    	ASTForStatement afs = (ASTForStatement) n.getFirstParentOfType(ASTForStatement.class);
    	// 0 is ForInit, 1 is Expression, 2 is ForUpdate
    	ASTExpression ae = (ASTExpression) afs.jjtGetChild(1);
    	ASTForUpdate afu = (ASTForUpdate) afs.jjtGetChild(2);
    	// There are always 2 children of the relational expression, the 2nd contains the upper bound we're after
    	ASTRelationalExpression expComparator = (ASTRelationalExpression) ae.getFirstChildOfType(ASTRelationalExpression.class);
    	ASTPrimaryPrefix ppExp = (ASTPrimaryPrefix) expComparator.jjtGetChild(1).getFirstDescendantOfType(ASTPrimaryPrefix.class);
    	// The expression will either be a literal number or a variable representing a number
    	String upperBound = "";
    	if (ppExp.hasDescendantOfType(ASTLiteral.class)) {
    		upperBound = ppExp.getFirstDescendantOfType(ASTLiteral.class).getImage();
    	} else if (ppExp.hasDescendantOfType(ASTName.class)) {
    		upperBound = ppExp.getFirstDescendantOfType(ASTName.class).getImage();
    	}
    	ASTPostfixExpression updateExp = (ASTPostfixExpression) afu.getFirstDescendantOfType(ASTPostfixExpression.class);
    	String initial = "";
    	if (ppInit.hasDescendantOfType(ASTLiteral.class)) {
    		initial = ppInit.getFirstDescendantOfType(ASTLiteral.class).getImage();
    	} else if (ppInit.hasDescendantOfType(ASTName.class)) {
    		initial = ppInit.getFirstDescendantOfType(ASTName.class).getImage();
    	}
    	System.out.println("start: " + initial + " - end: " + upperBound + " - update: " + updateExp.getImage());
    }
    
    /**
     * We have a ForStatement node. There is a child that tells us the
	 * loop variable type and another child that gives us the structure
	 * being iterated through
	 * This is of the form: for (type i : ds<type>)  
	 * 
     * @param n - ASTForStatement node
     */
    private void getForStatementDetails(Node n) {
    	ASTExpression forExp = (ASTExpression) n.getFirstDescendantOfType(ASTExpression.class);
    	String bound = "";
    	if (forExp != null) {
    		bound = forExp.getFirstDescendantOfType(ASTName.class).getImage();
    	}
    	System.out.println("Structure being iterated through: " + bound);
    }
    
    
    public void execute(CurrentPath path) {
    	System.out.println("----");
    }
}