package hello_world_pkg;

import java.math.BigDecimal;

public class FindbugsTutorial {
	
	public static void main (String [] args) {
		BigDecimal a = new BigDecimal("0.1");
		BigDecimal b = a.add(new BigDecimal(0.1));
		System.out.println(b);
	}
}
