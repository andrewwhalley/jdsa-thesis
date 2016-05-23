package thesis.pmd.fragment.initial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A container class to store all of the details of a
 * data structure in the code. This includes each usage as
 * defined by the DSUsage class.
 *  
 * @author andrewwhalley
 *
 */
public class DSUsageContainer {
	private final String ls = System.getProperty("line.separator");
	private String varName;
	private String varType;
	private ArrayList<String> varGenerics;
	private ArrayList<DSUsage> usages;
	private Complexity finalComplexity;
	// Retrieve a reference to the complexities map for this variable type
	private HashMap<String, Complexity> complexitiesRef;
	
	public DSUsageContainer(String varName, String varType, ArrayList<String> varGenerics) {
		this.setVarName(varName);
		this.setVarType(varType);
		this.varGenerics = new ArrayList<String>(varGenerics);
		this.finalComplexity = new Complexity();
		// Ensure ArrayList of usages is not null
		this.usages = new ArrayList<DSUsage>();
		complexitiesRef = JavaCollectionsComplexities.DSDetermination(varType);
		// Check if error has occurred
		if (complexitiesRef == null) {
			System.out.println("Error has occurred. Cannot process this " + 
					"Variable: " + varName + " of type: " + varType);
		}
		
	}
	
	private void setVarType(String varType) {
		this.varType = varType;
	}
	
	public String getVarType() {
		return this.varType;
	}

	public String getVarName() {
		return varName;
	}
	
	public ArrayList<String> getGenTypes() {
		return this.varGenerics;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}
	
	public ArrayList<DSUsage> getUsages() {
		return new ArrayList<DSUsage>(this.usages);
	}
	
	public void addUsage(DSUsage dsu, Complexity loopComplexity) {
		// Need to get the complexity of the usage
		dsu.setComplexity(new Complexity(complexitiesRef.get(dsu.getUsageType())));
		dsu.setLoopComplexity(loopComplexity);
		dsu.setCalcComplexity();
		this.usages.add(dsu);
	}
	
	public String usagesToString() {
		StringBuilder sb = new StringBuilder();
		for (DSUsage dsu : this.getUsages()) {
			if (this.getUsages().indexOf(dsu) == 0) {
				sb.append(dsu.toString());
				sb.append(ls);
				continue;
			}
			sb.append("\t\t");
			sb.append(dsu.toString());
			sb.append(ls);
		}
		return sb.toString();
	}
	
	public String toString() {
		String generics = "";
		for (int i=0; i<varGenerics.size(); i++) {
			generics += varGenerics.get(i);
			if (i+1 < varGenerics.size()) {
				generics += ", ";
			}
		}
		return this.getVarName() + " - Type = " + this.getVarType() + 
				"<" + generics + ">" + ls + 
				"\t Usages = " + this.usagesToString() + ls +
				"\t Total Complexity = " + getFinalComplexity();
	}
	
	public void finalComplexityCalc() {
		for (DSUsage dsu : usages) {
			this.finalComplexity.add(dsu.getCalcComplexity());
		}
	}
	
	public ArrayList<Complexity> sortComplexities() {
		ArrayList<Complexity> comps = new ArrayList<Complexity>();
		for (DSUsage dsuc : usages) {
			comps.add(new Complexity(dsuc.getCalcComplexity()));
		}
		Collections.sort(comps);
		return comps;
	}
	
	public Complexity[] countCalcComplexity() {
		// Create it assuming each complexity is of different order
		Complexity[] calcComps = new Complexity[usages.size()];
		
		return calcComps;
	}
	
	public Complexity getFinalComplexity() {
		return this.finalComplexity;
	}
	
	public int compareUsages(DSUsageContainer mapping) {
		return 0;
	}
}
