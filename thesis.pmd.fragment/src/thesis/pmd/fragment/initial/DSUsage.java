package thesis.pmd.fragment.initial;

/**
 * DSUsage - A Class that is used to store the information about an
 * individual call made on a data structure. This is gained through
 * the use of PMD.
 * 
 * @author andrewwhalley
 *
 */
public class DSUsage {
	private String usageType;
	private int beginLine;
	private int endLine;
	private Complexity complexity;
	private Complexity calcComplexity;
	
	public DSUsage (String usage, int beginLine, int endLine) {
		setUsageType(usage);
		setBeginLine(beginLine);
		setEndLine(endLine);
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public int getBeginLine() {
		return beginLine;
	}

	public void setBeginLine(int beginLine) {
		this.beginLine = beginLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public Complexity getComplexity() {
		return this.complexity;
	}

	public void setComplexity(Complexity comp) {
		this.complexity = comp;
	}
	
	public Complexity getCalcComplexity() {
		return this.calcComplexity;
	}
	
	public void setCalcComplexity() {
		
	}
	
	public String toString() {
		String compString = this.getComplexity() == null ? "-" : 
			this.getComplexity().toString();
		String calcCompString = this.getCalcComplexity() == null ? "-" : 
			this.getCalcComplexity().toString();
		return this.getUsageType() + 
				", " + compString +
				", " + this.getBeginLine() + 
				", " + this.getEndLine() + 
				", " + calcCompString;
	}
}
