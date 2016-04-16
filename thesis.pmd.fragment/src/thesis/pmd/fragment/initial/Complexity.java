package thesis.pmd.fragment.initial;
import java.util.*;
import java.lang.*;


public class Complexity {
	private String loopComp; // Variable for the loop complexity
    private Polynomial polyPart;
    private Logarithm logPart;
    private Exponential expPart;
    private Factorial factPart;
    
    /**
     * Instantiate a new empty complexity. Useful for 
     * loop complexity determination
     */
    public Complexity() {
    }
    
    /**
     * Need this because Complexity is immutable
     * @param C
     */
    public Complexity(Complexity C) {
    	if (C == null) {
    		return;
    	}
    	this.polyPart = C.polyPart;
    	this.expPart = C.expPart;
    	this.logPart = C.logPart;
    }
   
    public Complexity(Exponential e, Polynomial p, Logarithm l) {
        this.polyPart = p;
        this.logPart = l;
        this.expPart = e;
    }
    
    public Complexity(Exponential e) {
    	this.expPart = e;
    }
    
    public Complexity(Polynomial p) {
    	this.polyPart = p;
    }
    
    public Complexity(Logarithm l) {
    	this.logPart = l;
    }
    
    public Complexity(Factorial f) {
    	this.factPart = f;
    }
   
    public Polynomial getPoly() {
        return polyPart;
    }
   
    public Logarithm getLog() {
        return logPart;
    }
   
    public Exponential getExp() {
        return expPart;
    }
    
    public Factorial getFact() {
    	return factPart;
    }
    
    public void add(Complexity c) {
    	// Cover the polynomial part
    	if (this.polyPart == null && c.getPoly() != null) {
    		this.polyPart = c.getPoly();
    	} else if (c.getPoly() != null) {
    		this.polyPart = this.polyPart.plus(c.getPoly());
    	}
    }
   
    public void multiply(Complexity c) {
    	if (this.polyPart == null && c.getPoly() != null) {
    		this.polyPart = c.getPoly();
    	} else if (c.getPoly() != null) {
    		this.polyPart = this.polyPart.times(c.getPoly());
    	}
        // Don't change the log part
    }
   
    public int compareTo(Complexity c) {
        // 1 if greater than c
        // -1 if less than c
        // 0 if equal to c
    	// -2 if we cannot compare this and c
    	if (this.expPart != null) {
    		/* 
    		 * Need to check if the base of exp is non constant
    		 * this is the highest complexity and hence needs to be
    		 * separate from the below exp check
    		 */
    		if (c.getExp() != null) {
    			/* 
        		 * if this has a non constant base and c doesn't or has no 
        		 * expPart at all, this is bigger
        		 */
        		if (!c.getExp().getNonConstBase().equals("") && 
        				this.getExp().getNonConstBase().equals("")) {
        			return -1;
        		}
        		// If both this and c have non constant base, we can't compare
        		if (!c.getExp().getNonConstBase().equals("") && 
        				!this.getExp().getNonConstBase().equals("")) {
        			return -2;
        		}
    		}
    		if (!this.getExp().getNonConstBase().equals("")) {
    			return 1;
    		}
    	} else if (c.getExp() != null) {
    		// if c has a non constant base and this doesn't, c is bigger
    		if (!c.getExp().getNonConstBase().equals("")) {
    			return -1;
    		}
    	}
    	if (this.factPart != null) {
    		// The factorial part is the greatest and will always be n!
    		// (There aren't any variations on the factorial class
    		if (c.getFact() == null) {
    			return 1;
    		} else {
    			return 0;
    		}
    	} else if (c.getFact() != null) {
    		return -1;
    	}
        if (this.expPart != null) {
            // if c doesn't have an exponential part then this is bigger
            if (c.getExp() == null) {
                return 1;
            }
            if (this.expPart.getBase() > c.expPart.getBase()) {
                return 1;
            }
            if (this.expPart.getBase() < c.expPart.getBase()) {
                return -1;
            }
            // if the exp parts are the same, move on to the polynomial parts
        } else if (c.expPart != null) {
            // Need this part for if c has an exp part but this doesn't
            return -1;
        }
        if (this.getPoly() != null) {
            if (c.getPoly() == null) {
                return 1;
            }
            if (this.getPoly().degree() > c.getPoly().degree()) {
                // polynomial part is of higher degree, so return 1
                return 1;
            }
            if (this.getPoly().degree() < c.getPoly().degree()) {
                return -1;
            }
            // if the polynomial parts are the same, move onto the log part
        } else if (c.getPoly() != null) {
            // need this to account for when c has a poly part but this one doesn't
            return -1;
        }
        if (this.logPart != null) {
        	
        }
        return 0;
    }
   
    public String toString() {
        String output = "O(";
        if (expPart != null) {
        	output += expPart.toString() + " ";
        }
        if (polyPart != null) {
            output += polyPart.toString() + " ";
        }
        if (logPart != null) {
            output += logPart.toString();
        }
        output = output.trim();
        output += ")";
        return output;
    }
    
