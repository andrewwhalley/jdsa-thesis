package thesis.pmd.fragment.initial;
import java.util.*;
import java.lang.*;


public class Complexity {
    private Polynomial polyPart;
    private Logarithm logPart;
    private Exponential expPart;
   
    public Complexity(Exponential e, Polynomial p, Logarithm l) {
        this.polyPart = p;
        this.logPart = l;
        this.expPart = e;
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
   
    public void multiply(Complexity c) {
        this.polyPart.times(c.getPoly());
        // Don't change the log part
    }
   
    public int compareTo(Complexity c) {
        // 1 if greater than c
        // -1 if less than c
        // 0 if equal to c
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
    	Complexity c1 = new Complexity(new Exponential(2), new Polynomial(2), null);
    	Complexity c2 = new Complexity(new Exponential(2), new Polynomial(1), new Logarithm());
    	Complexity c3 = new Complexity(null, new Polynomial(0), null);
    	
    	System.out.println(c1.toString());
    	System.out.println(c2.toString());
    	System.out.println(c3.toString());
    	
    	int bigger = c1.compareTo(c2);
    	System.out.println("Value of comparison is: " + bigger);
    }
}

class Polynomial {

        // Coefficient is always 1
        private int deg;     // degree of polynomial (0 for the zero polynomial)
        private boolean simplified = false;
   
        // a * x^b
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
     
            System.out.println("p(x) =        " + p);
            System.out.println("Degree of p: " + p.degree());
            System.out.println("Simplified p: " + p);
           
        }
   
    }
   
    class Factorial {
        public Factorial() {
           
        }
    }
   
    class Exponential {
        // Assume exponential component itself is 'n'
        private int base;
               
        public Exponential(int b) {
            this.base = b;
        }

		public int getBase() {
			// TODO Auto-generated method stub
			return this.base;
		}
		
		public String toString() {
			return this.base > 1 ? this.base + "^n" : "";
		}
   }
   
    class Logarithm {
       
        public Logarithm() {
           
        }
       
        public String toString() {
            return "log(n)";
        }
    }


