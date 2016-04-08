package thesis.pmd.fragment.initial;
// thesis.pmd.fragment.initial.DetectDataStructure
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.symboltable.ScopedNode;
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
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
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
	private ArrayList<DSUsageContainer> comparisonStructures;
	
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
    	if (comparisonStructures == null) {
    		comparisonStructures = new ArrayList<DSUsageContainer>();
    	}
    	VariableNameDeclaration varAnalyse = node.getNameDeclaration();
    	// List proof of concept
    	if ((!varAnalyse.getTypeImage().equals("List")) && (!varAnalyse.getTypeImage().equals("ArrayList"))
    			&& (!varAnalyse.getTypeImage().equals("LinkedList"))) {
    		return data;
    	}
    	int index = 0;
    	Complexity loopComplexity;
    	String varName = varAnalyse.getImage();
    	String varDecType = varAnalyse.getTypeImage();
    	// Loop through all occurrences of the variable
        for (NameOccurrence occurrence : node.getUsages()) {
            // Will be null if the variable use is not in a loop
        	loopComplexity = insideLoop(occurrence);
//        		System.out.println("Inside loop on line: " + loopLine);
            String usage = occurrence.getLocation().getImage();
            index = usage.lastIndexOf('.');
            // This means that no method has been invoked on the data structure.
            if (index == -1) {
            	// Check to see if it is a declaration or return value
            	if (getIndexOfDS(varName) == -1) {
            		// declaration
            		// get the generics type(s) for the DS
            		dataStructures.add(new DSUsageContainer(varName, 
            				getRuntimeType(occurrence), 
            				getGenericsTypes(occurrence.getLocation())));
            	}
            	// Could be a function returning the ds.
            	
            	continue;
            }
            // Need to check if it is a first usage but it is not a declaration
            // This can occur if the DS is passed to another function
            if (index != -1 && getIndexOfDS(varName) == -1) {
            	// In runtime type need to match up with varName. Maybe create a new method all together to deal
            	// with this specific situation... 
            	ArrayList<String> types = getMethodParamType(occurrence, varName);
            	if (types == null || types.isEmpty()) {
            		// Something went wrong :|
            		break;
            	}
            	String runtimeType = types.remove(0);
            	dataStructures.add(new DSUsageContainer(varName, runtimeType,
            			types));
            }
            usage = usage.substring(index+1, usage.length());
            DSUsage currentUsage = new DSUsage(usage,
            		occurrence.getLocation().getBeginLine(),
            		occurrence.getLocation().getEndLine());
            dataStructures.get(getIndexOfDS(varName)).addUsage(currentUsage, loopComplexity);
        }
        System.out.println("--- Variables in use ---");
        System.out.println("--- Size of Array: " + dataStructures.size() + " ---");
        for (DSUsageContainer dsuc : dataStructures) {
        	dsuc.finalComplexityCalc();
        	System.out.println(dsuc.toString());
        	// Get the Data Structure(s) we can compare each usage against
        	for (String s : JavaCollectionsComplexities.DSToCompareTo(dsuc.getVarType())) {
        		System.out.println("Comparing DS: " + s);
        		comparisonStructures.add(new DSUsageContainer("compStructure", s, dsuc.getGenTypes()));
        	}
        	// Do the comparison
        	for (DSUsageContainer mapped : comparisonStructures) {
//        		mapped.addUsage();
        	}
        }
        
        return data;
    }
    
    private ArrayList<String> getGenericsTypes(ScopedNode coi) {
    	List<ASTClassOrInterfaceType> gens = coi.
				findDescendantsOfType(ASTClassOrInterfaceType.class);
		ArrayList<String> genNames = new ArrayList<String>();
		if (gens != null) {
			for (ASTClassOrInterfaceType coit : gens) {
				genNames.add(coit.getImage());
			}
		}
		return genNames;
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
    	
    	if (ase == null) {
    		// I don't know if this will occur anymore
    	}
    	
    	// Get the type declaration of the variable
    	ASTClassOrInterfaceType dsType = ase.
    			getFirstDescendantOfType(ASTClassOrInterfaceType.class);
    	return dsType != null? dsType.getImage() : "";
    }
    
    private ArrayList<String> getMethodParamType(NameOccurrence occurrence, String varName) {
    	// Check the method parameters for a compatible class/interface
		ASTMethodDeclaration amd = occurrence.getLocation().
				getFirstParentOfType(ASTMethodDeclaration.class);
		if (amd == null) {
			// The variable must be a return value
    		return null;
		}
		ASTFormalParameters params = amd.getFirstDescendantOfType(ASTFormalParameters.class);
		if (params == null) {
			return null;
		}
		List<ASTClassOrInterfaceType> coiParams = params.findDescendantsOfType(ASTClassOrInterfaceType.class);
		if (coiParams.isEmpty()) {
			return null;
		}
		ArrayList<String> type = new ArrayList<String>();
		String currType = "";
		// Need to check for lists/sets/maps/queues 
		for (int i=0; i<coiParams.size(); i++) {
			currType = coiParams.get(i).getImage();
			ASTFormalParameter afp = coiParams.get(i).getFirstParentOfType(ASTFormalParameter.class);
			if (afp == null) {
				// this shouldn't occur
				continue;
			}
			ASTVariableDeclaratorId avdi = afp.getFirstChildOfType(ASTVariableDeclaratorId.class);
			if (avdi == null) {
				// This shouldn't occur either
				continue;
			}
			String currVarName = avdi.getImage();
			// Check for List implementations
			if ((currType.equals("LinkedList") || currType.equals("ArrayList")) && currVarName.equals(varName)) {
				type.add(currType);
				// Get generics for the LL or AL
				break;
			}
			// Check for List interface
			if (currType.equals("List") && currVarName.equals(varName)) {
				// We need to determine where this was called from. This is harder.
				// Get the generics type that the List is using
				// All of the ASTClassOrInterface children for 
				break;
			}
		}
    	return type;
    }
    
    private Complexity insideLoop(NameOccurrence occurrence) {
        //Node n = occurrence.getLocation().jjtGetParent();
    	Complexity overallLoopComplexity = new Complexity();
        List<ASTDoStatement> doParents = occurrence.getLocation().getParentsOfType(ASTDoStatement.class);
        List<ASTWhileStatement> whileParents = occurrence.getLocation().getParentsOfType(ASTWhileStatement.class);
        List<ASTForStatement> forParents = occurrence.getLocation().getParentsOfType(ASTForStatement.class);
        for (ASTDoStatement n : doParents) {
        	// Need to deal with Do statements later 
        }
        for (ASTWhileStatement n : whileParents) {
        	overallLoopComplexity.add(getWhileDetails(n));
        }
        for (ASTForStatement n : forParents) {
        	/*
        	 *  This can either be a:
        	 *  for (Integer k : myList)
        	 *  or
        	 *  for (int i=1; i<n; i++)
        	 *  style of for loop.
        	 *  If it is the latter there should be a ForInit child of n.
        	 */
        	if (n.hasDescendantOfType(ASTForInit.class)) {
        		overallLoopComplexity.add(getForInitDetails((ASTForInit) n.getFirstChildOfType(ASTForInit.class)));
        	} else {
        		overallLoopComplexity.add(getForStatementDetails(n));
        	}
        }
        return overallLoopComplexity;
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
    private Complexity getWhileDetails(Node n) {
		ASTRelationalExpression whileComp = (ASTRelationalExpression) 
				n.getFirstDescendantOfType(ASTRelationalExpression.class);
		if (whileComp == null) {
			System.err.println("An error occurred processing while loop");
			// Just make it constant
			return new Complexity(new Polynomial(0));
		}
		String comp = whileComp.getImage();
		// Need to check the loop value. If it is a number it is O(C) = O(1)
		// If it is a variable it is O(n) - default for now
		return new Complexity();
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
    private Complexity getForInitDetails(Node n) {
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
    	// check if upperbound is a digit (the first value is all we need to check. 
    	// If it is a digit this loop is constant time, otherwise it is O(m)
    	return Character.digit(upperBound.charAt(0),10) < 0 ? new Complexity(new Polynomial(1)) : new Complexity(new Polynomial(0)); 
    }
    
    /**
     * We have a ForStatement node. There is a child that tells us the
	 * loop variable type and another child that gives us the structure
	 * being iterated through
	 * This is of the form: for (type i : ds<type>)  
	 * 
     * @param n - ASTForStatement node
     */
    private Complexity getForStatementDetails(Node n) {
    	ASTExpression forExp = (ASTExpression) n.getFirstDescendantOfType(ASTExpression.class);
    	String bound = "";
    	if (forExp != null) {
    		bound = forExp.getFirstDescendantOfType(ASTName.class).getImage();
    	}
    	System.out.println("Structure being iterated through: " + bound);
    	// This should be automatically O(m)
    	return new Complexity(new Polynomial(1));
    }
    
    
    public void execute(CurrentPath path) {
    	System.out.println("----");
    }
}