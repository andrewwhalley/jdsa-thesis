package thesis.pmd.fragment.initial;
// thesis.pmd.fragment.initial.DetectDataStructure
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.symboltable.ScopedNode;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.dfa.pathfinder.CurrentPath;
import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTDoStatement;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTForInit;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
//import net.sourceforge.pmd.lang.java.ast.ASTForUpdate;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
//import net.sourceforge.pmd.lang.java.ast.ASTPostfixExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTRelationalExpression;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.ASTVariableInitializer;
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetectDataStructure extends AbstractJavaRule {
	
	private ArrayList<DSUsageContainer> dataStructures;
	private ArrayList<DSUsageContainer> comparisonStructures;
	// Variable to store mappings between method names and the local and passed
	// data structure name/type: (MethodName -> (methodVarName, passedVarType))
	private HashMap<String, HashMap<String, String>> methodMaps;
	
	/**
	 * Find the data structure based off it's name
	 * in the array of data structures
	 * @param DSName
	 * @return
	 */
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
	
	/**
	 * Quick method that takes a name of a data structure and returns
	 * the type
	 * @param DSName - name of data structure
	 * @return Corresponding type of DS or "" if it's not initialised
	 */
	private String getVarType (String DSName) {
		if (dataStructures == null) {
			return "";
		}
		return dataStructures.get(getIndexOfDS(DSName)).getVarType();
	}
	
	/**
	 * Find the comparison data structure based off it's name
	 * and type in the array of comparison data structures
	 * @param DSName
	 * @return
	 */
	private int[] getIndexOfCompDS (String DSName, String compType) {
		if (compType.equals("")) {
			return null;
		}
		int maxSize = JavaCollectionsComplexities.DSToCompareTo(compType).length;
		int[] indices = new int[maxSize];
		if (comparisonStructures == null) {
			return null;
		}
		int currIndex = 0;
		for (int i = 0; i < comparisonStructures.size(); i++) {
			if (comparisonStructures.get(i).getVarName().equals(DSName)) {
				indices[currIndex++] = i;
			}
		}
		return indices;
	}
	
	/**
	 * Skip any data structure that has a name and type
	 * corresponding to the input parameters
	 * @param varName
	 * @param varType
	 * @return
	 */
	private boolean alreadyComparing(String varName, String varType) {
		for (DSUsageContainer dsuc : comparisonStructures) {
			if (dsuc.getVarName().equals(varName) && dsuc.getVarType().equals(varType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Visit is the main method that is called on this class.
	 * Starting at the root node, get all the variable declarators
	 * and explore them with the explore method.
	 */
	public Object visit(ASTCompilationUnit root, Object data) {
		// ASTCompilationUnit is the root node
		// Get all the ASTVariableDeclaratorId nodes
		List<ASTVariableDeclaratorId> varDecs = 
				root.findDescendantsOfType(ASTVariableDeclaratorId.class);
		if (varDecs == null) {
			// No variable declarators, return
			return data;
		}
		if (dataStructures == null) {
    		dataStructures = new ArrayList<DSUsageContainer>();
    	}
    	if (comparisonStructures == null) {
    		comparisonStructures = new ArrayList<DSUsageContainer>();
    	}
    	if (methodMaps == null) {
    		methodMaps = new HashMap<String, HashMap<String, String>>();
    	}
		// Visit each variable declarator node using explore method (original)
		for (ASTVariableDeclaratorId avdi : varDecs) {
			explore(avdi);
		}
		// Begin Comparison
        setupComparison();
        doComparison();
		return data;
	}

	/**
	 * Any Variable Declarator that is found in the code is explored
	 * through this method. The data for the variable declarator will be
	 * retrieved and added to a list for comparison later
	 */
    public void explore(ASTVariableDeclaratorId node) {
    	VariableNameDeclaration varAnalyse = node.getNameDeclaration();
    	// List proof of concept
    	if ((!varAnalyse.getTypeImage().equals("List")) && (!varAnalyse.getTypeImage().equals("ArrayList"))
    			&& (!varAnalyse.getTypeImage().equals("LinkedList"))) {
    		return;
    	}
    	int index = 0;
    	Complexity loopComplexity;
    	String varName = varAnalyse.getImage();
    	if (getIndexOfDS(varName) != -1) {
			// It is already in the ds list, move on to the next one
    		return;
    	}
    	// Need to check if 'node' has a right side declaration (in-line declaration)
    	// Need to check that node.jjtGetParent() isn't null first
    	Node n = node.jjtGetParent();
    	if (n != null) {
    		// Make sure it is a right side declaration
    		if (n.jjtGetNumChildren() > 1) {
		    	ASTVariableInitializer avi = n.getFirstChildOfType(ASTVariableInitializer.class);
		    	if (avi != null) {
		    		ASTClassOrInterfaceType coit = avi.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
		    		if (coit != null) {
		    			dataStructures.add(new DSUsageContainer(varName,
		    					coit.getImage(), getGenericsTypes(coit)));
		    		}
		     	}
    		}
    	}
    	// Loop through all occurrences of the variable
        for (NameOccurrence occurrence : node.getUsages()) {
            String usage = occurrence.getLocation().getImage();
            index = usage.lastIndexOf('.');
            // This means that no method has been invoked on the data structure.
            if (index == -1) {
            	// Check to see if it is a declaration
            	if (getIndexOfDS(varName) == -1) {
            		// declaration
            		// get the generics type(s) for the DS
            		dataStructures.add(new DSUsageContainer(varName, 
            				getRuntimeType(occurrence), 
            				getGenericsTypes(occurrence.getLocation())));
            		continue;
            	}
            	// Could be being passed to another method
            	String[] methodName = checkIfMethodCall(occurrence.getLocation(), varName);
            	if (methodName != null) {
            		addToMap(methodName, node, varName);
            	}
            	// Could be a function returning the ds.
            	
            	continue;
            }
            // Need to check if it is a first usage but it is not a declaration
            // This can occur if the DS is passed to another function
            if (index != -1 && getIndexOfDS(varName) == -1) {
            	// Need to get method name
            	ASTMethodDeclarator amd = node.getFirstParentOfType(ASTMethodDeclarator.class);
            	if (amd == null) {
            		// weird stuff
            		break;
            	}
            	String methodName = amd.getImage();
            	// then lookup method and varName in the hashMap to find variable type
            	HashMap<String, String> mappedVars = methodMaps.get(methodName);
            	if (mappedVars == null) {
            		// Weird stuff
            		break;
            	}
            	String varType = mappedVars.get(varName);
            	if (varType == null) {
            		// Weird stuff
            		break;
            	}
            	dataStructures.add(new DSUsageContainer(varName, varType, 
            			getGenericsTypes(occurrence.getLocation())));
            }
            // index is the '.' in myList.add() for example
            usage = usage.substring(index+1, usage.length());
            /* 
             * Need to get the node details for the Iterator that was created
             * Will get different complexity results for iterators generated
             * from different types
             */
        	int dsIndex = getIndexOfDS(varName);
            if (usage.equals("iterator")) {
            	handleIterator(occurrence, dsIndex, false);
            	continue;
            }
            /* 
             * Need to get the node details for the ListIterator that was created
             * Will get different complexity results for iterators generated
             * from different types
             */
            if (usage.equals("listIterator")) {
            	handleIterator(occurrence, dsIndex, true);
            	continue;
            }
            // Will be empty Complexity if the variable use is not in a loop
        	loopComplexity = insideLoop(occurrence, usage);
            DSUsage currentUsage = new DSUsage(usage,
            		occurrence.getLocation().getBeginLine(),
            		occurrence.getLocation().getEndLine());
            dataStructures.get(dsIndex).addUsage(currentUsage, loopComplexity);
        }
    }

    /**
     * The variable is a method parameter. We need to find the method
     * declaration node based off the method name being called so that
     * we can find the name of the variable 
     * @param methodName - String array of size 2 with method name and 
     * parameter number for the variable as entries
     * @param node - The variable declarator node that is being analysed
     * @param varName - The name of the variable being passed to the method
     * (So that we can get the data structure type that's being passed)
     */
    private void addToMap(String[] methodName, ASTVariableDeclaratorId node,
    		String varName) {
    	// Is actually a method parameter
		// Get root node to find all method declarators
		ASTCompilationUnit rootNode = node.getFirstParentOfType(ASTCompilationUnit.class);
		List<ASTMethodDeclarator> methods = rootNode.findDescendantsOfType(ASTMethodDeclarator.class);
		for (ASTMethodDeclarator amd : methods) {
			if (amd.getImage().equals(methodName[0])) {
				// get the name of the parameter at position methodName[1] (safe parse, is always an int)
				// children will be FormalParameters and then FormalParameter
				Node parameters = amd.jjtGetChild(0).jjtGetChild(Integer.parseInt(methodName[1]));
				if (parameters == null) {
					// something weird happened
					break;
				}
				ASTVariableDeclaratorId avdi = parameters.getFirstDescendantOfType(ASTVariableDeclaratorId.class);
				if (avdi == null) {
					// something weird happened
					break;
				}
				String varType = getVarType(varName);
				HashMap<String, String> getExisting = methodMaps.get(methodName[0]);
				if (getExisting == null) {
					// Put the values into the hashmap
					methodMaps.put(methodName[0], new HashMap<String, String>(){
						/**
						 * Add generated serial UID to this HashMap
						 */
						private static final long serialVersionUID = -610092311225845114L;

					{
						put(avdi.getImage(), varType);
					}});
				} else {
					getExisting.put(avdi.getImage(), varType);
					// This step may be redundant (non-mutable)
					methodMaps.put(methodName[0], getExisting);
				}
				break;
			}
		}
		
	}

	/**
     * Checks if there is a method call node with this variable as a parameter
     * @param location
     */
	private String[] checkIfMethodCall(ScopedNode node, String varName) {
		if (node == null) {
			return null;
		}
		ASTArgumentList argumentParents = node.getFirstParentOfType(ASTArgumentList.class);
		if (argumentParents == null) { 
			return null;
		}
		// If we're here then it's a method. Get the name of the method
		ASTPrimaryExpression ape = argumentParents.getFirstParentOfType(ASTPrimaryExpression.class);
		if (ape == null) {
			// Something weird has happened
			return null;
		}
		ASTName methodName = ape.getFirstDescendantOfType(ASTName.class);
		if (methodName == null) {
			return null;
		}
		// Get the number of the parameter that we have 
		List<ASTName> methodParams = argumentParents.findDescendantsOfType(ASTName.class);
		int paramNumber = 0;
		for (int i = 0; i < methodParams.size(); i++) {
			if (methodParams.get(i).getImage().equals(varName)) {
				// found it
				paramNumber = i;
				break;
			}
		}
		return new String[] {methodName.getImage(), "" + paramNumber};
	}

	/**
     * 
     * @param occurrence
     * @param varType
     */
    private void handleIterator(NameOccurrence occurrence, int dsIndex, Boolean isListIterator) {
		// Need to get the name of the Iterator variable (Actually don't care about this)
    	// Need to get the node for the Iterator variable
    	ASTLocalVariableDeclaration lvd = occurrence.getLocation().
    			getFirstParentOfType(ASTLocalVariableDeclaration.class);
    	if (lvd == null) {
    		// Something went wrong
    		return;
    	}
    	ASTVariableDeclaratorId avdi = (ASTVariableDeclaratorId) lvd.jjtGetChild(lvd.jjtGetNumChildren()-1).jjtGetChild(0);
    	if (avdi == null) {
    		// Something went wrong
    		return;
    	}
    	// Get the usages from the Iterator node (needs it's own visit implementation)
    	iteratorVisit(avdi, dsIndex, isListIterator);
	}

	/**
	 * Just like the visit method, but implemented only for Iterator and ListIterator types
	 * @param avdi
	 * @param dsIndex
	 * @param isListIterator
	 */
    private void iteratorVisit(ASTVariableDeclaratorId avdi, int dsIndex, Boolean isListIterator) {
    	DSUsageContainer dsuc = dataStructures.get(dsIndex);
    	// Calculate Iterator complexity from the usages and varType
		for (NameOccurrence no : avdi.getUsages()) {
			ScopedNode node = no.getLocation();
	        String usage = node.getImage();
	        int index = usage.lastIndexOf('.');
	        if (index == -1) {
	        	// something odd, skip this one
	        	continue;
	        }
	        if (isListIterator) {
	        	// Stored in the complexities hashMap as ListIterator.methodName
	    		usage = "ListIterator." + usage.substring(index+1, usage.length());
	    	} else {
	    		// Stored in the complexities hashMap as Iterator.methodName
	    		usage = "Iterator." + usage.substring(index+1, usage.length());
	    	}
	        Complexity loopComplexity = insideLoop(no, usage);
	        // Store the complexities with the other complexities for the original variable
	        dsuc.addUsage(new DSUsage(usage, node.getBeginLine(), 
	        		node.getEndLine()), loopComplexity);
		}
	}

	/**
	 * Retrieve the generics types for the data structure. E.g.
	 * ArrayList<String>
	 * Would return a list containing the entry: String
	 * because String is the generics type for the List being analysed
	 * @param coi - The node representing the data structure
	 * @return List containing generics types for the passed in node
	 */
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
    
	/**
	 * Get the runtime type of the variable. This is necessary for variables
	 * declared as an interface type. For example: 
	 * List<String> var = new ArrayList<String>();
	 * Will get that var is an ArrayList.
	 * @param occurrence - the NameOccurrence of the variable being analysed
	 * @return The runtime type (As a String) or an empty String
	 */
    private String getRuntimeType(NameOccurrence occurrence) {
    	// NameOccurrence should be a primary prefix name
    	// Get the StatementExpression parent so that we can find the type declaration
    	ASTStatementExpression ase = occurrence.getLocation().
    			getFirstParentOfType(ASTStatementExpression.class);
    	Node avd = occurrence.getLocation().jjtGetParent();
    	if (avd instanceof ASTVariableDeclarator) {
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
       
    /**
     * Base method for determining if the usage is inside a loop
     * and what sort of loop it is. Handle this in the appropriate
     * methods
     * 
     * @param occurrence - the usage details (get node using getLocation)
     * @param usage - the string repreenting the usage
     * @return
     */
    private Complexity insideLoop(NameOccurrence occurrence, String usage) {
        //Node n = occurrence.getLocation().jjtGetParent();
    	Complexity overallLoopComplexity = new Complexity();
    	Node usageNode = occurrence.getLocation();
        List<ASTDoStatement> doParents = usageNode.getParentsOfType(ASTDoStatement.class);
        List<ASTWhileStatement> whileParents = usageNode.getParentsOfType(ASTWhileStatement.class);
        List<ASTForStatement> forParents = usageNode.getParentsOfType(ASTForStatement.class);
        for (ASTDoStatement n : doParents) {
        	// Need to deal with Do statements later (will likely be same as while statement) 
        	overallLoopComplexity.multiply(getDoDetails(n, usage));
        }
        for (ASTWhileStatement n : whileParents) {
        	overallLoopComplexity.multiply(getWhileDetails(n, usage));
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
        		overallLoopComplexity.multiply(getForInitDetails((ASTForInit) 
        				n.getFirstChildOfType(ASTForInit.class), occurrence.getImage(), usage));
        	} else {
        		overallLoopComplexity.multiply(getForStatementDetails(n));
        	}
        }
        return overallLoopComplexity;
    }
    
    /**
     * Get the complexity of the Do loop
     * @param n - the Do node
     * @param usage - The name of the variable being analysed
     * @return Complexity associated with this Do statement
     */
    private Complexity getDoDetails(Node n, String usage) {
    	return new Complexity(new Polynomial(0));
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
     * @param usage 
     */
    private Complexity getWhileDetails(Node n, String usage) {
    	// Loss of precision - Will generalise loops to O(n), since O(logn)
    	// cannot be calculated in a simple manner
    	List<ASTRelationalExpression> comparisons = 
    			n.findDescendantsOfType(ASTRelationalExpression.class);
    	
		if (comparisons.isEmpty()) {
			// Check if it is something like while (iterator.hasNext())
			ASTName loopVar = n.getFirstDescendantOfType(ASTName.class);
			if (loopVar != null) {
				// This is automatically O(n)
				return new Complexity(new Polynomial(1));
			}
			System.err.println("An error occurred processing while loop");
		}
    	for (ASTRelationalExpression whileComp : comparisons) {
	//		String comp = whileComp.getImage();
			// Right hand side of expression is 2nd child
			ASTPrimaryExpression ape = (ASTPrimaryExpression) whileComp.jjtGetChild(1);
			if (ape == null) {
				// Check next one
				continue;
			}
			// Need to check the RHS value. If it is an ASTLiteral it is O(C) = O(1)
			// If it is an ASTName it is O(n) - default for now
			if (ape.hasDescendantOfType(ASTName.class)) {
				return new Complexity(new Polynomial(1));
			}
    	}
    	// None left to check, make it constant
		return new Complexity(new Polynomial(0));
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
	 * @param usage 
     */
    private Complexity getForInitDetails(Node n, String varName, String usage) {
    	Boolean isVar = false;
//    	ASTPrimaryPrefix ppInit = (ASTPrimaryPrefix) n.getFirstDescendantOfType(ASTPrimaryPrefix.class);
    	ASTForStatement afs = (ASTForStatement) n.getFirstParentOfType(ASTForStatement.class);
    	// 0 is ForInit, 1 is Expression, 2 is ForUpdate
    	ASTExpression ae = (ASTExpression) afs.jjtGetChild(1);
//    	ASTForUpdate afu = (ASTForUpdate) afs.jjtGetChild(2);
    	// There are always 2 children of the relational expression, the 2nd contains the upper bound we're after
    	ASTRelationalExpression expComparator = (ASTRelationalExpression) ae.getFirstChildOfType(ASTRelationalExpression.class);
    	ASTPrimaryPrefix ppExp = (ASTPrimaryPrefix) expComparator.jjtGetChild(1).getFirstDescendantOfType(ASTPrimaryPrefix.class);
    	// The expression will either be a literal number or a variable representing a number
    	String upperBound = "";
    	if (ppExp.hasDescendantOfType(ASTLiteral.class)) {
    		upperBound = ppExp.getFirstDescendantOfType(ASTLiteral.class).getImage();
    	} else if (ppExp.hasDescendantOfType(ASTName.class)) {
    		isVar = true;
    		upperBound = ppExp.getFirstDescendantOfType(ASTName.class).getImage();
    	}
    	// This means the usage we're analysing is in the loop declaration
    	// and hence it's not actually inside the loop - so no loop complexity.
    	if (upperBound.equals(varName + "." + usage)) {
    		return new Complexity();
    	}
//    	ASTPostfixExpression updateExp = (ASTPostfixExpression) afu.getFirstDescendantOfType(ASTPostfixExpression.class);
//    	String initial = "";
//    	if (ppInit.hasDescendantOfType(ASTLiteral.class)) {
//    		initial = ppInit.getFirstDescendantOfType(ASTLiteral.class).getImage();
//    	} else if (ppInit.hasDescendantOfType(ASTName.class)) {
//    		initial = ppInit.getFirstDescendantOfType(ASTName.class).getImage();
//    	}
    	// TODO: Apply initial and updateExp analysis to more accurately determine the loop complexity 
    	// check if upperbound is a digit (the first value is all we need to check. 
    	// If it is a digit this loop is constant time, otherwise it is O(m)
    	return isVar ? new Complexity(new Polynomial(1)) : new Complexity(new Polynomial(0)); 
    }
    
    /**
     * We have a ForStatement node. There is a child that tells us the
	 * loop variable type and another child that gives us the structure
	 * being iterated through
	 * This is of the form: for (type i : ds<type>)  
	 * 
     * @param n - ASTForStatement node
     * @param  
     */
    private Complexity getForStatementDetails(Node n) {
//    	ASTExpression forExp = (ASTExpression) n.getFirstDescendantOfType(ASTExpression.class);
//    	String bound = "";
//    	if (forExp != null) {
//    		bound = forExp.getFirstDescendantOfType(ASTName.class).getImage();
//    	}
    	// This should be automatically O(n)
    	return new Complexity(new Polynomial(1));
    }
    
    /**
     * Create the data structure for comparison using the list of 
     * data structures generated from the explore method.
     */
    private void setupComparison() {
    	ArrayList<DSUsage> currCompUsages = null;
        for (int i = 0; i < dataStructures.size(); i++) {
        	DSUsageContainer dsuc = dataStructures.get(i);
        	// Ignore and remove data structures with no usages
        	if (dsuc.getUsages().isEmpty()) {
        		// Decrement after removal so we don't skip one
        		dataStructures.remove(i--);
        		continue;
        	}
        	dsuc.finalComplexityCalc();
//        	System.out.println(dsuc.toString());
        	// Get the Data Structure(s) we can compare each usage against
        	for (String s : JavaCollectionsComplexities.DSToCompareTo(dsuc.getVarType())) {
        		System.out.println("Comparing DS: " + s);
        		// Don't add to the comparison structure if it's already comparing the same name and type
        		if (alreadyComparing(dsuc.getVarName(), s)) {
        			continue;
        		}
        		// Name the variable in the comparison structure the same name as the original variable
        		// This will help to setup the comparisons
        		comparisonStructures.add(new DSUsageContainer(dsuc.getVarName(), s, dsuc.getGenTypes()));
        	}
        	// Do the comparison
        	for (DSUsageContainer mapped : comparisonStructures) {
        		// Skip this DSUsageContainer if it has previously been mapped
        		if (!mapped.getUsages().isEmpty()) {
        			continue;
        		}
        		// Need to get the related complexities
        		currCompUsages = new ArrayList<DSUsage>(dsuc.getUsages());
        		for (DSUsage dsu : currCompUsages) {
        			mapped.addUsage(new DSUsage(dsu), new Complexity(dsu.getLoopComplexity()));
        		}
        		mapped.finalComplexityCalc();
        	}
        }
    }
    
    /**
     * Method that compares the two data structure lists that have been
     * built up (actual data structures vs possible data structures)
     */
    private void doComparison() {
    	System.out.println("----------------");
    	System.out.println("Comparison Stage");
    	System.out.println("----------------");
    	System.out.println("Original Data Structures");
    	System.out.println(dataStructures.toString());
    	System.out.println("----------------");
    	System.out.println("Generated Data Structures");
    	System.out.println(comparisonStructures.toString());
    	System.out.println("----------------");
    	// Simple comparison of final complexity:
    	for (DSUsageContainer dsuc : dataStructures) {
    		// get valid comparison structure (same name)
    		int[] indices = getIndexOfCompDS(dsuc.getVarName(), dsuc.getVarType());
    		// skip if it's an invalid comparison
    		if (indices == null) {
    			continue;
    		}
    		for (int i = 0; i < indices.length; i++) {
    			Complexity dsComplexity = dsuc.getFinalComplexity();
    			DSUsageContainer compDSUC = comparisonStructures.get(indices[i]);
    			Complexity compComplexity = compDSUC.getFinalComplexity();
    			// Complexities are the same
    			if (dsComplexity.compareTo(compComplexity) == 0) {
    				// Do additional analysis on the DS's as a final complexity isn't good enough
    				System.out.println("Final Complexity Analysis is same. Beginning secondary analysis");
    				// Sort complexities
    				ArrayList<Complexity> sortedComps = dsuc.sortComplexities();
    				ArrayList<Complexity> sortedGenComps = compDSUC.sortComplexities();
    				System.out.println("Sorted Complexities for in use data structure");
    				System.out.println(sortedComps.toString());
    				System.out.println("Sorted Complexities for generated data structure");
    				System.out.println(sortedGenComps.toString());
    				// Compare these two lists
    				int compVal = 0;
    				int index = sortedComps.size() - 1;
    				// Loop through the complexities in reverse order
    				// Break if the complexities are different or we have gone through them all
    				while (compVal == 0) {
    					compVal = sortedComps.get(index).compareTo(sortedGenComps.get(index));
    					if (--index < 0) {
    						break;
    					}
    				}
    				switch (compVal) {
    				case -1:
    					// Complexity in original list is smaller than generated
    					System.out.println("The data structure in use is likely the more efficient.");
    					System.out.println("The JDSA program recommends the use of: " + dsuc.getVarType());
    					break;
    				case 0:
    					// Complexities are the same
    					System.out.println("The data structure usages are exactly equal. Therefore the"
    							+ " JDSA program cannot make a recommendation for this scenario.");
    					break;
    				case 1:
    					// Complexity in original list is larger than generated
    					System.out.println("The generated data structure may be more efficient for this scenario");
    					System.out.println("The JDSA program recommends the use of: " + compDSUC.getVarType());
    					break;
    				}
//    				System.out.println("Complexity the same for: " + dsuc.getVarName() +
//    						" of types: " + dsuc.getVarType() + " - " + compDSUC.getVarType());
    			}
    			if (dsComplexity.compareTo(compComplexity) < 0) {
    				System.out.println("Complexity is better for: " + dsuc.getVarType() +
    						" than: " + compDSUC.getVarType() + " - Variable: " + dsuc.getVarName());
    				System.out.println("The JDSA program recommends the use of: " + dsuc.getVarType());
    			}
    			if (dsComplexity.compareTo(compComplexity) > 0) {
    				System.out.println("Complexity is worse for: " + dsuc.getVarType() +
    						" than: " + compDSUC.getVarType() + " - Variable: " + dsuc.getVarName());
    				System.out.println("The JDSA program recommends the use of: " + compDSUC.getVarType());
    			}
    			System.out.println("----------------");
    		}
    	}
    	
    	// Clear the data structure lists so next comparison is fresh
    	dataStructures.clear();
    	comparisonStructures.clear();
    	methodMaps.clear();
    }
    
    /**
     * From super. Probably isn't necessary
     * @param path
     */
    public void execute(CurrentPath path) {
    	System.out.println("----");
    }
}