    // Test values
    public static void main(String [] args) {
//    	Complexity c1 = new Complexity(new Exponential(2), new Polynomial(2), null);
//    	Complexity c2 = new Complexity(new Exponential(2), new Polynomial(1), new Logarithm());
    	Complexity constant = new Complexity(null, new Polynomial(0), null);
    	Complexity empty = new Complexity();
    	Complexity empty2 = new Complexity();
    	constant.add(empty);
    	empty2.add(constant);
    	System.out.println("Test sum: " + empty2.toString());
//    	
//    	System.out.println(c1.toString());
//    	System.out.println(c2.toString());
//    	System.out.println(c3.toString());
//    	
//    	int bigger = c1.compareTo(c2);
//    	System.out.println("Value of comparison is: " + bigger);
    	
    	Exponential e1 = new Exponential(2);
        Exponential e2 = new Exponential(3);
        Exponential e3 = new Exponential("n");
        Exponential e4 = new Exponential("m");
        
        Complexity c1 = new Complexity(e1);
        Complexity c2 = new Complexity(e2);
        Complexity c3 = new Complexity(e3);
        Complexity c4 = new Complexity(e4);
        
        System.out.println("Comparing " + c1.toString() + " and " + c2.toString() + " --- " + c1.compareTo(c2));
        System.out.println("Comparing " + c3.toString() + " and " + c4.toString() + " --- " + c3.compareTo(c4));
        System.out.println("Comparing " + c1.toString() + " and " + c3.toString() + " --- " + c1.compareTo(c3));
        System.out.println("Constant: " + constant.toString());
    }
}

class Polynomial {

        // Coefficient is always 1
        private int deg;     // degree of polynomial (0 for the zero polynomial)
   
        // x^a
        public Polynomial(int a) {
            deg = a;
        }
       
        // This will just give the highest degree
        public Polynomial plus(Polynomial b) {
            return this.degree() > b.degree() ? this : b;
        }
   
        // return the degree of this polynomial (0 for the zero polynomial)
        public int degree() {
            return deg;
        }
   
        // return (a * b) which is just a multiplication of highest degrees
        public Polynomial times(Polynomial b) {
        	if (b == null) {
        		return this;
        	}
            Polynomial c = new Polynomial(this.degree() + b.degree());
            return c;
        }

        // do a and b represent the same polynomial?
        public boolean eq(Polynomial b) {
            
            return this.degree() != b.degree() ? false : true;
        }
   
        // convert to string representation
        public String toString() {
            if (deg == 0) return "1";
            if (deg == 1) return "n";
            return "n^" + deg;
        }
        
        public static void main (String[] args) throws java.lang.Exception
        {
            // Constant is to the power 0. No Polynomial part is null
            Polynomial constant = new Polynomial(0);
            
            Polynomial p1   = new Polynomial(3);
            Polynomial p2   = new Polynomial(2);
            Polynomial p3   = new Polynomial(0);
            Polynomial p4   = new Polynomial(1);
            Polynomial p    = p1.plus(p2).plus(p3).plus(p4);   // x^3
            
            System.out.println("Constant: " + constant);
            System.out.println("p(x) =        " + p);
            System.out.println("Degree of p: " + p.degree());
            System.out.println("Simplified p: " + p);
           
        }
   
    }
	
	/**
	 * A Factorial is always n! in this context, so it simply needs a 
	 * marker to state it exists, i.e. a Boolean.
	 * 
	 * @author andrewwhalley
	 */
    class Factorial {
    	private boolean factExists = false;
        public Factorial() {
           factExists = true;
        }
        
        public void plus(Factorial f) {
        	// TODO: Implement addition if needed
        }
        
        public void multiply (Factorial f) {
        	// TODO: Implement multiplication if needed
        }
        
        public String toString() {
        	if (factExists) {
        		return "n!";
        	}
        	return "";
        }
    }
   
    class Exponential {
        // TODO: Generalise this - Assume exponential component itself is 'n'
        private int base;
        private String nBase = "";        
        public Exponential(int b) {
            this.base = b;
        }
        
        /**
         * This constructor is for if it is a non constant base, e.g. n^n
         * This is the highest Big-O complexity 
         *  
         * @param b
         */
        public Exponential(String b) {
        	this.nBase = b;
        }
        
        public void plus(Exponential e) {
        	// TODO: Implement addition
        }
        
        public void multiply (Exponential e) {
        	// TODO: Implement multiplication
        }

		public int getBase() {
			// This will be true if the base isn't constant
			if (!nBase.equals("")) {
				return -1;
			}
			return this.base;
		}
		
		public String getNonConstBase() {
			return this.nBase;
		}
		
		public String toString() {
			if (!nBase.equals("")) {
				return nBase + "^n";
			}
			return this.base > 1 ? this.base + "^n" : "";
		}
   }
   
    class Logarithm {
    	// TODO: Generalise this - Assume the value itself is 'n'
        public Logarithm() {
           
        }
        
        public void plus(Logarithm l) {
        	// TODO: Implement addition
        }
        
        public void multiply (Logarithm l) {
        	// TODO: Implement multiplication
        }
       
        public String toString() {
            return "log(n)";
        }
    }


