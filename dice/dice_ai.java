package com.dice;

import java.util.Random;

public class dice_ai {

    public dice_ai()
    {
    }


    public static long factorial(int n)
    {
        return (n <= 1) ? 1 : n*factorial(n-1); // he-he... let's mix a ternary operator (?) and recursion...
    }

    public static long combination(int n, int p)
    {
        return factorial(n) / (factorial(n - p) * factorial(p));
    }
    
    public static double prob_true(int x, int n, double p1, double p2){
		if (x <= 0)
			return 1.0;
		//* combo(n, x)
		double p_total = 0;
		for (int i=x-1; i>=0; i--) {
			long combo = dice_ai.combination(n, i);
			double p_i = combo * Math.pow(p1, i) * Math.pow(p2, (n-i));
			p_total += p_i;
			//System.out.println(" x = "+ i + " Combo = "+combo + " p(i) = " + p_i);
		}
		double ptrue = 1.0 - p_total;
		
		return ptrue;
	}
    
    public static double prob_true_with_pliar(double ptrue, double pliar){
    	if (ptrue == 1.0)
    		return 0;
    	Random r = new Random();
    	double rand = r.nextDouble();
    	if (!r.nextBoolean())
			rand = -rand;
    	double p = ptrue + (1.0 - ptrue) * rand;
		
		return p;
    }
    
    public static boolean prob_open(double ptrue, double pnext){
    	if (ptrue < 0.05)
    		return true;
    	
    	if (pnext >= (1-ptrue))
			return false;    	
    	
    	Random r = new Random();
		double rand = r.nextDouble();
		
		System.out.println("rand: "+ rand + " - ptrue: " + ptrue);
		return (rand>ptrue);	     	
    }
    
}
