package thesis.pmd.fragment.initial;

import java.util.ArrayList;
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
	private ArrayList<DSUsage> usages;
	private Complexity finalComplexity;
	// Retrieve a reference to the complexities map for this variable type
	private HashMap<String, Complexity> complexitiesRef;
	
	public DSUsageContainer(String varName, String varType) {
		this.setVarName(varName);
		this.setVarType(varType);
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

	public void setVarName(String varName) {
		this.varName = varName;
	}
	
	public ArrayList<DSUsage> getUsages() {
		return new ArrayList<DSUsage>(this.usages);
	}
	
	public void addUsage(DSUsage dsu) {
		// Need to get the complexity of the usage
		dsu.setComplexity(complexitiesRef.get(dsu.getUsageType()));
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
		return this.getVarName() + " - Type = " + this.getVarType() + ls + 
				"\t Usages = " + this.usagesToString() + 
				"Total Complexity = :^) yet to be calculated";
	}
}
