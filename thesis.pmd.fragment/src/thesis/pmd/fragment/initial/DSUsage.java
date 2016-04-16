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
	private Complexity loopComplexity;
	private Complexity calcComplexity;
	
	public DSUsage (String usage, int beginLine, int endLine) {
		setUsageType(usage);
		setBeginLine(beginLine);
		setEndLine(endLine);
		this.complexity = new Complexity();
		this.calcComplexity = new Complexity();
	}
	
	public DSUsage(DSUsage dsu) {
		setUsageType(dsu.getUsageType());
		setBeginLine(dsu.getBeginLine());
		setEndLine(dsu.getEndLine());
		this.complexity = new Complexity();
		this.calcComplexity = new Complexity();
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
		this.complexity = new Complexity(comp);
	}
	
	public Complexity getCalcComplexity() {
		return this.calcComplexity;
	}
	
	public Complexity getLoopComplexity() {
		return this.loopComplexity;
	}
	
	public void setCalcComplexity(Complexity C) {
		this.loopComplexity = new Complexity(C);
		// Get around immutability of Complexity
		Complexity tempComp = new Complexity(this.complexity);
		tempComp.multiply(this.loopComplexity);
		this.calcComplexity.multiply(tempComp);
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
				", " + getLoopComplexity() +
				", " + calcCompString;
	}
}
